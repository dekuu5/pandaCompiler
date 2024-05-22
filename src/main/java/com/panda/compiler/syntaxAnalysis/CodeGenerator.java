package com.panda.compiler.syntaxAnalysis;

import java.util.HashMap;

public class CodeGenerator implements RulesVisitor<String> {

    @Override
    public String visitTranslationUnit(TranslationUnit node) {
        StringBuilder sb = new StringBuilder();
        for (ExternalDeclaration rule : node.rules) {
            sb.append(rule.accept(this)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String visitFunctionDefinition(FunctionDefinition node) {
        StringBuilder sb = new StringBuilder();
        sb.append("void ").append(node.identifier.name).append("(");
        if (node.parameters != null) {
            sb.append(node.parameters.accept(this));
        }
        sb.append(") ").append(node.compoundStatement.accept(this));
        return sb.toString();
    }

    @Override
    public String visitVariableDeclaration(VariableDeclaration node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.typeSpecifier.accept(this)).append(" ")
                .append(node.declarator.accept(this));
        if (node.expression != null) {
            sb.append(" = ").append(node.expression.accept(this));
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public String visitDeclaration(Declaration node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.typeSpecifier.accept(this)).append(" ")
                .append(node.declarator.accept(this));
        if (node.expression != null) {
            sb.append(" = ").append(node.expression.accept(this));
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public String visitParameterList(ParameterList node) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < node.parameters.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(node.parameters.get(i).accept(this));
        }
        return sb.toString();
    }

    @Override
    public String visitParameterDeclaration(ParameterDeclaration node) {
        return node.typeSpecifier.accept(this) + " " + node.declarator.accept(this);
    }

    @Override
    public String visitDeclarator(Declarator node) {
        if (node.size != null) {
            return node.identifier.name + "[" + node.size.value + "]";
        } else {
            return node.identifier.name;
        }
    }

    @Override
    public String visitTypeSpecifier(TypeSpecifier node) {
        return node.keyword.toString().toLowerCase();
    }

    @Override
    public String visitCompoundStatement(CompoundStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (Statement statement : node.statements) {
            sb.append(statement.accept(this)).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String visitAssignmentStatement(AssignmentStatement node) {
        return node.declarator.accept(this) + " = " + node.expression.accept(this) + ";";
    }

    @Override
    public String visitJumpStatement(JumpStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.keyword.toString().toLowerCase());
        if (node.expression != null) {
            sb.append(" ").append(node.expression.accept(this));
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public String visitIFStatement(ControlFlowStatement.If node) {
        StringBuilder sb = new StringBuilder();
        sb.append("if (").append(node.condition.accept(this)).append(") ")
                .append(node.statement.accept(this));
        if (node.elseStatement != null) {
            sb.append(" else ").append(node.elseStatement.accept(this));
        }
        return sb.toString();
    }

    @Override
    public String visitWhileStatement(ControlFlowStatement.While node) {
        return "while (" + node.condition.accept(this) + ") " + node.statement.accept(this);
    }

    @Override
    public String visitForStatement(ControlFlowStatement.For node) {
        StringBuilder sb = new StringBuilder();
        sb.append("for (");
        if (node.initialization != null) {
            sb.append(node.initialization.accept(this));
        }
        sb.append("; ");
        if (node.condition != null) {
            sb.append(node.condition.accept(this));
        }
        sb.append("; ");
        if (node.increment != null) {
            sb.append(node.increment.accept(this));
        }
        sb.append(") ").append(node.statement.accept(this));
        return sb.toString();
    }

    @Override
    public String visitExpressionStatement(ExpressionStatement node) {
        return (node.expression != null ? node.expression.accept(this) : "") + ";";
    }

    @Override
    public String visitPrintStatement(PrintStatement node) {
        return "printf(" + node.expression.accept(this) + ");";
    }

    @Override
    public String visitInputStatement(InputStatement node) {
        return "scanf(\"%d\", &inputVariable);"; // Example for integer input, adjust accordingly
    }

    @Override
    public String visitLogicalExpression(LogicalExpression node) {
        return node.firstTerm.accept(this) + " " + node.operator.toString().toLowerCase() + " " + node.term.accept(this);
    }

    @Override
    public String visitLogicalTerm(LogicalTerm node) {
        return node.firstComparison.accept(this) + " " + node.operator.toString().toLowerCase() + " " + node.comparison.accept(this);
    }

    @Override
    public String visitComparison(Comparison node) {
        return node.left.accept(this) + " " + node.operator.toString().toLowerCase() + " " + node.right.accept(this);
    }

    @Override
    public String visitAdditiveExpression(AdditiveExpression node) {
        return node.left.accept(this) + " " + node.operator.toString().toLowerCase() + " " + node.right.accept(this);
    }

    @Override
    public String visitMultiplicativeExpression(MultiplicativeExpression node) {
        return node.left.accept(this) + " " + node.operator.toString().toLowerCase() + " " + node.right.accept(this);
    }

    @Override
    public String visitPrimaryExpression(PrimaryExpression node) {
        if (node.identifier != null) {
            return node.identifier;
        } else if (node.constant != null) {
            return node.constant.accept(this);
        } else {
            return "(" + node.subExpression.accept(this) + ")";
        }
    }

    @Override
    public String visitIdentifier(Identifier node) {
        return node.name;
    }

    @Override
    public String visitIntegerConstant(Constant.IntegerConstant node) {
        return Integer.toString(node.value);
    }

    @Override
    public String visitFloatConstant(Constant.FloatConstant node) {
        return Float.toString(node.value);
    }

    @Override
    public String visitCharConstant(Constant.CharConstant node) {
        return "'" + node.value + "'";
    }

    @Override
    public String visitStringConstant(Constant.StringConstant node) {
        return "\"" + node.value + "\"";
    }

    @Override
    public String visitBooleanConstant(Constant.BooleanConstant node) {
        return node.value ? "true" : "false";
    }

    @Override
    public String visitEOFDeclaration(EOFDeclaration node) {
        return ""; // Handle the end of file token if necessary
    }
}
