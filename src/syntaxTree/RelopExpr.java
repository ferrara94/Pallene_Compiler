package syntaxTree;

import visitor.CVisitor;
import visitor.SemanticVisitor;

public class RelopExpr extends Expr {
    public RelopExpr(String name) {
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
