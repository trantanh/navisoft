package com.trantanh.eet.v2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Eet2ResponseParser {
    public EetGatewayResponse parse(String xml) {
        try {
            Document document = parseDocument(xml);
            Element header = first(document, "Hlavicka");
            OffsetDateTime responseTime = parseDate(attribute(header, "dat_prij", "dat_odmit"));
            Element confirmation = firstOrNull(document, "Potvrzeni");
            List<String> warnings = warnings(document);
            if (confirmation != null) {
                verifyConfirmationSignature(document);
                return new EetGatewayResponse(true, confirmation.getAttribute("pok"), null, null,
                        responseTime, Boolean.parseBoolean(confirmation.getAttribute("test")), warnings);
            }
            Element error = first(document, "Chyba");
            return new EetGatewayResponse(false, null, Integer.valueOf(error.getAttribute("kod")),
                    error.getTextContent().trim(), responseTime,
                    Boolean.parseBoolean(error.getAttribute("test")), warnings);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Neplatná odpověď systému EET 2.0", exception);
        }
    }

    void verifySignature(String xml) {
        try {
            verifyConfirmationSignature(parseDocument(xml));
        } catch (Exception exception) {
            throw new SecurityException("Elektronický podpis XML není platný", exception);
        }
    }

    private Document parseDocument(String xml) throws Exception {
        return secureFactory().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    private DocumentBuilderFactory secureFactory() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        return factory;
    }

    private Element first(Document document, String localName) {
        Element element = firstOrNull(document, localName);
        if (element == null) {
            throw new IllegalArgumentException("V odpovědi chybí element " + localName);
        }
        return element;
    }

    private Element firstOrNull(Document document, String localName) {
        NodeList nodes = document.getElementsByTagNameNS(Eet2RequestSigner.EET_NS, localName);
        return nodes.getLength() == 0 ? null : (Element) nodes.item(0);
    }

    private List<String> warnings(Document document) {
        NodeList nodes = document.getElementsByTagNameNS(Eet2RequestSigner.EET_NS, "Varovani");
        List<String> result = new ArrayList<>();
        for (int index = 0; index < nodes.getLength(); index++) {
            Element warning = (Element) nodes.item(index);
            result.add(warning.getAttribute("kod_varov") + ": " + warning.getTextContent().trim());
        }
        return result;
    }

    private String attribute(Element element, String... names) {
        for (String name : names) {
            if (element.hasAttribute(name)) {
                return element.getAttribute(name);
            }
        }
        return null;
    }

    private OffsetDateTime parseDate(String value) {
        return value == null || value.isBlank() ? null : OffsetDateTime.parse(value);
    }

    private void verifyConfirmationSignature(Document document) throws Exception {
        Element signatureElement = firstByNamespace(document, XMLSignature.XMLNS, "Signature");
        Element tokenElement = firstByNamespace(document, Eet2RequestSigner.WSSE_NS, "BinarySecurityToken");
        byte[] encodedCertificate = Base64.getMimeDecoder().decode(tokenElement.getTextContent().trim());
        X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509")
                .generateCertificate(new ByteArrayInputStream(encodedCertificate));
        markIdAttributes(document.getDocumentElement());
        DOMValidateContext context = new DOMValidateContext(certificate.getPublicKey(), signatureElement);
        context.setProperty("org.jcp.xml.dsig.secureValidation", Boolean.TRUE);
        XMLSignature signature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(context);
        if (!signature.validate(context)) {
            boolean signatureValueValid = signature.getSignatureValue().validate(context);
            boolean referencesValid = signature.getSignedInfo().getReferences().stream()
                    .map(Reference.class::cast)
                    .allMatch(reference -> validateReference(reference, context));
            throw new SecurityException("Elektronický podpis EET není platný (SignatureValue="
                    + signatureValueValid + ", Reference=" + referencesValid + ")");
        }
    }

    private boolean validateReference(javax.xml.crypto.dsig.Reference reference, DOMValidateContext context) {
        try {
            return reference.validate(context);
        } catch (Exception exception) {
            return false;
        }
    }

    private Element firstByNamespace(Document document, String namespace, String localName) {
        NodeList nodes = document.getElementsByTagNameNS(namespace, localName);
        if (nodes.getLength() == 0) {
            throw new SecurityException("Potvrzení EET neobsahuje " + localName);
        }
        return (Element) nodes.item(0);
    }

    private void markIdAttributes(Element element) {
        if (element.hasAttributeNS(Eet2RequestSigner.WSU_NS, "Id")) {
            element.setIdAttributeNS(Eet2RequestSigner.WSU_NS, "Id", true);
        }
        if (element.hasAttributeNS(XMLConstants.XML_NS_URI, "id")) {
            element.setIdAttributeNS(XMLConstants.XML_NS_URI, "id", true);
        }
        for (int index = 0; index < element.getChildNodes().getLength(); index++) {
            if (element.getChildNodes().item(index) instanceof Element child) {
                markIdAttributes(child);
            }
        }
    }
}
