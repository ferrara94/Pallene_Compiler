package syntaxTree.statement.Others;

import syntaxTree.Statement;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class WHILE_DO extends Statement {
    public WHILE_DO(String name) {
        super(name);
    }
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

}
