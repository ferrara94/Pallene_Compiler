package syntaxTree.expr.binaryexpr.relop;

import syntaxTree.Expr;
import syntaxTree.RelopExpr;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class AND_R extends RelopExpr {
    public AND_R(String name) {
        super(name);
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
    public String toStringSemantic() {
        return "("+this.getChildByIndex(0).toStringSemantic() +" and "
                + this.getChildByIndex(1).toStringSemantic() +")" ;
    }
    public String getSymbolC() {return "&&";}
    public String getSymbol() {
        return "and";
    }
}
