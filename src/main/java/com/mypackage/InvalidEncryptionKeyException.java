package com.mypackage;

public class InvalidEncryptionKeyException extends Exception {
    String key;
    int line;
    public int getLine() {
        return line;
    }

    public String getFileOfException() {
        return fileOfException;
    }

    public String getFunction() {
        return function;
    }

    String fileOfException;
    String function;
    
    public InvalidEncryptionKeyException(String key, int line,
            String fileOfException, String function) {
        super();
        this.key = key;
        this.line = line;
        this.fileOfException = fileOfException;
        this.function = function;
    }
    
    public String getKey() {
        return key;
    }

}
