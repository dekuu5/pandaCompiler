package com.panda.compiler.syntaxAnalysis;

public class ASTPrinter implements RulesVisitor<String> {

    private int indentLevel = 0;

    private void printIndented(String message) {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("  ");
        }
        System.out.println(message);
        // Debug statement
        System.out.println("DEBUG: Current indent level: " + indentLevel);
    }


    private void increaseIndent() {
        indentLevel++;
    }

    private void decreaseIndent() {
        indentLevel--;
    }

    @Override

    public String visitTranslationUnit(TranslationUnit translationUnit) {
        printIndented("TranslationUnit");
        System.out.println("DEBUG: Visiting TranslationUnit with rules size: " + translationUnit.rules.size());
        increaseIndent();
        for (ExternalDeclaration rule : translationUnit.rules) {
            rule.accept(this);
        }
        decreaseIndent();
        return null;
    }


    @Override
    public String visitFunctionDefinition(FunctionDefinition functionDefinition) {
        printIndented("FunctionDefinition: " + functionDefinition.identifier.name);
        increaseIndent();
        if (functionDefinition.parameters != null) {
            functionDefinition.parameters.accept(this);
        }
        functionDefinition.compoundStatement.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitDeclarationSpecifier(VariableDeclaration declarationSpecifier) {
        printIndented("VariableDeclaration: " + declarationSpecifier.declarator.identifier.name);
        increaseIndent();
        declarationSpecifier.typeSpecifier.accept(this);
        if (declarationSpecifier.expression != null) {
            declarationSpecifier.expression.accept(this);
        }
        decreaseIndent();
        return null;
    }

    @Override
    public String visitDeclaration(Declaration declaration) {
        printIndented("Declaration: " + declaration.declarator.identifier.name);
        increaseIndent();
        declaration.typeSpecifier.accept(this);
        if (declaration.expression != null) {
            declaration.expression.accept(this);
        }
        decreaseIndent();
        return null;
    }

    @Override
    public String visitTypeSpecifier(TypeSpecifier typeSpecifier) {
        printIndented("TypeSpecifier: " + typeSpecifier.keyword);
        return null;
    }

    @Override
    public String visitParameterList(ParameterList parameterList) {
        printIndented("ParameterList");
        increaseIndent();
        for (ParameterDeclaration parameter : parameterList.parameters) {
            parameter.accept(this);
        }
        decreaseIndent();
        return null;
    }

    @Override
    public String visitJumpStatement(JumpStatement jumpStatement) {
        printIndented("JumpStatement: " + jumpStatement.keyword);
        if (jumpStatement.expression != null) {
            increaseIndent();
            jumpStatement.expression.accept(this);
            decreaseIndent();
        }
        return null;
    }

    @Override
    public String visitDeclarator(Declarator declarator) {
        printIndented("Declarator: " + declarator.identifier.name);
        if (declarator.size != null) {
            increaseIndent();
            declarator.size.accept(this);
            decreaseIndent();
        }
        return null;
    }

    @Override
    public String visitParameterDeclaration(ParameterDeclaration parameterDeclaration) {
        printIndented("ParameterDeclaration");
        increaseIndent();
        parameterDeclaration.typeSpecifier.accept(this);
        parameterDeclaration.declarator.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitCompoundStatement(CompoundStatement compoundStatement) {
        printIndented("CompoundStatement");
        increaseIndent();
        for (Statement statement : compoundStatement.statements) {
            statement.accept(this);
        }
        decreaseIndent();
        return null;
    }

    @Override
    public String visitIFStatement(ControlFlowStatement.If anIf) {
        printIndented("IfStatement");
        increaseIndent();
        anIf.condition.accept(this);
        anIf.statement.accept(this);
        if (anIf.elseStatement != null) {
            anIf.elseStatement.accept(this);
        }
        decreaseIndent();
        return null;
    }

    @Override
    public String visitIntegerConstant(Constant.IntegerConstant integerConstant) {
        printIndented("IntegerConstant: " + integerConstant.value);
        return null;
    }

    @Override
    public String visitFloatConstant(Constant.FloatConstant floatConstant) {
        printIndented("FloatConstant: " + floatConstant.value);
        return null;
    }

    @Override
    public String visitWhileStatement(ControlFlowStatement.While aWhile) {
        printIndented("WhileStatement");
        increaseIndent();
        aWhile.condition.accept(this);
        aWhile.statement.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitForStatement(ControlFlowStatement.For aFor) {
        printIndented("ForStatement");
        increaseIndent();
        aFor.initialization.accept(this);
        aFor.condition.accept(this);
        aFor.increment.accept(this);
        aFor.statement.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitExpressionStatement(ExpressionStatement expressionStatement) {
        printIndented("ExpressionStatement");
        increaseIndent();
        expressionStatement.expression.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitStringConstant(Constant.StringConstant stringConstant) {
        printIndented("StringConstant: " + stringConstant.value);
        return null;
    }

    @Override
    public String visitCharConstant(Constant.CharConstant charConstant) {
        printIndented("CharConstant: " + charConstant.value);
        return null;
    }

    @Override
    public String visitBooleanConstant(Constant.BooleanConstant booleanConstant) {
        printIndented("BooleanConstant: " + booleanConstant.value);
        return null;
    }

    @Override
    public String visitIdentifier(Identifier identifier) {
        printIndented("Identifier: " + identifier.name);
        return null;
    }

    @Override
    public String visitLogicalExpression(LogicalExpression logicalExpression) {
        printIndented("LogicalExpression: " + logicalExpression.operator);
        increaseIndent();
        logicalExpression.firstTerm.accept(this);
        logicalExpression.term.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitLogicalTerm(LogicalTerm logicalTerm) {
        printIndented("LogicalTerm: " + logicalTerm.operator);
        increaseIndent();
        logicalTerm.firstComparison.accept(this);
        logicalTerm.comparison.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitPrimaryExpression(PrimaryExpression primaryExpression) {
        printIndented("PrimaryExpression");
        increaseIndent();
        if (primaryExpression.identifier != null) {
            printIndented("Identifier: " + primaryExpression.identifier);
        } else if (primaryExpression.constant != null) {
            primaryExpression.constant.accept(this);
        } else if (primaryExpression.subExpression != null) {
            primaryExpression.subExpression.accept(this);
        }
        decreaseIndent();
        return null;
    }



    @Override
    public String visitMultiplicativeExpression(MultiplicativeExpression multiplicativeExpression) {
        printIndented("MultiplicativeExpression: " + multiplicativeExpression.operator);
        increaseIndent();
        multiplicativeExpression.left.accept(this);
        multiplicativeExpression.right.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitAdditiveExpression(AdditiveExpression additiveExpression) {
        printIndented("AdditiveExpression: " + additiveExpression.operator);
        increaseIndent();
        additiveExpression.left.accept(this);
        additiveExpression.right.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitComparison(Comparison comparison) {
        printIndented("Comparison: " + comparison.operator);
        increaseIndent();
        comparison.left.accept(this);
        comparison.right.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitPrintStatement(PrintStatement printStatement) {
        printIndented("PrintStatement");
        increaseIndent();
        if (printStatement.expression != null) {
            printStatement.expression.accept(this);
        }
        decreaseIndent();
        return null;
    }

    @Override
    public String visitAssignmentStatement(AssignmentStatement assignmentStatement) {
        printIndented("AssignmentStatement");
        increaseIndent();
        assignmentStatement.declarator.accept(this);
        assignmentStatement.expression.accept(this);
        decreaseIndent();
        return null;
    }

    @Override
    public String visitInputStatement(InputStatement inputStatement) {
        printIndented("InputStatement");
        return null;
    }

    @Override
    public String visitEOFDeclaration(EOFDeclaration eofDeclaration) {
        return " ";
    }


}
