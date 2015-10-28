package com.mypackage;

import java.util.Comparator;

public class CompareKeyStrength implements Comparator<EncryptionAlgorithm> {

    public int compare(EncryptionAlgorithm o1, EncryptionAlgorithm o2) {
        if(o1.getKeyStrength() > o2.getKeyStrength()) {
            return 1;
        } else if(o1.getKeyStrength() == o2.getKeyStrength()) {
            return 0;
        } else {
            return -1;
        }
    }

}
