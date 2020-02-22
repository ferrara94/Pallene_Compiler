package Scope;

import syntaxTree.Node;
import syntaxTree.expr.constant.ID;

public class TypeSystem {

    private String  nodeA;
    private String nodeB;
    private String op;

    public TypeSystem(String nodeA, String nodeB, String op) {

        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.op = op;

    }

    public TypeSystem(String nodeA, String op) {

        this.nodeA = nodeA;
        this.nodeB = "";
        this.op = op;

    }

    public String chekType() {

            if(op.equals("relop")) {

                //tipo con id non dichiarati
                if( (nodeA.equals("unknown") || (nodeB.equals("unknown")) ) ) {return "ErrorType"; }

                if( (nodeA.equals("NIL") || (nodeB.equals("NIL")) ) ) {return "ErrorType"; }

                //tipo diverso con bool
                if( (nodeA.equals("STRING") && (nodeB.equals("BOOL")) ) ) {return "ErrorType"; }
                if( (nodeB.equals("STRING") && (nodeA.equals("BOOL")) ) ) {return "ErrorType"; }

                //tipo diverso con bool
                if( (nodeA.equals("INT") && (nodeB.equals("BOOL")) ) ) {return "ErrorType"; }
                if( (nodeB.equals("INT") && (nodeA.equals("BOOL")) ) ) {return "ErrorType"; }

                //tipo diverso con string
                if( (nodeA.equals("STRING") && (nodeB.equals("INT")) ) ) {return "ErrorType"; }
                if( (nodeB.equals("STRING") && (nodeA.equals("INT")) ) ) {return "ErrorType"; }

                //tipo diverso con string
                if( (nodeA.equals("FLOAT") && (nodeB.equals("BOOL")) ) ) {return "ErrorType"; }
                if( (nodeB.equals("FLOAT") && (nodeA.equals("BOOL")) ) ) {return "ErrorType"; }

                //tipo diverso con float
                if( (nodeA.equals("STRING") && (nodeB.equals("FLOAT")) ) ) {return "ErrorType"; }
                if( (nodeB.equals("STRING") && (nodeA.equals("FLOAT")) ) ) {return "ErrorType"; }

                //se hanno errori di tipo nelle epsressioni con int
                if( (nodeA.equals("ErrorType") && (nodeB.equals("INT")) ) ) { return nodeA; }
                if( (nodeB.equals("ErrorType") && (nodeA.equals("INT")) ) ) {return nodeB; }

                //se hanno errori di tipo nelle epsressioni con float
                if( (nodeA.equals("ErrorType") && (nodeB.equals("FLOAT")) ) ) {return nodeA; }
                if( (nodeB.equals("ErrorType") && (nodeA.equals("FLOAT")) ) ) {return nodeB; }

                //se hanno errori di tipo nelle epsressioni con string
                if( (nodeA.equals("ErrorType") && (nodeB.equals("STRING")) ) ) {return nodeA; }
                if( (nodeB.equals("ErrorType") && (nodeA.equals("STRING")) ) ) {return nodeB; }

                return "BOOL";
            }

            if(op.equals("not")) {
                if(nodeA.equals("BOOL")) {return "BOOL";}
                else return "ErrorType";
            }

            if(op.equals("sharp")) {
                if(nodeA.equals("STRING")) {return "INT";}
                else return "ErrorType";
            }


            if(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/")) {

                //se sono dello stesso tipo
                if(nodeA.equals(nodeB)) {return nodeA;}
                //tipo diverso con float
                if( (nodeA.equals("INT") && (nodeB.equals("FLOAT")) ) ) {return nodeB; }
                if( (nodeA.equals("FLOAT") && (nodeB.equals("INT")) ) ) {return nodeA; }

                //tipo diverso con string
                if( (nodeA.equals("STRING") && (nodeB.equals("INT")) ) ) {return "ErrorType"; }
                if( (nodeB.equals("STRING") && (nodeA.equals("INT")) ) ) {return "ErrorType"; }

                if( (nodeA.equals("STRING") && (nodeB.equals("FLOAT")) ) ) {return "ErrorType"; }
                if( (nodeB.equals("STRING") && (nodeA.equals("FLOAT")) ) ) {return "ErrorType"; }

                //
                //se hanno errori di tipo nelle epsressioni con int
                if( (nodeA.equals("ErrorType") && (nodeB.equals("INT")) ) ) { return nodeA; }
                if( (nodeB.equals("ErrorType") && (nodeA.equals("INT")) ) ) {return nodeB; }

                //se hanno errori di tipo nelle epsressioni con float
                if( (nodeA.equals("ErrorType") && (nodeB.equals("FLOAT")) ) ) {return nodeA; }
                if( (nodeB.equals("ErrorType") && (nodeA.equals("FLOAT")) ) ) {return nodeB; }

                //se hanno errori di tipo nelle epsressioni con string
                if( (nodeA.equals("ErrorType") && (nodeB.equals("STRING")) ) ) {return nodeA; }
                if( (nodeB.equals("ErrorType") && (nodeA.equals("STRING")) ) ) {return nodeB; }

            }


        return "unknown";
    }


}
