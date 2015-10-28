package com.mypackage;

public class EncryptionLogEventArgs {
    String file;
    EncryptionAlgorithm algorithm;
    String resultFile;
    KeyMaker key;
    
    public EncryptionLogEventArgs(String file, EncryptionAlgorithm algorithm,
            String resultFile, KeyMaker key) {
        super();
        this.file = file;
        this.algorithm = algorithm;
        this.resultFile = resultFile;
        this.key = key;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((algorithm == null) ? 0 : algorithm.hashCode());
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        result = prime * result
                + ((resultFile == null) ? 0 : resultFile.hashCode());
        result = prime * result
                + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    //equals and == will be the the same when using two objects that refer to 
    //the same place in memory.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EncryptionLogEventArgs other = (EncryptionLogEventArgs) obj;
        if (algorithm == null) {
            if (other.algorithm != null)
                return false;
        } else if (!algorithm.equals(other.algorithm))
            return false;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!file.equals(other.file))
            return false;
        if (resultFile == null) {
            if (other.resultFile != null)
                return false;
        } else if (!resultFile.equals(other.resultFile))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }
}
