package com.panda.compiler.syntaxAnalysis;

import com.panda.compiler.lexicalAnalysis.Token;
import com.panda.compiler.lexicalAnalysis.TokenType;

import java.util.HashMap;
import java.util.List;

public abstract class GrammarRules {
    public abstract <R> R accept(RulesVisitor<R> visitor);
}
class TranslationUnit extends GrammarRules {
    final List<ExternalDeclaration> rules;

    TranslationUnit(List<ExternalDeclaration> rules) {
        this.rules = rules;
    }

    @Override
    public <R> R accept(RulesVisitor<R> visitor) {
        return visitor.visitTranslationUnit(this);
    }
}
    abstract class ExternalDeclaration extends GrammarRules {
         static class FunctionDefinition extends ExternalDeclaration {
            final Identifier identifier;
             final ParameterList parameters;
             final CompoundStatement compoundStatement;
        FunctionDefinition(Identifier identifier , ParameterList parameters, CompoundStatement compoundStatement) {
            this.identifier = identifier;
            this.parameters = parameters;
            this.compoundStatement = compoundStatement;
        }
        FunctionDefinition(Identifier identifier, CompoundStatement compoundStatement) {
            this.identifier = identifier;
            this.parameters = null;
            this.compoundStatement = compoundStatement;
        }

        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitFunctionDefinition(this);
        }
    }
         static class VariableDeclaration extends ExternalDeclaration {
             Declarator declarator;
             TypeSpecifier typeSpecifier;
             Expression expression;
                VariableDeclaration(Declarator declarator, TypeSpecifier typeSpecifier, Expression expression) {
                    this.declarator = declarator;
                    this.typeSpecifier = typeSpecifier;
                    this.expression = expression;
                }

             @Override
         public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitDeclarationSpecifier(this);
         }
    }
    }
    class Declaration extends GrammarRules {
        final TypeSpecifier typeSpecifier;
        final Declarator declarator;
        final Expression expression;
        Declaration(TypeSpecifier typeSpecifier, Declarator declarator, Expression expression) {
            this.typeSpecifier = typeSpecifier;
            this.declarator = declarator;
            this.expression = expression;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitDeclaration(this);
        }
    }


class ParameterList extends GrammarRules {
    final List<ParameterDeclaration> parameters;
    ParameterList(List<ParameterDeclaration> parameters) {
        this.parameters = parameters;
    }
    @Override
    public <R> R accept(RulesVisitor<R> visitor) {
        return visitor.visitParameterList(this);
    }
}

class ParameterDeclaration extends GrammarRules {
    final TypeSpecifier typeSpecifier;
    final Declarator declarator;
    ParameterDeclaration(TypeSpecifier typeSpecifier, Declarator declarator) {
        this.typeSpecifier = typeSpecifier;
        this.declarator = declarator;
    }
    @Override
    public <R> R accept(RulesVisitor<R> visitor) {
        return visitor.visitParameterDeclaration(this);
    }
}
class Declarator extends GrammarRules   {
    final Identifier identifier;
    final Declarator declarator;
    final int size;
    Declarator(Identifier identifier) {
        this.identifier = identifier;
        this.declarator = null;
        this.size = 0;
    }
    Declarator(Declarator declarator, int size) {
        this.identifier = null;
        this.declarator = declarator;
        this.size = size;
    }
    Declarator(Declarator declarator) {
        this(declarator, 0);
    }

    @Override
    public <R> R accept(RulesVisitor<R> visitor) {
        return visitor.visitDeclarator(this);
    }
}
class TypeSpecifier extends GrammarRules {
    final TokenType keyword;
    TypeSpecifier(TokenType keyword) {
        this.keyword = keyword;
    }
    @Override
    public <R> R accept(RulesVisitor<R> visitor) {
        return visitor.visitTypeSpecifier(this);
    }
}
class CompoundStatement extends GrammarRules {
    final List<Statement> statements;
    CompoundStatement(List<Statement> statements) {
        this.statements = statements;
    }
    @Override
    public <R> R accept(RulesVisitor<R> visitor) {
        return visitor.visitCompoundStatement(this);
    }
}

abstract class Statement extends GrammarRules {}

    class VariableDeclarationStatement extends Statement {
        ExternalDeclaration.VariableDeclaration declaration;

        VariableDeclarationStatement(ExternalDeclaration.VariableDeclaration declaration) {
            this.declaration = declaration;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
                return visitor.visitVariableDeclarationStatement(this);
        }

    }
    class AssignmentStatement extends Statement {
        Declarator declarator;
        Expression expression;
        AssignmentStatement(Declarator declarator, Expression expression) {
            this.declarator = declarator;
            this.expression = expression;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitAssignmentStatement(this);
        }
    }

    class JumpStatement extends Statement {
        TokenType keyword;
        Expression expression;
        JumpStatement(TokenType keyword, Expression expression) {
            this.keyword = keyword;
            this.expression = expression;
        }
        JumpStatement(TokenType keyword) {
            this.keyword = keyword;
            this.expression = null;
        }

        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitJumpStatement(this);
        }

    }

    abstract class ControlFlowStatement extends Statement {
        static class If extends ControlFlowStatement {
            final Expression condition;
            final CompoundStatement statement;
            final CompoundStatement elseStatement;

            If(Expression condition, CompoundStatement statement, CompoundStatement elseStatement) {
                this.condition = condition;
                this.statement = statement;
                this.elseStatement = elseStatement;
            }

            If(Expression condition, CompoundStatement statement) {
                this.condition = condition;
                this.statement = statement;
                this.elseStatement = null;
            }

            public <R> R accept(RulesVisitor<R> visitor) {
                return visitor.visitIFStatement(this);
            }
        }

        static class Switch extends ControlFlowStatement {
            final Expression expression;
            final HashMap<Constant,Statement> caseStatements;
            final Statement defaultStatement;
            Switch(Expression expression, HashMap<Constant,Statement> caseStatements, Statement defaultStatement) {
                this.expression = expression;
                this.caseStatements = caseStatements;
                this.defaultStatement = defaultStatement;
            }
            public <R> R accept(RulesVisitor<R> visitor) {
                return visitor.visitSwitchStatement(this);
            }

        }

        static class While extends ControlFlowStatement {
            final Expression condition;
            final CompoundStatement statement;
            While(Expression condition, CompoundStatement statement) {
                this.condition = condition;
                this.statement = statement;
            }
            public <R> R accept(RulesVisitor<R> visitor) {
                return visitor.visitWhileStatement(this);
            }
        }

        static class For extends ControlFlowStatement {
            final Statement initialization;
            final Expression condition;
            final Statement increment;
            final CompoundStatement statement;
            For(Statement initialization, Expression condition, Statement increment, CompoundStatement statement) {
                this.initialization = initialization;
                this.condition = condition;
                this.increment = increment;
                this.statement = statement;
            }
            public <R> R accept(RulesVisitor<R> visitor) {
                return visitor.visitForStatement(this);
            }
        }
    }
    class ExpressionStatement extends Statement {
        Expression expression;
        ExpressionStatement(Expression expression) {
            this.expression = expression;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitExpressionStatement(this);
        }
    }

    class PrintStatement extends Statement {
        public PrintStatement(Expression expression) {
            super();
        }
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitPrintStatement(this);
        }
    }

abstract class Expression extends GrammarRules {}
     class LogicalExpression extends Expression {
        LogicalTerm firstTerm;
        TokenType operator;
        LogicalTerm term;

        LogicalExpression(LogicalTerm firstTerm, TokenType operator, LogicalTerm term) {
            this.firstTerm = firstTerm;
            this.operator = operator;
            this.term = term;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitLogicalExpression(this);
        }
        
    }

    // Logical term node
     class LogicalTerm extends Expression {
        Comparison firstComparison;
        Token operator;
        Comparison comparison;

        LogicalTerm(Comparison firstComparison, Token operator, Comparison comparison) {
            this.firstComparison = firstComparison;
            this.operator = operator;
            this.comparison = comparison;
        }
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitLogicalTerm(this);
        }
    }

    // Comparison node
     class Comparison extends Expression {
        AdditiveExpression left;
        Token operator;
        AdditiveExpression right;

        Comparison(AdditiveExpression left, Token operator, AdditiveExpression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitComparison(this);
        }
    }

    // Additive expression node
     class AdditiveExpression extends Expression {
        MultiplicativeExpression left;
        Token operator;
        MultiplicativeExpression right;

        AdditiveExpression(MultiplicativeExpression left, Token operator, MultiplicativeExpression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitAdditiveExpression(this);
        }
    }

    // Multiplicative expression node
     class MultiplicativeExpression extends Expression {
        UnaryExpression left;
        Token operator;
        UnaryExpression right;

        MultiplicativeExpression(UnaryExpression left, Token operator, UnaryExpression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitMultiplicativeExpression(this);
        }
    }

     class UnaryExpression extends Expression {
        PrimaryExpression expression;
        Token operator;

        UnaryExpression(PrimaryExpression expression, Token operator) {
            this.expression = expression;
            this.operator = operator;
        }

        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitUnaryExpression(this);
        }
    }


     class PrimaryExpression extends Expression {
        final String identifier; // Can be null if it's not an identifier
        final Constant constant; // Can be null if it's not a constant
        final Expression subExpression;
        PrimaryExpression(String identifier) {
            this.identifier = identifier;
            this.constant = null;
            this.subExpression = null;
        }
        PrimaryExpression(Constant constant) {
            this.identifier = null;
            this.constant = constant;
            this.subExpression = null;
        }
        PrimaryExpression(Expression subExpression) {
            this.identifier = null;
            this.constant = null;
            this.subExpression = subExpression;
        }

        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitPrimaryExpression(this);
        }
    }

 class Identifier extends GrammarRules {
    String name;
    Identifier(String name) {
        this.name = name;
    }

    @Override
    public <R> R accept(RulesVisitor<R> visitor) {
        return visitor.visitIdentifier(this);
    }
}
abstract class Constant extends GrammarRules{
    static class IntegerConstant extends Constant {
        int value;
        IntegerConstant(int value) {
            this.value = value;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitIntegerConstant(this);
        }
    }

    static class FloatConstant extends Constant {
        float value;
        FloatConstant(float value) {
            this.value = value;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitFloatConstant(this);
        }
    }

    static class CharConstant extends Constant {
        char value;
        CharConstant(char value) {
            this.value = value;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitCharConstant(this);
        }

    }

    static class StringConstant extends Constant {
        String value;
        StringConstant(String value) {
            this.value = value;
        }
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitStringConstant(this);
        }
    }

    static class BooleanConstant extends Constant {
        boolean value;

        BooleanConstant(boolean value) {
            this.value = value;
        }

        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitBooleanConstant(this);
        }
    }
        
    }

