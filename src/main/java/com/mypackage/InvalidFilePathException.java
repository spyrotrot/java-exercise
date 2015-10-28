package com.mypackage;

public class InvalidFilePathException extends Exception {
    String file;
    int line;
    String fileOfException;
    String function;
    
    public InvalidFilePathException(String file, int line,
            String fileOfException, String function) {
        super();
        this.file = file;
        this.line = line;
        this.fileOfException = fileOfException;
        this.function = function;
    }
    
    public int getLine() {
        return line;
    }

    public String getFileOfException() {
        return fileOfException;
    }

    public String getFunction() {
        return function;
    }

    public String getFile() {
        return file;
    }

}
