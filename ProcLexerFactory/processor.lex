package struktor.processor;

import java_cup.runtime.Symbol;
%%
%cup

%{
// remember the last occured identifier for Array support
private String identifier = null;
%}

%eofval{
return (new Symbol(Psym.EOF));
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

";"        { return new Symbol(Psym.SEMI); }
"++"       { return new Symbol(Psym.INC); }
"+"        { return new Symbol(Psym.PLUS); }
"--"       { return new Symbol(Psym.DEC); }
"-"        { return new Symbol(Psym.MINUS); }
"*"        { return new Symbol(Psym.TIMES); }
"."        { return new Symbol(Psym.PART); }
"/"        { return new Symbol(Psym.DIVIDE); }
"("        { return new Symbol(Psym.LPAREN); }
")"        { return new Symbol(Psym.RPAREN); }
"%"        { return new Symbol(Psym.MOD); }
"=="       { return new Symbol(Psym.EQU); }
"!="       { return new Symbol(Psym.NEQ); }
"<"        { return new Symbol(Psym.LES); }
">"        { return new Symbol(Psym.MOR); }
"<="       { return new Symbol(Psym.LEQ); }
">="       { return new Symbol(Psym.MEQ); }
"="        { return new Symbol(Psym.ASSIGN); }
"||"       { return new Symbol(Psym.LOGOR);}
"&&"       { return new Symbol(Psym.LOGAND);}
"!"        { return new Symbol(Psym.NOT); }
"&"        { return new Symbol(Psym.ADRESS); }
"(int)"    { return new Symbol(Psym.INTCAST); }
"(double)" { return new Symbol(Psym.DOUBLECAST); }
"return"   { return new Symbol(Psym.RET); }
"break"    { return new Symbol(Psym.BREAK); }
"continue" { return new Symbol(Psym.CONTINUE); }


<YYINITIAL>      "sizeof("      { return new Symbol(Psym.SIZEOF); }

<YYINITIAL>      "int"          { return new Symbol(Psym.TYPENAME, new String("int")); }
<YYINITIAL>      "double"       { return new Symbol(Psym.TYPENAME, new String("double")); }
<YYINITIAL>      "char"         { return new Symbol(Psym.TYPENAME, new String("char")); }
<YYINITIAL>      "int*"         { return new Symbol(Psym.TYPENAME, new String("int*")); }
<YYINITIAL>      "double*"      { return new Symbol(Psym.TYPENAME, new String("double*")); }
<YYINITIAL>      "char*"        { return new Symbol(Psym.TYPENAME, new String("char*")); }

<YYINITIAL>      {identifier}\(    { return new Symbol(Psym.FUNC, new String(yytext().substring(0,yytext().length() - 1))); }
<YYINITIAL>      ,                 { return new Symbol(Psym.PARAM);}

<YYINITIAL>      {identifier}\[      { yybegin(ARRAY1);
                                       identifier = new String(yytext().substring(0,yytext().length() - 1));
                                       return new Symbol(Psym.TIMES);}

<ARRAY1>         \$?                 { yybegin(ARRAY2); return new Symbol(Psym.LPAREN);}
<ARRAY2>         \$?                 { yybegin(ARRAY3); return new Symbol(Psym.VARIABLE, identifier);}
<ARRAY3>         \$?                 { yybegin(YYINITIAL); return new Symbol(Psym.PLUS); }
<YYINITIAL>      \]\[                { yybegin(YYINITIAL); return new Symbol(Psym.TIMES); }
<YYINITIAL>      \]                  { yybegin(YYINITIAL); return new Symbol(Psym.RPAREN);}



<YYINITIAL>      \"{string}\"        {return new Symbol(Psym.STRING, new String(yytext().substring(1,yytext().length() - 1)));   }


<YYINITIAL>      '{char}'            {return new Symbol(Psym.CHARACTER, new Character(yytext().charAt(1)));   }


<YYINITIAL>      {dec}+              { return new Symbol(Psym.INTEGER, new Integer(yytext())); }


<YYINITIAL>      {dec}+"."{dec}*{exp}?|{dec}*"."{dec}+{exp}?|{dec}+{exp} { return new Symbol(Psym.DOUBLE, new Double(yytext())); }


<YYINITIAL>      {identifier}        { return new Symbol(Psym.VARIABLE, new String(yytext())); }


<YYINITIAL>      [ \t\r\n\f]         { /* ignore white space. */ }
<YYINITIAL>      . { throw new ProcessorException("Illegal Character: "+yytext()); }
