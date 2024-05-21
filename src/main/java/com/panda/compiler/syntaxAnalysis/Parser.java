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
    public GrammarRules parse() {
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
        Token nextToken = peek();
        return switch (nextToken.type()) {
            case FN -> parseFunctionDefinition();
            case LET -> parseVariableDeclaration();
            case EOF -> parseEOFDeclaration();
            default -> throw new RuntimeException("Unexpected token: " + nextToken);
        };
    }

    private EOFDeclaration parseEOFDeclaration() {
        consumeToken(TokenType.EOF);
        return new EOFDeclaration();
    }
    private FunctionDefinition parseFunctionDefinition() {
        consumeToken(TokenType.FN);
        Identifier identifier = parseIdentifier();
        consumeToken(TokenType.LPARENTHESIS);
        List<ParameterDeclaration> parameters;
        if (!check(TokenType.RPARENTHESIS)) {
            parameters = parseParameterList();
        consumeToken(TokenType.RPARENTHESIS);
        CompoundStatement compoundStatement = parseCompoundStatement();
        return new FunctionDefinition(identifier, (ParameterList) parameters, compoundStatement);}
        else {
            consumeToken(TokenType.RPARENTHESIS);
            CompoundStatement compoundStatement = parseCompoundStatement();
            return new FunctionDefinition(identifier,  compoundStatement);
        }
    }

    private CompoundStatement parseCompoundStatement() {
        consumeToken(TokenType.LCURLYBRACKET);
        List<Statement> statements = new ArrayList<>();
        while (!check(TokenType.RCURLYBRACKET)) {
            statements.add((Statement) parseStatement());
        }
        consumeToken(TokenType.RCURLYBRACKET);
        return new CompoundStatement(statements);   
        

    }

    private GrammarRules parseStatement() {
        Token nextToken = peek();
        return switch (nextToken.type()) {
            case LET -> parseVariableStatementdeclartion();
            case IF -> parseIfStatement();
            case WHILE -> parseWhileStatement();
            case FOR -> parseForStatement();
            case RETURN -> parseReturnStatement();
            case BREAK -> parseBreakStatement();
            case CONTINUE -> parseContinueStatement();
            case PRINT -> parsePrintStatement();
            case IDENTIFIER -> parseAssignmentStatement();
            case INPUT -> parseInputStatement();
            default -> error(nextToken,"Unexpected token: ");
        };
    }

    private Statement parseVariableStatementdeclartion() {
        return new VarableDeclarationStatement(parseVariableDeclaration());
    }

    private GrammarRules parseInputStatement() {
        consumeToken(TokenType.INPUT);
        consumeToken(TokenType.LPARENTHESIS);
        consumeToken(TokenType.RPARENTHESIS);
        consumeToken(TokenType.SEMICOLON);
        return new InputStatement();
    }

    private Statement parsePrintStatement() {
        consumeToken(TokenType.PRINT);
        consumeToken(TokenType.LPARENTHESIS);
        Expression expression = parseExpression();
        consumeToken(TokenType.RPARENTHESIS);
        consumeToken(TokenType.SEMICOLON);
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
        consumeToken(TokenType.FOR);
        consumeToken(TokenType.LPARENTHESIS);
        Statement initialization = parseAssignmentStatement();
        Expression condition = parseExpression();
        consumeToken(TokenType.SEMICOLON);
        Expression increment = parseExpression();
        consumeToken(TokenType.RPARENTHESIS);
        GrammarRules body = parseStatement();
        return new ControlFlowStatement.For(initialization, condition, increment, (CompoundStatement) body);
    }

    private Statement parseAssignmentStatement() {
        Declarator ide = parseDeclarator();
        consumeToken(TokenType.ASSIGNMENT);
        Expression expression = parseExpression();
        consumeToken(TokenType.SEMICOLON);
        return new AssignmentStatement(ide, expression);


    }

    private Statement parseWhileStatement() {
        consumeToken(TokenType.WHILE);
        consumeToken(TokenType.LPARENTHESIS);
        Expression condition = parseExpression();
        consumeToken(TokenType.RPARENTHESIS);
        GrammarRules body = parseStatement();
        return new ControlFlowStatement.While(condition, (CompoundStatement) body);
    }

    private Statement parseIfStatement() {
        consumeToken(TokenType.IF);
        consumeToken(TokenType.LPARENTHESIS);
        Expression condition = parseExpression();
        consumeToken(TokenType.RPARENTHESIS);
        GrammarRules thenBranch = parseStatement();
        if (match(TokenType.ELSE)) {
            GrammarRules elseBranch = parseStatement();
            return new ControlFlowStatement.If(condition, (CompoundStatement) thenBranch, (CompoundStatement) elseBranch);
        }
        return new ControlFlowStatement.If(condition, (CompoundStatement) thenBranch);

    }

    private List<ParameterDeclaration> parseParameterList() {
        List<ParameterDeclaration> parameters = new ArrayList<>();
        parameters.add(parseParameterDeclaration());
        while (match(TokenType.COMMA)) {
            parameters.add(parseParameterDeclaration());
        }
        return parameters;
    }

    private ParameterDeclaration parseParameterDeclaration() {
        TypeSpecifier typeSpecifier = parseTypeSpecifier();
        Declarator declarator = parseDeclarator();
        return new ParameterDeclaration(typeSpecifier, declarator);
    }

    private Identifier parseIdentifier() {
        Token nextToken = peek();
        if (nextToken.type() != TokenType.IDENTIFIER) {
            error(nextToken, "Expected identifier, found: " + nextToken);
        }
        advance();
        return new Identifier(nextToken.value());

    }

    private VariableDeclaration parseVariableDeclaration() {
        consumeToken(TokenType.LET);
        Declarator declarator = parseDeclarator();
        consumeToken(TokenType.COLON);
        TypeSpecifier typeSpecifier = parseTypeSpecifier();
        if (check(TokenType.SEMICOLON)) {
            consumeToken(TokenType.SEMICOLON);
            return new VariableDeclaration(declarator, typeSpecifier, null);
        }
        consumeToken(TokenType.ASSIGNMENT);
        Expression expression = parseExpression();
        consumeToken(TokenType.SEMICOLON);
        return new VariableDeclaration(declarator, typeSpecifier, expression);
    }

    private Expression parseExpression() {
        return parseLogicalExpression();
    }

    private Expression parseLogicalExpression() {
        Expression left =  parseLogicalTerm();
        while (match(TokenType.OR, TokenType.AND)) {
            TokenType operator = previous().type();
            Expression right = parseLogicalTerm();
            assert left instanceof LogicalTerm;
            left = new LogicalExpression((LogicalTerm)left, operator, (LogicalTerm) right);
        }
        return left;
    }

    private Expression parseLogicalTerm() {
        Expression left = parseAdditiveExpression();
        while (match(TokenType.EQUAL, TokenType.NOTEQUAL, TokenType.BIGGER, TokenType.SMALLER, TokenType.BIGGEREQUAL, TokenType.SMALLEREQUAL)) {
            TokenType operator = previous().type();
            Expression right = parseAdditiveExpression();
            left = new Comparison((AdditiveExpression) left, operator, (AdditiveExpression) right);
        }
        return left;

    }
    private Expression parseAdditiveExpression() {
        Expression left = parseMultiplicativeExpression();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            TokenType operator = previous().type();
            Expression right = parseMultiplicativeExpression();
            left = new AdditiveExpression((MultiplicativeExpression) left, operator, (MultiplicativeExpression) right);
        }
        return left;
    }

    private Expression parseMultiplicativeExpression() {
        Expression left = parsePrimaryExpression();
        while (match(MULT, DIV, REMINDER)) {
            TokenType operator = previous().type();
            Expression right = parsePrimaryExpression();
            left = new MultiplicativeExpression((PrimaryExpression)left, operator,(PrimaryExpression) right);
        }
        return left;
    }



    private Expression parsePrimaryExpression() {
        if (check(TokenType.IDENTIFIER)) {
            return new PrimaryExpression(String.valueOf(parseIdentifier()));
        }
        if (match(TokenType.NUMBER)) {
            return new PrimaryExpression(new Constant.IntegerConstant(Integer.parseInt(previous().value())));
        }
        if (match(TokenType.FLOAT)) {
            return new PrimaryExpression(new Constant.FloatConstant(Float.parseFloat(previous().value())));
        }
        if (match(TokenType.STRING)) {
            return new PrimaryExpression(new Constant.StringConstant(previous().value()));
        }
        if (match(TokenType.TRUE, TokenType.FALSE)) {
            return new PrimaryExpression(new Constant.BooleanConstant(previous().type() == TokenType.TRUE));
        }
        if (match(TokenType.LPARENTHESIS)) {
            Expression expression = parseExpression();
            consumeToken(TokenType.RPARENTHESIS);
            return expression;
        }
        return error(peek(), "Expected primary expression, found: " + peek());

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
            return error(nextToken, "Expected identifier, found: " + nextToken);
        }
        if (check(TokenType.LBRACKET)) {
            consumeToken(TokenType.LBRACKET);
            if(!check(TokenType.NUMBER)){
                return error(peek(), "Expected number, found: " + peek());
            }
            Constant.IntegerConstant size = parseIntegerConstant(consumeNextToken().value());
            consumeToken(TokenType.RBRACKET);
            return new Declarator(new Identifier(nextToken.value()), size);
        }

        return new Declarator(new Identifier(nextToken.value()));
    }

    private Constant.IntegerConstant parseIntegerConstant(String value) {
        return new Constant.IntegerConstant(Integer.parseInt(value));
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
