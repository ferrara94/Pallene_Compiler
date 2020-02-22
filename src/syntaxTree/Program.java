package syntaxTree;

import Scope.SymbolTableStack;
import visitor.CVisitor;
import visitor.SemanticVisitor;

import java.util.LinkedList;

public class Program extends Node{

    private Global global;
    private LinkedList<Function> functions;
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public SymbolTableStack accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    public Program(String name, Global global, LinkedList<Function> functions) {
        super(name);
        this.global = global;
        this.functions = functions;
    }

    public Global getGlobal() {
        return global;
    }

    public LinkedList<Function> getFunctions() {
        return functions;
    }
}
