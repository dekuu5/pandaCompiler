package com.panda.compiler.syntaxAnalysis;

import java.util.List;

interface RulesVisitor<R> {
    R visitExternalDeclaration(GrammarRules.ExternalDeclaration externalDeclaration);
    R visitFunctionDefinition(GrammarRules.FunctionDefinition functionDefinition);
    R visitDeclarationSpecifier(DeclarationSpecifier declarationSpecifier);
    R visitDeclaration(Declaration declaration);
    R visitTypeSpecifier(TypeSpecifier typeSpecifier);
    R visitStorageClassSpecifier(StorageClassSpecifier storageClassSpecifier);
}
public abstract class GrammarRules {
    public abstract <R> R accept(RulesVisitor<R> visitor);
    static class ExternalDeclaration extends GrammarRules {
        final List<GrammarRules> rules;
        ExternalDeclaration(List<GrammarRules> rules) {
            this.rules = rules;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitExternalDeclaration(this);
        }
    }
    static class FunctionDefinition extends GrammarRules {
        final List<GrammarRules> rules;
        FunctionDefinition(List<GrammarRules> rules) {
            this.rules = rules;
        }
        @Override
        public <R> R accept(RulesVisitor<R> visitor) {
            return visitor.visitFunctionDefinition(this);
        }
    }
}
