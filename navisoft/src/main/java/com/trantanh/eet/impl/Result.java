package com.trantanh.eet.impl;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class Result {

    private final String HLAVICKA = "eet:Hlavicka";
    private final String POTVRZENI = "eet:Potvrzeni";

    String xml;

    public Result(String xml) {
        this.xml = xml;
    }

    public Document stringToDom(String xmlSource)
            throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    }

    public String getFik() {
        try {
            Document doc = stringToDom(xml);
            NodeList nList = doc.getElementsByTagName(POTVRZENI);
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    return eElement.getAttribute("fik");
                }
            }
            return "";
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getBkp() throws SAXException, ParserConfigurationException, IOException {

        Document doc = stringToDom(xml);

        NodeList nList = doc.getElementsByTagName(HLAVICKA);

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                return eElement.getAttribute("bkp");
            }

        }
        return "";
    }

    public String getDatPrijat() {

        try {
            Document doc = stringToDom(xml);

            NodeList nList = doc.getElementsByTagName(HLAVICKA);

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    return eElement.getAttribute("dat_prij");
                }
            }
            return "";
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
