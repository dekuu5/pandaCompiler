package com.panda.compiler;

import com.panda.reader.FileReaderForlexicalAnalysis;

public class Compiler {
    FileReaderForlexicalAnalysis fileReaderForlexicalAnalysis;
    public Compiler(String fileName) {
        System.out.println("Compiler is running...");
        try {
            fileReaderForlexicalAnalysis = new FileReaderForlexicalAnalysis(fileName);
        } catch (Exception e) {
            System.out.println("File not found");
        }
    }

    public void compile() {
        System.out.println("Compiling...");
        while (fileReaderForlexicalAnalysis.CheckForLine()) {
            System.out.println(fileReaderForlexicalAnalysis.getLine());
        }
        System.out.println("Compilation finished");
        fileReaderForlexicalAnalysis.close();
    }

}
