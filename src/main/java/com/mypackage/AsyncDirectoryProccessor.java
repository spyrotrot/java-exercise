package com.mypackage;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import com.google.inject.*;

//async time on test: 457
public class AsyncDirectoryProccessor implements IDirectoryProccessor {
    private EncryptionAlgorithm<KeyMaker> algorithm;
    private EncryptionListener logger;
    @Inject
    public AsyncDirectoryProccessor(EncryptionAlgorithm<KeyMaker> algorithm,
            EncryptionListener logger) {
        this.algorithm = algorithm;
        this.logger = logger;
        
    }
    
//encrypts a directory using a thread for each file
    public void encryptDirectory(String folder) throws IOException,
            InvalidEncryptionKeyException, InterruptedException, ExecutionException {
        File[] files = new File(folder).listFiles();
        String absolutePath = files[0].getAbsolutePath();
        long startTime = System.currentTimeMillis();
        int key = new KeyMaker().getKey();
        ExecutorService service = Executors.newFixedThreadPool(files.length);
        
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
                Runnable runnable = new FileEncryptorThread(absolutePath, 
                        absolutePath.substring(0, 
                                absolutePath.lastIndexOf(File.separator) + 1)
                        + "encrypted" + File.separator
                        + absolutePath.substring(
                                absolutePath.lastIndexOf(File.separator) + 1), 
                                absolutePath.substring(0,
                                        absolutePath.lastIndexOf(File.separator) + 1)
                                + "encrypted"+ File.separator +"key.txt", 
                                key, 
                                Operation.encrypt,
                                algorithm);
                ((FileEncryptorThread)runnable).addListener(logger);
                service.execute(runnable);
            }
        }
        service.shutdownNow();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        EncryptionLog4JLogger.logger.debug("encryption ended in " 
                + (System.currentTimeMillis() - startTime) + " milliseconds");
        
    }
    
  //decrypts a directory using a thread for each file
    public void decryptDirectory(String folder) throws IOException,
            InvalidEncryptionKeyException, InterruptedException, ExecutionException {
        File[] files = new File(folder).listFiles();
        String absolutePath = files[0].getAbsolutePath();
        long startTime = System.currentTimeMillis();
        int key = new KeyMaker().getKey();
        ExecutorService service = Executors.newFixedThreadPool(files.length);
        
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
                Runnable runnable = new FileEncryptorThread(absolutePath, 
                        absolutePath.substring(0, 
                                absolutePath.lastIndexOf(File.separator) + 1)
                        + "decrypted" + File.separator
                        + absolutePath.substring(
                                absolutePath.lastIndexOf(File.separator) + 1), 
                                absolutePath.substring(0,
                                        absolutePath.lastIndexOf(File.separator) + 1)
                                + File.separator +"key.txt", 
                                key, 
                                Operation.decrypt,
                                algorithm);
                ((FileEncryptorThread)runnable).addListener(logger);
                service.execute(runnable);
            }
        }
        service.shutdownNow();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        EncryptionLog4JLogger.logger.debug("decryption ended in " 
                + (System.currentTimeMillis() - startTime) + " milliseconds");
        
    }

}
