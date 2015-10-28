package com.mypackage;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ShiftMultiplyEncryptionTest 
extends ShiftEncryptionTest {

    @Test
    public void shouldShiftMultiplyEncrypt() throws InvalidEncryptionKeyException {
        Injector injector = Guice.createInjector(new AlgorithmModule(
                new ShiftMultiplyEncryption().toString()));
        EncryptionAlgorithm<KeyMaker> algorithm = injector
                .getInstance(EncryptionAlgorithm.class);
        shouldShiftEncrypt(algorithm);
    }
    
    @Test(expected = InvalidEncryptionKeyException.class)
    public void shouldThrowException() throws InvalidEncryptionKeyException {
        Injector injector = Guice.createInjector(new AlgorithmModule(
                new ShiftMultiplyEncryption().toString()));
        EncryptionAlgorithm<KeyMaker> algorithm = injector
                .getInstance(EncryptionAlgorithm.class);
        exceptionTest(algorithm);
    }
}
