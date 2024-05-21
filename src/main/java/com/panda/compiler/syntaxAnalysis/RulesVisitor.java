package com.panda.compiler.syntaxAnalysis;

public interface RulesVisitor<R> {
    R visitTranslationUnit(TranslationUnit translationUnit);
    R visitFunctionDefinition(FunctionDefinition functionDefinition);
    R visitDeclarationSpecifier(VariableDeclaration declarationSpecifier);
    R visitDeclaration(Declaration declaration);
    R visitTypeSpecifier(TypeSpecifier typeSpecifier);

    R visitParameterList(ParameterList parameterList);

    R visitJumpStatement(JumpStatement jumpStatement);

    R visitDeclarator(Declarator declarator);

    R visitParameterDeclaration(ParameterDeclaration parameterDeclaration);

    R visitCompoundStatement(CompoundStatement compoundStatement);

    R visitIFStatement(ControlFlowStatement.If anIf);


    R visitIntegerConstant(Constant.IntegerConstant integerConstant);

    R visitFloatConstant(Constant.FloatConstant floatConstant);

    R visitWhileStatement(ControlFlowStatement.While aWhile);

    R visitForStatement(ControlFlowStatement.For aFor);

    R visitExpressionStatement(ExpressionStatement expressionStatement);

    R visitStringConstant(Constant.StringConstant stringConstant);

    R visitCharConstant(Constant.CharConstant charConstant);

    R visitBooleanConstant(Constant.BooleanConstant booleanConstant);

    R visitIdentifier(Identifier identifier);

    R visitLogicalExpression(LogicalExpression logicalExpression);

    R visitLogicalTerm(LogicalTerm logicalTerm);

    R visitPrimaryExpression(PrimaryExpression primaryExpression);


    R visitMultiplicativeExpression(MultiplicativeExpression multiplicativeExpression);

    R visitAdditiveExpression(AdditiveExpression additiveExpression);

    R visitComparison(Comparison comparison);

    R visitPrintStatement(PrintStatement printStatement);

    R visitAssignmentStatement(AssignmentStatement assignmentStatement);

    R visitInputStatement(InputStatement inputStatement);

    R visitEOFDeclaration(EOFDeclaration eofDeclaration);
}