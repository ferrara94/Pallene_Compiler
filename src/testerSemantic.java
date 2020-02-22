import Scope.SymbolTableStack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import syntaxTree.Program;
import visitor.NodeVisitor;
import visitor.SemanticVisitor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.IOException;

public class testerSemantic {

    public static void main(String[] args) throws IOException {
        String xmlFilePath = "xmlfile.xml";


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
        PalleneLexer lexer = new PalleneLexer(new FileReader("test_file/test1.txt"));
        //PalleneLexer lexer = new PalleneLexer(new FileReader("test_file/menuOperazioni.txt"));
        parser p = new parser(lexer, document, root, tempE);

        try {
            Program program = (Program) p.parse().value;

            SymbolTableStack scope = new SymbolTableStack();
            SemanticVisitor visitor = new SemanticVisitor(scope);

            NodeVisitor nodeV = new NodeVisitor(visitor);
            nodeV.visitingTree(program);


            //System.out.println(program.listChildren());
            String br="-------------------------------------------------------------------";
            String br2="-------------------------------------------------------------------";
            System.err.println(br+"\n"+br2);
            //System.out.println(visitor.getSymbolTableStack().toString());
            //System.out.println(visitor.getSymbolTableStack().isContained("DefFunOp"));


            /*System.out.println("Son0: " + program.getChildByIndex(0).getName());
            System.out.println("Son1: " + program.getChildByIndex(1).getName());*/
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println(e.getLocalizedMessage());
        }


    }

}
