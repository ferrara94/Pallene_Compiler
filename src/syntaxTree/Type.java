package syntaxTree;

import visitor.CVisitor;
import visitor.SemanticVisitor;

public class Type extends Node {
    public Type(String name) {
        super(name);
    }

    @Override
    public String accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }

}
