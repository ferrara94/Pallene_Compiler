package visitor;

import Scope.*;
import syntaxTree.*;
import syntaxTree.expr.binaryexpr.arithop.DIV;
import syntaxTree.expr.binaryexpr.arithop.MINUS;
import syntaxTree.expr.binaryexpr.arithop.PLUS;
import syntaxTree.expr.binaryexpr.arithop.TIMES;
import syntaxTree.expr.binaryexpr.relop.*;
import syntaxTree.expr.constant.*;
import syntaxTree.expr.unaryexpr.MINUS_EXPR;
import syntaxTree.expr.unaryexpr.NOT_EXPR;
import syntaxTree.expr.unaryexpr.SHARP_EXPR;
import syntaxTree.statement.ASSIGNMENT.ASSIGNMENT;
import syntaxTree.statement.ASSIGNMENT.FOR_ASSIGN;
import syntaxTree.statement.ASSIGNMENT.MIX_ASSIGNMENT;
import syntaxTree.statement.Conditional.IF_THEN;
import syntaxTree.statement.Conditional.IF_THEN_ELSE;
import syntaxTree.statement.IO.READ;
import syntaxTree.statement.IO.WRITE;
import syntaxTree.statement.Others.*;
import syntaxTree.type.Primitive;
import syntaxTree.type.Struct;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class CVisitor implements Visitor {

    private File file;
    private String nameFile ="";
    private String toInizialize ="";
    private SymbolTableStack SymbolStack;
    private Hashtable<String,String> stringInit;
    private TypeSystem typeSystem;

    public CVisitor(String nameFile, SymbolTableStack SymbolStack) {
        this.file = new File(nameFile);
        this.nameFile = nameFile;
        this.SymbolStack = SymbolStack;
        this.stringInit = new Hashtable<String,String>();
    }

    private boolean checkFile() {
        if(this.file.exists()) {
            //System.out.println("file exist");
            return true;
        }else {
            System.out.println("file doesn't exist");
            return false;
        }
    }

    private void writeOnFile(String text) throws FileNotFoundException {


        if(this.checkFile()) {

            FileOutputStream fos = new FileOutputStream(this.nameFile, true);
            PrintWriter writer = new PrintWriter(fos);
            writer.append(text);

            writer.close();
        } else {
            PrintWriter writer = new PrintWriter(this.file);
            writer.println(text);
            writer.close();
        }
    }

    @Override
    public Object visit(Program node) {


        String cCode = "#include <stdio.h>\n" +
                "#include <stdlib.h>\n" +
                "#include <stdbool.h>\n"+
                "#include <string.h>\n"+
                "#include <math.h>\n";
        try {
            this.writeOnFile(cCode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        for(Node n: node.getChildren())
            n.accept(this);
        return null;
    }

    @Override
    public Object visit(Global node) {

        for(Node n: node.getChildren()){
            n.accept(this);
        }

        return null;
    }

    @Override
    public Object visit(VarDecl node) {
        String cCode = "";
        String id = "";
        String idTemp="";
        String type = "";
        String varInt = "";
        String toReturn="";


        Node parent = node.getParent();
        while( !(parent instanceof Global ) && !(parent instanceof LOCAL) ) {
            parent = parent.getParent();
        }


        for(Node n: node.getChildren()) {

            if(n instanceof ID) {
                EntryInfoParD entryInfoParD = (EntryInfoParD) n.accept(this);
                id = entryInfoParD.getId();
                idTemp = entryInfoParD.getId();
            }
            if(n instanceof Type) {
                type = (String) n.accept(this);
                if (type.equals("char")) {id += "[30]";}
            }

            if(n instanceof VarIntValue) {

                varInt = (String) n.accept(this);

                if(type.equals("char")) {


                    if(parent instanceof Global) {
                        this.stringInit.put(idTemp, varInt);
                        varInt = "";

                    }

                } //equals char

            }

        }


        if(parent instanceof Global) {
            if(!varInt.equals(""))
                toInizialize += "\n\t" + id + " = " + varInt + ";";
            cCode += "\n" + type + " " + id + ";";
        } else {
            if(!varInt.equals(""))
                toReturn += "\n" + type + " " + id + " = " + varInt+ ";";
            else toReturn += "\n" + type + " " + id + ";";

        }

        try {
            this.writeOnFile(cCode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return toReturn;
    }

    @Override
    public EntryInfoParD visit(ID node) {


        Node parent = node.getParent();
        while ( (!parent.getName().equals("Global")) && (!parent.getName().equals("DefFunOp"))
                && ( !( parent instanceof FOR ) ) && (!( parent instanceof LOCAL ))) {
            parent = parent.getParent();
        }


        if( parent instanceof LOCAL ){


            String localID = parent.getNumber();


            SymbolTable localTable = this.SymbolStack.getSymbolTableByNLocal(localID);


            if(localTable.containsKey(node.getName())) {
                EntryInfo entryInfoID = localTable.getEntryInfoByName(node.getName());
                return new EntryInfoParD(node.getName(),entryInfoID.getKind(),entryInfoID.getType());
            }

            SymbolTable gParentTable = localTable.getParentTable();

            while ( (!gParentTable.getTypeNode().equals("DEF_FUN")) && (!gParentTable.getTypeNode().equals("FOR"))) {
                if(gParentTable.containsKey(node.getName())) {
                    EntryInfo entryInfoID = gParentTable.getEntryInfoByName(node.getName());
                    return new EntryInfoParD(node.getName(),entryInfoID.getKind(),entryInfoID.getType());
                }

                gParentTable = gParentTable.getParentTable();
            }

            if(gParentTable.getTypeNode().equals("DEF_FUN")) {
                parent = new Def_fun("DefFunOp");
                parent.addChild(new ID(gParentTable.getScopeName(),gParentTable.getScopeName()));
            }

            if(gParentTable.getTypeNode().equals("FOR")) {
                parent = new FOR("FOR_STATEMENT");
                parent.setNumber(gParentTable.getNumberLocal());
            }

        } // end if LOCAL


        if( parent instanceof FOR ){


            String forID = parent.getNumber();


            SymbolTable forTable = this.SymbolStack.getSymbolTableByNLocal(forID);


            if(forTable.containsKey(node.getName())) {
                EntryInfo entryInfoID = forTable.getEntryInfoByName(node.getName());
                return new EntryInfoParD(node.getName(),entryInfoID.getKind(),entryInfoID.getType());
            }

            SymbolTable gParentTable = forTable.getParentTable();

            while ( (!gParentTable.getTypeNode().equals("DEF_FUN")) && (!gParentTable.getTypeNode().equals("LOCAL"))) {
                if(gParentTable.containsKey(node.getName())) {
                    EntryInfo entryInfoID = gParentTable.getEntryInfoByName(node.getName());
                    return new EntryInfoParD(node.getName(),entryInfoID.getKind(),entryInfoID.getType());
                }

                gParentTable = gParentTable.getParentTable();
            }

            if(gParentTable.getTypeNode().equals("DEF_FUN")) {
                parent = new Def_fun("DefFunOp");
                parent.addChild(new ID(gParentTable.getScopeName(),gParentTable.getScopeName()));
            }


            if(gParentTable.getTypeNode().equals("LOCAL")) {


                String localID = parent.getNumber();


                SymbolTable localTable = this.SymbolStack.getSymbolTableByNLocal(localID);


                if(localTable.containsKey(node.getName())) {
                    EntryInfo entryInfoID = localTable.getEntryInfoByName(node.getName());
                    return new EntryInfoParD(node.getName(),entryInfoID.getKind(),entryInfoID.getType());
                }

                gParentTable = localTable.getParentTable();

                while ( (!gParentTable.getTypeNode().equals("DEF_FUN")) && (!gParentTable.getTypeNode().equals("FOR"))) {
                    if(gParentTable.containsKey(node.getName())) {
                        EntryInfo entryInfoID = gParentTable.getEntryInfoByName(node.getName());
                        return new EntryInfoParD(node.getName(),entryInfoID.getKind(),entryInfoID.getType());
                    }

                    gParentTable = gParentTable.getParentTable();
                }

                if(gParentTable.getTypeNode().equals("DEF_FUN")) {
                    parent = new Def_fun("DefFunOp");
                    parent.addChild(new ID(gParentTable.getScopeName(),gParentTable.getScopeName()));
                }

                if(gParentTable.getTypeNode().equals("FOR")) {
                    parent = new FOR("FOR_STATEMENT");
                    parent.addChild(new ID(gParentTable.getScopeName(),gParentTable.getScopeName()));
                }

            }

        } // end if FOR


        if( parent.getName().equals("DefFunOp") ){


            String functID = parent.getChildByIndex(0).getName();


            SymbolTable functTable = this.SymbolStack.getSymbolTableByName(functID);


            if(functTable.containsKey(node.getName())) {
                EntryInfo entryInfoID = functTable.getEntryInfoByName(node.getName());
                return new EntryInfoParD(node.getName(),entryInfoID.getKind(),entryInfoID.getType());
            }
        } // end if DefunOp


        SymbolTable programTable = this.SymbolStack.getSymbolTableByName("ProgramOp");



        if(programTable.containsKey(node.getName())) {
            EntryInfo entryInfoID = programTable.getEntryInfoByName(node.getName());
            return new EntryInfoParD(node.getName(), entryInfoID.getKind(), entryInfoID.getType());
        }


        return new EntryInfoParD(node.getName(),"var","");
    }

    @Override
    public Object visit(Type node) {
        if(node.getName().equals("STRING")) {return "char";}
        if(node.getName().equals("NIL")) {return "void";}
        return node.getName().toLowerCase();
    }

    @Override
    public Object visit(VarIntValue node) {

        Node n = node.getChildByIndex(0);
        String id="";
        EntryInfoParD entryInfoParD;
        if(n instanceof ID) {
            entryInfoParD = (EntryInfoParD) n.accept(this);
            id = entryInfoParD.getId();
        } else
            id = (String) n.accept(this);

        return id;
    }

    @Override
    public Object visit(Function node) {
        String signs = "";
        String id="";
        String type="";
        String parD="";
        String comma="";
        EntryInfoParD entryInfoParD;


        for(Node defs: node.getChildren()) {

            for(Node el: defs.getChildren()) {
                if(el instanceof ID) {

                    entryInfoParD = (EntryInfoParD) el.accept(this);
                    id = entryInfoParD.getId();
                }
                if(el instanceof Type) {

                    type = (String)el.accept(this );
                }
                if(el instanceof ParDecl) {

                    parD += comma+el.accept(this);
                    comma =", ";
                }

            } // end internal for


            if(!id.equals("main")) {

                signs = "\n"+type +" " +id + "(" +parD +");";

            parD = comma = "";

                try {
                    this.writeOnFile(signs);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } //end externals for

        signs = "\nvoid inizializeGlobalVars();";
        try {
            this.writeOnFile(signs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String function ="";
        for(Node n: node.getChildren()) {

           function += "\n\n" + n.accept(this);
        }

        function += "\n\nvoid inizializeGlobalVars() { \n"
                + this.toInizialize +this.inizializeString() +"\n\n}";

        this.toInizialize = "";


        try {
            this.writeOnFile(function);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object visit(Def_fun node) {

        String id ="";
        String type ="";
        String stat ="";
        String parD = "";
        String SRPAR =")";
        String SLPAR ="(";
        String comma ="";
        String cCode ="";
        EntryInfoParD entryInfoParD;

        for(Node n: node.getChildren()) {

            if(n instanceof ID) {
                entryInfoParD = (EntryInfoParD) n.accept(this);
                id = entryInfoParD.getId();
            }

            if(n instanceof ParDecl) {
                parD += comma+n.accept(this);
                comma =", ";
            }

            if(n instanceof Type) {

                if(id.equals("main")) {
                    type="int";
                    parD = "void";
                }
                else {
                    type = (String)n.accept(this);
                }
            }

            if(n instanceof Statement) {
                stat +="\t"+(String) n.accept(this)+"\n";
            }

        } // end for
        if(id.equals("main")) {
            stat = "\tinizializeGlobalVars();\n" + stat + "\treturn 0;";
        }

        cCode += type +" "+id+ SLPAR+ parD+ SRPAR+" {\n"+ stat +"\n}";


        return cCode;
    }




    @Override
    public Object visit(Stat node) {
        return null;
    }


    @Override
    public Object visit(ParDecl node) {
        String cCode ="";
        String id ="";
        String type="";
        EntryInfoParD entryInfoParD;

        for (Node n: node.getChildren()) {

            if(n instanceof ID) {

                entryInfoParD = (EntryInfoParD) n.accept(this);
                id = entryInfoParD.getId();
            }
            if(n instanceof Type) {

                type = (String)n.accept(this);
                if (type.equals("char")) {id += "[30]";}
            }
        }
        cCode = type + " " +id;
        return cCode;
    }

    @Override
    public Object visit(Expr node) {
        return null;
    }

    @Override
    public Object visit(ASSIGNMENT node) {
        String cCode = "";
        EntryInfoParD entryInfoParD = null;
        String aText ="";
        String bText ="";


        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);

        if(a instanceof ID) {

            entryInfoParD = (EntryInfoParD) a.accept(this);
            aText = entryInfoParD.getId();

        }else {aText =(String) a.accept(this);}

        if(b instanceof ID){

            entryInfoParD = (EntryInfoParD) b.accept(this);
            bText = entryInfoParD.getId();

        } else {
            bText = (String) b.accept(this);
        }

        if( (b instanceof STRING_CONST) || (entryInfoParD.getType().equals("STRING")) ) {

            cCode += "strcpy("+aText+", "+bText+");";

        }else
            cCode += aText + " = " + bText +";";



        return cCode;

    }

    @Override
    public Object visit(MIX_ASSIGNMENT node) {
        return null;
    }

    @Override
    public Object visit(IF_THEN node) {
        String condition="";
        String statement="";
        String cCode = "";

        for(Node n: node.getChildren()){

            if(n instanceof RelopExpr) {
                condition = (String) n.accept(this);
            }
            if(n instanceof Statement) {
                statement += n.accept(this) + "\n\t";
            }

        }

        cCode = "if( "+condition+ " ) {" +"\n\t"+statement+"\n\t}";

        return cCode;
    }

    @Override
    public Object visit(IF_THEN_ELSE node) {
        String condition="";
        String statement="";
        String elseStatement ="";
        String cCode = "";

        for(Node n: node.getChildren()){

            if(n instanceof RelopExpr) {
                condition = (String) n.accept(this);
            }

            if( (n instanceof Statement) ) {
                if(!(n.getName().equals("ELSE"))) {
                    statement += n.accept(this) + "\n\t";
                }else {
                    elseStatement += n.accept(this) + "\n\t";
                }
            }

        } // end for
        cCode = "if( "+condition+ " ) {" +"\n\t"+statement+"\n\t}";
        cCode += "else {\n\t"+ elseStatement+"}";
        return cCode;
    }

    @Override
    public Object visit(Statement node) {

        String toReturn ="";
        for (Node n: node.getChildren()) {
            toReturn += "\n\t"+(String)n.accept(this);
        }

        return toReturn;
    }

    @Override
    public Object visit(READ node) {
        EntryInfoParD entryInfoParD;
        String cCode ="";
        String value="";
        String id="";

        if(node.sizeNode()>1) {
            for (Node n: node.getChildren()) {
                entryInfoParD = (EntryInfoParD) n.accept(this);
                id = entryInfoParD.getId();
                if(entryInfoParD.getType().equals("STRING")) {value="%s";}
                if(entryInfoParD.getType().equals("INT")) {value="%d";}
                if(entryInfoParD.getType().equals("FLOAT")) {value="%f";}
                if(entryInfoParD.getType().equals("BOOL")) {value="%d";}

                cCode += "\tscanf(\""+value+"\", &"+id+");"+ "\n\tprintf(\"\\n\");"+ "\n\t" ;
            }

        } else {
            Node n = node.getChildByIndex(0);
            entryInfoParD = (EntryInfoParD) n.accept(this);
            id = entryInfoParD.getId();

            if(entryInfoParD.getType().equals("STRING")) {value="%s";}
            if(entryInfoParD.getType().equals("INT")) {value="%d";}
            if(entryInfoParD.getType().equals("FLOAT")) {value="%f";}
            if(entryInfoParD.getType().equals("BOOL")) {value="%d";}

            cCode = "\tscanf(\""+value+"\", &"+id+");"+ "\n\t\tprintf(\"\\n\");";
        }
        return cCode;
    }

    @Override
    public Object visit(WRITE node) {

        EntryInfoParD entryInfoParD;
        String cCode ="";
        String message ="";
        String value="";
        String id="";


        if(node.sizeNode()>1) {

            for (Node n : node.getChildren()) {

                if(n instanceof SHARP_EXPR) {
                    id = n.getChildByIndex(0).getName();
                    value = "%d";
                    cCode += "printf(\""+value+"\", atoi("+id+"));"+ "\n\tprintf(\"\\n\");";
                }

                if(n instanceof NOT_EXPR) {
                    Node child = ((NOT_EXPR) n).getChildN();

                    if (child instanceof ID) {
                        entryInfoParD = (EntryInfoParD) child.accept(this);
                        id = entryInfoParD.getId();
                        if (entryInfoParD.getType().equals("BOOL")) {
                            value = "%d";
                        }
                        if (entryInfoParD.getType().equals("STRING")) {
                            value = "%s";
                        }
                        if (entryInfoParD.getType().equals("INT")) {
                            value = "%d";
                        }
                        if (entryInfoParD.getType().equals("FLOAT")) {
                            value = "%f";
                        }
                        cCode += "printf(\"" + value + "\", " + "!"+id + ");" + "\n\tprintf(\"\\n\");";
                    }else
                        cCode += "printf(\"" + "%d" + "\", " + n.accept(this) + ");" + "\n\tprintf(\"\\n\");";

                } // end if notexpr

                if(n instanceof RelopExpr) {
                    Node a= n.getChildByIndex(0);
                    Node b= n.getChildByIndex(1);
                    cCode += this.relopWrite(a,b, n);
                }

                if(n instanceof ArithExpr) {
                    Node a= n.getChildByIndex(0);
                    Node b= n.getChildByIndex(1);
                    cCode += this.arithWrite(a,b, n);
                }


                if (n instanceof STRING_CONST) {
                    message = (String)((STRING_CONST) n).getStringValue() ;
                    cCode += "printf("+ message +");\n\t";
                }
                if (n instanceof INT_CONST) {
                    message = (String)((INT_CONST) n).getIntValue();
                    cCode += "printf("+ message +");\n\t";
                }
                if (n instanceof FLOAT_CONST) {
                    message = (String)((FLOAT_CONST) n).getFloatValue();
                    cCode += "printf("+ message +");\n\t";
                }
                if (n instanceof ID) {

                    entryInfoParD = (EntryInfoParD) n.accept(this);
                    id = entryInfoParD.getId();
                    if(entryInfoParD.getType().equals("BOOL")) {value="%d"; }
                    if(entryInfoParD.getType().equals("STRING")) {value="%s";}
                    if(entryInfoParD.getType().equals("INT")) {value="%d";}
                    if(entryInfoParD.getType().equals("FLOAT")) {value="%f";}
                    cCode += "printf(\""+value+"\", "+id+");\n\t";
                }
            } //end for
            cCode += "\nprintf(\"\\n\");";

        } else { //end if

            Node nodeW = node.getChildByIndex(0);

            if(nodeW instanceof SHARP_EXPR) {
                id = nodeW.getChildByIndex(0).getName();
                value = "%d";
                cCode = "printf(\""+value+"\", atoi("+id+"));"+ "\n\tprintf(\"\\n\");";
            }

            if(nodeW instanceof RelopExpr) {
                Node a= nodeW.getChildByIndex(0);
                Node b= nodeW.getChildByIndex(1);
                cCode += this.relopWrite(a,b, nodeW);
            }

            if(nodeW instanceof ArithExpr) {
                Node a= nodeW.getChildByIndex(0);
                Node b= nodeW.getChildByIndex(1);
                cCode += this.arithWrite(a,b, nodeW);
            }

            if (nodeW instanceof STRING_CONST) {
                message = (String)((STRING_CONST) nodeW).getStringValue();
                if(message.equals("\"break\"")){

                    cCode += "break;\n\t";
                }else
                    cCode = "printf("+ message +");" + "\n\tprintf(\"\\n\");";
            }
            if (nodeW instanceof INT_CONST) {
                message = (String)((INT_CONST) nodeW).getIntValue();
                cCode = "printf(\""+ message +"\");" + "\n\tprintf(\"\\n\");";
            }
            if (nodeW instanceof FLOAT_CONST) {
                message = (String)((FLOAT_CONST) nodeW).getFloatValue();
                cCode = "printf(\""+ message +"\");" + "\n\tprintf(\"\\n\");";
            }
            if (nodeW instanceof ID) {
                entryInfoParD = (EntryInfoParD) nodeW.accept(this);
                id = entryInfoParD.getId();
                if(entryInfoParD.getType().equals("BOOL")) {value="%d";}
                if(entryInfoParD.getType().equals("STRING")) {value="%s";}
                if(entryInfoParD.getType().equals("INT")) {value="%d";}
                if(entryInfoParD.getType().equals("FLOAT")) {value="%f";}
                cCode = "printf(\""+value+"\", "+id+");"+ "\n\tprintf(\"\\n\");";
            }
            if(nodeW instanceof NOT_EXPR) {
                Node child = ((NOT_EXPR) nodeW).getChildN();
                if (child instanceof ID) {
                    entryInfoParD = (EntryInfoParD) child.accept(this);
                    id = entryInfoParD.getId();
                    if (entryInfoParD.getType().equals("BOOL")) {
                        value = "%d";
                    }
                    if (entryInfoParD.getType().equals("STRING")) {
                        value = "%s";
                    }
                    if (entryInfoParD.getType().equals("INT")) {
                        value = "%d";
                    }
                    if (entryInfoParD.getType().equals("FLOAT")) {
                        value = "%f";
                    }
                    cCode = "printf(\"" + value + "\", " + "!"+id + ");" + "\n\tprintf(\"\\n\");";
                }else  cCode = "printf(\"" + "%d" + "\", " + nodeW.accept(this) + ");" + "\n\tprintf(\"\\n\");";
            }
        }
        //

        return "\t"+cCode;
    }

    @Override
    public Object visit(FOR node) {
        String init="";
        String condition="";
        String id="";
        String cCode="";
        String statement="";

        for(Node n: node.getChildren()) {

            if(n instanceof FOR_ASSIGN) {
                init = (String) n.accept(this);
                id = n.getChildByIndex(0).getName();
            }
            if(n instanceof RelopExpr){
                condition = (String)n.accept(this);
            }
            if( (n instanceof Statement) && !(n instanceof FOR_ASSIGN) ) {
                statement += n.accept(this);
            }

        } //end for

        cCode = "\nfor( "+init+"; "+condition+"; "+id+"++) {\n\t\t"+
        statement+"\n\t}";

        return cCode;
    }

    @Override
    public Object visit(NOP node) {
        return "";
    }

    @Override
    public Object visit(RETURN node) {
        String cCode = "";
        String  toReturn = "";
        EntryInfoParD entryInfoParD;

        if(node.getChildByIndex(0) instanceof ID) {
            entryInfoParD = (EntryInfoParD) node.getChildByIndex(0).accept(this);
            toReturn = entryInfoParD.getId();
        }else {
            toReturn = (String) node.getChildByIndex(0).accept(this);
        }
        cCode += "return " + toReturn +";";
        return cCode;
    }

    @Override
    public Object visit(WHILE_DO node) {
        String statement="";
        String condition="";
        String cCode ="";

        for(Node n: node.getChildren()) {

            if(n instanceof RelopExpr){
                condition = (String) n.accept(this);
            }
            if(n instanceof Statement) {
                statement += n.accept(this) +"\n\t";
            }


        } //end for

        cCode = "\nwhile( "+condition+" ){\n\t"+statement+"\n}";
        return cCode;
    }

    @Override
    public Object visit(LOCAL node) {
        String varD = "";
        String cCode ="";
        String statement ="";

        for (Node n: node.getChildren()) {
            if(n instanceof VarDecl) {

                varD += (String) n.accept(this)+"\t";

            }
            if(n instanceof Statement) {
                statement += n.accept(this)+"\n\t";
            }
        }

        cCode = "\t"+varD +statement;

        return cCode;
    }

    @Override
    public Object visit(ID_STATEMENT node) {
        return null;
    }

    @Override
    public Object visit(Primitive node) {
        return null;
    }

    @Override
    public Object visit(Struct node) {
        return null;
    }


    @Override
    public Object visit(DIV node) {
        EntryInfoParD entryInfoParD;
        String cCode = "";
        String aText = "";
        String bText = "";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            aText = entryInfoParD.getId();
        }else {
            aText =(String) a.accept(this);
        }

        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            bText = entryInfoParD.getId();
        } else {
            bText =(String) b.accept(this);
        }

        cCode += aText + " / " + bText;


        return cCode;
    }

    @Override
    public Object visit(MINUS node) {
        EntryInfoParD entryInfoParD;
        String cCode = "";
        String aText = "";
        String bText = "";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            aText = entryInfoParD.getId();
        }else {
            aText =(String) a.accept(this);
        }

        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            bText = entryInfoParD.getId();
        } else {
            bText =(String) b.accept(this);
        }

        cCode += aText + " - " + bText;


        return cCode;
    }

    @Override
    public Object visit(TIMES node) {
        EntryInfoParD entryInfoParD;
        String cCode = "";
        String aText = "";
        String bText = "";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            aText = entryInfoParD.getId();
        }else {
            aText =(String) a.accept(this);
        }

        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            bText = entryInfoParD.getId();
        } else {
            bText =(String) b.accept(this);
        }

        cCode += aText + " * " + bText;


        return cCode;
    }

    @Override
    public Object visit(PLUS node) {

        EntryInfoParD entryInfoParD;
        String cCode = "";
        String aText = "";
        String bText = "";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            aText = entryInfoParD.getId();
        }else {
            aText =(String) a.accept(this);
        }

        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            bText = entryInfoParD.getId();
        } else {
            bText =(String) b.accept(this);
        }

        cCode += aText + " + " + bText;


        return cCode;
    }

    @Override
    public Object visit(AND_R node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        String cCode ="";

        String id1= (String) a.accept(this);
        String id2= (String) b.accept(this);

        cCode = "( "+id1+")"+" && " +"( "+id2+")";
        return cCode;
    }

    @Override
    public Object visit(EQ_R node) {
        EntryInfoParD entryInfoParD;
        String id1="";
        String id2="";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        String cCode ="";

        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            id1 = entryInfoParD.getId();
        } else {
            id1 = (String) a.accept(this);
        }
        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            id2 = entryInfoParD.getId();
        }else {
            id2 = (String) b.accept(this);
        }

        cCode = id1 +" == " +id2;
        return cCode;
    }

    @Override
    public Object visit(GE_R node) {
        EntryInfoParD entryInfoParD;
        String id1="";
        String id2="";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        String cCode ="";

        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            id1 = entryInfoParD.getId();
        } else {
            id1 = (String) a.accept(this);
        }
        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            id2 = entryInfoParD.getId();
        }else {
            id2 = (String) b.accept(this);
        }

        cCode = id1 +" >= " +id2;
        return cCode;
    }

    @Override
    public Object visit(GT_R node) {
        EntryInfoParD entryInfoParD;
        String id1="";
        String id2="";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        String cCode ="";

        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            id1 = entryInfoParD.getId();
        } else {
            id1 = (String) a.accept(this);
        }
        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            id2 = entryInfoParD.getId();
        }else {
            id2 = (String) b.accept(this);
        }

        cCode = id1 +" > " +id2;
        return cCode;
    }

    @Override
    public Object visit(LE_R node) {
        EntryInfoParD entryInfoParD;
        String id1="";
        String id2="";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        String cCode ="";

        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            id1 = entryInfoParD.getId();
        } else {
            id1 = (String) a.accept(this);
        }
        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            id2 = entryInfoParD.getId();
        }else {
            id2 = (String) b.accept(this);
        }

        cCode = id1 +" <= " +id2;
        return cCode;
    }

    @Override
    public Object visit(LT_R node) {
        EntryInfoParD entryInfoParD;
        String id1="";
        String id2="";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        String cCode ="";

        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            id1 = entryInfoParD.getId();
        } else {
            id1 = (String) a.accept(this);
        }
        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            id2 = entryInfoParD.getId();
        }else {
            id2 = (String) b.accept(this);
        }

        cCode = id1 +" < " +id2;
        return cCode;
    }

    @Override
    public Object visit(NE_R node) {
        EntryInfoParD entryInfoParD;
        String id1="";
        String id2="";
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        String cCode ="";

        if(a instanceof ID) {
            entryInfoParD = (EntryInfoParD) a.accept(this);
            id1 = entryInfoParD.getId();
        } else {
            id1 = (String) a.accept(this);
        }
        if(b instanceof ID) {
            entryInfoParD = (EntryInfoParD) b.accept(this);
            id2 = entryInfoParD.getId();
        }else {
            id2 = (String) b.accept(this);
        }

        cCode = id1 +" != " +id2;
        return cCode;
    }

    @Override
    public Object visit(OR_R node) {
        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);
        String cCode ="";

        String id1= (String) a.accept(this);
        String id2= (String) b.accept(this);

        cCode = "( "+id1+")"+" || " +"( "+id2+")";
        return cCode;
    }

    @Override
    public Object visit(BOOLEAN_CONST node) {
        if(node.toStringSemantic().equals("TRUE")){return "true";}
        if(node.toStringSemantic().equals("FALSE")) {return "false";}
        return "";
    }

    @Override
    public Object visit(EXPR_CONST node) {
        return null;
    }

    @Override
    public Object visit(FLOAT_CONST node) {
        return (String)node.getFloatValue();
    }



    @Override
    public Object visit(FUNCT_EXPR node) {
        String cCode="";
        String id="";
        String params="";
        EntryInfoParD entryInfoParD;

        for(Node n: node.getChildren()){

            if(n instanceof ID) {
                entryInfoParD = (EntryInfoParD) n.accept(this);
                id = entryInfoParD.getId();
            }
            if(n instanceof PARAMETERS_F) {
                params = (String) n.accept(this);
            }

        }
        cCode = id+"("+params+")";
        return cCode;
    }

    @Override
    public Object visit(PARAMETERS_F node) {
        String cCode="";
        String params = "";
        String comma ="";
        String id ="";
        EntryInfoParD entryInfoParD;

        for(Node n: node.getChildren()){

            if (n instanceof ID) {
                entryInfoParD = (EntryInfoParD) n.accept(this);
                id = entryInfoParD.getId();
            }
            if(n instanceof FUNCT_EXPR) {
                id = (String)n.accept(this);
            }
            if(n instanceof INT_CONST) {id = (String)((INT_CONST) n).getIntValue();}
            if(n instanceof STRING_CONST){}
            if(n instanceof FLOAT_CONST) {id = (String)((FLOAT_CONST) n).getFloatValue();}

            if(n instanceof ArithExpr){
                id = (String)n.accept(this);
            }

            cCode += comma + id;
            comma =", ";

        }

        return cCode;
    }

    @Override
    public Object visit(INT_CONST node) {
        return node.getIntValue();
    }

    @Override
    public Object visit(NIL_CONST node) {
        return "void";
    }

    @Override
    public Object visit(STRING_CONST node) {
        return node.getStringValue();
    }

    @Override
    public Object visit(TYPE_CONST node) {
        return null;
    }

    @Override
    public Object visit(MINUS_EXPR node) {
        Node n = node.getChildByIndex(0);
        EntryInfoParD entryInfoParD;
        String id="";

        if(n instanceof ID) {
            entryInfoParD = (EntryInfoParD) n.accept(this);
            id = entryInfoParD.getId();
            id = "-"+id;
        }
        if(n instanceof INT_CONST) {id = "-"+(String)((INT_CONST) n).getIntValue();}
        if(n instanceof STRING_CONST){}
        if(n instanceof FLOAT_CONST) {id = "-"+(String)((FLOAT_CONST) n).getFloatValue();}
        return id;
    }

    @Override
    public Object visit(NOT_EXPR node) {
        Node n = node.getChildByIndex(0);
        EntryInfoParD entryInfoParD = null;
        if(n instanceof ID) {
            entryInfoParD = (EntryInfoParD) n.accept(this);
            return "!"+entryInfoParD.getId();
        }
        return "!"+n.accept(this);
    }

    @Override
    public Object visit(SHARP_EXPR node) {
        String cCode="";
        String value="";
        Node child = node.getChildByIndex(0);

        Node parent = node.getParent();
        while( !(parent instanceof Global) && !(parent instanceof VarDecl) && !(parent instanceof ASSIGNMENT) ) {
            parent = parent.getParent();
        }


            if(child instanceof STRING_CONST) {

                value = ((STRING_CONST) child).getStringValue();
                cCode = "atoi("+value+")";

            } else cCode = "atoi("+child.getName()+")";



        return cCode;
    }

    @Override
    public Object visit(RelopExpr node) {
        return null;
    }

    @Override
    public Object visit(ArithExpr node) {
        return null;
    }

    @Override
    public Object visit(Node node) {
        return null;
    }

    @Override
    public Object visit(ARRAY_STAT node) {
        return null;
    }

    @Override
    public Object visit(FOR_ASSIGN node) {
        String id="";
        EntryInfoParD entryInfoParD;
        String cCode = "";
        String type ="";
        String value="";

        for(Node n: node.getChildren()) {

            if (n instanceof INT_CONST) {
                type = "int";
                value = (String)((INT_CONST) n).getIntValue();
            }
            if (n instanceof STRING_CONST) {
                type = "char";
                value = (String)((STRING_CONST) n).getStringValue();
            }
            if (n instanceof FLOAT_CONST) {
                type = "float";
                value = (String)((FLOAT_CONST) n).getFloatValue();
            }
            if (n instanceof ID) {
                id = n.getName();
            }

        }//end for Node

        cCode = type +" "+id+" = "+value;
        return cCode;
    }

    private String inizializeString() {
        String cCode ="";
        String id="";
        String varInt ="";

        for(Map.Entry <String, String> map: this.stringInit.entrySet()) {
            id = map.getKey();
            varInt = map.getValue();
            cCode += "\n\tstrcpy("+id+", "+varInt+");";
        }

        return cCode;
    }

    private String relopWrite(Node a, Node b, Node parent) {
        String cCode="";
        String aName="";
        String bName="";

        if (a instanceof STRING_CONST) {
            aName = (String)((STRING_CONST) a).getStringValue() ;
        }
        if (a instanceof INT_CONST) {
            aName = (String)((INT_CONST) a).getIntValue();
        }
        if (a instanceof FLOAT_CONST) {
            aName = (String)((FLOAT_CONST) a).getFloatValue();
        }
        if(a instanceof BOOLEAN_CONST) {aName= (String) a.accept(this);}
        if(a instanceof ID) {aName = a.getName();}

        if (b instanceof STRING_CONST) {
            bName = (String)((STRING_CONST) b).getStringValue() ;
        }
        if (b instanceof INT_CONST) {
            bName = (String)((INT_CONST) b).getIntValue();
        }
        if (b instanceof FLOAT_CONST) {
            bName = (String)((FLOAT_CONST) b).getFloatValue();
        }
        if(b instanceof BOOLEAN_CONST) {bName= (String) b.accept(this);}
        if(b instanceof ID) {bName = b.getName();}


        cCode = "printf(\"" + "%d" + "\", " +aName + parent.getSymbolC() + bName+ ");"
                + "\n\tprintf(\"\\n\");";

        return cCode;
    }

    private String arithWrite(Node a, Node b, Node parent) {
        String cCode="";
        String aName="";
        String bName="";
        String valueA = "";
        String valueB ="";

        if (a instanceof STRING_CONST) {
            aName = (String)((STRING_CONST) a).getStringValue() ;
        }
        if (a instanceof INT_CONST) {
            aName = (String)((INT_CONST) a).getIntValue();
            valueA = "INT";
        }
        if (a instanceof FLOAT_CONST) {
            aName = (String)((FLOAT_CONST) a).getFloatValue();
            valueA = "FLOAT";
        }
        if(a instanceof BOOLEAN_CONST) {aName= (String) a.accept(this);}
        if(a instanceof ID) {
            EntryInfoParD e = (EntryInfoParD) a.accept(this);
            aName = a.getName();
            valueA = e.getType();
        }

        if (b instanceof STRING_CONST) {
            bName = (String)((STRING_CONST) b).getStringValue() ;
        }
        if (b instanceof INT_CONST) {
            bName = (String)((INT_CONST) b).getIntValue();
            valueB = "INT";
        }
        if (b instanceof FLOAT_CONST) {
            bName = (String)((FLOAT_CONST) b).getFloatValue();
            valueB = "FLOAT";
        }
        if(b instanceof BOOLEAN_CONST) {bName= (String) b.accept(this);}
        if(b instanceof ID) {
            bName = b.getName();
            EntryInfoParD e = (EntryInfoParD) b.accept(this);
            valueB = e.getKind();
        }

        this.typeSystem = new TypeSystem(valueA, valueB, parent.getSymbol());
        String value = this.typeSystem.chekType();

        if (value.equals("BOOL")) {
            value = "%d";
        }
        if (value.equals("STRING")) {
            value = "%s";
        }
        if (value.equals("INT")) {
            value = "%d";
        }
        if (value.equals("FLOAT")) {
            value = "%f";
        }

        cCode = "printf(\"" + value + "\", " +aName + parent.getSymbol() + bName+ ");"
                + "\n\tprintf(\"\\n\");";

        return cCode;
    }

}
