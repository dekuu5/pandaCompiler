package com.panda.compiler;

import com.panda.reader.FileReaderForlexicalAnalysis;

import java.util.LinkedList;
import java.util.List;

public class LexicalAnalysis {
    private List<Token> tokens;
    FileReaderForlexicalAnalysis fileReaderForlexicalAnalysis;
    int lineNumber;
    int columnNumber;

    public LexicalAnalysis(String fileName) {
        tokens= new LinkedList<>();
        try {
            fileReaderForlexicalAnalysis = new FileReaderForlexicalAnalysis(fileName);
        } catch (Exception e) {
            System.out.println("File not found");
        }
        lineNumber =1;
        columnNumber =1;
    }
    public void analyze(){
        while(fileReaderForlexicalAnalysis.CheckForLine()){
            String line = fileReaderForlexicalAnalysis.getLine();
            analyzeLine(line);
            printTokens();
            lineNumber++;
            columnNumber =1;
        }
        fileReaderForlexicalAnalysis.close();
    }

    private void analyzeLine(String line) {
          int i = 0;
        while(i<line.length()){
                char currentChar = line.charAt(i);
                if(Character.isWhitespace(currentChar)){
                    if (currentChar == '\n') {
                        return;
                    } else {
                        columnNumber++;
                    }
                    i++;
                }else if(isAlphanumeric(currentChar)) {
                    StringBuilder value = new StringBuilder();
                    while(i<line.length() && !Character.isWhitespace(line.charAt(i)) && isAlphanumeric(line.charAt(i))){
                        value.append(line.charAt(i));
                        i++;
                    }
                    tokens.add(new Token(getTokenType(value.toString()), value.toString(), lineNumber, columnNumber));
                    columnNumber += value.length();
                }else {
                  TokenType type = getSpecialCharacterType(currentChar);
                  tokens.add(new Token(type, String.valueOf(currentChar), lineNumber, columnNumber));
                  columnNumber++;
                  i++;
                }

          }
    }

    private boolean isAlphanumeric(char currentChar) {
        return Character.isLetterOrDigit(currentChar) || currentChar == '_';
    }
    private static TokenType getTokenType(String value) {
        // Example: Determine the TokenType based on the value
        if (value.matches("[a-zA-Z]+")) {
            return TokenType.IDENTIFIER;
        } else if (value.matches("\\d+")) {
            return TokenType.NUMBER;
        } else {
            return TokenType.UNKNOWN;
        }
    }

    private TokenType getSpecialCharacterType(char currentChar) {
        return switch (currentChar) {
            case '+', '-', '*', '/' -> TokenType.OPERATOR;
            case '(', ')' -> TokenType.PARENTHESIS;
            case ';' -> TokenType.SEMICOLON;
            case '=' -> TokenType.ASSIGNMENT;
            default -> TokenType.UNKNOWN;
        };
    }

    public List<Token> getTokens() {
        return tokens;
    }
    public void printTokens(){
        for (Token token: tokens){
            System.out.println(token);
        }
    }




}
