package syntaxTree.statement.ASSIGNMENT;

import syntaxTree.Expr;
import syntaxTree.Statement;
import syntaxTree.expr.constant.ID;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class ASSIGNMENT extends Statement {

    private ID id;
    private Expr expr;

    public ASSIGNMENT(String name, ID id, Expr expr) {
        super(name);
        this.id = id;
        this.expr = expr;
    }

    public ASSIGNMENT(String name) {
        super(name);
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public String accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toStringSemantic(){
        return "( "+ this.getChildByIndex(0).getName() + " = "+ this.getChildByIndex(1).getName()
                +" )";
    }

    public ID getId() {
        return id;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }
}
