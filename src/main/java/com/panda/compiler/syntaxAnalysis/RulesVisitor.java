package com.panda.compiler.syntaxAnalysis;

public interface RulesVisitor<R> {
    R visitTranslationUnit(TranslationUnit translationUnit);
    R visitFunctionDefinition(ExternalDeclaration.FunctionDefinition functionDefinition);
    R visitDeclarationSpecifier(ExternalDeclaration.VariableDeclaration declarationSpecifier);
    R visitDeclaration(Declaration declaration);
    R visitTypeSpecifier(TypeSpecifier typeSpecifier);

    R visitParameterList(ParameterList parameterList);

    R visitJumpStatement(Statement.JumpStatement jumpStatement);

    R visitDeclarator(Declarator declarator);

    R visitParameterDeclaration(ParameterDeclaration parameterDeclaration);

    R visitCompoundStatement(CompoundStatement compoundStatement);

    R visitIFStatement(Statement.ControlFlowStatement.If anIf);

    R visitVariableDeclarationStatement(Statement.VariableDeclarationStatement variableDeclarationStatement);

    R visitIntegerConstant(Constant.IntegerConstant integerConstant);

    R visitFloatConstant(Constant.FloatConstant floatConstant);

    R visitSwitchStatement(Statement.ControlFlowStatement.Switch aSwitch);

    R visitWhileStatement(Statement.ControlFlowStatement.While aWhile);

    R visitForStatement(Statement.ControlFlowStatement.For aFor);

    R visitExpressionStatement(Statement.ExpressionStatement expressionStatement);

    R visitStringConstant(Constant.StringConstant stringConstant);

    R visitCharConstant(Constant.CharConstant charConstant);

    R visitBooleanConstant(Constant.BooleanConstant booleanConstant);

    R visitIdentifier(Identifier identifier);

    R visitLogicalExpression(Expression.LogicalExpression logicalExpression);

    R visitLogicalTerm(Expression.LogicalTerm logicalTerm);

    R visitPrimaryExpression(Expression.PrimaryExpression primaryExpression);

    R visitUnaryExpression(Expression.UnaryExpression unaryExpression);

    R visitMultiplicativeExpression(Expression.MultiplicativeExpression multiplicativeExpression);

    R visitAdditiveExpression(Expression.AdditiveExpression additiveExpression);

    R visitComparison(Expression.Comparison comparison);

    R visitPrintStatement(Statement.PrintStatement printStatement);

    R visitAssignmentStatement(Statement.AssignmentStatement assignmentStatement);
}
