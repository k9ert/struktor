package struktor.processor;
import java_cup.runtime.Symbol;


class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

// remember the last occured identifier for Array support
private String identifier = null;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int ARRAY3 = 4;
	private final int ARRAY2 = 3;
	private final int YYINITIAL = 0;
	private final int ARRAY1 = 2;
	private final int yy_state_dtrans[] = {
		0,
		101,
		48,
		49,
		50
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NOT_ACCEPT,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NOT_ACCEPT,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NOT_ACCEPT,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NOT_ACCEPT,
		/* 113 */ YY_NOT_ACCEPT,
		/* 114 */ YY_NOT_ACCEPT,
		/* 115 */ YY_NOT_ACCEPT,
		/* 116 */ YY_NOT_ACCEPT,
		/* 117 */ YY_NOT_ACCEPT,
		/* 118 */ YY_NOT_ACCEPT,
		/* 119 */ YY_NOT_ACCEPT,
		/* 120 */ YY_NOT_ACCEPT,
		/* 121 */ YY_NOT_ACCEPT,
		/* 122 */ YY_NOT_ACCEPT,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NOT_ACCEPT,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"41:8,42,46,43,41,47:2,41:18,46,11,39,41,37,9,15,44,7,8,4,2,35,3,5,6,34:10,4" +
"1,1,12,10,13,41:2,33:4,45,33:21,36,40,38,41,33,41,26,22,28,19,24,31,33,32,1" +
"6,33,27,23,33,17,20,33:2,25,29,18,21,33:4,30,41,14,41:3,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,133,
"0,1:2,2,3,1,4,1,5,1:2,6,7,8,9,10,11,12,13,1,14,1:3,15,1:10,16,1:2,17,1,18,1" +
",19,18,1:3,18,20,21,22,23,1:2,24,25,26,1:6,27,26,28,29,30,31,26,32,33,34,35" +
",36,37,38,39,40,41,42,25,43,44,45,46,47,48,49,50,51,52,53,29,54,55,56,57,58" +
",59,60,61,62,10,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,18" +
",82,83,84,85,86,87,88,89,90")[0];

	private int yy_nxt[][] = unpackFromString(91,48,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,123:2,127,123:2,128,123:2,129,123" +
":2,130,131,123:4,18,19,53:2,20,64,53:3,21,67,123,21:2,-1:50,22,-1:48,23,-1:" +
"78,24,-1:29,51,-1:2,63,-1:38,25,-1:47,26,-1:47,27,-1:47,28,-1:51,29,-1:48,3" +
"0,-1:39,31,-1:8,123,54,123:17,-1,32,-1:8,123,-1:7,132,-1:18,66,-1:9,18,-1:1" +
"0,66,-1:38,33,-1:35,66,-1:9,24,-1:10,66,-1:6,37,-1:2,31,-1:8,123:19,-1,32,-" +
"1:8,123,-1:6,41,-1:2,31,-1:8,123:19,-1,32,-1:8,123,-1:9,31,-1:8,123:19,-1,3" +
"2,-1:8,123,-1:6,44,-1:2,31,-1:8,123:19,-1,32,-1:8,123,-1:2,1,2,3,4,5,52,7,8" +
",9,10,11,12,13,14,103,16,-1:6,105,-1:2,107,-1:2,109,-1:8,60,-1:10,1,2,3,4,5" +
",52,7,8,9,10,11,12,13,14,103,16,-1:6,105,-1:2,107,-1:2,109,-1:8,61,-1:10,1," +
"2,3,4,5,52,7,8,9,10,11,12,13,14,103,16,-1:6,105,-1:2,107,-1:2,109,-1:8,62,-" +
"1:27,77,-1:37,31,-1:8,123:2,35,123:16,-1,32,-1:8,123,-1:36,55,-1:14,69:38,3" +
"4,71,69:2,-1,69:4,-1:20,79,-1:34,31,-1:8,123:5,78,123:13,-1,32,-1:8,123,-1:" +
"4,81:2,-1:30,55,-1:14,73:38,-1,75,73:2,-1,73:4,-1:7,31,-1:8,123:8,125,123:1" +
"0,-1,32,-1:8,123,-1:9,31,-1:8,123:2,80,123:16,-1,32,-1:8,123,-1:3,69:38,56," +
"71,69,83,85,69:2,83,69,-1:7,31,-1:8,123,82,123:17,-1,32,-1:8,123,-1:46,36,-" +
"1:10,31,-1:8,123:10,84,123:8,-1,32,-1:8,123,-1:41,73,-1:2,87:2,36,-1,87,-1:" +
"8,31,-1:8,123:14,86,123:4,-1,32,-1:8,123,-1:20,89,-1:36,31,-1:8,123:6,88,12" +
"3:12,-1,32,-1:8,123,-1:23,91,-1:33,31,-1:8,123:5,92,123:13,-1,32,-1:8,123,-" +
"1:9,31,-1:8,123:2,94,123:16,-1,32,-1:8,123,-1:3,69:38,34,71,69,83,85,69:2,8" +
"3,69,-1:7,31,-1:8,123:9,38,123:9,-1,32,-1:8,123,-1:42,69,-1,85:2,-1:2,85,-1" +
":8,31,-1:8,123:8,96,123:10,-1,32,-1:8,123,-1:42,73,-1,87:2,-1:2,87,-1:8,31," +
"-1:8,123:7,98,123:11,-1,32,-1:8,123,-1:10,39,-1:46,31,-1:8,123:11,40,123:7," +
"-1,32,-1:8,123,-1:24,95,-1:32,31,-1:8,123:9,100,123:9,-1,32,-1:8,123,-1:9,3" +
"1,-1:8,102,123:18,-1,32,-1:8,123,-1:25,97,-1:31,31,-1:8,123:4,104,123:14,-1" +
",32,-1:8,123,-1:26,99,-1:30,31,-1:8,123:8,42,123:10,-1,32,-1:8,123,-1:10,46" +
",-1:46,31,-1:8,123,43,123:17,-1,32,-1:8,123,-1:2,1,2,3,4,5,52,7,8,9,10,11,1" +
"2,13,14,103,16,-1:6,105,-1:2,107,-1:2,109,-1:26,31,-1:8,123,106,123:17,-1,3" +
"2,-1:8,123,-1:9,31,-1:8,123:15,108,123:3,-1,32,-1:8,123,-1:27,126,-1:29,31," +
"-1:8,123:5,110,123:13,-1,32,-1:8,123,-1:26,111,-1:30,45,-1:8,123:19,-1,32,-" +
"1:8,123,-1:22,112,-1:34,31,-1:8,123:8,47,123:10,-1,32,-1:8,123,-1:20,114,-1" +
":46,115,-1:56,116,-1:42,117,-1:44,118,-1:56,57,-1:45,119,-1:38,120,-1:48,58" +
",-1:47,121,-1:51,122,-1:50,59,-1:47,93,-1:9,124,-1:10,93,-1:9,31,-1:8,123:1" +
"0,90,123:8,-1,32,-1:8,123,-1:26,113,-1:30,31,-1:8,123:4,65,123:14,-1,32,-1:" +
"8,123,-1:9,31,-1:8,123:9,68,123:9,-1,32,-1:8,123,-1:9,31,-1:8,123:8,70,123:" +
"10,-1,32,-1:8,123,-1:9,31,-1:8,123:4,72,123:11,74,123:2,-1,32,-1:8,123,-1:9" +
",31,-1:8,76,123:18,-1,32,-1:8,123,-1:26,66,-1:9,124,-1:10,66,-1:2");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException, 
ProcessorException

		{
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

return (new Symbol(Psym.EOF));
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(Psym.SEMI); }
					case -3:
						break;
					case 3:
						{ return new Symbol(Psym.PLUS); }
					case -4:
						break;
					case 4:
						{ return new Symbol(Psym.MINUS); }
					case -5:
						break;
					case 5:
						{ return new Symbol(Psym.TIMES); }
					case -6:
						break;
					case 6:
						{ return new Symbol(Psym.PART); }
					case -7:
						break;
					case 7:
						{ return new Symbol(Psym.DIVIDE); }
					case -8:
						break;
					case 8:
						{ return new Symbol(Psym.LPAREN); }
					case -9:
						break;
					case 9:
						{ return new Symbol(Psym.RPAREN); }
					case -10:
						break;
					case 10:
						{ return new Symbol(Psym.MOD); }
					case -11:
						break;
					case 11:
						{ return new Symbol(Psym.ASSIGN); }
					case -12:
						break;
					case 12:
						{ return new Symbol(Psym.NOT); }
					case -13:
						break;
					case 13:
						{ return new Symbol(Psym.LES); }
					case -14:
						break;
					case 14:
						{ return new Symbol(Psym.MOR); }
					case -15:
						break;
					case 15:
						{ throw new ProcessorException("Illegal Character: "+yytext()); }
					case -16:
						break;
					case 16:
						{ return new Symbol(Psym.ADRESS); }
					case -17:
						break;
					case 17:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -18:
						break;
					case 18:
						{ return new Symbol(Psym.INTEGER, new Integer(yytext())); }
					case -19:
						break;
					case 19:
						{ return new Symbol(Psym.PARAM);}
					case -20:
						break;
					case 20:
						{ yybegin(YYINITIAL); return new Symbol(Psym.RPAREN);}
					case -21:
						break;
					case 21:
						{ /* ignore white space. */ }
					case -22:
						break;
					case 22:
						{ return new Symbol(Psym.INC); }
					case -23:
						break;
					case 23:
						{ return new Symbol(Psym.DEC); }
					case -24:
						break;
					case 24:
						{ return new Symbol(Psym.DOUBLE, new Double(yytext())); }
					case -25:
						break;
					case 25:
						{ return new Symbol(Psym.EQU); }
					case -26:
						break;
					case 26:
						{ return new Symbol(Psym.NEQ); }
					case -27:
						break;
					case 27:
						{ return new Symbol(Psym.LEQ); }
					case -28:
						break;
					case 28:
						{ return new Symbol(Psym.MEQ); }
					case -29:
						break;
					case 29:
						{ return new Symbol(Psym.LOGOR);}
					case -30:
						break;
					case 30:
						{ return new Symbol(Psym.LOGAND);}
					case -31:
						break;
					case 31:
						{ return new Symbol(Psym.FUNC, new String(yytext().substring(0,yytext().length() - 1))); }
					case -32:
						break;
					case 32:
						{ yybegin(ARRAY1);
                                       identifier = new String(yytext().substring(0,yytext().length() - 1));
                                       return new Symbol(Psym.TIMES);}
					case -33:
						break;
					case 33:
						{ yybegin(YYINITIAL); return new Symbol(Psym.TIMES); }
					case -34:
						break;
					case 34:
						{return new Symbol(Psym.STRING, new String(yytext().substring(1,yytext().length() - 1)));   }
					case -35:
						break;
					case 35:
						{ return new Symbol(Psym.TYPENAME, new String("int")); }
					case -36:
						break;
					case 36:
						{return new Symbol(Psym.CHARACTER, new Character(yytext().charAt(1)));   }
					case -37:
						break;
					case 37:
						{ return new Symbol(Psym.TYPENAME, new String("int*")); }
					case -38:
						break;
					case 38:
						{ return new Symbol(Psym.TYPENAME, new String("char")); }
					case -39:
						break;
					case 39:
						{ return new Symbol(Psym.INTCAST); }
					case -40:
						break;
					case 40:
						{ return new Symbol(Psym.BREAK); }
					case -41:
						break;
					case 41:
						{ return new Symbol(Psym.TYPENAME, new String("char*")); }
					case -42:
						break;
					case 42:
						{ return new Symbol(Psym.TYPENAME, new String("double")); }
					case -43:
						break;
					case 43:
						{ return new Symbol(Psym.RET); }
					case -44:
						break;
					case 44:
						{ return new Symbol(Psym.TYPENAME, new String("double*")); }
					case -45:
						break;
					case 45:
						{ return new Symbol(Psym.SIZEOF); }
					case -46:
						break;
					case 46:
						{ return new Symbol(Psym.DOUBLECAST); }
					case -47:
						break;
					case 47:
						{ return new Symbol(Psym.CONTINUE); }
					case -48:
						break;
					case 48:
						{ yybegin(ARRAY2); return new Symbol(Psym.LPAREN);}
					case -49:
						break;
					case 49:
						{ yybegin(ARRAY3); return new Symbol(Psym.VARIABLE, identifier);}
					case -50:
						break;
					case 50:
						{ yybegin(YYINITIAL); return new Symbol(Psym.PLUS); }
					case -51:
						break;
					case 52:
						{ return new Symbol(Psym.PART); }
					case -52:
						break;
					case 53:
						{ throw new ProcessorException("Illegal Character: "+yytext()); }
					case -53:
						break;
					case 54:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -54:
						break;
					case 55:
						{ return new Symbol(Psym.DOUBLE, new Double(yytext())); }
					case -55:
						break;
					case 56:
						{return new Symbol(Psym.STRING, new String(yytext().substring(1,yytext().length() - 1)));   }
					case -56:
						break;
					case 57:
						{ return new Symbol(Psym.BREAK); }
					case -57:
						break;
					case 58:
						{ return new Symbol(Psym.RET); }
					case -58:
						break;
					case 59:
						{ return new Symbol(Psym.CONTINUE); }
					case -59:
						break;
					case 60:
						{ yybegin(ARRAY2); return new Symbol(Psym.LPAREN);}
					case -60:
						break;
					case 61:
						{ yybegin(ARRAY3); return new Symbol(Psym.VARIABLE, identifier);}
					case -61:
						break;
					case 62:
						{ yybegin(YYINITIAL); return new Symbol(Psym.PLUS); }
					case -62:
						break;
					case 64:
						{ throw new ProcessorException("Illegal Character: "+yytext()); }
					case -63:
						break;
					case 65:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -64:
						break;
					case 67:
						{ throw new ProcessorException("Illegal Character: "+yytext()); }
					case -65:
						break;
					case 68:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -66:
						break;
					case 70:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -67:
						break;
					case 72:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -68:
						break;
					case 74:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -69:
						break;
					case 76:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -70:
						break;
					case 78:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -71:
						break;
					case 80:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -72:
						break;
					case 82:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -73:
						break;
					case 84:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -74:
						break;
					case 86:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -75:
						break;
					case 88:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -76:
						break;
					case 90:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -77:
						break;
					case 92:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -78:
						break;
					case 94:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -79:
						break;
					case 96:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -80:
						break;
					case 98:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -81:
						break;
					case 100:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -82:
						break;
					case 102:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -83:
						break;
					case 104:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -84:
						break;
					case 106:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -85:
						break;
					case 108:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -86:
						break;
					case 110:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -87:
						break;
					case 123:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -88:
						break;
					case 124:
						{ return new Symbol(Psym.DOUBLE, new Double(yytext())); }
					case -89:
						break;
					case 125:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -90:
						break;
					case 127:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -91:
						break;
					case 128:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -92:
						break;
					case 129:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -93:
						break;
					case 130:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -94:
						break;
					case 131:
						{ return new Symbol(Psym.VARIABLE, new String(yytext())); }
					case -95:
						break;
					case 132:
						{ return new Symbol(Psym.DOUBLE, new Double(yytext())); }
					case -96:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
