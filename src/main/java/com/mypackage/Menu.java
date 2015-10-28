package com.mypackage;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Menu {
    private Console console;
    private int choice;
    private String filePath;
    private DoubleEncryption fileEncryption;
    private IDirectoryProccessor iDirectoryProccessor;
    private EncryptionListener logger;
    private XMLReader settings;
    private JAXBXMLSettings result;
    
    //default constructor
    public Menu() throws IOException, ParserConfigurationException{
        console = System.console();
        if (console == null) {
            System.out.println("Unable to fetch console");
            System.exit(0);;
        }
        File xsd = new File("xsd.xml");
        File file = new File( "test.xml" );
        try {            
            settings = new DOMParser();
            settings.validate(xsd);
            settings.read(file);
            try {
                settings.validate(xsd);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                EncryptionLog4JLogger.logger.error(exceptionAsString);
                System.exit(0);
            }
        } catch (SAXException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            EncryptionLog4JLogger.logger.error(exceptionAsString);
            System.exit(0);
        } catch (JAXBException e1) {
            StringWriter sw = new StringWriter();
            e1.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            EncryptionLog4JLogger.logger.error(exceptionAsString);
            System.exit(0);
        }
        Injector injector = Guice.createInjector(new AlgorithmModule(
                settings.getAlgorithm().toString()));
        EncryptionAlgorithm<KeyMaker> algorithm = injector.getInstance(EncryptionAlgorithm.class);

        logger = new EncryptionLog4JLogger();
        fileEncryption = new DoubleEncryption(algorithm);
        iDirectoryProccessor = new AsyncDirectoryProccessor(
                algorithm, logger);
        fileEncryption.getFileEncryptor().addListener(logger);
    }
    
    //shows the menu and asks to make a choice
    public void show() throws IOException, InterruptedException, ExecutionException { 
        while(true) {
            System.out.println("Type a number from the menu " 
                + "and press Enter:");
            System.out.println("    1. Encrypt\n" 
                + "    2. Decrypt\n"
                +"    3. Exit ");
            try {
                makeChoice();
            } catch (InvalidFilePathException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                EncryptionLog4JLogger.logger.error("in file:" + e.fileOfException 
                        + " function:" + e.function + " line:" + e.line 
                        + " :the file " + e.getFile() 
                        + " does not exist.\n" 
                        + exceptionAsString + "\nUser: " 
                        + System.getProperty("user.name"));
            } catch (InvalidEncryptionKeyException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                EncryptionLog4JLogger.logger.error("in file:" + e.fileOfException 
                        + " function:" + e.function + " line:" + e.line
                        +" :Encryption key " 
                        + e.getKey() + " is not in the right format\n" 
                        + exceptionAsString + "\nUser: " 
                        + System.getProperty("user.name"));
            } catch (JAXBException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                EncryptionLog4JLogger.logger.error(exceptionAsString);
            }
        }
    }
    
    //lets the user choose from the options in the menu
    private void makeChoice() throws IOException,
    InvalidFilePathException, InvalidEncryptionKeyException, InterruptedException, 
    ExecutionException, JAXBException{
        try {
            choice=Integer.parseInt(console.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Choice should be a number.");
            show();
            return;
        }
        switch(choice){
        case 1:            
            encrypt();
            break;
        case 2:
            decrypt();
            break;
        case 3:
            System.exit(0);
            break;
        default:
            System.out.println("The selected number should "
                    + "be between 1 and 2.");
            show();
            break;
        }
    }
    
    //does the encryption option
    public void encrypt() throws IOException, InvalidFilePathException, 
    InvalidEncryptionKeyException, InterruptedException, ExecutionException, JAXBException {
        String absolutePath;  //path of the encrypted file
        filePath = settings.getSource();
        if(new File(filePath).exists()) {
            absolutePath = new File(filePath).getAbsolutePath();
            if(new File(filePath).isDirectory()) {
                iDirectoryProccessor.encryptDirectory(filePath);
            } else {
                fileEncryption.encrypt(absolutePath, 
                        addToName(absolutePath,"_encrypted"));
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXBXMLSettings.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            result = new JAXBXMLSettings();
            OutputStream os = new FileOutputStream( "result.xml" );
            result.setAlgorithm(settings.getAlgorithm());
            result.setKeyPath(absolutePath.substring
                        (0, absolutePath.lastIndexOf(File.separator)) 
                        + File.separator + "key.txt");
            result.setSource(absolutePath);
            jaxbMarshaller.marshal( result, os);
        } else {
            throw new InvalidFilePathException(filePath, 
                    Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString(), "encrypt");
        }
    }
    
    //does the decryption option
    public void decrypt() throws IOException, 
    InvalidFilePathException, InvalidEncryptionKeyException, InterruptedException, 
    ExecutionException, JAXBException {
        String keyPath;
        String absolutePath;
        filePath = settings.getSource();
        if(!(new File(filePath).exists())) {
            throw new InvalidFilePathException(filePath, 
                    Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString(), "decrypt");
        }
        if(new File(filePath).isDirectory()) {
            iDirectoryProccessor.decryptDirectory(filePath);
            absolutePath = new File(filePath).getAbsolutePath();
            
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXBXMLSettings.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            result = new JAXBXMLSettings();
            OutputStream os = new FileOutputStream( "result.xml" );
            result.setAlgorithm(settings.getAlgorithm());
            result.setSource(absolutePath);
            jaxbMarshaller.marshal( result, os);
            return;
        }
        keyPath = settings.getKeyPath();
        if(!(new File(keyPath).exists() && !new File(keyPath).isDirectory())) {
            throw new InvalidFilePathException(keyPath, 105,
                    getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString(), "decrypt");
        }
        absolutePath = new File(filePath).getAbsolutePath();
        fileEncryption.decrypt(absolutePath, 
                addToName(absolutePath,"_decrypted"), 
                new File(keyPath).getAbsolutePath());
        
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXBXMLSettings.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        result = new JAXBXMLSettings();
        OutputStream os = new FileOutputStream( "result.xml" );
        result.setAlgorithm(settings.getAlgorithm());
        result.setSource(absolutePath);
        jaxbMarshaller.marshal( result, os);
    }
    
  //adds "toAdd" to the end of the file name
    public static String addToName(String name, String toAdd) {
        String newName;
        int indexOfDot;
        
        indexOfDot = name.lastIndexOf(".");
        if(indexOfDot >= 0) {
            newName = name.substring(0,indexOfDot)+toAdd
                    +name.substring(indexOfDot);
        } else {
            newName = name + toAdd;
        }
        return newName;
    }
    
    //checks if str is a number
    public static boolean isNumeric(String str)  
    {  
      try  
      {  
        Integer.parseInt(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }
}
