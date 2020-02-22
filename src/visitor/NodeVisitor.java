package visitor;

import Scope.SymbolTableStack;
import syntaxTree.Node;

public class NodeVisitor  {
    private Node tree;
    private SemanticVisitor visitor;
    private CVisitor cVisitor;

    public NodeVisitor( SemanticVisitor visitor) {
        this.visitor = visitor;
    }

    public NodeVisitor(CVisitor cVisitor) {
        this.cVisitor = cVisitor;
    }

    public void visitingTree(Node node) {
            node.accept(this.visitor);


    }

    public void writeC(Node node) {
        node.accept(this.cVisitor);
        //System.out.println(node.listChildren());
    }


}
