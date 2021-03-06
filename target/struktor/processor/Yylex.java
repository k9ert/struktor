/* The following code was generated by JFlex 1.4.3 on 26.11.11 14:55 */

package struktor.processor;

import java_cup.runtime.ComplexSymbolFactory;
import struktor.strukelements.Dec;
import struktor.strukelements.DecList;

@SuppressWarnings("all")


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 26.11.11 14:55 from the specification file
 * <tt>/home/kim/src/struktor/trunk/parser/processor.lex</tt>
 */
class Yylex implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int ARRAYINDEX2 = 12;
  public static final int ARRAYINDEX1 = 10;
  public static final int STRING = 2;
  public static final int ARRAY3 = 8;
  public static final int ARRAY2 = 6;
  public static final int YYINITIAL = 0;
  public static final int ARRAY1 = 4;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3,  3,  4,  4,  5,  5,  6, 6
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\10\0\1\3\1\56\1\6\1\0\2\55\22\0\1\56\1\22\1\5"+
    "\1\0\1\52\1\20\1\26\1\54\1\16\1\17\1\13\1\12\1\50"+
    "\1\7\1\14\1\15\10\1\2\1\1\0\1\11\1\23\1\21\1\24"+
    "\2\0\4\2\1\10\1\2\24\2\1\51\1\4\1\53\1\0\1\2"+
    "\1\0\1\41\1\35\1\43\1\32\1\37\1\46\1\2\1\47\1\27"+
    "\1\2\1\42\1\36\1\2\1\30\1\33\2\2\1\40\1\44\1\31"+
    "\1\34\4\2\1\45\1\0\1\25\uff83\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\1\3\2\0\1\4\1\5\1\6"+
    "\1\4\1\7\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\4"+
    "\1\25\6\6\1\26\1\27\1\4\1\14\4\0\1\1"+
    "\1\2\1\3\1\30\3\4\1\31\1\0\1\32\1\33"+
    "\1\34\2\0\1\35\1\36\1\37\2\0\1\40\1\41"+
    "\1\42\1\43\1\44\1\45\7\6\1\46\5\0\1\32"+
    "\2\0\1\35\3\0\1\47\6\6\1\50\6\0\1\51"+
    "\4\6\1\52\1\6\3\0\1\53\1\0\1\6\1\54"+
    "\2\6\1\55\1\6\1\54\3\0\1\56\1\57\2\6"+
    "\1\57\2\0\1\60\1\6\1\61\1\0\1\62\2\63";

  private static int [] zzUnpackAction() {
    int [] result = new int[136];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\57\0\136\0\215\0\274\0\353\0\u011a\0\u0149"+
    "\0\u0178\0\u01a7\0\u01d6\0\u0149\0\u0205\0\u0149\0\u0234\0\u0149"+
    "\0\u0263\0\u0149\0\u0292\0\u0149\0\u0149\0\u02c1\0\u02f0\0\u031f"+
    "\0\u034e\0\u037d\0\u03ac\0\u03db\0\u040a\0\u0439\0\u0468\0\u0497"+
    "\0\u04c6\0\u0149\0\u04f5\0\u0524\0\u0149\0\u037d\0\u0553\0\u0582"+
    "\0\u05b1\0\u0149\0\u0149\0\u0149\0\u05e0\0\u0553\0\u0582\0\u05b1"+
    "\0\u060f\0\u063e\0\u066d\0\u0149\0\u0149\0\u01d6\0\u069c\0\u0149"+
    "\0\u0149\0\u0149\0\u06cb\0\u06fa\0\u0149\0\u0149\0\u0149\0\u0149"+
    "\0\u0149\0\u0149\0\u0729\0\u0758\0\u0787\0\u07b6\0\u07e5\0\u0814"+
    "\0\u0843\0\u0149\0\u0872\0\u08a1\0\u08d0\0\u08ff\0\u092e\0\u095d"+
    "\0\u095d\0\u098c\0\u01d6\0\u09bb\0\u09ea\0\u0a19\0\u0a48\0\u0a77"+
    "\0\u0aa6\0\u0ad5\0\u0b04\0\u0b33\0\u0b62\0\u0149\0\u0b91\0\u0bc0"+
    "\0\u0bef\0\u0c1e\0\u0c4d\0\u0c7c\0\u0149\0\u0cab\0\u0cda\0\u0d09"+
    "\0\u0d38\0\u0d67\0\u0d96\0\u0dc5\0\u0df4\0\u0e23\0\u0149\0\u0e52"+
    "\0\u0e81\0\u01a7\0\u0eb0\0\u0edf\0\u0149\0\u0f0e\0\u0149\0\u0f3d"+
    "\0\u0f6c\0\u0f9b\0\u0fca\0\u01a7\0\u0ff9\0\u1028\0\u0149\0\u1057"+
    "\0\u1086\0\u0149\0\u10b5\0\u0149\0\u10e4\0\u0149\0\u01a7\0\u0149";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[136];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\10\1\11\1\12\2\10\1\13\1\14\1\15\1\12"+
    "\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25"+
    "\1\26\1\27\1\30\1\31\1\32\1\33\1\34\2\12"+
    "\1\35\2\12\1\36\2\12\1\37\2\12\1\40\1\41"+
    "\3\12\1\42\2\10\1\43\1\44\2\14\7\0\1\15"+
    "\1\0\1\16\1\17\1\20\1\45\1\22\1\23\1\24"+
    "\1\25\1\26\1\27\1\30\1\31\1\46\1\33\6\0"+
    "\1\47\2\0\1\50\2\0\1\51\22\0\1\15\1\0"+
    "\1\16\1\17\1\20\1\45\1\22\1\23\1\24\1\25"+
    "\1\26\1\27\1\30\1\31\1\46\1\33\6\0\1\47"+
    "\2\0\1\50\2\0\1\51\6\0\1\52\13\0\1\15"+
    "\1\0\1\16\1\17\1\20\1\45\1\22\1\23\1\24"+
    "\1\25\1\26\1\27\1\30\1\31\1\46\1\33\6\0"+
    "\1\47\2\0\1\50\2\0\1\51\6\0\1\53\13\0"+
    "\1\15\1\0\1\16\1\17\1\20\1\45\1\22\1\23"+
    "\1\24\1\25\1\26\1\27\1\30\1\31\1\46\1\33"+
    "\6\0\1\47\2\0\1\50\2\0\1\51\6\0\1\54"+
    "\4\0\1\10\1\55\4\10\1\0\1\15\1\10\1\16"+
    "\1\17\1\20\1\45\1\22\1\23\1\24\1\25\1\26"+
    "\1\27\1\30\1\31\1\32\1\33\6\10\1\56\2\10"+
    "\1\57\2\10\1\60\14\10\1\61\4\10\1\0\1\15"+
    "\1\10\1\16\1\17\1\20\1\45\1\22\1\23\1\24"+
    "\1\25\1\26\1\27\1\30\1\31\1\32\1\33\6\10"+
    "\1\56\2\10\1\57\2\10\1\60\13\10\60\0\1\11"+
    "\6\0\1\62\3\0\1\63\22\0\1\62\20\0\2\12"+
    "\5\0\1\12\5\0\1\64\10\0\21\12\1\0\1\65"+
    "\5\0\4\66\1\67\1\70\1\0\50\66\7\0\1\71"+
    "\61\0\1\72\45\0\1\63\104\0\1\73\2\0\1\74"+
    "\45\0\1\75\56\0\1\76\56\0\1\77\56\0\1\100"+
    "\62\0\1\101\57\0\1\102\31\0\2\12\5\0\1\12"+
    "\5\0\1\64\10\0\1\12\1\103\17\12\1\0\1\65"+
    "\6\0\2\12\5\0\1\12\5\0\1\64\10\0\4\12"+
    "\1\104\14\12\1\0\1\65\6\0\2\12\5\0\1\12"+
    "\5\0\1\64\10\0\11\12\1\105\7\12\1\0\1\65"+
    "\6\0\2\12\5\0\1\12\5\0\1\64\10\0\10\12"+
    "\1\106\10\12\1\0\1\65\6\0\2\12\5\0\1\12"+
    "\5\0\1\64\10\0\4\12\1\107\13\12\1\110\1\0"+
    "\1\65\6\0\2\12\5\0\1\12\5\0\1\64\10\0"+
    "\1\111\20\12\1\0\1\65\56\0\1\112\5\0\4\113"+
    "\1\114\2\0\50\113\40\0\1\115\55\0\1\116\52\0"+
    "\1\117\24\0\1\55\56\0\1\61\56\0\1\120\5\0"+
    "\1\121\2\0\1\121\45\0\1\63\6\0\1\62\26\0"+
    "\1\62\17\0\3\66\1\122\1\67\1\123\1\124\47\66"+
    "\1\122\30\0\1\125\61\0\1\126\24\0\2\12\5\0"+
    "\1\12\5\0\1\64\10\0\2\12\1\127\16\12\1\0"+
    "\1\65\6\0\2\12\5\0\1\12\5\0\1\64\10\0"+
    "\5\12\1\130\13\12\1\0\1\65\6\0\2\12\5\0"+
    "\1\12\5\0\1\64\10\0\10\12\1\131\10\12\1\0"+
    "\1\65\6\0\2\12\5\0\1\12\5\0\1\64\10\0"+
    "\2\12\1\132\16\12\1\0\1\65\6\0\2\12\5\0"+
    "\1\12\5\0\1\64\10\0\1\12\1\133\17\12\1\0"+
    "\1\65\6\0\2\12\5\0\1\12\5\0\1\64\10\0"+
    "\12\12\1\134\6\12\1\0\1\65\6\0\2\12\5\0"+
    "\1\12\5\0\1\64\10\0\16\12\1\135\2\12\1\0"+
    "\1\65\61\0\1\136\5\0\1\137\1\0\1\113\1\137"+
    "\45\0\1\136\1\0\1\137\37\0\1\140\50\0\1\141"+
    "\55\0\1\142\27\0\1\120\55\0\3\66\1\122\1\67"+
    "\1\70\1\124\47\66\1\122\3\0\1\124\1\66\1\0"+
    "\1\124\47\0\1\124\31\0\1\143\61\0\1\144\23\0"+
    "\2\12\5\0\1\12\2\0\1\145\2\0\1\64\10\0"+
    "\21\12\1\0\1\65\6\0\2\12\5\0\1\12\5\0"+
    "\1\64\10\0\6\12\1\146\12\12\1\0\1\65\6\0"+
    "\2\12\5\0\1\12\5\0\1\64\10\0\12\12\1\147"+
    "\6\12\1\0\1\65\6\0\2\12\5\0\1\12\5\0"+
    "\1\64\10\0\5\12\1\150\13\12\1\0\1\65\6\0"+
    "\2\12\5\0\1\12\5\0\1\64\10\0\2\12\1\151"+
    "\16\12\1\0\1\65\6\0\2\12\5\0\1\12\5\0"+
    "\1\64\10\0\11\12\1\152\7\12\1\0\1\65\6\0"+
    "\2\12\5\0\1\12\5\0\1\64\10\0\10\12\1\153"+
    "\10\12\1\0\1\65\10\0\1\137\1\113\1\0\1\137"+
    "\47\0\1\137\41\0\1\154\51\0\1\155\53\0\1\156"+
    "\44\0\1\157\74\0\1\160\22\0\2\12\5\0\1\12"+
    "\5\0\1\64\10\0\7\12\1\161\11\12\1\0\1\65"+
    "\6\0\2\12\5\0\1\12\5\0\1\64\10\0\13\12"+
    "\1\162\5\12\1\0\1\65\6\0\2\12\5\0\1\12"+
    "\5\0\1\64\10\0\11\12\1\163\7\12\1\0\1\65"+
    "\6\0\2\12\5\0\1\12\5\0\1\64\10\0\1\164"+
    "\20\12\1\0\1\65\6\0\2\12\5\0\1\12\2\0"+
    "\1\165\2\0\1\64\10\0\21\12\1\0\1\65\6\0"+
    "\2\12\5\0\1\12\5\0\1\64\10\0\4\12\1\166"+
    "\14\12\1\0\1\65\47\0\1\167\54\0\1\170\45\0"+
    "\1\171\65\0\1\172\21\0\2\12\5\0\1\12\5\0"+
    "\1\64\10\0\10\12\1\173\10\12\1\0\1\65\6\0"+
    "\2\12\5\0\1\12\5\0\1\64\10\0\1\12\1\174"+
    "\17\12\1\0\1\65\6\0\2\12\5\0\1\12\5\0"+
    "\1\64\10\0\1\12\1\175\17\12\1\0\1\65\6\0"+
    "\2\12\5\0\1\12\5\0\1\64\10\0\17\12\1\176"+
    "\1\12\1\0\1\65\35\0\1\177\56\0\1\200\65\0"+
    "\1\201\20\0\2\12\5\0\1\12\2\0\1\202\2\0"+
    "\1\64\10\0\21\12\1\0\1\65\6\0\2\12\5\0"+
    "\1\12\5\0\1\64\10\0\5\12\1\203\13\12\1\0"+
    "\1\65\6\0\2\12\5\0\1\12\5\0\1\204\10\0"+
    "\21\12\1\0\1\65\41\0\1\205\41\0\1\206\40\0"+
    "\2\12\5\0\1\12\5\0\1\64\10\0\10\12\1\207"+
    "\10\12\1\0\1\65\44\0\1\210\17\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[4371];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\3\1\2\0\1\11\3\1\1\11\1\1\1\11"+
    "\1\1\1\11\1\1\1\11\1\1\2\11\14\1\1\11"+
    "\2\1\1\11\4\0\3\11\5\1\1\0\1\1\2\11"+
    "\2\0\3\11\2\0\6\11\7\1\1\11\5\0\1\1"+
    "\2\0\1\1\3\0\7\1\1\11\6\0\1\11\6\1"+
    "\3\0\1\11\1\0\4\1\1\11\1\1\1\11\3\0"+
    "\4\1\1\11\2\0\1\11\1\1\1\11\1\0\1\11"+
    "\1\1\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[136];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
// remember the last occured identifier for Array support
private String identifier = null;
// remember the dimension of that array identifier
private int identifierDim;

private ComplexSymbolFactory symFact;

Yylex(java.io.Reader in, ComplexSymbolFactory symFact) {
	this(in);
	this.symFact=symFact;
}


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Yylex(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  Yylex(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 132) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) throws struktor.StruktorException {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new struktor.StruktorException(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  throws struktor.StruktorException {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException, ProcessorException
, struktor.StruktorException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 49: 
          { return symFact.newSymbol("sizeof(",Psym.SIZEOF);
          }
        case 52: break;
        case 5: 
          { return symFact.newSymbol("anInt",Psym.INTEGER, new Integer(yytext()));
          }
        case 53: break;
        case 43: 
          { return symFact.newSymbol("(int)",Psym.INTCAST);
          }
        case 54: break;
        case 24: 
          { yybegin(YYINITIAL); return symFact.newSymbol("anInt",Psym.INTEGER, new Integer(Integer.parseInt(yytext())));
          }
        case 55: break;
        case 15: 
          { return symFact.newSymbol(")",Psym.RPAREN);
          }
        case 56: break;
        case 12: 
          { return symFact.newSymbol(".",Psym.PART);
          }
        case 57: break;
        case 47: 
          { return symFact.newSymbol("return",Psym.RET);
          }
        case 58: break;
        case 23: 
          { yybegin(YYINITIAL); return symFact.newSymbol("]",Psym.RPAREN);
          }
        case 59: break;
        case 40: 
          { return symFact.newSymbol("aChar",Psym.CHARACTER, new Character(yytext().charAt(1)));
          }
        case 60: break;
        case 37: 
          { return symFact.newSymbol("&&",Psym.LOGAND);
          }
        case 61: break;
        case 38: 
          { yybegin(ARRAYINDEX2); 
									   identifierDim++;
									   return symFact.newSymbol("+",Psym.PLUS);
          }
        case 62: break;
        case 22: 
          { return symFact.newSymbol("aParameter",Psym.PARAM);
          }
        case 63: break;
        case 4: 
          { throw new ProcessorException("Illegal Character: "+yytext());
          }
        case 64: break;
        case 46: 
          { return symFact.newSymbol("double",Psym.TYPENAME, new String("double"));
          }
        case 65: break;
        case 7: 
          { /* ignore white space. */
          }
        case 66: break;
        case 51: 
          { return symFact.newSymbol("continue",Psym.CONTINUE);
          }
        case 67: break;
        case 45: 
          { return symFact.newSymbol("char*",Psym.TYPENAME, new String("char*"));
          }
        case 68: break;
        case 10: 
          { return symFact.newSymbol("+",Psym.PLUS);
          }
        case 69: break;
        case 9: 
          { return symFact.newSymbol(";",Psym.SEMI);
          }
        case 70: break;
        case 39: 
          { return symFact.newSymbol("int",Psym.TYPENAME, new String("int"));
          }
        case 71: break;
        case 36: 
          { return symFact.newSymbol("||",Psym.LOGOR);
          }
        case 72: break;
        case 21: 
          { return symFact.newSymbol("&",Psym.ADRESS);
          }
        case 73: break;
        case 19: 
          { return symFact.newSymbol("<",Psym.LES);
          }
        case 74: break;
        case 1: 
          { yybegin(ARRAY2); return symFact.newSymbol("arrayIndex",Psym.LPAREN);
          }
        case 75: break;
        case 26: 
          { return symFact.newSymbol("aDouble",Psym.DOUBLE, new Double(yytext()));
          }
        case 76: break;
        case 41: 
          { return symFact.newSymbol("int*",Psym.TYPENAME, new String("int*"));
          }
        case 77: break;
        case 25: 
          { yybegin(YYINITIAL); return symFact.newSymbol("anInt",Psym.INTEGER, new Integer(Integer.parseInt(yytext())*(DecList.getDim(identifier, identifierDim))));
          }
        case 78: break;
        case 30: 
          { return symFact.newSymbol("--",Psym.DEC);
          }
        case 79: break;
        case 48: 
          { return symFact.newSymbol("double*",Psym.TYPENAME, new String("double*"));
          }
        case 80: break;
        case 17: 
          { return symFact.newSymbol("=",Psym.ASSIGN);
          }
        case 81: break;
        case 33: 
          { return symFact.newSymbol("!=",Psym.NEQ);
          }
        case 82: break;
        case 2: 
          { yybegin(ARRAY3); return symFact.newSymbol("arrayIndex",Psym.VARIABLE, identifier);
          }
        case 83: break;
        case 14: 
          { return symFact.newSymbol("(",Psym.LPAREN);
          }
        case 84: break;
        case 13: 
          { return symFact.newSymbol("/",Psym.DIVIDE);
          }
        case 85: break;
        case 50: 
          { return symFact.newSymbol("(double)",Psym.DOUBLECAST);
          }
        case 86: break;
        case 20: 
          { return symFact.newSymbol(">",Psym.MOR);
          }
        case 87: break;
        case 27: 
          { return symFact.newSymbol("aFunctionCall",Psym.FUNC, new String(yytext().substring(0,yytext().length() - 1)));
          }
        case 88: break;
        case 29: 
          { return symFact.newSymbol("aString",Psym.STRING, new String(yytext().substring(1,yytext().length() - 1)));
          }
        case 89: break;
        case 42: 
          { return symFact.newSymbol("char",Psym.TYPENAME, new String("char"));
          }
        case 90: break;
        case 34: 
          { return symFact.newSymbol("<=",Psym.LEQ);
          }
        case 91: break;
        case 11: 
          { return symFact.newSymbol("*",Psym.TIMES);
          }
        case 92: break;
        case 16: 
          { return symFact.newSymbol("%",Psym.MOD);
          }
        case 93: break;
        case 6: 
          { return symFact.newSymbol("aVariable",Psym.VARIABLE, new String(yytext()));
          }
        case 94: break;
        case 28: 
          { yybegin(ARRAY1);
                                       identifier = new String(yytext().substring(0,yytext().length() - 1));
                                       identifierDim = 1;
                                       return symFact.newSymbol("[",Psym.TIMES);
          }
        case 95: break;
        case 18: 
          { return symFact.newSymbol("!",Psym.NOT);
          }
        case 96: break;
        case 31: 
          { return symFact.newSymbol("++",Psym.INC);
          }
        case 97: break;
        case 32: 
          { return symFact.newSymbol("==",Psym.EQU);
          }
        case 98: break;
        case 44: 
          { return symFact.newSymbol("break",Psym.BREAK);
          }
        case 99: break;
        case 8: 
          { return symFact.newSymbol("-",Psym.MINUS);
          }
        case 100: break;
        case 35: 
          { return symFact.newSymbol(">=",Psym.MEQ);
          }
        case 101: break;
        case 3: 
          { yybegin(ARRAYINDEX1); return symFact.newSymbol("+",Psym.PLUS);
          }
        case 102: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              { return (symFact.newSymbol("EOF",Psym.EOF));
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
