package syntaxTree.expr.constant;

import syntaxTree.Expr;

import java.util.LinkedList;

public class EXPR_CONST extends Expr {

    private LinkedList<Expr> exprs;

    public EXPR_CONST(String name, LinkedList<Expr> exprs) {
        super(name);
        this.exprs = exprs;
    }

    public LinkedList<Expr> getExprs() {
        return exprs;
    }
}
