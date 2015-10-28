package com.mypackage;

import java.util.Random;

public class KeyMaker {
    private int key;
    
    //default constructor generates random key
    public KeyMaker() {
        Random rand = new Random();
        key = rand.nextInt(100) + 1; //plus one so that the won't be 0 
    }                                //and it would not encrypt
    
    //generates a new key
    public void generateKey() {
        Random rand = new Random();
        key = rand.nextInt(100) + 1;
    }
    
    //setter for key
    public void setKey(int newKey) {
        key = newKey;
    }
    
    //getter for key
    public int getKey() {
        return key;
    }
}
