package com.panda.compiler;

import com.panda.compiler.lexicalAnalysis.Token;
import com.panda.compiler.lexicalAnalysis.Toknizer;
import com.panda.compiler.syntaxAnalysis.ASTPrinter;
import com.panda.compiler.syntaxAnalysis.GrammarRules;
import com.panda.compiler.syntaxAnalysis.Parser;
import com.panda.compiler.syntaxAnalysis.*;

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
        // Parser parser = new Parser(tokens);
        for(Token t : tokens) {
            System.out.println(t);
        }

       Parser parser =  new Parser(tokens);
        GrammarRules tree = parser.parse();
        System.out.println("Parser created");
        ASTPrinter printer = new ASTPrinter();
        System.out.println("ASTPrinter created");

        String ast = tree.accept(printer);
        System.out.println("ASTPrinter accepted");
        System.out.println(ast);
        System.out.println("Compilation finished");

    }

}