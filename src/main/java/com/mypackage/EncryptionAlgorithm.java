package com.mypackage;

public interface EncryptionAlgorithm<T> {
    public String encrypt(String text, T key) throws InvalidEncryptionKeyException;
    public String decrypt(String text, T key) throws InvalidEncryptionKeyException;
    public int getKeyStrength(); 
}
