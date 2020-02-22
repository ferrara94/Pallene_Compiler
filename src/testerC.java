import Scope.SymbolTableStack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import syntaxTree.Program;
import visitor.CVisitor;
import visitor.NodeVisitor;
import visitor.SemanticVisitor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class testerC {

    public static void main(String[] args) throws IOException {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        Element tempE = document.createElement("root");

        //PalleneLexer lexer = new PalleneLexer(new FileReader(args[0]));
        PalleneLexer lexer = new PalleneLexer(new FileReader("test_file/menuOperazioni.txt"));

        parser p = new parser(lexer, document, root, tempE);

        try {
            Program program = (Program) p.parse().value;

            //analisi semantica
            SymbolTableStack scope = new SymbolTableStack();
            SemanticVisitor visitor = new SemanticVisitor(scope);

            NodeVisitor nodeV = new NodeVisitor(visitor);
            nodeV.visitingTree(program);

            //System.out.println(visitor.getSymbolTableStack().toString());

            //traduzione in c
            CVisitor cVisitor = new CVisitor("intermediate.c",visitor.getSymbolTableStack());

            NodeVisitor nodeVC = new NodeVisitor(cVisitor);
            nodeVC.writeC(program);

        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println(e.getLocalizedMessage());
        }



    }

}
