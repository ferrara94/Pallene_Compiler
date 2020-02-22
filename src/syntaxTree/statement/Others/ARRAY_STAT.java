package syntaxTree.statement.Others;

import syntaxTree.Node;
import visitor.SemanticVisitor;

public class ARRAY_STAT extends Node {
    public ARRAY_STAT(String name) {
        super(name);
    }

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }

}
