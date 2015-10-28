package com.mypackage;

public interface EncryptionListener {
    public void encryptionStarted();
    public void encryptionEnded(EncryptionLogEventArgs e);
    public void decryptionStarted();
    public void decryptionEnded(EncryptionLogEventArgs e);
}
