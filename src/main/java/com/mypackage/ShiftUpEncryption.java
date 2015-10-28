package com.mypackage;
//encryption by adding the key value to each character
public class ShiftUpEncryption extends ShiftEncryption {

    @Override
    public int encryptChar(char c, int key) throws InvalidEncryptionKeyException {
        if(key > 101 || key < 0)
            throw new InvalidEncryptionKeyException(String.valueOf(key), 7,
                    getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString(), "encryptChar");
        return (int)c + key;
    }

    @Override
    public int decryptChar(char c, int key) throws InvalidEncryptionKeyException {
        if(key > 101 || key < 0)
            throw new InvalidEncryptionKeyException(String.valueOf(key), 16,
                    getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString(), "decryptChar");
        return (int)c - key;
    }

    public int getKeyStrength() {
        return 3;
    }
    
    @Override
    public String toString() {
        return "ShiftUpEncryption";
    }
}
