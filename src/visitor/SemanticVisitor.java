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

import java.util.ArrayList;
import java.util.Collections;

public class SemanticVisitor implements Visitor {

    private SymbolTableStack SymbolStack;
    private ArrayList<Node> tree;
    private TypeSystem typeSystem; //used to check types(i.e. on operations)
    private int conLocal = 0; //used to distinguish local statements in symbol tables
    private int conFor = 10000; //used to distinguish for statements in symbol tables
    private int errNum = 1; //keep track of errors number

    public SemanticVisitor(SymbolTableStack SymbolStack) {
        this.SymbolStack = SymbolStack;
    }

    public void setSymbolStack(SymbolTableStack symbolStack) {
        SymbolStack = symbolStack;
    }

    public SymbolTableStack getSymbolTableStack() {
        return this.SymbolStack;
    }

    /*
    Since that functions must be used before the declaration line,
    we need to obtain all functions signs and add them into main
    symbol table
    */
    @Override
    public SymbolTableStack visit(Program node) {

        SymbolTable s = new SymbolTable(node.getName());
        this.SymbolStack.push(s);

        for(Node n: node.getChildren()){
            //read declaration functions
            if(n instanceof Function) {
                for( Node def: n.getChildren()) {
                    def.accept(this);
                }
            }
        } //end for function

        //after we've obtained signs, let's go through tree
        for(Node n: node.getChildren()){
            n.accept(this);
        }


        //return the full symbol table
        return this.SymbolStack;
    }

    @Override
    public SymbolTableStack visit(Global node) {

        if (this.SymbolStack.isContained("ProgramOp")) {


            for (Node n : node.getChildren()) {
                //we obtain informations about declaration
                n.accept(this);
            } // end for

        }//end if

        //we check if there is main function
        if(!this.SymbolStack.isContained("main")){
            String br="-------------------------------------------------------------------";
            System.out.println(br+"\nerror number ~ "+this.errNum++);
            System.err.println("\nMissing main() function\n");
        }
        return this.SymbolStack;

    }

    @Override
    public EntryInfoParD visit(VarDecl node) {

        SymbolTable parentTable = null;

        /*
        //we obtain the parent name;
        */

        Node parent = node.getParent();
        while ( (!parent.getName().equals("Global")) && (!(parent instanceof LOCAL)) ) {
            parent = parent.getParent();
        }

        if(parent.getName().equals("Global")) {
            //we obtain the main symbol table
            parentTable = this.SymbolStack.getSymbolTableByName("ProgramOp");
        } else {
            //we obtain the local symbol table
            parentTable = this.SymbolStack.getSymbolTableByNLocal(parent.getNumber());
        }

        String key = "";
        String type = "";



        for (Node n: node.getChildren()) {
            //we obtain id declaration
            if (n instanceof ID) {
                key = n.toStringSemantic();
            }
            //we obtain type declaration
            if (n instanceof Type) {
                type = (String)n.accept(this);
            }

            // = ---
            if (n instanceof VarIntValue) {
                /*
                // at this point i don't consider call functions but only
                // informations like type or parameters
                */

                String typeVarIntValue = (String) n.accept(this);

                //we check if the assignment type is correct
                //typeA = typeB dove typeA:int & typeB:int
                if (!(type.equals(typeVarIntValue))) {
                    if(!typeVarIntValue.equals("unknown")) {
                        String br="-------------------------------------------------------------------";
                        System.out.println(br+"\nerror number ~ "+this.errNum++);
                        System.err.println("\nAssignment Type error ~ { " +key+ " = " + n.toStringSemantic()+ " }:\n\t" + key + " is " + type +
                                " ~ " + n.toStringSemantic() + " is " + typeVarIntValue);
                    }
                }

            } // end Varint value
        } //end for

        //we check variables double declarations
        if(parentTable.containsKey(key)) {
            String br="-------------------------------------------------------------------";
            System.out.println(br+"\nerror number ~ "+this.errNum++);
            System.err.println("\nDouble declaration:\n\tVariable \"" + key +
                    "\" is already defined in the scope " );
        } else {
            //we add it to main tables
            parentTable.put(key, new EntryInfo("var",type));
            //System.err.println(programTable);
        }
        //return id and type of declaration
        return new EntryInfoParD(key,"var", type);
    }


    @Override
    public String visit(ID node) {


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
                return entryInfoID.getType();
            }

            SymbolTable gParentTable = localTable.getParentTable();


            while ( (!gParentTable.getTypeNode().equals("DEF_FUN")) && (!gParentTable.getTypeNode().equals("FOR"))) {
                if(gParentTable.containsKey(node.getName())) {
                    EntryInfo entryInfoID = gParentTable.getEntryInfoByName(node.getName());
                    return entryInfoID.getType();
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
                return entryInfoID.getType();
            }

            SymbolTable gParentTable = forTable.getParentTable();

            while ( (!gParentTable.getTypeNode().equals("DEF_FUN")) && (!gParentTable.getTypeNode().equals("LOCAL"))) {
                if(gParentTable.containsKey(node.getName())) {
                    EntryInfo entryInfoID = gParentTable.getEntryInfoByName(node.getName());
                    return entryInfoID.getType();
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
                    return entryInfoID.getType();
                }

                gParentTable = localTable.getParentTable();

                while ( (!gParentTable.getTypeNode().equals("DEF_FUN")) && (!gParentTable.getTypeNode().equals("FOR"))) {
                    if(gParentTable.containsKey(node.getName())) {
                        EntryInfo entryInfoID = gParentTable.getEntryInfoByName(node.getName());
                        return entryInfoID.getType();
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


               return entryInfoID.getType();
            }
        } // end if DefunOp


        SymbolTable checkTable = this.SymbolStack.getSymbolTableByName("ProgramOp");
        if(checkTable.containsKey(node.getName())) {

            EntryInfo entryCheck = checkTable.getEntryInfoByName(node.getName());


            if (entryCheck.getKind().equals("funct") && !(node.getParent() instanceof FUNCT_EXPR)) {


                Node statParentNode = node.getParent();
                while (!(statParentNode instanceof Statement)) {
                    statParentNode = statParentNode.getParent();
                }
                String br="-------------------------------------------------------------------";
                System.out.println(br+"\nerror number ~ "+this.errNum++);
                System.err.println("\nCannot Resolve symbol \"" + node.getName() + "\"  in ~ "
                        + node.getParent().toStringSemantic() + "\n\t        " +
                        "   Could be needed the form \" " + node.getName() + "(x, y) \"");
            } // fine controllo

        }

            SymbolTable programTable = this.SymbolStack.getSymbolTableByName("ProgramOp");



            if(programTable.containsKey(node.getName())) {
                EntryInfo entryInfoID = programTable.getEntryInfoByName(node.getName());


                return entryInfoID.getType();
            } else {


                if(node.getParent() instanceof FUNCT_EXPR) {
                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nIdentifier Error ~ " + node.getParent().toStringSemantic());
                    System.err.println("\tCannot resolve function ~ " + node.getParent().toStringSemantic() );
                    return "unknown";
                }


                String br="-------------------------------------------------------------------";
                System.out.println(br+"\nerror number ~ "+this.errNum++);
                System.err.println("\nIdentifier Error ~ " + node.getParent().toStringSemantic());
                System.err.println("\t" + node.getName() + " isn't declared ");
                return "unknown";
            }


    } // end method for ID

    @Override
    public String visit(FOR node) {

        SymbolTable s = null;


        Node parent = node.getParent();
        while ( (!parent.getName().equals("Global")) && (!parent.getName().equals("DefFunOp"))
                && ( !(parent instanceof FOR) ) && ( !(parent instanceof LOCAL)) ) {
            parent = parent.getParent();
        }


        if( parent.getName().equals("DefFunOp") ){

            String functID = parent.getChildByIndex(0).getName();


            SymbolTable functTable = this.SymbolStack.getSymbolTableByName(functID);


            String nFor = Integer.toString(this.conFor);
            node.setNumber(nFor);


            functTable.put(node.getName()+"("+nFor+")",new EntryInfo("statement",nFor));

            s = new SymbolTable(node.getName(),functID);

            s.setNumberLocal(nFor);
            s.setParentTable(functTable);
            s.setTypeNode("FOR");
            this.SymbolStack.push(s);


            this.conFor--;


        } // end if DefunOp


        if( (parent instanceof FOR) || (parent instanceof LOCAL) ){
            String parentForNumber = parent.getNumber();

            SymbolTable parentForTable = this.SymbolStack.getSymbolTableByNLocal(parentForNumber);

            String nFor = Integer.toString(this.conFor);
            node.setNumber(nFor);

            parentForTable.put(node.getName()+"("+nFor+")",new EntryInfo("statement",nFor));

            s = new SymbolTable(node.getName(),parent.getName()+"("+parentForNumber +")");
            s.setNumberLocal(nFor);
            s.setParentTable(parentForTable);
            s.setTypeNode("FOR");
            this.SymbolStack.push(s);

            this.conFor--;


        } // end if For & LOCAL



        EntryInfoParD entry = null;

        for (Node n: node.getChildren()) {

            if(n instanceof FOR_ASSIGN){

                entry = (EntryInfoParD) n.accept(this);

                    SymbolTable forTable = this.SymbolStack.getSymbolTableByNLocal(s.getNumberLocal());
                    forTable.put(entry.getId(), entry.getEntryInfo());


                // end ID if


            } else if(n instanceof RelopExpr){
                String typeExpr = (String)n.accept(this);


                if(typeExpr.equals("ErrorType")) {

                    String typeA = (String)n.getChildByIndex(0).accept(this);
                    String typeB = (String)n.getChildByIndex(1).accept(this);

                    String nameA = (String)n.getChildByIndex(0).toStringSemantic();
                    String nameB = (String)n.getChildByIndex(1).toStringSemantic();

                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nComparison Error:\n\tCannot be applied the operation ~ "
                            +nameA+":"+typeA+" " + n.getSymbol() +" "+nameB+":" +typeB);
                }

            } else {
                n.accept(this);
            }


        } // end for

        return "null";
    }


    @Override
    public String visit(LOCAL node) {

        String nameParent = "";

        Node parent = node.getParent();
        while ( (!parent.getName().equals("Global")) && (!parent.getName().equals("DefFunOp"))
                && (!parent.getName().equals("FOR_STATEMENT")) && (!(parent instanceof LOCAL))) {
            parent = parent.getParent();
        }




        if( parent.getName().equals("DefFunOp") ){

            String functID = parent.getChildByIndex(0).getName();


            SymbolTable functTable = this.SymbolStack.getSymbolTableByName(functID);


            String nLocal = Integer.toString(this.conLocal);
            node.setNumber(nLocal);


            functTable.put(node.getName()+"("+nLocal+")",new EntryInfo("statement",nLocal));

            SymbolTable s = new SymbolTable(node.getName(),functID);

            s.setNumberLocal(nLocal);
            s.setParentTable(functTable);
            s.setTypeNode("LOCAL");
            this.SymbolStack.push(s);

            this.conLocal++;


        } // end if DefunOp


        if( (parent instanceof LOCAL) || (parent instanceof FOR) ){


            String parentLocalNumber = parent.getNumber();


            SymbolTable parentLocalTable = this.SymbolStack.getSymbolTableByNLocal(parentLocalNumber);

            String nLocal = Integer.toString(this.conLocal);
            node.setNumber(nLocal);


            parentLocalTable.put(node.getName()+"("+nLocal+")",new EntryInfo("statement",nLocal));

            SymbolTable s = new SymbolTable(node.getName(),parent.getName()+"("+parentLocalNumber +")");
            s.setNumberLocal(nLocal);
            s.setParentTable(parentLocalTable);
            s.setTypeNode("LOCAL");
            this.SymbolStack.push(s);

            this.conLocal++;


        } // end if LOCAL & For parent


        for(Node n: node.getChildren()) {
                n.accept(this);
        }




        return null;
    }

    @Override
    public Object visit(VarIntValue node) {

        return node.getChildByIndex(0).accept(this);
    }


    @Override
    public EntryInfoParD visit(FOR_ASSIGN node) {

        String key ="";
        String type = "";

        Node id = node.getChildByIndex(0);
        key = id.getName();

        Node expression = node.getChildByIndex(1);
        type = (String) expression.accept(this);


        EntryInfo entry= new EntryInfoParD(key,"var",type);


        return (EntryInfoParD) entry;
    }

    @Override
    public SymbolTableStack visit(Function node) {

        SymbolTable programTable = this.SymbolStack.getSymbolTableByName("ProgramOp");

        for (Node def_fun: node.getChildren()) {
            for (Node stat: def_fun.getChildren()) {


                if(stat instanceof Statement) {
                    stat.accept(this);
                }
            }
        } // end for


        for (Node def_fun: node.getChildren()) {


            String functID = def_fun.getChildByIndex(0).getName();

            SymbolTable funcTable = this.SymbolStack.getSymbolTableByName(functID);

            if(programTable.containsKey(functID)) {


                EntryInfo entryFunct = programTable.getEntryInfoByName(functID);


                if( !(entryFunct.getType().equals("NIL")) ) {
                    if( !(funcTable.containsKey("return")) ) {
                        String br="-------------------------------------------------------------------";
                        System.out.println(br+"\nerror number ~ "+this.errNum++);
                        System.err.println("\nMissing Return Statement in\n\t~ "+functID+"(): "+ entryFunct.getType());
                    } // end if
                    // end if
                } else {
                    if( (funcTable.containsKey("return")) ) {
                        String br="-------------------------------------------------------------------";
                        System.out.println(br+"\nerror number ~ "+this.errNum++);
                        System.err.println("\nReturn Error in function "+ functID +":\n\t~ "
                            +"Cannot return a value from a method with NIL return type" );

                    } // end if
                }

            }


        }
        return null;
    }


    @Override
    public Object visit(Def_fun node) {


        String parent = node.getParent().getParent().getName();
        String name = node.getChildByIndex(0).getName();

        SymbolTable defFunTable = new SymbolTable(name,parent);
        defFunTable.setTypeNode("DEF_FUN");


        SymbolTable parentTable = this.SymbolStack.getSymbolTableByName(parent);
        String key = "";
        String type = "";

        for(Node n: node.getChildren()) {

            if (n instanceof ID) {
                key = n.getName();
            }
            if (n instanceof Type) {

                type = ((Type) n).accept(this);

            }
            if (n instanceof ParDecl) {
                EntryInfoParD entry =  (EntryInfoParD) n.accept(this);


                defFunTable.put(entry.getId(), entry.getEntryInfo());
            }
        } // end for

        if (!this.SymbolStack.isContained(key)) {


            parentTable.put(key, new EntryInfo("funct", type));
            this.SymbolStack.push(defFunTable);

        } else {
            String br="-------------------------------------------------------------------";
            System.out.println(br+"\nerror number ~ "+this.errNum++);
            System.err.println("\nDouble function declaration ~ " + key );

        }


        return this.SymbolStack;
    }

    @Override
    public EntryInfo visit(ParDecl node) {

        String id = node.getId().getName();
        String kind = "var";
        String type = node.getType().getName();


        return new EntryInfoParD(id, kind, type);
    }

    @Override
    public String visit(Statement node) {

        for (Node s: node.getChildren()) {


            if(s instanceof RETURN) {



                if(s.accept(this).equals("true")) {

                    Node functionIdNode = node.getParent();
                    while(!(functionIdNode.getName().equals("DefFunOp"))) {
                        functionIdNode = functionIdNode.getParent();
                    }

                    String functionID = functionIdNode.getChildByIndex(0).getName();
                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nThere are Different Return Statements ~ "+functionID);
                }

            }else {
                s.accept(this);
            }
        }
        return null;
    }

    @Override
    public String visit(RETURN node) {
        String flag = "false";


        SymbolTable programTable = this.SymbolStack.getSymbolTableByName("ProgramOp");


        Node functionIdNode = node.getParent();
        while(!(functionIdNode.getName().equals("DefFunOp"))) {
            functionIdNode = functionIdNode.getParent();
        }
        String functionID = functionIdNode.getChildByIndex(0).getName();
        String nameReturn = node.getChildByIndex(0).toStringSemantic();


        SymbolTable functTable = this.SymbolStack.getSymbolTableByName(functionID);


        if(functTable.containsKey("return")) {

            flag = "true";

        }



        String typeReturn = (String)node.getChildByIndex(0).accept(this);
        String functType = programTable.get(functionID).getType();


        if(functType.equals("NIL")){
            functTable.put("return",new EntryInfo("return","NIL"));
            return null;
        }


        if( !(functType.equals(typeReturn)) ) {

            if(typeReturn.equals("ErrorType")){


                Node typeNode = node.getChildByIndex(0);


                if(typeNode instanceof RelopExpr) {

                    Node nodeA = typeNode.getChildByIndex(0);
                    Node nodeB = typeNode.getChildByIndex(1);


                    String typeA = (String) nodeA.accept(this);
                    String typeB = (String) nodeB.accept(this);;
                    String cause = "";


                    if(typeA.equals("ErrorType")) {cause=nodeA.getName();}
                    else if(typeB.equals("ErrorType")) {cause=nodeB.getName();}
                    else {cause=typeNode.toStringSemantic();}

                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nReturn Type Error caused by Relop Error:\n\t~ Cause is "+cause);
                } // end relopExpr


                if(typeNode instanceof ArithExpr) {


                    Node nodeA = typeNode.getChildByIndex(0);
                    Node nodeB = typeNode.getChildByIndex(1);


                    String typeA = (String) nodeA.accept(this);
                    String typeB = (String) nodeB.accept(this);;
                    String cause = "";


                    if(typeA.equals("ErrorType")) {cause=nodeA.toStringSemantic()+":"+typeA;}
                    else if(typeB.equals("ErrorType")) {cause=nodeB.toStringSemantic()+":"+typeB;}
                    else {cause=typeNode.toStringSemantic()+":"+typeReturn;}

                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nReturn Type Error caused by Arithmetic Error:\n\t~ Cause is "+cause );

                } // end arithmeticExpr

            } // end ErrorType if
            else {
                String br="-------------------------------------------------------------------";
                System.out.println(br+"\nerror number ~ "+this.errNum++);
                System.err.println("\nReturn Type Error in "+functionID +"()\n\t " +
                        "expected:" + functType + " ~ found:" + typeReturn + "{ " + nameReturn + " }");
            }

        } //end equals if


        functTable.put("return",new EntryInfo("return","statement"));


        return flag;


    }




    @Override
    public String visit(ASSIGNMENT node) {
        String nameA="";
        String nameB ="";


        if(node.getChildByIndex(0) instanceof ID) {
             nameA = node.getChildByIndex(0).getName();
        }else {nameA = node.getChildByIndex(0).toStringSemantic();}

        if(node.getChildByIndex(1) instanceof ID) {
            nameB = node.getChildByIndex(1).getName();
        }else {nameB = node.getChildByIndex(1).toStringSemantic();}



        String typeA = (String)node.getChildByIndex(0).accept(this);
        String typeB = (String)node.getChildByIndex(1).accept(this);


        if (!typeA.equals(typeB)) {

            if( !( (typeA.equals("ErrorType")) || (typeB.equals("ErrorType")) ) ) {

                String br="-------------------------------------------------------------------";
                System.out.println(br+"\nerror number ~ "+this.errNum++);
                System.err.println("\nAssignment Type error:\n\t" + nameA + " is " + typeA +
                        " ~ " + nameB + " is " + typeB);

            } else {

                Node typeNode = node.getChildByIndex(1);

                if(typeNode instanceof RelopExpr) {
                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nAssignment Type Error caused by Relop Error:\n\tCannot be applied the operation ~ " +
                           nameA+":"+typeA+ " "+typeNode.getSymbol()+" "+nameB+":"+typeB );

                } // end relopexpr if

                if(typeNode instanceof ArithExpr) {

                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nAssignment Type Error caused by Arithmetic Error:\n\tCannot be applied the operation ~ " +
                            nameA+":"+typeA+ " "+typeNode.getSymbol()+" "+nameB+":"+typeB );

                } // end arithexpr if


            }

        }

        return null;
    }

    @Override
    public Object visit(FUNCT_EXPR node) {



        SymbolTable functTable = null;
        EntryInfo entryInfo = null;

        String type = "unknown";
        String typeParameterDef ="";
        String typeParameterGiven ="";

        for(Node n: node.getChildren()) {


            if(n instanceof ID) {
                type = (String)n.accept(this);


                if(this.SymbolStack.isContained(n.getName())) {
                    functTable = this.SymbolStack.getSymbolTableByName(n.getName());
                }else {return "unknown";}

            } // end ID if



            if(n instanceof PARAMETERS_F) {



                if( (n.sizeNode() == 0) && (functTable.getTypes().size() != n.sizeNode()) )  {
                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nNumber Parameter error:\n\t"+functTable.getScopeName()+" - found: " +n.sizeNode() +
                            " ~ expected: "+functTable.getTypes().size());

                }

                int i = 0;
                for(Node params: n.getChildren()){

                    typeParameterGiven = (String)params.accept(this);



                    if(i<n.sizeNode()) {

                        if ( n.sizeNode() != functTable.getTypes().size() ) {
                            String br="-------------------------------------------------------------------";
                            System.out.println(br+"\nerror number ~ "+this.errNum++);
                            System.err.println("\nNumber Parameter error:\n\t"+functTable.getScopeName()+" - found: " +n.sizeNode() +
                                    " ~ expected: "+functTable.getTypes().size());
                            break;
                        } else {
                            typeParameterDef = functTable.getTypes().get(i);


                            if (!(typeParameterGiven.equals("unknown"))) {
                                if (!(typeParameterDef.equals(typeParameterGiven))) {
                                    String br="-------------------------------------------------------------------";
                                    System.out.println(br+"\nerror number ~ "+this.errNum++);

                                    System.err.println("\nIncompatible Parameter Type error:\n\t" +functTable.getScopeName()+ " - found:" + typeParameterGiven +
                                            "{" + params.toStringSemantic() + "}" + " ~ " + "expected: " + typeParameterDef);
                                } // end equals if


                                i++;
                            }
                        }
                    } //end i<node.size
                } // end for
            } //If parameters

        } //end for

        return type;
    }

    @Override
    public Object visit(PARAMETERS_F node){
        for (Node n: node.getChildren()) {
            //n.accept(this);
        }

        return null;
    }






    @Override
    public SymbolTableStack visit(Stat node) {
        return null;
    }


    //

    @Override
    public String visit(FLOAT_CONST node) {
        return "FLOAT";
    }
    @Override
    public String visit(INT_CONST node) {
        return "INT";
    }
    @Override
    public Object visit(STRING_CONST node) {
        return "STRING";
    }

    //

    @Override
    public Object visit(Expr node) {
        System.err.println("expr");
        return "";
    }



    @Override
    public SymbolTableStack visit(MIX_ASSIGNMENT node) {
        return null;
    }

    @Override
    public SymbolTableStack visit(IF_THEN node) {

        for(Node n: node.getChildren()) {
            if( (n instanceof RelopExpr) || (n instanceof ArithExpr) ) {
                String toCheck = (String)n.accept(this);
                if(!toCheck.equals("BOOL")) {

                    Node nodeA = n.getChildByIndex(0);
                    Node nodeB = n.getChildByIndex(1);


                    String typeA = (String) nodeA.accept(this);
                    String typeB = (String) nodeB.accept(this);;
                    String cause = "";


                    if(typeA.equals("ErrorType")) {cause=nodeA.getName();}
                    else if(typeB.equals("ErrorType")) {cause=nodeB.getName();}
                    else {cause=n.toStringSemantic();}

                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nError caused by \""+ node.getName() +"\" Condition:\n\t~ Cause is " + cause);
                }
            }else {
                n.accept(this);
            }
        }
        return null;
    }

    @Override
    public SymbolTableStack visit(IF_THEN_ELSE node) {

        for(Node n: node.getChildren()) {
            if( (n instanceof RelopExpr) || (n instanceof ArithExpr) ) {

                String toCheck = (String)n.accept(this);

                if(!toCheck.equals("BOOL")) {

                    Node nodeA = n.getChildByIndex(0);
                    Node nodeB = n.getChildByIndex(1);


                    String typeA = (String) nodeA.accept(this);
                    String typeB = (String) nodeB.accept(this);;
                    String cause = "";


                    if(typeA.equals("ErrorType")) {cause=nodeA.getName();}
                    else if(typeB.equals("ErrorType")) {cause=nodeB.getName();}
                    else {cause=n.toStringSemantic();}
                    
                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nError caused by \""+ node.getName() +"\" Condition:\n\t~ Cause is " + cause);
                }
            }else {
                n.accept(this);
            }
        }
        return null;
    }



    @Override
    public SymbolTableStack visit(READ node) {

        for (Node n: node.getChildren()) {

            if( !(n instanceof ID) ) {
                String br="-------------------------------------------------------------------";
                System.out.println(br+"\nerror number ~ "+this.errNum++);
                System.err.println("\nError in Read Statement:\n\t"+node.toStringSemantic()+"\n\t\t" +
                        "The correct form is: ( ID <== )" );
            } else
                n.accept(this);
        }
        return null;
    }

    @Override
    public SymbolTableStack visit(WRITE node) {
        for (Node n: node.getChildren()) {
            if(n instanceof RelopExpr) {
                String a = (String) n.getChildByIndex(0).accept(this);
                String b = (String) n.getChildByIndex(1).accept(this);
                this.typeSystem = new TypeSystem(a, b, "relop");
                if(this.typeSystem.chekType().equals("ErrorType")) {
                    System.err.println("\nWrite error: \n\tError type caused by ~ "+n.toStringSemantic());
                }
            }
            if(n instanceof ArithExpr) {
                String a = (String) n.getChildByIndex(0).accept(this);
                String b = (String) n.getChildByIndex(1).accept(this);
                this.typeSystem = new TypeSystem(a, b, n.getSymbol());
                if(this.typeSystem.chekType().equals("ErrorType")) {
                    String br="-------------------------------------------------------------------";
                    System.out.println(br+"\nerror number ~ "+this.errNum++);
                    System.err.println("\nWrite error: \n\tError type caused by ~ "+n.toStringSemantic());
                }
            }
            n.accept(this);
        }
        return null;
    }


    @Override
    public SymbolTableStack visit(NOP node) {
        return null;
    }



    @Override
    public SymbolTableStack visit(WHILE_DO node) {
        for(Node n: node.getChildren())
            n.accept(this);
        return null;
    }


    @Override
    public SymbolTableStack visit(ID_STATEMENT node) {
        return null;
    }

    @Override
    public SymbolTableStack visit(Primitive node) {
        return null;
    }

    @Override
    public Node visit(Struct node) {
        return null;
    }

    @Override
    public String visit(Type node) {
        return node.getName();
    }

    @Override
    public Object visit(PLUS node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);



        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);;


        this.typeSystem = new TypeSystem(aType, bType, "+");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(DIV node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);

        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);;

        this.typeSystem = new TypeSystem(aType, bType, "/");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(MINUS node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);

        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);;

        this.typeSystem = new TypeSystem(aType, bType, "-");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(TIMES node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);


        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);

        this.typeSystem = new TypeSystem(aType, bType, "*");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(ArithExpr node) {

        return null;
    }

    @Override
    public Object visit(RelopExpr node) {
       return "";
    }

    @Override
    public Object visit(AND_R node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);



        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);


        this.typeSystem = new TypeSystem(aType, bType, "relop");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(EQ_R node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);


        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);


        this.typeSystem = new TypeSystem(aType, bType, "relop");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(GE_R node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);


        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);

        this.typeSystem = new TypeSystem(aType, bType, "relop");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(GT_R node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);


        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);

        this.typeSystem = new TypeSystem(aType, bType, "relop");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(LE_R node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);


        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);


        this.typeSystem = new TypeSystem(aType, bType, "relop");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(LT_R node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);


        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);



        this.typeSystem = new TypeSystem(aType, bType, "relop");

        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(NE_R node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);


        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);

        this.typeSystem = new TypeSystem(aType, bType, "relop");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(OR_R node) {

        Node a = node.getChildByIndex(0);
        Node b = node.getChildByIndex(1);


        String aType = (String)a.accept(this);
        String bType= (String)b.accept(this);

        this.typeSystem = new TypeSystem(aType, bType, "relop");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(BOOLEAN_CONST node) {
        return "BOOL";
    }

    @Override
    public Object visit(EXPR_CONST node) {
        return null;
    }



    @Override
    public Object visit(NIL_CONST node) {
        return null;
    }



    @Override
    public Object visit(TYPE_CONST node) {
        return null;
    }

    @Override
    public Object visit(MINUS_EXPR node) {
        String type = (String)node.getChildByIndex(0).accept(this);
        if( (type.equals("STRING")) || (type.equals("BOOL"))  ) {return "ErrorType";}
        return type;
    }

    @Override
    public Object visit(NOT_EXPR node) {

        Node a = node.getChildByIndex(0);


        String aType = (String)a.accept(this);

        this.typeSystem = new TypeSystem(aType, "not");
        return this.typeSystem.chekType();
    }

    @Override
    public Object visit(SHARP_EXPR node) {



        Node a = node.getChildByIndex(0);


        String aType = (String)a.accept(this);
        this.typeSystem = new TypeSystem(aType, "sharp");

        return this.typeSystem.chekType();
    }


    @Override
    public Object visit(Node node) {
        return null;
    }

    @Override
    public Object visit(ARRAY_STAT node) {
        return null;
    }


}
