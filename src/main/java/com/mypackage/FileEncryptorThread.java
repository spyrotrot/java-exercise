package com.mypackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileEncryptorThread extends FileEncryptor implements Runnable {
    private String originalFilePath;
    private String newFilePath;
    private String keyPath;
    private Operation op; //encrypt/decrypt
    
    //constructor for getting file to encrypt
    public FileEncryptorThread(String originalFilePath,
            String newFilePath,
            String keyPath,
            int key,
            Operation op,
            EncryptionAlgorithm<KeyMaker> algorithm) {
                this.originalFilePath = originalFilePath;
                this.newFilePath = newFilePath;
                this.keyPath = keyPath;
                this.key = new KeyMaker();
                this.key.setKey(key);
                this.op = op;
                this.algorithm = algorithm;
                readWriteFile = new FileAccessor();
                
            }

    public void run() {
        try {
            if(op == Operation.encrypt) {
                encryptFile(originalFilePath, newFilePath, keyPath);
            } else {
                decryptFile(originalFilePath, newFilePath, keyPath);
            }
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            EncryptionLog4JLogger.logger.error(e.getMessage() +
                    "\n" + exceptionAsString);
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
        }
        
    }
    

}
