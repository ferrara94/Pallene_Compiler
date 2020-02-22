package syntaxTree.statement.Others;

import syntaxTree.Statement;
import visitor.CVisitor;

public class NOP extends Statement {
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    public NOP(String name) {
        super(name);
    }

}
