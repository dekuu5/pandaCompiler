package com.panda.compiler.lexicalAnalysis;

public record Token(TokenType type, String value, int lineNumber, int columnNumber) {
    // Constructor

    // Override toString() for debugging purposes
    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", position=(" + lineNumber + "," + columnNumber + ")}";
    }
}


