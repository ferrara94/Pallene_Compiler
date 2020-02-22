package syntaxTree.expr.constant;

import syntaxTree.Expr;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class BOOLEAN_CONST extends Expr {

    private boolean value;
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    public boolean isValue() {
        return value;
    }

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toStringSemantic() {
        if(this.value)
            return "TRUE";
        else return "FALSE";
    }



    public BOOLEAN_CONST(String name, boolean value) {
        super(name);
        this.value = value;
    }
}
