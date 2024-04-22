package com.panda;


import com.panda.compiler.Compiler;

public class Main {
    public static void main(String[] args) {
        String fileName = "src/main/resources/input.panda";
        // the compiler object has all the logic of the compiler itself,
        // and it is in a separate package
        Compiler compiler = new Compiler( fileName );
        compiler.compile();
    }
}