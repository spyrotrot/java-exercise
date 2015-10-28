package com.mypackage;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JAXBAdapter extends XmlAdapter<String, EncryptionAlgorithm<KeyMaker>> {

    @Override
    public String marshal(EncryptionAlgorithm<KeyMaker> arg0) {
        return arg0.toString();
    }

    @Override
    public EncryptionAlgorithm<KeyMaker> unmarshal(String arg0) {
        if(arg0.equals("ShiftMultiplyEncryption")) {
            return new ShiftMultiplyEncryption();
        } else if(arg0.equals("XOREncryption")) {
            return new XorEncryption();
        }
        return new ShiftUpEncryption(); //shift up encryption as default
    }


}
