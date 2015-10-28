package com.mypackage;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


//the same test can be used for all three algorithms
public abstract class ShiftEncryptionTest {
    
    //using key maker was not needed
    //but it demonstrates mocking usage
    public void shouldShiftEncrypt(
            EncryptionAlgorithm<KeyMaker> encryption) 
                    throws InvalidEncryptionKeyException {
        String encryptedString;
        String decryptedString;
        KeyMaker key = mock(KeyMaker.class);
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "\nabcdefghijklmnopqrstuvwxy z";
        for(int i = 1; i < 101; i++) {
            when(key.getKey()).thenReturn(i);
            encryptedString = encryption.
                    encrypt(testString, key);
            assertNotEquals(
                    "Encryption did not"
                    + " encrypt with key: " + key.getKey(), 
                    testString, encryptedString);
            decryptedString = encryption.
                    decrypt(encryptedString, key);
            assertEquals("Encryption could not be"
                    + " decrypted with key: "+ key.getKey(), 
                    testString, decryptedString);
        }
    }
    
    public void exceptionTest(EncryptionAlgorithm<KeyMaker> encryption) 
            throws InvalidEncryptionKeyException {
        KeyMaker key = mock(KeyMaker.class);
        when(key.getKey()).thenReturn(102);
        encryption.encrypt("abcd", key);
    }
}
