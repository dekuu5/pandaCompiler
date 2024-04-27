package com.panda.compiler.lexicalAnalysis;

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
            if(isThisCommentLine(line)){
                lineNumber++;
                columnNumber =1;
                continue;
            }
            analyzeLine(line);
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
                }else if (currentChar == '"') {
                    StringBuilder value = new StringBuilder();
                    while(i<line.length()-1 ){
                        if (line.charAt(i) == '"' && line.charAt(i-1) == '\\')
                            break;
                        value.append(line.charAt(i));
                        i++;
                    }
                    if (line.charAt(i) == '"')
                        value.append(line.charAt(i));

                    tokens.add(new Token(
                            getTokenType(value.toString()),
                            value.toString(),
                            lineNumber,
                            columnNumber
                    ));
                    columnNumber += value.length();

                }
                else if(isAlphanumeric(currentChar)) {
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
        if (value.matches("[a-zA-Z_][a-zA-Z0-9_]*")) { // matches any word that start with a letter or _ and have any number of letters or characters
            return TokenType.IDENTIFIER;

        } else if (value.matches("\"(\\.|[^\"])*")){
            return TokenType.STRING;
        }
        else if (value.matches("[-+]?[0-9]+")||value.matches("[-+]?[0-9]*\\.[0-9]+")) { // matches any positive or negative integer or float number
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
    private boolean isThisCommentLine(String line){
        int i =0;
        char currentChar;
        if(i < line.length()) currentChar=line.charAt(i); else return false;
        while (Character.isWhitespace(currentChar)){ i++; currentChar=line.charAt(i);}
        if (currentChar == '/' && i + 1 < line.length() && line.charAt(i + 1) == '/')  return true;
        if (currentChar == '/' && i + 1 < line.length() && line.charAt(i + 1) == '*') return true;
        if (i + 1 < line.length() && currentChar=='*') return true;
        return i + 1 < line.length() && line.charAt(i) == '*' && line.charAt(i + 1) == '/';


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
