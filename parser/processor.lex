package struktor.processor;

import java_cup.runtime.ComplexSymbolFactory;
%%
%cup

%{
// remember the last occured identifier for Array support
private String identifier = null;
private ComplexSymbolFactory symFact;

Yylex(java.io.Reader in, ComplexSymbolFactory symFact) {
	this(in);
	this.symFact=symFact;
}
%}

%eofval{
return (symFact.newSymbol("EOF",Psym.EOF));
%eofval}

%yylexthrow{
ProcessorException
%yylexthrow}

digit             = [0-9]
alpha             = [a-zA-Z_]
alnum             = [a-zA-Z_0-9]
identifier        = {alpha}{alnum}*
whitespacechar    = [\n\ \t\b\012]
char              = (\\\"|[^\n\"]|\\{whitespacechar}+\\)
string            = {char}*
oct               =   [0-7]
dec               =   [0-9]
hex               =   [0-9a-fA-F]
sign              =  [+-]?
exp               =   ([eE]{sign}{dec}+)

%state STRING ARRAY1 ARRAY2 ARRAY3

%%

";"        { return symFact.newSymbol(";",Psym.SEMI); }
"++"       { return symFact.newSymbol("++",Psym.INC); }
"+"        { return symFact.newSymbol("+",Psym.PLUS); }
"--"       { return symFact.newSymbol("--",Psym.DEC); }
"-"        { return symFact.newSymbol("-",Psym.MINUS); }
"*"        { return symFact.newSymbol("*",Psym.TIMES); }
"."        { return symFact.newSymbol(".",Psym.PART); }
"/"        { return symFact.newSymbol("/",Psym.DIVIDE); }
"("        { return symFact.newSymbol("(",Psym.LPAREN); }
")"        { return symFact.newSymbol(")",Psym.RPAREN); }
"%"        { return symFact.newSymbol("%",Psym.MOD); }
"=="       { return symFact.newSymbol("==",Psym.EQU); }
"!="       { return symFact.newSymbol("!=",Psym.NEQ); }
"<"        { return symFact.newSymbol("<",Psym.LES); }
">"        { return symFact.newSymbol(">",Psym.MOR); }
"<="       { return symFact.newSymbol("<=",Psym.LEQ); }
">="       { return symFact.newSymbol(">=",Psym.MEQ); }
"="        { return symFact.newSymbol("=",Psym.ASSIGN); }
"||"       { return symFact.newSymbol("||",Psym.LOGOR);}
"&&"       { return symFact.newSymbol("&&",Psym.LOGAND);}
"!"        { return symFact.newSymbol("!",Psym.NOT); }
"&"        { return symFact.newSymbol("&",Psym.ADRESS); }
"(int)"    { return symFact.newSymbol("(int)",Psym.INTCAST); }
"(double)" { return symFact.newSymbol("(double)",Psym.DOUBLECAST); }
"return"   { return symFact.newSymbol("return",Psym.RET); }
"break"    { return symFact.newSymbol("break",Psym.BREAK); }
"continue" { return symFact.newSymbol("continue",Psym.CONTINUE); }


<YYINITIAL>      "sizeof("      { return symFact.newSymbol("sizeof(",Psym.SIZEOF); }

<YYINITIAL>      "int"          { return symFact.newSymbol("int",Psym.TYPENAME, new String("int")); }
<YYINITIAL>      "double"       { return symFact.newSymbol("double",Psym.TYPENAME, new String("double")); }
<YYINITIAL>      "char"         { return symFact.newSymbol("char",Psym.TYPENAME, new String("char")); }
<YYINITIAL>      "int*"         { return symFact.newSymbol("int*",Psym.TYPENAME, new String("int*")); }
<YYINITIAL>      "double*"      { return symFact.newSymbol("double*",Psym.TYPENAME, new String("double*")); }
<YYINITIAL>      "char*"        { return symFact.newSymbol("char*",Psym.TYPENAME, new String("char*")); }

<YYINITIAL>      {identifier}\(    { return symFact.newSymbol("aFunctionCall",Psym.FUNC, new String(yytext().substring(0,yytext().length() - 1))); }
<YYINITIAL>      ,                 { return symFact.newSymbol("aParameter",Psym.PARAM);}

<YYINITIAL>      {identifier}\[      { yybegin(ARRAY1);
                                       identifier = new String(yytext().substring(0,yytext().length() - 1));
                                       return symFact.newSymbol("[",Psym.TIMES);}

<ARRAY1>         \$?                 { yybegin(ARRAY2); return symFact.newSymbol("arrayIndex",Psym.LPAREN);}
<ARRAY2>         \$?                 { yybegin(ARRAY3); return symFact.newSymbol("arrayIndex",Psym.VARIABLE, identifier);}
<ARRAY3>         \$?                 { yybegin(YYINITIAL); return symFact.newSymbol("+",Psym.PLUS); }
<YYINITIAL>      \]\[                { yybegin(YYINITIAL); return symFact.newSymbol("*",Psym.TIMES); }
<YYINITIAL>      \]                  { yybegin(YYINITIAL); return symFact.newSymbol("]",Psym.RPAREN);}



<YYINITIAL>      \"{string}\"        {return symFact.newSymbol("aString",Psym.STRING, new String(yytext().substring(1,yytext().length() - 1)));   }


<YYINITIAL>      '{char}'            {return symFact.newSymbol("aChar",Psym.CHARACTER, new Character(yytext().charAt(1)));   }


<YYINITIAL>      {dec}+              { return symFact.newSymbol("anInt",Psym.INTEGER, new Integer(yytext())); }


<YYINITIAL>      {dec}+"."{dec}*{exp}?|{dec}*"."{dec}+{exp}?|{dec}+{exp} { return symFact.newSymbol("aDouble",Psym.DOUBLE, new Double(yytext())); }


<YYINITIAL>      {identifier}        { return symFact.newSymbol("aVariable",Psym.VARIABLE, new String(yytext())); }


<YYINITIAL>      [ \t\r\n\f]         { /* ignore white space. */ }
<YYINITIAL>      . { throw new ProcessorException("Illegal Character: "+yytext()); }
