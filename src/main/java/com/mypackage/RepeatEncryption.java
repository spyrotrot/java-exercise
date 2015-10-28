package com.mypackage;

import java.io.File;
import java.io.IOException;
//repeats an encryption with the same key
public class RepeatEncryption {
    private int n;
    private EncryptionAlgorithm algorithm;
    
    //constructor to set algorithm and number of repeats
    public RepeatEncryption(EncryptionAlgorithm nAlgorithm, int nN) {
        algorithm=nAlgorithm;
        n=nN;
    }
    
    //encrypts a file
    public void encrypt(String originalFilePath, 
            String newFilePath) throws IOException, InvalidEncryptionKeyException {
        String absolutePath = new File(newFilePath).getAbsolutePath();
        FileEncryptor encriptor = new FileEncryptor(algorithm);
        encriptor.encryptFile(originalFilePath, 
                newFilePath, 
                absolutePath.substring
                (0, absolutePath.lastIndexOf(File.separator)) + "\\key.txt");
        for(int i = 0; i < n-1; i++) {
            encriptor.encryptFile(newFilePath, 
                    newFilePath, 
                    absolutePath.substring
                    (0, absolutePath.lastIndexOf(File.separator)) + "\\key.txt");
        }
    }
    
    //decrypts a file with the key in keyPath
    public void decrypt(String originalFilePath, 
            String newFilePath, String keyPath) throws IOException {
        FileEncryptor encriptor = new FileEncryptor(algorithm);
        try {
            encriptor.decryptFile(originalFilePath, 
                    newFilePath, 
                    keyPath);
            for(int i = 0; i < n-1; i++) {
                encriptor.decryptFile(newFilePath, 
                        newFilePath, 
                        keyPath);
            }
        } catch (InvalidEncryptionKeyException e) {
            System.out.println("ERROR: Encryption key is"
                    + " not in the right format");
        }
    }
    
    @Override
    public String toString() {
        return "Repeat Encryption" + n + "times(" + algorithm.toString() + ")";
    }
}
