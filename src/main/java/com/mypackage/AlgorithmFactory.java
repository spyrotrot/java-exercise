package com.mypackage;

public class AlgorithmFactory {

    public EncryptionAlgorithm<KeyMaker> getAlgorithm(String algorithm){
        if(algorithm == null){
           return null;
        }         
        if(algorithm.equalsIgnoreCase("ShiftUpEncryption")){
           return new ShiftUpEncryption();
           
        } else if(algorithm.equalsIgnoreCase("ShiftMultiplyEncryption")){
           return new ShiftMultiplyEncryption();
           
        } else if(algorithm.equalsIgnoreCase("XOREncryption")){
           return new XorEncryption();
        }
        
        return null;
     }
}
