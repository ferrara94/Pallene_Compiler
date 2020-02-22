package syntaxTree.statement.ASSIGNMENT;

import syntaxTree.Statement;

public class MIX_ASSIGNMENT extends Statement {
    public MIX_ASSIGNMENT(String name) {

        super(name);
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }
}
