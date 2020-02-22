package syntaxTree.expr.unaryexpr;

import syntaxTree.Expr;
import syntaxTree.Node;
import syntaxTree.expr.constant.ID;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class NOT_EXPR extends Expr {
    private Node child;

    public NOT_EXPR(String name, Node child) {
        super(name);
        this.child = child;
    }

    public NOT_EXPR(String name) {
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


    public String toStringSemantic() {
        return "not("+this.getChildByIndex(0).toStringSemantic()+")";

    }

    public Node getChildN() {
        return child;
    }

    public String toGetID() {
        if(this.child instanceof ID) {
            return this.child.getName();
        }else
            return "";
    }

}

