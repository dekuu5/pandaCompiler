package com.panda.compiler.syntaxAnalysis;

import com.panda.compiler.lexicalAnalysis.Token;
import com.panda.compiler.lexicalAnalysis.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int current = 0;
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    // Entry point for parsing
    public TranslationUnit parse() {
        return parseTranslationUnit();
    }

    // Parsing methods for different non-terminal symbols
    private TranslationUnit parseTranslationUnit() {
        List<ExternalDeclaration> externalDeclarations = new ArrayList<>();
        while (current < tokens.size()) {
            externalDeclarations.add(parseExternalDeclaration());
        }
        return new TranslationUnit(externalDeclarations);
    }

    private ExternalDeclaration parseExternalDeclaration() {
        Token nextToken = peekNextToken();
        return switch (nextToken.type()) {
            case FN -> parseFunctionDefinition();
            case LET -> parseVariableDeclaration();
            default -> throw new RuntimeException("Unexpected token: " + nextToken);
        };
    }
    
    private ExternalDeclaration.FunctionDefinition parseFunctionDefinition() {
        consumeToken(TokenType.FN);
        Identifier identifier = parseIdentifier();
        consumeToken(TokenType.LPARENTHESIS);
        List<ParameterDeclaration> parameters = parseParameterList();
        consumeToken(TokenType.RPARENTHESIS);
        CompoundStatement compoundStatement = parseCompoundStatement();
        return new ExternalDeclaration.FunctionDefinition(identifier, parameters, compoundStatement);
    }

    private ExternalDeclaration.VariableDeclaration parseVariableDeclaration() {
        consumeToken(TokenType.LET);
        Declarator declarator = parseDeclarator();
        consumeToken(TokenType.COLON);
        TypeSpecifier typeSpecifier = parseTypeSpecifier();
        consumeToken(TokenType.ASSIGNMENT);
        Expression expression = parseExpression();
        consumeToken(TokenType.SEMICOLON);
        return new ExternalDeclaration.VariableDeclaration(declarator, typeSpecifier, expression);
    }

    private Declarator parseDeclarator() {
        Token nextToken = consumeNextToken();
        if (nextToken.type() != TokenType.IDENTIFIER) {
            throw new RuntimeException("Expected an identifier, found: " + nextToken);
        }
        return new Declarator(new Identifier(nextToken.value()));
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
    private void consumeToken(TokenType type) {
        Token nextToken = consumeNextToken();
        if (nextToken.type() != type) {
            throw new RuntimeException("Expected token of type " + type + ", found: " + nextToken);
        }
    }
    private Token consumeNextToken() {
        if (current >= tokens.size()) {
            throw new RuntimeException("Unexpected end of input");
        }
        return tokens.get(current++);
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
    private Token peekNextToken() {
        if (current + 1 >= tokens.size()) {
            throw new RuntimeException("Unexpected end of input");
        }
        return tokens.get(current + 1);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

}
