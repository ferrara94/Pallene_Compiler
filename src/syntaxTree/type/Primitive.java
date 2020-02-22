package syntaxTree.type;

import syntaxTree.Type;

public class Primitive extends Type {
    public Primitive(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "< " + this.getName() + " >";
    }

}
