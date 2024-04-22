package com.panda.compiler;

public class Token {
        private TokenType type;
        private String value;
        private int lineNumber;
        private int columnNumber;

        // Constructor
        public Token(TokenType type, String value, int lineNumber, int columnNumber) {
            this.type = type;
            this.value = value;
            this.lineNumber = lineNumber;
            this.columnNumber = columnNumber;
        }

        // Getters
        public TokenType getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public int getColumnNumber() {
            return columnNumber;
        }

        // Override toString() for debugging purposes
        @Override
        public String toString() {
            return "Token{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    ", position=("+ lineNumber +"," + columnNumber +")}";
        }
    }


