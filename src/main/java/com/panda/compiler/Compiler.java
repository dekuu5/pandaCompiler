package com.panda.compiler;

import com.panda.compiler.lexicalAnalysis.LexicalAnalysis;
import com.panda.compiler.lexicalAnalysis.Token;
import com.panda.compiler.lexicalAnalysis.Toknizer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Compiler {
//    LexicalAnalysis lexicalAnalysis;
    Toknizer toknizer;

    String sourse;
    public Compiler(String fileName) throws IOException {
        System.out.println("Compiler is running...");

        byte[] bytes = Files.readAllBytes(Paths.get(fileName));
        sourse = new String(bytes, Charset.defaultCharset());
        toknizer = new Toknizer(sourse);

    }

    public void compile() {
        System.out.println("Compiling...");

        List<Token> tokens =  toknizer.scanTokens();

        for(Token t : tokens) {
            System.out.println(t);
        }
        System.out.println("Compilation finished");

    }

}
