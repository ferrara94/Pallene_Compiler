package syntaxTree;

import Scope.EntryInfo;
import syntaxTree.expr.constant.ID;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class ParDecl extends Node {

    private ID id;
    private Type type;
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }

    public ParDecl(String name, ID id, Type type) {
        super(name);
        this.id = id;
        this.type = type;
    }

    @Override
    public EntryInfo accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    public ID getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }
}
