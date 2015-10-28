package com.mypackage;
//abstract class for shifting encryptions
public abstract class ShiftEncryption implements EncryptionAlgorithm<KeyMaker> {
    //encrypts text with key
    public String encrypt(String text, KeyMaker key) throws InvalidEncryptionKeyException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            builder.append((char)(encryptChar(c, key.getKey())));
        }
        return builder.toString();
    }
    
    //decrypts text with key
    public String decrypt(String text, KeyMaker key) throws InvalidEncryptionKeyException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            builder.append((char)(decryptChar(c, key.getKey())));
        }
        return builder.toString();
    }
    
    public abstract int encryptChar(char c, int key) throws InvalidEncryptionKeyException;//encrypts c with key
    public abstract int decryptChar(char c, int key) throws InvalidEncryptionKeyException;//decrypts c with key
}
