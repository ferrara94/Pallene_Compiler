package syntaxTree;

import Scope.SymbolTableStack;
import visitor.CVisitor;
import visitor.SemanticVisitor;

import java.util.LinkedList;

public class Function extends Node {

    private LinkedList<Def_fun> def_funs;

    public Function(String name) {
        super(name);
        this.def_funs = new LinkedList<Def_fun>();
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public SymbolTableStack accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    public LinkedList<Def_fun> getDef() {
        return this.def_funs;
    }

    public void addDefFun(Def_fun def_fun) {
        this.def_funs.add(def_fun);
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }

}
