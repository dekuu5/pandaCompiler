package com.panda.compiler;

import com.panda.compiler.lexicalAnalysis.LexicalAnalysis;

public class Compiler {
    LexicalAnalysis lexicalAnalysis;
    public Compiler(String fileName) {
        System.out.println("Compiler is running...");
       lexicalAnalysis= new LexicalAnalysis(fileName);
    }

    public void compile() {
        System.out.println("Compiling...");
        lexicalAnalysis.analyze();
        lexicalAnalysis.printTokens();
        System.out.println("Compilation finished");

    }

}
