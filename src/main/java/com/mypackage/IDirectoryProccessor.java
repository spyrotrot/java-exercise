package com.mypackage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface IDirectoryProccessor {
    public void encryptDirectory(String folder) throws IOException, 
    InvalidEncryptionKeyException, InterruptedException, ExecutionException;
    public void decryptDirectory(String folder) throws IOException,
    InvalidEncryptionKeyException, InterruptedException, ExecutionException;

}
