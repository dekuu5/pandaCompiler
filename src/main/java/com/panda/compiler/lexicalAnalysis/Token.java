package com.panda.compiler.lexicalAnalysis;

public record Token(TokenType type, String value, Object literal, int line) {
    // Constructor

    // Override toString() for debugging purposes
    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +

                ", position=(" + line +")}";
    }
}


