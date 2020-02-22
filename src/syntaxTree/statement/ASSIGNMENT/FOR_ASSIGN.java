package syntaxTree.statement.ASSIGNMENT;

import syntaxTree.Expr;
import syntaxTree.Statement;
import syntaxTree.expr.constant.ID;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class FOR_ASSIGN extends Statement {
    private ID id;
    private Expr expr;

    public FOR_ASSIGN(String name) {
        super(name);
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    public FOR_ASSIGN(String name, ID id, Expr expr) {
        super(name);
        this.id = id;
        this.expr = expr;
    }

    public FOR_ASSIGN(String name, ID id) {
        super(name);
        this.id = id;
    }

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }
}
