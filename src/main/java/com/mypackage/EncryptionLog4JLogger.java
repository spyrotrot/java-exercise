package com.mypackage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class EncryptionLog4JLogger implements EncryptionListener {
    private long startTime;
    private long endTime;
    
    public EncryptionLog4JLogger() {
        startTime = 0;
    }
    
    final static Logger logger = LogManager.getLogger(EncryptionLog4JLogger.class);

    //logs at encryption start
    public void encryptionStarted() {
        logger.debug("encryption started.");
        startTime = System.currentTimeMillis();
        
    }
    
    //logs at encryption end
    public void encryptionEnded(EncryptionLogEventArgs e) {
        endTime = System.currentTimeMillis() - startTime;
        logger.debug("The encryption for file " + e.file 
                + " with algorithm " + e.algorithm.toString() 
                + " and key " + e.key.getKey()
                + " took " + endTime + " miliseconds. "
                        + "The encrypted file is located at " + e.resultFile);
        
    }

    //logs at decryption start
    public void decryptionStarted() {
        logger.debug("decryption started.");
        startTime = System.currentTimeMillis();
        
    }

    //logs at decryption end
    public void decryptionEnded(EncryptionLogEventArgs e) {
        endTime = System.currentTimeMillis() - startTime;
        logger.debug("The decryption for file " + e.file 
                + " with algorithm " + e.algorithm.toString() 
                + " and key " + e.key.getKey()
                + " took " + endTime + " miliseconds. "
                        + "The encrypted file is located at " + e.resultFile);
        
    }

}
