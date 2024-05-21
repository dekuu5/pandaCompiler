package com.panda.compiler.syntaxAnalysis;

import com.panda.compiler.lexicalAnalysis.Token;
import com.panda.compiler.lexicalAnalysis.TokenType;

import java.util.ArrayList;
import java.util.List;

import static com.panda.compiler.lexicalAnalysis.TokenType.*;

public class Parser {
    private final List<Token> tokens;
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
        return new ExternalDeclaration.FunctionDefinition(identifier, (ParameterList) parameters, compoundStatement);
    }

    private CompoundStatement parseCompoundStatement() {
        consumeToken(TokenType.LCURLYBRACKET);
        List<Statement> statements = new ArrayList<>();
        while (!check(TokenType.RCURLYBRACKET)) {
            statements.add(parseStatement());
        }
        consumeToken(TokenType.RCURLYBRACKET);
        return new CompoundStatement(statements);


    }

    private Statement parseStatement() {
        Token nextToken = peekNextToken();
        return switch (nextToken.type()) {
            //  case LET -> parseVariableDeclaration();
            case IF -> parseIfStatement();
            case WHILE -> parseWhileStatement();
            case FOR -> parseForStatement();
            case RETURN -> parseReturnStatement();
            case BREAK -> parseBreakStatement();
            case CONTINUE -> parseContinueStatement();
            case PRINT -> parsePrintStatement();
            default -> error(nextToken,"Unexpected token: ");
        };
    }

    private Statement parsePrintStatement() {
        consumeToken(TokenType.PRINT);
        consumeToken(TokenType.LPARENTHESIS);
        Expression expression = parseExpression();
        consumeToken(TokenType.RPARENTHESIS);
        return new PrintStatement(expression);
    }

    private Statement parseContinueStatement() {
        consumeToken(TokenType.CONTINUE);
        consumeToken(TokenType.SEMICOLON);
        return new JumpStatement(TokenType.CONTINUE);
    }

    private Statement parseBreakStatement() {
        consumeToken(TokenType.BREAK);
        consumeToken(TokenType.SEMICOLON);
        return new JumpStatement(TokenType.BREAK);
    }

    private Statement parseReturnStatement() {
        consumeToken(RETURN);
        Expression expression = parseExpression();
        consumeToken(TokenType.SEMICOLON);
        return new JumpStatement(RETURN, expression);
    }

    private Statement parseForStatement() {
        return null;
    }

    private Statement parseWhileStatement() {
        return null;
    }

    private Statement parseIfStatement() {
        return null;
    }

    private List<ParameterDeclaration> parseParameterList() {
        return List.of();
    }

    private Identifier parseIdentifier() {

        return null;
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

    private Expression parseExpression() {
        return parseLogicalExpression();
    }

    private Expression parseLogicalExpression() {
        Expression left =  parseLogicalTerm();
        while (match(TokenType.OR, TokenType.AND)) {
            TokenType operator = previous().type();
            LogicalTerm right = parseLogicalTerm();
            left = new LogicalExpression((LogicalTerm)left, operator, (LogicalTerm)right);
        }
        return left;
    }

    private LogicalTerm parseLogicalTerm() {
    }


    private TypeSpecifier parseTypeSpecifier() {
        if (match(TokenType.INT, TokenType.FLOAT, TokenType.CHAR, TokenType.BOOL, TokenType.UINT, TokenType.STR, TokenType.VOID)) {
            return new TypeSpecifier(previous().type());
        }

        return  error( peek(), "Expected type specifier, found: " + peek());
    }

    private Declarator parseDeclarator() {
        Token nextToken = consumeNextToken();
        if (nextToken.type() != TokenType.IDENTIFIER) {
            return  error(nextToken, "Expected identifier, found: " + nextToken);
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
            error(nextToken, "Expected token of type " + type + ", found: " + nextToken);
        }
    }


    private Token consumeNextToken() {
        if (current >= tokens.size()) {
            error(previous(), "Unexpected end of input");
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

    private  <R> R error(Token token, String message) {
        throw new RuntimeException("Error at token " + token + ": " + message);
    }

}