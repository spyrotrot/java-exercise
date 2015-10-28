package com.mypackage;

import com.google.inject.AbstractModule;

public class AlgorithmModule extends AbstractModule {
    EncryptionAlgorithm<KeyMaker> algorithm;

    public AlgorithmModule(String algorithm) {
        AlgorithmFactory factory = new AlgorithmFactory();
        this.algorithm = factory.getAlgorithm(algorithm);
    }
    
    @Override
    protected void configure() {
        bind(EncryptionAlgorithm.class).to(algorithm.getClass());

    }

}
