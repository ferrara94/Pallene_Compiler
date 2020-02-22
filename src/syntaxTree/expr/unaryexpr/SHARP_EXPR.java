package syntaxTree.expr.unaryexpr;

import Scope.SymbolTableStack;
import syntaxTree.Expr;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class SHARP_EXPR extends Expr {
    public SHARP_EXPR(String name) {
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
    public String toStringSemantic(){
        return "#" + this.getChildByIndex(0).toStringSemantic() ;
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }
}
