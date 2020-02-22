package syntaxTree;

import Scope.EntryInfo;
import syntaxTree.expr.constant.ID;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class VarDecl extends Node{

    private ID id;
    private Type type;
    private VarIntValue varIntValue;


    public VarDecl(String name, ID id, Type type, VarIntValue varIntValue) {
        super(name);
        this.id = id;
        this.type = type;
        this.varIntValue = varIntValue;
    }

    public VarDecl(String name, ID id, Type type) {
        super(name);
        this.id = id;
        this.type = type;

    }

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    public ID getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public VarIntValue getVarIntValue() {
        return varIntValue;
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
