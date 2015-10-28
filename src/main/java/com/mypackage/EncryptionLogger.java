package com.mypackage;

public class EncryptionLogger implements EncryptionListener {
    private long startTime;
    private long endTime;
    
    public EncryptionLogger() {
        startTime = 0;
        endTime = 0;
    }
    
    //logs at encryption start
    public void encryptionStarted() {
        startTime=System.currentTimeMillis();
        
    }

    //logs at encryption end
    public void encryptionEnded(EncryptionLogEventArgs e) {
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("The encryption for file " + e.file 
                + " with algorithm " + e.algorithm.toString() 
                + " and key " + e.key.getKey()
                + " took " + endTime + " miliseconds. "
                        + "The encrypted file is located at " + e.resultFile);

    }

    //logs at decryption start
    public void decryptionStarted() {
        startTime=System.currentTimeMillis();

    }

    //logs at decryption end
    public void decryptionEnded(EncryptionLogEventArgs e) {
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("The decryption for file " + e.file 
                + " with algorithm " + e.algorithm.toString() 
                + " and key " + e.key.getKey()
                + " took " + endTime + " miliseconds. "
                        + "The encrypted file is located at " + e.resultFile);

    }

}
