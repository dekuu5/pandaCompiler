package com.panda.compiler.syntaxAnalysis;

import com.panda.compiler.lexicalAnalysis.Token;
import com.panda.compiler.lexicalAnalysis.TokenType;
import com.panda.compiler.lexicalAnalysis.Toknizer;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int current = 0;
    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }
    public void parse() {
        while (!isAtEnd()) {
            externalDeclaration();
        }
    }

    // Parses an external declaration
    private void externalDeclaration() {
        if (match(TokenType.FN)) {
            functionDefinition();
        } else {
            declaration();
        }
    }

    // Parses a function definition
    private void functionDefinition() {
        while (!check(TokenType.LEFT_BRACE)) {
            declarationSpecifier();
        }
        // Parse the rest of the function definition
    }

    // Parses a declaration specifier
    private void declarationSpecifier() {
        if (match(TokenType.TYPE_SPECIFIER)) {
            // Parse type specifier
        } else if (match(TokenType.STORAGE_CLASS_SPECIFIER)) {
            // Parse storage class specifier
        } else if (match(TokenType.TYPE_QUALIFIER)) {
            // Parse type qualifier
        }
    }

    // Parses a declaration
    private void declaration() {

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

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
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
