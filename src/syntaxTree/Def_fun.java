package syntaxTree;

import Scope.SymbolTableStack;
import syntaxTree.expr.constant.ID;
import visitor.CVisitor;
import visitor.SemanticVisitor;

import java.util.LinkedList;

public class Def_fun extends Node {

    private ID id;
    private LinkedList<ParDecl> parDecls;
    private Type type;
    private LinkedList<Statement> statements;

    public  Def_fun(String name) {
        super(name);
    }

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }

    public Def_fun(String name, ID id, LinkedList<ParDecl> parDecls, Type type, LinkedList<Statement> statements) {
        super(name);
        this.id = id;
        this.parDecls = parDecls;
        this.type = type;
        this.statements = statements;
    }

    public Def_fun(String name, ID id, Type type, LinkedList<Statement> statements) {
        super(name);
        this.id = id;
        this.type = type;
        this.statements = statements;
    }

    public ID getId() {
        return id;
    }

    public LinkedList<ParDecl> getParDecls() {
        return parDecls;
    }

    public Type getType() {
        return type;
    }

    public LinkedList<Statement> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }

}
