package com.panda.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderForlexicalAnalysis {
    // this class is responsible for reading the source code file
    // and returning line by line the content of the file for tokenization

    String fileName;
    FileReader fileReader;
    BufferedReader bufferedReader;


    String line;

    public FileReaderForlexicalAnalysis(String fileName) throws FileNotFoundException {
        System.out.println("FileReaderFor-lexicalAnalysis is running...");
        this.fileName = fileName;
        try {
            this.fileReader = new FileReader(fileName);
            this.bufferedReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            throw e;
        }

    }

    public String getLine() {
        return line;
    }
    public Boolean CheckForLine(){
        try {
            line = bufferedReader.readLine();
            return line != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
