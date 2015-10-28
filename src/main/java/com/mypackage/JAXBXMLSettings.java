package com.mypackage;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

@XmlRootElement( name = "Settings" )
public class JAXBXMLSettings implements XMLReader {
    private EncryptionAlgorithm<KeyMaker> algorithm;
    private String source; //source file/folder
    private String keyPath;
    private Schema schema;
    
    public void read(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXBXMLSettings.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        if(schema != null) {
            jaxbUnmarshaller.setSchema(schema);
            jaxbUnmarshaller.setEventHandler(new JAXBValidation());
        }
        this.algorithm = ((JAXBXMLSettings) jaxbUnmarshaller.unmarshal(file)).algorithm;
        this.source = ((JAXBXMLSettings) jaxbUnmarshaller.unmarshal(file)).source;
        this.keyPath = ((JAXBXMLSettings) jaxbUnmarshaller.unmarshal(file)).keyPath;
    }
    
    public EncryptionAlgorithm<KeyMaker> getAlgorithm() {
        return algorithm;
    }
    
    @XmlElement(name = "Algorithm" )
    @XmlJavaTypeAdapter( JAXBAdapter.class )
    public void setAlgorithm(EncryptionAlgorithm<KeyMaker> algorithm) {
        this.algorithm = algorithm;
    }
    
    public String getSource() {
        return source;
    }
    
    @XmlElement(name = "Source" )
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getKeyPath() {
        return keyPath;
    }
    
    @XmlElement(name = "KeyPath" )
    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public void validate(File file) throws SAXException, IOException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
        schema = sf.newSchema(file);
        
    }
    
    
    
}
