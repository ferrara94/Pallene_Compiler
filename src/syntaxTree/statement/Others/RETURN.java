package syntaxTree.statement.Others;

import Scope.SymbolTableStack;
import syntaxTree.Statement;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class RETURN extends Statement {
    public RETURN(String name)
    {
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

    @Override
    public String toStringSemantic() {
        return "return "+ this.getChildByIndex(0).toStringSemantic();
    }

}
