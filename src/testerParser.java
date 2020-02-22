import org.w3c.dom.Document;
import org.w3c.dom.Element;
import syntaxTree.*;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class testerParser {
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
        parser p = new parser(lexer, document, root, tempE);

        try {
            //p.debug_parse();
            //p.parse();

            Program program = (Program) p.parse().value;

            System.out.println(program.listChildren());
            //System.out.println(p.debug_parse().value);



            // create sintax_tree
            document.appendChild(program.element);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;
            try {
                transformer = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // Si può usare StreamResult result = new StreamResult(System.out);
            // per fare in modo da avere il risultato sullo
            // standard output ...
            // Oppure usarlo per il debugging

            try {
                transformer.transform(domSource, streamResult);
            } catch (TransformerException e) {
                e.printStackTrace();
            }

            System.out.println("File XML Creato");




        } catch (Exception e) {
            ;
        }


    }
}
