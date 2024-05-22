package com.panda.compiler.lexicalAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.panda.compiler.lexicalAnalysis.TokenType.*;

public class Toknizer {

    String source;
    int line = 1;
    int current = 0;
    int start = 0;

    List<Token> tokens = new ArrayList<>();
    static HashMap<String,TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fn",    FN);
        keywords.put("if",     IF);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("true",   TRUE);
        keywords.put("let",    LET);
        keywords.put("while",  WHILE);
        keywords.put("break",  BREAK);
        keywords.put("continue", CONTINUE);
        keywords.put("input", INPUT);

    }
    static HashMap<String,TokenType> typeSpecifier;
    static {
        typeSpecifier = new HashMap<>();
        typeSpecifier.put("int", INT);
        typeSpecifier.put("float", FLOAT);
        typeSpecifier.put("double", BOOL);
        typeSpecifier.put("char", CHAR);
        typeSpecifier.put("void", VOID);
        typeSpecifier.put("str", STR);
        typeSpecifier.put("uint", UINT);


    }


    public Toknizer(String source){
        this.source = source;

    }

    public List<Token> scanTokens(){
        while (!isAtEnd()){
            start=current;
            scanToken();
        }
        tokens.add(new Token(EOF, "",null,line ));
        return tokens;
    }

    /*
     * this function is the driver for the tokenizer
     * it takes char than switch all possible scenarios
     *
     * */
    private void scanToken() {
        char c = advance();
        switch (c){
            // logical operators
            case '!':
                addToken(checkAdvance('=')? NOTEQUAL: NOT);
                break;
            case '>':
                addToken(checkAdvance('=')? BIGGEREQUAL: BIGGER);
                break;
            case '<':
                addToken(checkAdvance('=')? SMALLEREQUAL: SMALLER);
                break;
            case '=':
                addToken(checkAdvance('=')? EQUAL: ASSIGNMENT);
                break;

            // Arithmetic operators

            case '+': addToken(PLUS); break;
            case '-': addToken(MINUS); break;
            case '*': addToken(MULT);break;
            case '^': addToken(POWER); break;

            case '/':
                if (checkAdvance('/')){
                    while (peek() != '\n' && !isAtEnd()) advance();
                }else
                    addToken(DIV);
                break;
            case ':':
                addToken(COLON);
                break;
            case'[':
                addToken(LBRACKET);
                break;
            case ']':
                addToken(RBRACKET);
                break;
            case ',':
                addToken(COMMA);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '(':
                addToken(LPARENTHESIS);
                break;
            case ')':
                addToken(RPARENTHESIS);
                break;
            case '{':
                addToken(LCURLYBRACKET);
                break;
            case '}':
                addToken(RCURLYBRACKET);
                break;
            case ' ': // Ignore whitespace.
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                line++;
                break;
            case '"':
                string();
                break;

            default:
                if (isDigit(c)){
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {//hand the error part
                    throw new RuntimeException("unknown token type: " + c);

//                    addToken(UNKNOWN);
//                    System.out.println("error message"+ c + line);
                }
                break;


        }

    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start,current);
        TokenType type = keywords.get(text);
        if (type == null) {
            type = typeSpecifier.get(text);
            if (type == null) {
                type = IDENTIFIER;
            }
        }
        addToken(type);
    }

    private boolean checkAdvance(char c) {
        if (isAtEnd()) return false;
        if (peek() != c) return false;
        advance();
        return true;
    }
    private void number(){
        while(isDigit(peek())) advance();
        // 123123.123123
        if(peek()=='.'&& isDigit(peekNext())){
            advance();
            while(isDigit(peek())) advance();
            addToken(NUMBERFLOAT,
                    Double.parseDouble(source.substring(start, current)));
            return;
        }
        addToken(NUMBERINT,
                Double.parseDouble(source.substring(start, current)));

    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private void string() {
        StringBuilder stringBuilder = new StringBuilder();
        while (peek() != '"' && !isAtEnd()) {
            char currentChar = advance();
            if (currentChar == '\n') {
                line++;
            }
            if (currentChar == '\\') {
                // Handle escape sequences
                stringBuilder.append(handleEscapeSequence());
            } else {
                stringBuilder.append(currentChar);
            }
        }

        if (isAtEnd()) {
            // Handle error: Unterminated string literal
            System.err.println("Unterminated string literal at line " + line);
            return;
        }

        // Consume the closing double quote
        advance();

        // Add the string token
        addToken(STRING, stringBuilder.toString());
    }

    private char handleEscapeSequence() {
        char nextChar = peek();
        switch (nextChar) {
            case 'n': return '\n'; // newline
            case 't': return '\t'; // tab
            case 'r': return '\r'; // carriage return
            case '"': return '"';  // double quote
            case '\\': return '\\'; // backslash
            default:
                // Handle unrecognized escape sequences
                System.err.println("Unrecognized escape sequence '\\" + nextChar + "' at line " + line);
                return nextChar;
        }
    }

    private char advance(){
        return source.charAt(current++);
    }
    private char peek(){
        return source.charAt(current);
    }
    private char peekNext(){
        if(current+1<source.length()){
            return source.charAt(current+1);
        } else return '\0';
    }
    private void addToken(TokenType t){
        addToken(t, null);
    }
    private void addToken(TokenType t, Object l){
        String text = source.substring(start,current);
        tokens.add(new Token(
                t,
                text,
                l,
                line));
    }

}