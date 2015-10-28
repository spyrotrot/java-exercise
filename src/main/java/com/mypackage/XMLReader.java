package com.mypackage;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public interface XMLReader {
    public void validate(File file) throws SAXException, IOException;
    public void read(File file) throws JAXBException, SAXException, IOException,
    ParserConfigurationException;
    public EncryptionAlgorithm<KeyMaker> getAlgorithm();
    public String getSource();
    public String getKeyPath();

}
