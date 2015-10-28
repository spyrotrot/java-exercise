package com.mypackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler implements XMLReader {
    private EncryptionAlgorithm<KeyMaker> algorithm;
    private String currentElement;
    private String source;
    private String keyPath;
    private SAXParserFactory spf;
    
    public void read(File file) throws SAXException, IOException, 
        ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        InputStream    xmlInput  =
                new FileInputStream(file.getAbsolutePath());
        SAXParser saxParser;
        if(spf == null) {
            saxParser = factory.newSAXParser();
        } else {
            saxParser = spf.newSAXParser();
        }
        saxParser.parse(xmlInput, this);
    }
    
    @Override
    //validates the elements and saves the current element
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if(!(qName.equals("Settings") || qName.equals("Algorithm")
                || qName.equals("Source") || qName.equals("KeyPath"))) {
            throw new SAXException();
        }
        currentElement = qName;
    }

    @Override
    //gets the content of the XML
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String value = new String(ch, start, length).trim();
        if(value.length() == 0) return; // ignore white space
        if(currentElement.equals("Algorithm")) {
            if(value.equals("ShiftMultiplyEncryption")) {
                algorithm = new ShiftMultiplyEncryption();
            } else if(value.equals("XOREncryption")) {
                algorithm = new XorEncryption();
            } else {
                algorithm = new ShiftUpEncryption(); //shift up as default
            }
        }
        if(currentElement.equals("Source")) {
            source = value;
        }
        if(currentElement.equals("KeyPath")) {
            keyPath = value;
        }
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

    public void validate(File file) throws SAXException, IOException {
        Schema schema;
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        schema = factory.newSchema(file);
        spf = SAXParserFactory.newInstance();
        spf.setSchema(schema);
        
    }
}
