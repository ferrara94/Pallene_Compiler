package syntaxTree;

import Scope.SymbolTableStack;
import visitor.CVisitor;
import visitor.SemanticVisitor;

import java.util.LinkedList;

public class Global extends Node{

    private LinkedList<VarDecl> varDecls;

    @Override
    public SymbolTableStack accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    public Global(String name) {
        super(name);
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    public Global(String name, LinkedList<VarDecl> varDecls) {
        super(name);
        this.varDecls = varDecls;
    }

    public LinkedList<VarDecl> getVarDecls() {
        return varDecls;
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }
}
