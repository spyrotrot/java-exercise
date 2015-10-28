package com.mypackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FileAccessor {
    private BufferedReader bufferReader;
    private BufferedWriter bufferWriter;
    
    //sets the file to read from
    public Boolean setBufferReader(String path, String encoding) throws UnsupportedEncodingException {
        try {
            bufferReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(path), encoding)); 
        } catch (FileNotFoundException e) {              
            System.out.println("File not found.");
            return false;
        }
        return true;
    }
    
    //reads a line
    public String readLine() throws IOException {
        return bufferReader.readLine();
    }
    
    //reads whole file
    public String readFile() throws IOException {
        StringBuilder builder = new StringBuilder();
        String aux = "";

        while ((aux = bufferReader.readLine()) != null) {
            builder.append(aux+"\n");
        }

        String text = builder.toString();
        return text;
    }
    
    //closes stream to read from file
    public void closeReaderStream() throws IOException {
        bufferReader.close();
    }
    
  //sets the file to write to
    public void setBufferWriter(String path, String encoding) throws IOException {
        bufferWriter = new BufferedWriter
                (new OutputStreamWriter(new FileOutputStream(path), 
                        encoding));
    }
    
    //writes whole file (with newlines)
    public void writeFile(String toWrite) throws IOException {
        String[] lines = toWrite.split("\\r?\\n");
        for(int i = 0; i < lines.length-1; i++) {
            bufferWriter.write(lines[i]);
            if(i < lines.length-2) {
                bufferWriter.newLine();
            }
        }
    }
    
    //writes everything it gets in one line
    public void writeLine(String toWrite) throws IOException {
        bufferWriter.write(toWrite);
    }
    
  //closes stream to write to file
    public void closeWriterStream() throws IOException {
        bufferWriter.close();
    }
}
