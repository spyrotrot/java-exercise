package com.mypackage;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class XorEncryptionTest 
extends ShiftEncryptionTest {

    @Test
    public void shouldXorEncrypt() throws InvalidEncryptionKeyException {
        Injector injector = Guice.createInjector(new AlgorithmModule(
                new XorEncryption().toString()));
        EncryptionAlgorithm<KeyMaker> algorithm = injector
                .getInstance(EncryptionAlgorithm.class);
        shouldShiftEncrypt(algorithm);
    }
    
    @Test(expected = InvalidEncryptionKeyException.class)
    public void shouldThrowException() throws InvalidEncryptionKeyException {
        Injector injector = Guice.createInjector(new AlgorithmModule(
                new XorEncryption().toString()));
        EncryptionAlgorithm<KeyMaker> algorithm = injector
                .getInstance(EncryptionAlgorithm.class);
        exceptionTest(algorithm);
    }
}
