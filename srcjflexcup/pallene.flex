import java_cup.runtime.*;
import java.io.*;

%%

%class PalleneLexer
%cup
%unicode
%line
%column

%{
    StringBuffer string = new StringBuffer();

      private Symbol getToken(int type) {
        return new Symbol(type);
      }

      private Symbol getToken(int type, String value) {
              return new Symbol(type, value);
            }

      private Symbol symbol(int type, String value) {
      	 return new Symbol(type, value);
      	}

      PalleneLexer() {}


%}

digit = [0-9]
digits = {digit}+
zero = 0

INT_CONST = 0|[1-9][0-9]*
exp = {digits}?[e|E][\+\-]?[0-9]+

float_1st = [0-9]+\.[0-9]+{exp}?
float_2nd = \.[0-9]+{exp}?
FLOAT_CONST = ( {float_1st} | {float_2nd} )


letter = [a-zA-Z]
//ID = {letter}({letter}|{digit})*
ID = [:jletter:] [:jletterdigit:]*

terminator = \r|\n|\r\n
blank = [ \t\f]

STRING_CONST = \"[a-zA-Z0-9 !?\-+/=\*|\\:;._'òàùèé£$€%&()\[\]{}\^#@Ç°<>§]*\"

%%

<YYINITIAL> {

    //"\n" { return getToken(sym.TERMINATOR);}
    "\n" {/*ignora*/}

    "do" {return getToken(sym.DO, "KEYWORD");}
    "if" { return getToken(sym.IF, "KEYWORD"); }
    "then" { return getToken(sym.THEN, "KEYWORD"); }
    "else" { return getToken(sym.ELSE, "KEYWORD"); }
    "while" { return getToken(sym.WHILE, "KEYWORD"); }
    "for" {return getToken(sym.FOR, "KEYWORD");}
    "float" { return getToken(sym.FLOAT, "KEYWORD"); }
    "int" { return getToken(sym.INT, "KEYWORD"); }
    "local" {return getToken(sym.LOCAL, "KEYWORD");}
    "string" {return getToken(sym.STRING, "KEYWORD");}
    "bool" {return getToken(sym.BOOL, "KEYWORD");}

    "main" { return getToken(sym.MAIN, "KEYWORD"); }
    "end" {return getToken(sym.END, "KEYWORD");}
    "function" {return getToken(sym.FUNCTION, "KEYWORD");}
    //"ID" {return getToken(sym.ID, "KEYWORD");}
    "global" {return getToken(sym.GLOBAL, "KEYWORD");}
    "nil" {return getToken(sym.NIL, "KEYWORD");}
    "->" {return getToken(sym.ARROW, "KEYWORD");}
    "nop" {return getToken(sym.NOP, "KEYWORD");}
    "return" {return getToken(sym.RETURN, "KEYWORD");}

    "(" { return getToken(sym.LPAR); }
    ")" { return getToken(sym.RPAR); }
    "{" { return getToken(sym.BLPAR); }
    "}" { return getToken(sym.BRPAR); }
    "," { return getToken(sym.COMMA); }
    "[" {return getToken(sym.SLPAR); }
    "]" {return getToken(sym.SRPAR); }
    ":" { return getToken(sym.COLON);}
    ";" { return getToken(sym.SEMICOLON); }

    "<==" {return getToken(sym.READ, "KEYWORD");}
    "==>" {return getToken(sym.WRITE, "KEYWORD");}
    "true" {return getToken(sym.TRUE, "KEYWORD");}
    "false" {return getToken(sym.FALSE, "KEYWORD");}

    "or" {return getToken(sym.OR, "KEYWORD");}
    "and" {return getToken(sym.AND, "KEYWORD");}
    "not" {return getToken(sym.NOT, "KEYWORD");}
    "#" {return getToken(sym.SHARP, "KEYWORD");}

    "<" { return getToken(sym.LT); }
    "<=" { return getToken(sym.LE); }
    "==" { return getToken(sym.EQ); }
    "<>" { return getToken(sym.NE); }
    ">" { return getToken(sym.GT); }
    ">=" { return getToken(sym.GE); }
    "<--" { return getToken(sym.ASSIGN); }
    "=" { return getToken(sym.ASSIGN); }

    "+" { return getToken(sym.PLUS); }
    "-" { return getToken(sym.MINUS); }
    "/" { return getToken(sym.DIV); }
    "*" { return getToken(sym.TIMES); }


   // {zero} { return getToken(sym.INT_CONST, Integer.parseInt(yytext()));}
    {INT_CONST} { return getToken(sym.INT_CONST, yytext());}
    {ID} { return getToken(sym.ID, yytext()); }
    {FLOAT_CONST} { return getToken(sym.FLOAT_CONST, yytext()); }
    {STRING_CONST} { return getToken(sym.STRING_CONST, yytext()); }
    {exp} { return getToken(sym.EXP, yytext()); }
    //{blank} { return getToken(sym.BLANK); }
    {blank} {/*ignora*/}



    <<EOF>> { return getToken(sym.EOF); }

    /* error fallback */
    [^] {
      throw new Error("Illegal character <"+yytext()+"> on L: " + yyline + " C: " + yycolumn);
    }


}