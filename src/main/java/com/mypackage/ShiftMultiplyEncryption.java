package com.mypackage;
//encryption by multiplying each character by the key
public class ShiftMultiplyEncryption extends ShiftEncryption {

    @Override
    public int encryptChar(char c, int key) throws InvalidEncryptionKeyException {
        if(key > 101 || key < 0)
            throw new InvalidEncryptionKeyException(String.valueOf(key), 7,
                    getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString(), "encryptChar");
        return ((int)c * ((key % 3)+2));  //because of encoding limitation
    }                                     //multiplying can use only small keys
                                          //and the key shouldn't be one or it  
    @Override                             //won't do anything
    public int decryptChar(char c, int key) throws InvalidEncryptionKeyException {
        if(key > 101 || key < 0)
            throw new InvalidEncryptionKeyException(String.valueOf(key), 16,
                    getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString(), "decryptChar");
        return ((int)c / ((key % 3)+2));
    }
    
    public int getKeyStrength() {
        return 1;
    }
    
    @Override
    public String toString() {
        return "ShiftMultiplyEncryption";
    }
}
