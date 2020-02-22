import java_cup.runtime.Symbol;

import java.io.*;

public class testerLexer {
    public static void main(String[] args) throws IOException {
        //PalleneLexer lexer = new PalleneLexer(new java.io.FileReader(args[0]));
        PalleneLexer lexer = new PalleneLexer(new java.io.FileReader("test_file/test1.txt"));

        sym symbolI = new sym();
        Symbol symbol = lexer.next_token();

        while (!symbolI.terminalNames[symbol.sym].equals("EOF")) {
            //System.out.println(token.toString());
            System.out.println(symbolI.terminalNames[symbol.sym]);
            symbol = lexer.next_token();
        }
        //System.out.println(symbol.value);
        //System.out.println(symbol);

    }
}
