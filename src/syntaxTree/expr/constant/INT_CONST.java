package syntaxTree.expr.constant;

import syntaxTree.Expr;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class INT_CONST extends Expr {

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    private String intValue;

    public String getIntValue() {
        return intValue;
    }

    public INT_CONST(String name, String intValue) {
        super(name);
        this.intValue = intValue;
    }

    @Override
    public String typeSemantic() {
        return "INT";
    };

    @Override
    public String toStringSemantic() {
        return this.intValue;
    }

    @Override
    public String toString() {
        return "< INT_CONST, " + this.intValue + " >";
    }
}
