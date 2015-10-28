package com.mypackage;

import java.io.File;
import java.io.IOException;

public class FolderEncryptor {
    private FileEncryptor fileEncryptor;
    
    //constructor for initialising each file encryptor
    public FolderEncryptor(FileEncryptor nFileEncryptor) {
        fileEncryptor = nFileEncryptor;
    }
    
    public FileEncryptor getFileEncryptor() {
        return fileEncryptor;
    }

    //encrypts each txt file in a folder
    public void encryptFolder(String folder) throws IOException, 
    InvalidEncryptionKeyException {
        File[] files = new File(folder).listFiles();
        String absolutePath = files[0].getAbsolutePath();
        long startTime = System.currentTimeMillis();
        new File(absolutePath.substring(0, 
                absolutePath.lastIndexOf(File.separator) + 1)
                + "encrypted").mkdir();
        for (File file : files) {
            absolutePath = file.getAbsolutePath();
            if(!file.isDirectory() && absolutePath.substring(
                    absolutePath.lastIndexOf(".")).equals(".txt")
                    && !(absolutePath.substring(
                            absolutePath.lastIndexOf(File.separator) 
                            + 1).equals("key.txt"))) {
                fileEncryptor.encryptFile(absolutePath, 
                        absolutePath.substring(0, 
                                absolutePath.lastIndexOf(File.separator) + 1)
                        + "encrypted" + File.separator
                        + absolutePath.substring(
                                absolutePath.lastIndexOf(File.separator) + 1),
                        absolutePath.substring(0,
                                absolutePath.lastIndexOf(File.separator) + 1)
                        + "encrypted"+ File.separator +"key.txt");
            }
        }
        EncryptionLog4JLogger.logger.debug("encryption ended in " 
                + (System.currentTimeMillis() - startTime) + " milliseconds");
    }
    
    //decrypts each txt file in a folder
    public void decryptFolder(String folder) throws IOException, 
    InvalidEncryptionKeyException {
        File[] files = new File(folder).listFiles();
        String absolutePath = files[0].getAbsolutePath();
        long startTime = System.currentTimeMillis();
        new File(absolutePath.substring(0, 
                absolutePath.lastIndexOf(File.separator) + 1)
                + "decrypted").mkdir();
        for (File file : files) {
            absolutePath = file.getAbsolutePath();
            if(!file.isDirectory() && absolutePath.substring(
                    absolutePath.lastIndexOf(".")).equals(".txt")
                    && !(absolutePath.substring(
                            absolutePath.lastIndexOf(File.separator) 
                            + 1).equals("key.txt"))) {
                fileEncryptor.decryptFile(absolutePath, 
                        absolutePath.substring(0, 
                                absolutePath.lastIndexOf(File.separator) + 1)
                        + "decrypted" + File.separator
                        + absolutePath.substring(
                                absolutePath.lastIndexOf(File.separator) + 1),
                        absolutePath.substring(0,
                                absolutePath.lastIndexOf(File.separator) + 1)
                        +"key.txt");
            }
        }
        EncryptionLog4JLogger.logger.debug("decryption ended in " 
                + (System.currentTimeMillis() - startTime) + " milliseconds");
    }

}
