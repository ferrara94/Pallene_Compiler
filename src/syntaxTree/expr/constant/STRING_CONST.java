package syntaxTree.expr.constant;

import syntaxTree.Expr;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class STRING_CONST extends Expr {

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }

    private String stringValue;

    public String getStringValue() {
        return stringValue;
    }

    public STRING_CONST(String name, String stringValue) {
        super(name);
        this.stringValue = stringValue;
    }

    @Override
    public String toStringSemantic() {
        return this.stringValue;
    }

    @Override
    public String typeSemantic() {
        return "STRING";
    };

    @Override
    public String toString() {
        return "< " + "STRING_CONST" +", " + this.getStringValue() + " >";
    }
}
