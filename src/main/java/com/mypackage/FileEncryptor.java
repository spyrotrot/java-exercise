package com.mypackage;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileEncryptor {
    private final String ENCODING = "UTF-8";
    protected EncryptionAlgorithm<KeyMaker> algorithm;
    protected FileAccessor readWriteFile;
    protected KeyMaker key;
    private List<EncryptionListener> listeners = new ArrayList<EncryptionListener>();

    public void addListener(EncryptionListener toAdd) {
        listeners.add(toAdd);
    }
    
    //constructor to set the encryption algorithm
    public FileEncryptor(EncryptionAlgorithm<KeyMaker> nAlgorithm) {
        algorithm = nAlgorithm;
        readWriteFile = new FileAccessor();
        key = new KeyMaker();
        
    }
    //default constructor
    public FileEncryptor() {
        
    }
    
    //encrypts file
    public void encryptFile(String originalFilePath, String newFilePath, 
            String keyPath) throws IOException, InvalidEncryptionKeyException {
        
        int count = 0;
        Method[] methods = this.getClass().getMethods();

        for(Method method : methods){
            count += hashName(method.getName());
        }
        count = count % 100;
        key.setKey(count);
        
        String encryptedFile; //the encrypted content of the file
        EncryptionLogEventArgs e = new EncryptionLogEventArgs(originalFilePath, 
                algorithm, newFilePath, key);
        Lock lock = new ReentrantLock();
        readWriteFile.setBufferReader(originalFilePath, ENCODING);
      //notify all listeners of encryption start
        for (EncryptionListener el : listeners) {
            el.encryptionStarted();
        }
        
        encryptedFile = algorithm.encrypt(readWriteFile.readFile(),
                key);
      //notify all listeners of encryption end
        for (EncryptionListener el : listeners) {
            el.encryptionEnded(e);
        }
        
        readWriteFile.closeReaderStream();
        readWriteFile.setBufferWriter(newFilePath, ENCODING);
        readWriteFile.writeLine(encryptedFile);
        readWriteFile.closeWriterStream();
        lock.lock();
        readWriteFile.setBufferWriter(keyPath, ENCODING);
        readWriteFile.writeLine(String.valueOf(key.getKey()));
        readWriteFile.closeWriterStream();
        lock.unlock();
    }
    
    //decrypts file
    public void decryptFile(String originalFilePath, String newFilePath, 
            String keyPath) throws IOException, InvalidEncryptionKeyException {
        String encryptedFile;  //the encrypted content of the file
        String strKey;
        EncryptionLogEventArgs e;
        KeyMaker k = new KeyMaker();
        Lock lock = new ReentrantLock();
        
        readWriteFile.setBufferReader(originalFilePath, ENCODING);
        encryptedFile = readWriteFile.readFile();
        readWriteFile.closeReaderStream();
        lock.lock();
        readWriteFile.setBufferReader(keyPath, ENCODING);
        strKey = readWriteFile.readLine();
        k.setKey(Integer.parseInt(strKey));
        e = new EncryptionLogEventArgs(originalFilePath, 
                algorithm, newFilePath, k);
        if(!Menu.isNumeric(strKey) || Integer.parseInt(strKey) < 0 
                || Integer.parseInt(strKey) > 101) {
            throw new InvalidEncryptionKeyException(strKey, 
                    Thread.currentThread().getStackTrace()[2].getLineNumber(), 
                    getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString(), "decryptFile");
        }
        key.setKey(Integer.parseInt(strKey));
        readWriteFile.closeReaderStream();
        lock.unlock();
        readWriteFile.setBufferWriter(newFilePath, ENCODING);
      //notify all listeners of decryption start
        for (EncryptionListener el : listeners) {
            el.decryptionStarted();
        }
        
        readWriteFile.writeFile(algorithm.decrypt(encryptedFile,
                key));
        //notify all listeners of decryption end
        for (EncryptionListener el : listeners) {
            el.decryptionEnded(e);
        }
        
        readWriteFile.closeWriterStream();
    }
    
    public EncryptionAlgorithm<KeyMaker> getAlgorithm() {
        return algorithm;
    }

    public void generateNewKey() {
        //key.generateKey();    
    }
    
    protected int hashName(String name) {
        char[] c_arr = name.toCharArray();
        int count = 0;
        for(char c : c_arr) {
            count += (int)c;
        }
        return count;
    }
  
}
