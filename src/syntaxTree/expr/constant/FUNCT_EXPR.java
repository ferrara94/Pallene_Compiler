package syntaxTree.expr.constant;

import syntaxTree.Expr;
import syntaxTree.Node;
import visitor.CVisitor;
import visitor.SemanticVisitor;

import java.util.LinkedList;

public class FUNCT_EXPR extends Expr {

    private ID id;
    private LinkedList<Expr> exprs = null;
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    public FUNCT_EXPR(String name, ID id, LinkedList<Expr> exprs) {
        super(name);
        this.id = id;
        this.exprs = exprs;
    }

    public FUNCT_EXPR(String name, ID id){
        super(name);
        this.id = id;
    }

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    public String getParameters() {
        String result = "";
        String comma = "";

        if(this.exprs != null) {

            for (Node n : this.exprs) {
                result += comma;
                result += n.toStringSemantic();
                comma = " ,";
            }

        }

        return result;
    }

    @Override
    public String toStringSemantic() {
        return this.id.getName() + "(" + this.getParameters() + ")";
    }

    public FUNCT_EXPR(String name){
        super(name);
    }

    public ID getId() {
        return id;
    }

    public LinkedList<Expr> getExprs() {
        return exprs;
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }
}
