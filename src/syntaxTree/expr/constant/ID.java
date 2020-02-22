package syntaxTree.expr.constant;

import Scope.SymbolTableStack;
import syntaxTree.Expr;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class ID extends Expr {

    private String name;
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public String accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toStringSemantic() {
        return this.getName();
    }

    public ID(String name, String name1) {
        super(name);
        this.name = name1;
    }

    @Override
    public String toString() {
        return "< ID, " + this.getName() +  " >";
    }

}
