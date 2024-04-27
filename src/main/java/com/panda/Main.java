package com.panda;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.util.List;

import com.panda.compiler.Compiler;



public class Main {
    public static void main(String[] args) throws IOException {
        String path = "src/main/resources/input.panda";
        Compiler compiler = new Compiler( path );
        compiler.compile();
    }
}