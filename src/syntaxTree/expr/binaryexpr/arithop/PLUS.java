package syntaxTree.expr.binaryexpr.arithop;

import Scope.SymbolTableStack;
import syntaxTree.ArithExpr;
import syntaxTree.Expr;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class PLUS extends ArithExpr {
    public PLUS(String name) {
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
    public String toStringSemantic() {
        return "("+this.getChildByIndex(0).toStringSemantic() +"+"
                + this.getChildByIndex(1).toStringSemantic() +")" ;

    }
    public String getSymbol() {
        return "+";
    }
    @Override
    public String toString() {
        return "< AddOp >";
    }
}
