package com.panda.compiler.syntaxAnalysis;

import com.panda.compiler.lexicalAnalysis.Token;
import com.panda.compiler.lexicalAnalysis.TokenType;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int current = 0;
    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }
    public void parse() {
        while (!isAtEnd()) {
         //   externalDeclaration();
        }
    }


    // Utility methods for parsing
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type() == type;
    }

    private void advance() {
        if (!isAtEnd()) current++;
    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

}
