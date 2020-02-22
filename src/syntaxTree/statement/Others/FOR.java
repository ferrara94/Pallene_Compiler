package syntaxTree.statement.Others;

import Scope.SymbolTableStack;
import syntaxTree.Statement;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class FOR extends Statement {
    public FOR(String name) {
        super(name);
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public String accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }
}
