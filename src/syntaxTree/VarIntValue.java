package syntaxTree;

import Scope.EntryInfo;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class VarIntValue extends Node {

    private Expr expr;

    public VarIntValue(String name, Expr expr) {
        super(name);
        this.expr = expr;
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
    public String toStringSemantic(){
        return  this.getChildByIndex(0).toStringSemantic();
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }
}
