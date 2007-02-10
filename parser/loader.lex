package struktor.strukelements;

import java.util.Stack;

import java_cup.runtime.Symbol;
import struktor.StruktorException;
import struktor.Tracer;

%%
%cup
%{
// variables here
       Stack stack = new Stack();
       int lineCounter=1;
       int lastControl=0;
       String tempCond="";
       int paraCount=0;
       public static final int INTEGER=1;
       public static final int DOUBLE=2;
       public static final int CHARACTER=3;
       public static final int STRING=4;
       public static final int POINTER=5;
%}

%yylexthrow{
StruktorException
%yylexthrow}

%eofval{
return (new Symbol(Lsym.EOF));
%eofval}

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
command           = ({alnum}|\*|&|\(|\").*
exp               =   ([eE]{sign}{dec}+)

%state STRUK PARAMLIST VARTYP1 VARTYP2 VALUE DEC DECSTART INSTRLIST BEGIN CONDITIONPARA CONDITION INPUT DOWHILE FOR

%%

<YYINITIAL>            "//@start struktogramm".*
                       { yybegin(STRUK); return new Symbol(Lsym.STARTSTRUK); }
                       "//@end struktogramm"
                       { yybegin(YYINITIAL); return new Symbol(Lsym.ENDSTRUK); }

<STRUK>                {identifier}\(
                       { yybegin(DECSTART);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.STRUKNAME, new String(yytext().substring(0,yytext().length()-1))); }

<DECSTART>             "int"
                       { Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.VARTYPE, new Integer(INTEGER));}
<DECSTART>             "double"
                       { Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.VARTYPE, new Integer(DOUBLE));}
<DECSTART>             "char"
                       { Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.VARTYPE, new Integer(CHARACTER));}
<DECSTART>             "String"
                       {
                         Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.VARTYPE, new Integer(STRING));}
<DECSTART>             ")"
                       {
                         Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.RPAR);}
<DEC>                  "*"
                       { Tracer.out(yytext());
                         return new Symbol(Lsym.POINTER);}

<DEC>                  {identifier}
                       { Tracer.out(yytext());
                         return new Symbol(Lsym.VARNAME, new String(yytext())); }

<DEC>                  "["
                       { Tracer.out(yytext());
                         return new Symbol(Lsym.LEPAR); }
<DEC>                  {dec}*
                       { Tracer.out(yytext());
                         return new Symbol(Lsym.INTEGER, new Integer(yytext())); }
<DEC>                  "]"
                       { Tracer.out(yytext());
                         return new Symbol(Lsym.REPAR); }
<DEC>                  "="
                       { Tracer.out(yytext());
                         yybegin(VALUE); return new Symbol(Lsym.ASSIGN); }
<VALUE>                \"{string}\"
                       { Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.STRING, new String(yytext().substring(1,yytext().length() - 1)));   }
<VALUE>                \'{string}\'
                       { Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.STRING, new String(yytext().substring(1,yytext().length() - 1)));   }
<VALUE>                {dec}+
                       { Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.INTEGER, new Integer(yytext())); }
<VALUE>                {dec}+"."{dec}*{exp}?|{dec}*"."{dec}+{exp}?|{dec}+{exp}
                       { Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.DOUBLE, new Double(yytext())); }
<DEC>                  ","
                       { Tracer.out(yytext());
                         stack.push(new Integer(lastControl));
                         yybegin(DECSTART);
                         return new Symbol(Lsym.COMMA); }
<DEC>                  ";"
                       { yybegin(DECSTART);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.SEMI); }
<DEC>                  ")"
                       { Tracer.out(yytext());
                         return new Symbol(Lsym.RPAR); }
<DEC>                  "{"
                       { Tracer.out(yytext());
                         stack.push(new Integer(lastControl));
                         return new Symbol(Lsym.LSPAR); }
<DEC>                  "//@*** Declarations ***"
                       { Tracer.out(yytext());
                         yybegin(DECSTART); }
<DEC, DECSTART>        "//@*** Program ***"
                       { Tracer.out(yytext());
                         yybegin(STRUK);}



<STRUK,BEGIN>          "if"
                       { Tracer.out(yytext());
                         lastControl = Lsym.IF;
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.IF); }
<CONDITIONPARA>        "("
                       { tempCond = "";
                         Tracer.out(yytext());
                         yybegin(CONDITION); }
<CONDITION>            "("
                       { tempCond = tempCond + yytext();
                         paraCount++;}
<CONDITION>            ")"
                       { if (paraCount <= 0)
                         {
                            Tracer.out(tempCond+")");
                            paraCount = 0;
                            yybegin(BEGIN);
                            return new Symbol(Lsym.CONDITION, tempCond);
                         }
                         else
                         {
                            tempCond = tempCond + yytext();
                            paraCount--;
                         }}
<CONDITION>            .
                       { tempCond = tempCond + yytext();}

<BEGIN>                "{"
                       { Tracer.out(yytext());
                         stack.push(new Integer(lastControl));
                         yybegin(STRUK);
                         return new Symbol(Lsym.LSPAR); }
<BEGIN>                ";"
                       { /* extra f�r Do-While(bla); */
                         Tracer.out(yytext());
                         yybegin(STRUK);
                         return new Symbol(Lsym.SEMI); }
<STRUK>                "}"
                       { Tracer.out(yytext());
                         int i = ((Integer)stack.pop()).intValue();
                         // Spezialfall do{}while(); <----- ;!!!
                         if (i == Lsym.DO)
                            yybegin(DOWHILE);
                         else
                            yybegin(STRUK);
                         return new Symbol(Lsym.RSPAR); }


<STRUK>                "else"
                       { Tracer.out(yytext());
                         lastControl = Lsym.ELSE;
                         yybegin(BEGIN);
                         return new Symbol(Lsym.ELSE); }
<STRUK,BEGIN>          "do"
                       { Tracer.out(yytext());
                         yybegin(BEGIN);
                         lastControl = Lsym.DO;
                         return new Symbol(Lsym.DO); }
<STRUK,BEGIN>          "while"
                       { Tracer.out(yytext());
                         lastControl = Lsym.WHILE;
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.WHILE); }
<STRUK,BEGIN>          "for(".*
                       { Tracer.out(yytext());
                         tempCond=yytext().substring(4,yytext().length()-1);
                         yybegin(FOR);
                         return new Symbol(Lsym.FOR); }
<FOR>                  \t?
                       { // matcht immer !!
                         lastControl = Lsym.FOR;
                         yybegin(BEGIN);
                         return new Symbol(Lsym.CONDITION,tempCond); }
<DOWHILE>              "while"
                       { Tracer.out(yytext());
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.WHILE); }
<STRUK,BEGIN>          "switch"
                       { Tracer.out(yytext());
                         lastControl = Lsym.SWITCH;
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.SWITCH); }
<STRUK,BEGIN>          "Input: "{command}
                       { yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.INPUT,new String(yytext().substring(7,yytext().length()) ));}
<STRUK,BEGIN>          "Output: "{command}
                       { yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.OUTPUT,new String(yytext().substring(7,yytext().length()) ));}
<STRUK,BEGIN>          "case "{command}":"
                       { yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.CASE,new String(yytext().substring(4,yytext().length()-1) ));}
<STRUK,BEGIN>          "default:"
                       { yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.DEFAULT);}
<STRUK,BEGIN>          "break;"
                       { yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.BREAK,new Integer(1));}
<STRUK,BEGIN>          {command}";"
                       { yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.COMMAND,new String(yytext()));}

<STRUK,BEGIN>          ";"
                       { yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.COMMAND,new String(""));}


"//Msg:".*             {Tracer.out(yytext());
                        return new Symbol(Lsym.ADMSG, new String(yytext().substring(6, yytext().length())));}

"//".*               {Tracer.out(yytext()); }



                       [\n]         { lineCounter++; }
                       [ \t\r\f]         { /* ignore white space. */ }
     . { throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
