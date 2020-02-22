package syntaxTree.expr.constant;

import syntaxTree.Expr;
import visitor.CVisitor;
import visitor.SemanticVisitor;

public class FLOAT_CONST extends Expr {

    private String floatValue;
    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }
    public String getFloatValue() {
        String toReturn = floatValue;

        int i=0;
        char character = floatValue.charAt(i);
        String sub1="";
        String sub2="";

        if(floatValue.contains("e")) {
            while (character != 'e') {
                i++;
                character = floatValue.charAt(i);
            }
            sub2 = floatValue.substring(i+1);
            sub1 = floatValue.substring(0,i);
            toReturn = sub1+"*exp("+sub2+")";

            /*
            p : float = 6e+9;
            k : float = 2.6e-99;
            */

        }

        return toReturn;
    }

    @Override
    public String accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toStringSemantic() {
        return this.floatValue;
    }

    @Override
    public String typeSemantic() {
        return "FLOAT";
    };

    public FLOAT_CONST(String name, String value) {
        super(name);
        this.floatValue = value;
    }
}
