package com.trantanh.eet.v2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.xml.sax.InputSource;

public class Eet2RequestSigner {
    static final String SOAP_NS = "http://schemas.xmlsoap.org/soap/envelope/";
    static final String EET_NS = "http://fs.gov.cz/eet/schema/v4";
    static final String WSSE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
    static final String RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
    static final String BASE64_ENCODING_TYPE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";
    static final String X509_VALUE_TYPE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";

    private static final Pattern EIC = Pattern.compile("CZ[0-9]{8,10}");
    private static final Pattern ID_POKL = Pattern.compile("[0-9a-zA-Z\\.,:;/#_\\- ]{1,20}");
    private static final Pattern PORAD_CIS = Pattern.compile("[0-9a-zA-Z\\.,:;/#_\\- ]{1,25}");

    public String sign(EetSale sale, EetCredentials credentials) {
        validate(sale);
        try {
            KeyMaterial material = loadKeyMaterial(credentials);
            Document document = newDocument();

            Element envelope = element(document, SOAP_NS, "soapenv:Envelope");
            envelope.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:v4", EET_NS);
            document.appendChild(envelope);

            Element header = element(document, SOAP_NS, "soapenv:Header");
            envelope.appendChild(header);
            Element security = element(document, WSSE_NS, "wsse:Security");
            security.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:wsu", WSU_NS);
            header.appendChild(security);

            String tokenId = "X509-" + compactId();
            Element binaryToken = element(document, WSSE_NS, "wsse:BinarySecurityToken");
            binaryToken.setAttribute("EncodingType", BASE64_ENCODING_TYPE);
            binaryToken.setAttribute("ValueType", X509_VALUE_TYPE);
            binaryToken.setAttributeNS(WSU_NS, "wsu:Id", tokenId);
            binaryToken.setTextContent(Base64.getEncoder().encodeToString(material.certificate().getEncoded()));
            security.appendChild(binaryToken);

            String bodyId = "id-" + compactId();
            Element body = element(document, SOAP_NS, "soapenv:Body");
            body.setAttributeNS(WSU_NS, "wsu:Id", bodyId);
            body.setIdAttributeNS(WSU_NS, "Id", true);
            envelope.appendChild(body);

            Element transaction = element(document, EET_NS, "v4:Trzba");
            body.appendChild(transaction);
            Element requestHeader = element(document, EET_NS, "v4:Hlavicka");
            requestHeader.setAttribute("uuid_zpravy", sale.messageUuid().toString());
            requestHeader.setAttribute("dat_odesl", formatDate(OffsetDateTime.now()));
            requestHeader.setAttribute("prvni_zaslani", Boolean.toString(sale.firstSend()));
            requestHeader.setAttribute("overeni", "false");
            transaction.appendChild(requestHeader);

            Element data = element(document, EET_NS, "v4:Data");
            data.setAttribute("eic_popl", sale.eicPopl());
            data.setAttribute("id_jednotky", Integer.toString(sale.idJednotky()));
            data.setAttribute("id_pokl", sale.idPokl());
            data.setAttribute("porad_cis", sale.poradCis());
            data.setAttribute("dat_trzby", formatDate(sale.datTrzby()));
            data.setAttribute("celk_trzba", sale.celkTrzba().setScale(2, RoundingMode.HALF_UP).toPlainString());
            transaction.appendChild(data);

            // Namespace declarations added by a serializer are significant for
            // exclusive canonicalization. Normalize the unsigned document first
            // so the digest is calculated over exactly what is sent on the wire.
            document = parse(serialize(document));
            security = (Element) document.getElementsByTagNameNS(WSSE_NS, "Security").item(0);
            body = (Element) document.getElementsByTagNameNS(SOAP_NS, "Body").item(0);
            body.setIdAttributeNS(WSU_NS, "Id", true);
            signBody(document, security, material, tokenId, bodyId);
            return serialize(document);
        } catch (Exception exception) {
            throw new IllegalStateException("Nelze vytvořit podepsanou datovou zprávu EET 2.0", exception);
        }
    }

    void validate(EetSale sale) {
        if (!EIC.matcher(sale.eicPopl()).matches()) {
            throw new EetDataValidationException("EIČ musí mít tvar CZ a 8 až 10 číslic");
        }
        if (sale.idJednotky() < 1 || sale.idJednotky() > 999_999_999) {
            throw new EetDataValidationException("Identifikátor evidenční jednotky je mimo povolený rozsah");
        }
        if (!ID_POKL.matcher(sale.idPokl()).matches()) {
            throw new EetDataValidationException("Neplatné označení pokladního zařízení");
        }
        if (!PORAD_CIS.matcher(sale.poradCis()).matches()) {
            throw new EetDataValidationException("Neplatné pořadové číslo tržby");
        }
        if (sale.celkTrzba().signum() == 0 || sale.celkTrzba().abs().compareTo(new java.math.BigDecimal("100000000")) >= 0) {
            throw new EetDataValidationException("Celková částka EET musí být nenulová a menší než 100 000 000 Kč");
        }
    }

    private void signBody(Document document, Element security, KeyMaterial material,
                          String tokenId, String bodyId) throws Exception {
        XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");
        Reference reference = factory.newReference(
                "#" + bodyId,
                factory.newDigestMethod(DigestMethod.SHA256, null),
                List.of(factory.newTransform(CanonicalizationMethod.EXCLUSIVE,
                        new ExcC14NParameterSpec(List.of("v4")))),
                null,
                null);
        SignedInfo signedInfo = factory.newSignedInfo(
                factory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE,
                        new ExcC14NParameterSpec(List.of("soapenv", "v4"))),
                factory.newSignatureMethod(RSA_SHA256, null),
                List.of(reference));

        Element tokenReference = element(document, WSSE_NS, "wsse:SecurityTokenReference");
        tokenReference.setAttributeNS(WSU_NS, "wsu:Id", "STR-" + compactId());
        Element referenceElement = element(document, WSSE_NS, "wsse:Reference");
        referenceElement.setAttribute("URI", "#" + tokenId);
        referenceElement.setAttribute("ValueType", X509_VALUE_TYPE);
        tokenReference.appendChild(referenceElement);
        KeyInfoFactory keyInfoFactory = factory.getKeyInfoFactory();
        KeyInfo keyInfo = keyInfoFactory.newKeyInfo(
                List.of(new DOMStructure(tokenReference)), "KI-" + compactId());

        DOMSignContext context = new DOMSignContext(material.privateKey(), security);
        context.setDefaultNamespacePrefix("ds");
        context.putNamespacePrefix(CanonicalizationMethod.EXCLUSIVE, "ec");
        XMLSignature signature = factory.newXMLSignature(signedInfo, keyInfo, null, "SIG-" + compactId(), null);
        signature.sign(context);
    }

    private KeyMaterial loadKeyMaterial(EetCredentials credentials) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        char[] password = credentials.certificatePassword();
        try (InputStream input = Files.newInputStream(credentials.certificatePath())) {
            keyStore.load(input, password);
        }
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (keyStore.isKeyEntry(alias) && keyStore.getKey(alias, password) instanceof PrivateKey privateKey
                    && keyStore.getCertificate(alias) instanceof X509Certificate certificate) {
                return new KeyMaterial(privateKey, certificate);
            }
        }
        throw new IllegalArgumentException("PKCS#12 neobsahuje privátní klíč a certifikát");
    }

    private Document newDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return factory.newDocumentBuilder().newDocument();
    }

    private Document parse(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    private Element element(Document document, String namespace, String name) {
        return document.createElementNS(namespace, name);
    }

    private String serialize(Document document) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        StringWriter output = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(output));
        return output.toString();
    }

    private String formatDate(OffsetDateTime value) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value.withNano(0));
    }

    private String compactId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private record KeyMaterial(PrivateKey privateKey, X509Certificate certificate) {
    }
}
