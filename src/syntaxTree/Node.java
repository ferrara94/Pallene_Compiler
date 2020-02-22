package syntaxTree;

import org.w3c.dom.Element;

import visitor.CVisitor;
import visitor.SemanticVisitor;
import visitor.Visitable;

import java.util.ArrayList;
import java.util.List;

public class Node implements Visitable {

    public Element element = null;
    private String name;
    private Node parent = null;
    private ArrayList<Node> children;
    public Node nodeTemp;
    private String number ="";

    public String getNumber() {
        return number;
    }

    public String getSymbolC() {return "";}

    public void setNumber(String number) {
        this.number = number;
    }

    public Node(String name) {
        this.name = name;
        this.children = new ArrayList<Node>();
    }

    @Override
    public Object accept(SemanticVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public Object accept(CVisitor visitor) {
        return visitor.visit(this);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public String getName() { return this.name; }

    public String getSymbol() { return "symbol";}

    @Override
    public String toString() {return this.getName();}

    public Object getValue() { return null; }

    public Node getParent() {
        if (this.hasParent())
            return this.parent;
        else
            return null;
    }

    public int sizeNode() {
        return this.children.size();
    }

    public boolean isLeaf() {
        if (this.sizeNode() == 0)
            return true;
        else
            return false;
    }

    public boolean addChild(Node child) {
        child.addParent(this);
        this.children.add(child);
        return true;
    }

    public boolean addChildren(List<Node> children) {
        for(Node n: children) {
            n.addParent(this);
            this.children.add(n);
        }
        return true;
    }

    public boolean addParent(Node parent) {
        if(this.getParent() != null)
            return false;
        this.parent = parent;
        return true;
    }

    public Node getChildByIndex(int index) {
        if (this.sizeNode() == 0)
            return null;
        else
            return this.children.get(index);
    }

    public Node getChildByName(String name) {
        if (this.sizeNode() == 0)
            return null;

        for (Node n : this.children) {
            if (n.name.equals(name))
                return n;
        }

        return null;

    }

    public boolean hasParent() {
        if (this.parent != null)
            return true;
        else
            return false;
    }

    public boolean removeParent() {
        if (this.hasParent()) {
            this.parent = null;
            return true;
        }
        else
            return false;
    }

    public String listChildren2() {
        String list = "";
        if (this.getName() != "Expr")
            list ="< " + this.getName() +" > " + "\n";
        for (Node n: this.children) {
            if (n.sizeNode() >0 ) {
                list += n.listChildren();
            }else {
                if (n.getName().equals(this.getName()))
                    list += "< /" + n.getName() + " >";
                else {
                    list += "   " + n.toString() ;
                }
            }
        }
        return list;
    }


    public String listChildren() {
        String list ="";
        if (this.getName() != "Expr")
            list = this.spaceParent(this) + this.toString() + "\n";
        if (this.sizeNode() > 0) {
            for (Node n: this.children ) {
                    list += n.listChildren();
            }
        } // end if

        return list;

    }

    public String spaceParent(Node n) {
        String space ="";
        if ( !n.hasParent() )
            return "";
        else {
            //if (n.sizeNode() > 0) {
                //System.out.println(n.getParent().getName());
                space += "    ";
            //}
            //else return "";
        }
        space += spaceParent(n.getParent());
        return space;
    }

    public String typeSemantic() {
        return "";
    }

    public String toStringSemantic() {
        return "";
    }


}