package syntaxTree;

import visitor.CVisitor;
import visitor.SemanticVisitor;

public class ArithExpr extends Expr {
    public ArithExpr(String name) {
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
