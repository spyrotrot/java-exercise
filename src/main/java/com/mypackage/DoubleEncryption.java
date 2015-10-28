package com.mypackage;

import java.io.File;
import java.io.IOException;

import com.google.inject.Inject;


public class DoubleEncryption {
    private EncryptionAlgorithm<KeyMaker> algorithm;
    private FileEncryptor fileEncryptor;
    
    public FileEncryptor getFileEncryptor() {
        return fileEncryptor;
    }

    //constructor to set algorithm
    @Inject
    public DoubleEncryption(EncryptionAlgorithm<KeyMaker> nAlgorithm) {
        algorithm=nAlgorithm;
        fileEncryptor = new FileEncryptor(algorithm);
    }
    
    //double encrypts a file
    public void encrypt(String originalFilePath, 
            String newFilePath) throws IOException, InvalidEncryptionKeyException {
        String absolutePath = new File(newFilePath).getAbsolutePath();
        fileEncryptor.encryptFile(originalFilePath, 
                newFilePath, 
                absolutePath.substring
                (0, absolutePath.lastIndexOf(File.separator)) + "\\key1.txt");
        fileEncryptor.generateNewKey();
        fileEncryptor.encryptFile(newFilePath, 
                newFilePath, 
                absolutePath.substring
                (0, absolutePath.lastIndexOf(File.separator)) + "\\key2.txt");
    }
    
    //double decrypts a file
    public void decrypt(String originalFilePath, String newFilePath, 
            String keyPath) throws IOException, 
            InvalidFilePathException, InvalidEncryptionKeyException {
        File f1 = new File(keyPath);
        if(keyPath.lastIndexOf("1.") != -1 
                && keyPath.charAt(keyPath.lastIndexOf(".")-1) == '1') {
            File f2 = new File(keyPath.substring(0, 
                    keyPath.lastIndexOf('1')) + "2" 
                    + keyPath.substring(keyPath.lastIndexOf('1') + 1));
            if(f1.exists() && !f1.isDirectory() 
                    && f2.exists() && !f2.isDirectory()) {
                try {
                    fileEncryptor.decryptFile(originalFilePath, 
                            newFilePath, 
                            keyPath.substring(0, 
                            keyPath.lastIndexOf('1')) + "2" 
                            + keyPath.substring(keyPath.lastIndexOf('1') + 1));
                    fileEncryptor.decryptFile(newFilePath, 
                            newFilePath, 
                            keyPath);
                } catch (InvalidEncryptionKeyException e) {
                    System.out.println("ERROR: Encryption key is"
                            + " not in the right format");
                }
            } else {
                throw new InvalidFilePathException(keyPath, 
                        Thread.currentThread().getStackTrace()[2].getLineNumber(), 
                        getClass().getProtectionDomain()
                        .getCodeSource().getLocation().toString(), "decrypt");
            }
        } else {
            throw new InvalidEncryptionKeyException(keyPath, 
                    Thread.currentThread().getStackTrace()[2].getLineNumber(), 
                    getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString(), "decrypt");
        }
    }
    
    @Override
    public String toString() {
        return "Double Encryption(" + algorithm.toString() + ")";
    }
}
