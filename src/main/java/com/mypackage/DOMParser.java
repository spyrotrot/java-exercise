package com.mypackage;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMParser implements XMLReader {
    private EncryptionAlgorithm<KeyMaker> algorithm;
    private String source;
    private String keyPath;
    private Document doc;
    
    public void validate(File file) throws SAXException, IOException {
        if(doc != null) {
            Schema schema;
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(language);
            schema = factory.newSchema(file);
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(doc));
        }
    }
    
    public void read(File fXmlFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        
        Element rootElement = doc.getDocumentElement();
        if(!rootElement.getTagName().equals("Settings")) {
            throw new SAXException();
        }
        NodeList nodes = rootElement.getChildNodes();
        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            Element element;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element)node;
            } else {
                continue;
            }
            if(!(element.getTagName().equals("Algorithm") 
                    || element.getTagName().equals("Source")
                    || element.getTagName().equals("KeyPath"))) {
                throw new SAXException();
            }
            if(element.getTagName().equals("Algorithm")) {
                if(node.getTextContent().equals("ShiftMultiplyEncryption")) {
                    algorithm = new ShiftMultiplyEncryption();
                } else if(node.getTextContent().equals("XOREncryption")) {
                    algorithm = new XorEncryption();
                } else {
                    algorithm = new ShiftUpEncryption(); //shift up as default
                }
            }
            if(element.getTagName().equals("Source")) {
                source = node.getTextContent();
            }
            if(element.getTagName().equals("KeyPath")) {
                keyPath = node.getTextContent();
            }
        }
        
        
    }

    public Document getDoc() {
        return doc;
    }

    public EncryptionAlgorithm<KeyMaker> getAlgorithm() {
        return algorithm;
    }

    public String getSource() {
        return source;
    }

    public String getKeyPath() {
        return keyPath;
    }

}
