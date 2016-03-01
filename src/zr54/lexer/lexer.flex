package zr54.lexer;
import zr54.parser.*;
import java_cup.runtime.*;
import java.io.*;
%%

%public
%class Lexer
%unicode
%line
%column
%cup

%{
  
  /* store a reference to the parser object */
  private BufferedWriter bw;
  private boolean even, error;
  private int start_line, start_column; //For string location
  private String errorMessage;
  
  StringBuilder string = new StringBuilder();
  
  private Symbol symbol(int type, Object value) {
  	Symbol symObj = new Symbol(type, yyline+1, yycolumn+1, value);
    return new Symbol(type, yyline+1, yycolumn+1, symObj);
  }
  
  private Symbol symbol(int type, Object value, int spec_line, int spec_column) {
  	Symbol symObj = new Symbol(type, spec_line+1, spec_column+1, value);
    return new Symbol(type, spec_line+1, spec_column+1, symObj);
  }

  public int getLine(){
    return yyline;
  }
  public int getColumn(){
    return yycolumn;
  }
  
  private char unicodeChar() {
    int r = 0;

    for (int k = zzMarkedPos-4; k < zzMarkedPos; k++) {
      int c = zzBuffer[k];
	  
      if (c >= 'a') 
        c-= 'a'-10;
      else if (c >= 'A')
        c-= 'A'-10;
      else
        c-= '0';

      r <<= 4;
      r += c;
    }
   
    return (char) r;
  }


  private char singleUnicodeChar() {
    int r = 0;

    for (int k = zzMarkedPos-5; k < zzMarkedPos-1; k++) {
      int c = zzBuffer[k];
	  
      if (c >= 'a') 
        c-= 'a'-10;
      else if (c >= 'A')
        c-= 'A'-10;
      else
        c-= '0';

      r <<= 4;
      r += c;
    }
   
    return (char) r;
  }

    private char singleASCIIChar() {
    int r = 0;

    for (int k = zzMarkedPos-3; k < zzMarkedPos-1; k++) {
      int c = zzBuffer[k];
	  
      if (c >= 'a') 
        c-= 'a'-10;
      else if (c >= 'A')
        c-= 'A'-10;
      else
        c-= '0';

      r <<= 4;
      r += c;
    }
   
    return (char) r;
  }

  private String ASCIIString(int startPos) {
      
    StringBuilder output = new StringBuilder();
    
    
    for (int k = zzMarkedPos-startPos; k < zzMarkedPos; k+=2) {
      
      int r = 0;
      
      for(int j = 0; j < 2; j++)
      {
      	  int c = zzBuffer[k+j];
          if (c >= 'a') 
        	c-= 'a'-10;
      	  else if (c >= 'A')
        	c-= 'A'-10;
      	  else
        	c-= '0';
      	  r <<= 4;
      	  r += c;
      }
      
      output.append((char) r);
      
    }
    return output.toString();
    
  }
  
  public String getErrorMessage() {
    if (error) {
      return errorMessage;
    } else {
      return null;
    }
  }
  
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = "//" {InputCharacter}* {LineTerminator}?

/* identifiers */
Letters = [a-zA-Z]
LetterDigits = [a-zA-Z0-9_\']
Identifier = {Letters}{LetterDigits}*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*

/* string and character literals */
StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]

/* Escapes and Markers */
UnicodeEscape         = {UnicodeMarker} {HexDigit} {4}
UnicodeMarker         = "u"+

UnprintableASCII      = {ASCIIMarker} {UnprintableHexDigit}
SingleASCIIEscape     = {ASCIIMarker} {ASCIIHexDigit}
ASCIIEscape           = {ASCIIMarker} {ASCIIHexDigit}+

ASCIIMarker           = "x"+
ASCIIHexDigit         = [2-9a-fA-F][0-9a-fA-F]
UnprintableHexDigit   = [0-1][0-9a-fA-F]
HexDigit              = [0-9a-fA-F]

%state STRING, CHARLITERAL, DIGITS

%%

<YYINITIAL> {

  /* keywords */
  "bool"                         { return symbol(sym.BOOL, "bool"); }
  "else"                         { return symbol(sym.ELSE, "else"); }
  "int"                          { return symbol(sym.INT, "int"); }
  "if"                           { return symbol(sym.IF, "if"); }
  "return"                       { return symbol(sym.RETURN, "return"); }
  "while"                        { return symbol(sym.WHILE, "while"); }
  "length"                       { return symbol(sym.LENGTH, "length"); }
  "use"                          { return symbol(sym.USE, "use"); }
  "_"							 { return symbol(sym.UNDERSCORE, "_"); }
  
  /* boolean literals */
  "true"                         { return symbol(sym.BOOLEAN_LITERAL, "true"); }
  "false"                        { return symbol(sym.BOOLEAN_LITERAL, "false"); }
  
  /* separators */
  "("                            { return symbol(sym.LPAREN, "("); }
  ")"                            { return symbol(sym.RPAREN, ")"); }
  "{"                            { return symbol(sym.LBRACE, "{"); }
  "}"                            { return symbol(sym.RBRACE, "}"); }
  "["                            { return symbol(sym.LBRACK, "["); }
  "]"                            { return symbol(sym.RBRACK, "]"); }
  ";"                            { return symbol(sym.SEMICOLON, ";"); }
  ","                            { return symbol(sym.COMMA, ","); }
  ":"                            { return symbol(sym.COLON, ":"); }
  
  /* operators */
  "="                            { return symbol(sym.EQ, "="); }
  ">"                            { return symbol(sym.GT, ">"); }
  "<"                            { return symbol(sym.LT, "<"); }
  "!"                            { return symbol(sym.NOT, "!"); }
  "=="                           { return symbol(sym.EQEQ, "=="); }
  "<="                           { return symbol(sym.LTEQ, "<="); }
  ">="                           { return symbol(sym.GTEQ, ">="); }
  "!="                           { return symbol(sym.NOTEQ, "!="); }
  "*>>"                          { return symbol(sym.HIGHMULT, "*>>"); }
  "+"                            { return symbol(sym.PLUS, "+"); }
  "-"                            { return symbol(sym.MINUS, "-"); }
  "*"                            { return symbol(sym.MULT, "*"); }
  "/"                            { return symbol(sym.DIV, "/"); }
  "&"                            { return symbol(sym.AND, "&"); }
  "|"                            { return symbol(sym.OR, "|"); }
  "%"                            { return symbol(sym.MOD, "%"); }
  
  /* string literal */
  \"                             { 
  								   start_line = yyline; 
  								   start_column = yycolumn;
  								   yybegin(STRING); 
  								   string.setLength(0);
  								 }

  /* character literal */
  \'                             { 
  								   start_line = yyline; 
  								   start_column = yycolumn;
  								   yybegin(CHARLITERAL); 
  								 }

  /* numeric literals */

  /* This is matched together with the minus, because the number is too big to 
     be represented by a positive integer. */
  {DecIntegerLiteral}            {  return symbol(sym.INTEGER_LITERAL, yytext()); }
  
  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }

  /* identifiers */ 
  {Identifier}                   { return symbol(sym.IDENTIFIER, yytext()); }  
}

<STRING> {
  \"                             { 
  								   yybegin(YYINITIAL); 
  								   return symbol(sym.STRING_LITERAL, string.toString(), start_line, start_column); }
  
  {StringCharacter}+             { string.append( yytext() ); }
  
  /* escape sequences */
  "\\b"                          { string.append( "\\b" ); }
  "\\t"                          { string.append( "\\t" ); }
  "\\n"                          { string.append( "\\n" ); }
  "\\f"                          { string.append( "\\f" ); }
  "\\r"                          { string.append( "\\r" ); }
  "\\\""                         { string.append( "\\\"" ); }
  "\\'"                          { string.append( "\\'" ); }
  
  "\\"               			 { even = false; string.append("\\"); }
  "\\" / "\\"          			 { even = !even; string.append("\\\\"); }
  "\\" / "u" | "x"        	     { 
                     			   if (even) {
                                       even = false;
                       				   string.append("\\");
                     				}
                     				else
                       					yybegin(DIGITS);
                   				}
  
  /* error cases */
  \\.              { 
    				 errorMessage = (yyline+1)+":"+(yycolumn+1)+" error:Illegal escape sequence \""+yytext()+"\"\n";
    				 error = true;
    				 return symbol(sym.EOF, null);
  				   }
  {LineTerminator} { 
  					 errorMessage = (yyline+1)+":"+(yycolumn+1)+" error:Unterminated string at end of line\n";
    				 error = true;
    				 return symbol(sym.EOF, null);
  				   }              
}

<DIGITS> {
  {UnicodeEscape}        { string.append(unicodeChar()); yybegin(STRING); }
  {ASCIIEscape}          { string.append(ASCIIString(yytext().length()-1)); yybegin(STRING); }
  {UnprintableASCII}     { string.append("\\").append( yytext() ); yybegin(STRING); }
  
  /* error cases */
  [^]              { 
  					 errorMessage = (start_line+1)+":"+(start_column+1)+" error:Illegal escape sequence \""+yytext()+"\"\n";
    				 error = true;
    				 return symbol(sym.EOF, null);
  				   }
  <<EOF>>          { 
  					 errorMessage = (start_line+1)+":"+(start_column+1)+" error:Illegal escape sequence \""+yytext()+"\"\n";
    				 error = true;
    				 return symbol(sym.EOF, null);
  				   }

}

<CHARLITERAL> {
  {SingleCharacter}\'            { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, yytext().charAt(0), start_line, start_column);}
  "\\"{SingleASCIIEscape}\'      { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, singleASCIIChar(), start_line, start_column);}
  "\\"{UnicodeEscape}\'          { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, singleUnicodeChar(), start_line, start_column);}
  
  /* escape sequences */
  "\\b"\'                        { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, '\b', start_line, start_column);}
  "\\t"\'                        { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, '\t', start_line, start_column);}
  "\\n"\'                        { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, '\n', start_line, start_column);}
  "\\f"\'                        { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, '\f', start_line, start_column);}
  "\\r"\'                        { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, '\r', start_line, start_column);}
  "\\\""\'                       { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, '\"', start_line, start_column);}
  "\\'"\'                        { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, '\'', start_line, start_column);}
  "\\\\"\'                       { yybegin(YYINITIAL); return symbol(sym.CHARACTER_LITERAL, '\\', start_line, start_column);}
  
  
  
  /* error cases */
  \\.              { 
  					 errorMessage = (start_line+1)+":"+(start_column+1)+" error:Illegal escape sequence \""+yytext()+"\"\n";
    				 error = true;
    				 return symbol(sym.EOF, null);
  				   }
  {LineTerminator} { 
  					 errorMessage = (start_line+1)+":"+(start_column+1)+" error:Unterminated string at end of line\n";
    				 error = true;
    				 return symbol(sym.EOF, null);
  				   }              
  \'               { 
  					 errorMessage = (start_line+1)+":"+(start_column+1)+" error:empty character literal\n";
    				 error = true;
    				 return symbol(sym.EOF, null);
  				   }
}


/* error fallback */
[^]                { 
  					 errorMessage = (yyline+1)+":"+(yycolumn+1)+" error:Illegal escape sequence \""+yytext()+"\"\n";
    				 error = true;
    				 return symbol(sym.EOF, null);
  				   }
<<EOF>>            { return symbol(sym.EOF, null); }