package syntaxTree.expr.unaryexpr;

import syntaxTree.Expr;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class MINUS_EXPR extends Expr {
    private Expr expr = null;
    public MINUS_EXPR(String name) {
        super(name);
    }

    public MINUS_EXPR(String name, Expr expr) {
        super(name);
        this.expr = expr;
    }

    @Override
    public String toStringSemantic() {
        return "-" + this.getChildByIndex(0).toStringSemantic();
    }

    public String getSymbol() {
        return "-";
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }


}
