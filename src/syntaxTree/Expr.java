package syntaxTree;

import Scope.SymbolTableStack;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class Expr extends Node {

    public Expr(String name) {
        super(name);
    }

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }

}
