package syntaxTree.statement.IO;

import syntaxTree.Statement;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class READ extends Statement {
    public READ(String name) {
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
    @Override
    public String toStringSemantic() {
        return " read: ( "+this.getChildByIndex(0).getName() + " <== )";
    }

}
