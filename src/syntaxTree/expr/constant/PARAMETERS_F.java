package syntaxTree.expr.constant;

import syntaxTree.Node;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class PARAMETERS_F extends Node {

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
        return this.getParent().toStringSemantic();
    }


    public PARAMETERS_F(String name) {
        super(name);
    }

}
