package syntaxTree;

import visitor.CVisitor;

public class Stat extends Node {
    public Stat(String name) {
        super(name);
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
