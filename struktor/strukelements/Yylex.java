package struktor.strukelements;
import java.util.Stack;

import java_cup.runtime.Symbol;
import struktor.StruktorException;
import struktor.Tracer;


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
	private final int FOR = 14;
	private final int PARAMLIST = 2;
	private final int DOWHILE = 13;
	private final int INPUT = 12;
	private final int VALUE = 5;
	private final int DECSTART = 7;
	private final int CONDITION = 11;
	private final int CONDITIONPARA = 10;
	private final int YYINITIAL = 0;
	private final int INSTRLIST = 8;
	private final int VARTYP2 = 4;
	private final int DEC = 6;
	private final int STRUK = 1;
	private final int BEGIN = 9;
	private final int VARTYP1 = 3;
	private final int yy_state_dtrans[] = {
		0,
		53,
		271,
		271,
		271,
		93,
		28,
		107,
		271,
		119,
		125,
		126,
		271,
		130,
		52
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
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NOT_ACCEPT,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NOT_ACCEPT,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NOT_ACCEPT,
		/* 77 */ YY_NO_ANCHOR,
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
		/* 98 */ YY_NOT_ACCEPT,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NOT_ACCEPT,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NOT_ACCEPT,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NOT_ACCEPT,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NOT_ACCEPT,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NOT_ACCEPT,
		/* 109 */ YY_NOT_ACCEPT,
		/* 110 */ YY_NOT_ACCEPT,
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
		/* 123 */ YY_NOT_ACCEPT,
		/* 124 */ YY_NOT_ACCEPT,
		/* 125 */ YY_NOT_ACCEPT,
		/* 126 */ YY_NOT_ACCEPT,
		/* 127 */ YY_NOT_ACCEPT,
		/* 128 */ YY_NOT_ACCEPT,
		/* 129 */ YY_NOT_ACCEPT,
		/* 130 */ YY_NOT_ACCEPT,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NOT_ACCEPT,
		/* 133 */ YY_NOT_ACCEPT,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NOT_ACCEPT,
		/* 138 */ YY_NOT_ACCEPT,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NOT_ACCEPT,
		/* 141 */ YY_NOT_ACCEPT,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NOT_ACCEPT,
		/* 144 */ YY_NOT_ACCEPT,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NOT_ACCEPT,
		/* 147 */ YY_NOT_ACCEPT,
		/* 148 */ YY_NOT_ACCEPT,
		/* 149 */ YY_NOT_ACCEPT,
		/* 150 */ YY_NOT_ACCEPT,
		/* 151 */ YY_NOT_ACCEPT,
		/* 152 */ YY_NOT_ACCEPT,
		/* 153 */ YY_NOT_ACCEPT,
		/* 154 */ YY_NOT_ACCEPT,
		/* 155 */ YY_NOT_ACCEPT,
		/* 156 */ YY_NOT_ACCEPT,
		/* 157 */ YY_NOT_ACCEPT,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NOT_ACCEPT,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NOT_ACCEPT,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NOT_ACCEPT,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NOT_ACCEPT,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NOT_ACCEPT,
		/* 169 */ YY_NOT_ACCEPT,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NOT_ACCEPT,
		/* 172 */ YY_NOT_ACCEPT,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NOT_ACCEPT,
		/* 175 */ YY_NOT_ACCEPT,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NOT_ACCEPT,
		/* 178 */ YY_NOT_ACCEPT,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NOT_ACCEPT,
		/* 181 */ YY_NOT_ACCEPT,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NO_ANCHOR,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NO_ANCHOR,
		/* 186 */ YY_NO_ANCHOR,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NO_ANCHOR,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NO_ANCHOR,
		/* 192 */ YY_NO_ANCHOR,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NO_ANCHOR,
		/* 195 */ YY_NO_ANCHOR,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NO_ANCHOR,
		/* 198 */ YY_NO_ANCHOR,
		/* 199 */ YY_NO_ANCHOR,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NO_ANCHOR,
		/* 202 */ YY_NO_ANCHOR,
		/* 203 */ YY_NO_ANCHOR,
		/* 204 */ YY_NO_ANCHOR,
		/* 205 */ YY_NO_ANCHOR,
		/* 206 */ YY_NO_ANCHOR,
		/* 207 */ YY_NO_ANCHOR,
		/* 208 */ YY_NO_ANCHOR,
		/* 209 */ YY_NO_ANCHOR,
		/* 210 */ YY_NO_ANCHOR,
		/* 211 */ YY_NO_ANCHOR,
		/* 212 */ YY_NO_ANCHOR,
		/* 213 */ YY_NO_ANCHOR,
		/* 214 */ YY_NO_ANCHOR,
		/* 215 */ YY_NO_ANCHOR,
		/* 216 */ YY_NO_ANCHOR,
		/* 217 */ YY_NO_ANCHOR,
		/* 218 */ YY_NO_ANCHOR,
		/* 219 */ YY_NO_ANCHOR,
		/* 220 */ YY_NO_ANCHOR,
		/* 221 */ YY_NO_ANCHOR,
		/* 222 */ YY_NO_ANCHOR,
		/* 223 */ YY_NO_ANCHOR,
		/* 224 */ YY_NO_ANCHOR,
		/* 225 */ YY_NO_ANCHOR,
		/* 226 */ YY_NO_ANCHOR,
		/* 227 */ YY_NO_ANCHOR,
		/* 228 */ YY_NO_ANCHOR,
		/* 229 */ YY_NO_ANCHOR,
		/* 230 */ YY_NO_ANCHOR,
		/* 231 */ YY_NO_ANCHOR,
		/* 232 */ YY_NOT_ACCEPT,
		/* 233 */ YY_NO_ANCHOR,
		/* 234 */ YY_NOT_ACCEPT,
		/* 235 */ YY_NO_ANCHOR,
		/* 236 */ YY_NO_ANCHOR,
		/* 237 */ YY_NO_ANCHOR,
		/* 238 */ YY_NO_ANCHOR,
		/* 239 */ YY_NOT_ACCEPT,
		/* 240 */ YY_NOT_ACCEPT,
		/* 241 */ YY_NO_ANCHOR,
		/* 242 */ YY_NO_ANCHOR,
		/* 243 */ YY_NO_ANCHOR,
		/* 244 */ YY_NOT_ACCEPT,
		/* 245 */ YY_NOT_ACCEPT,
		/* 246 */ YY_NO_ANCHOR,
		/* 247 */ YY_NO_ANCHOR,
		/* 248 */ YY_NO_ANCHOR,
		/* 249 */ YY_NOT_ACCEPT,
		/* 250 */ YY_NOT_ACCEPT,
		/* 251 */ YY_NO_ANCHOR,
		/* 252 */ YY_NOT_ACCEPT,
		/* 253 */ YY_NOT_ACCEPT,
		/* 254 */ YY_NOT_ACCEPT,
		/* 255 */ YY_NOT_ACCEPT,
		/* 256 */ YY_NO_ANCHOR,
		/* 257 */ YY_NOT_ACCEPT,
		/* 258 */ YY_NO_ANCHOR,
		/* 259 */ YY_NOT_ACCEPT,
		/* 260 */ YY_NO_ANCHOR,
		/* 261 */ YY_NO_ANCHOR,
		/* 262 */ YY_NO_ANCHOR,
		/* 263 */ YY_NOT_ACCEPT,
		/* 264 */ YY_NO_ANCHOR,
		/* 265 */ YY_NOT_ACCEPT,
		/* 266 */ YY_NO_ANCHOR,
		/* 267 */ YY_NOT_ACCEPT,
		/* 268 */ YY_NO_ANCHOR,
		/* 269 */ YY_NOT_ACCEPT,
		/* 270 */ YY_NO_ANCHOR,
		/* 271 */ YY_NOT_ACCEPT,
		/* 272 */ YY_NOT_ACCEPT,
		/* 273 */ YY_NOT_ACCEPT,
		/* 274 */ YY_NO_ANCHOR,
		/* 275 */ YY_NO_ANCHOR,
		/* 276 */ YY_NOT_ACCEPT,
		/* 277 */ YY_NOT_ACCEPT
	};
	private int yy_cmap[] = unpackFromString(1,130,
"13:8,34,48,35,13,55,33,13:18,7,13,31,13:3,52,36,19,26,27,39,40,39,37,1,18:1" +
"0,51,41,13,30,13:2,2,17:3,43,38,17:3,49,17:3,54,17,53,44,17:2,25,17:7,28,32" +
",29,13,17,13,5,21,23,16,14,45,11,24,20,17,9,22,12,15,10,50,17,6,3,4,8,17,47" +
",17:3,42,13,46,13:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,278,
"0,1,2,1:2,3,4,5,6,1:2,7:2,8:3,9,8:2,7,10,7,11,12,13,14,1,15,16,17,1:8,5:2,1" +
":8,18,1:3,19,20,1,21,9,10,7:4,22,23,24,1:2,7,25,26,8,27,28,29,7,30,31,32,33" +
",34,23,35,15,36,37,10,38,39,40,41,42,43,44,45,46,47,48,23,49,15,50,22,47,51" +
",52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76" +
",77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,1" +
"01,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119," +
"120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138" +
",139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,15" +
"7,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,1" +
"76,177,178,5,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,19" +
"4,195,196,197,198,199,200,201,202,18,203,8,204,205,206,207,208,209,210,211," +
"212,213,214,215,216,217,218,219,220,221,222")[0];

	private int yy_nxt[][] = unpackFromString(223,56,
"1,2,54:5,3,54:25,3,54,4,54:12,3,54:6,3,-1:57,5,-1:55,231,238,231:30,-1,231," +
"-1,231:18,158,231,-1,6:32,-1,6,-1,6:20,-1,231:32,-1,231,-1,231:20,-1,8:32,-" +
"1,8,-1,8:20,-1,67:32,-1,67,-1,67:5,12,67:14,-1,67:2,70:4,67,70:5,67,70:5,11" +
",70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,1" +
"6:32,-1,16,-1,16:20,-1,85:32,-1,85,-1,85:5,57,85:9,20,85:4,-1,22:32,-1,22,-" +
"1,22:20,-1,23:32,-1,23,-1,23:20,-1:14,95,-1:3,24,-1:18,25,95,-1:31,102,-1:3" +
",25,-1:19,102,-1:18,99:30,-1,100,99:2,-1,27,99:19,1,262,54,29:4,3,29:5,54,2" +
"9:4,64,54,29:6,30,31,32,33,34,54:2,3,54,4,54:2,29,54,35,36,37,29:3,54,29,3," +
"29:2,54:2,29:2,3,-1:3,29:4,-1,29:5,-1,29:5,-1,29:6,-1:12,29,-1:4,29:3,-1,29" +
",-1,29:2,-1:2,29:2,-1:2,243,-1:54,1,256,54:5,3,54:25,3,54,4,54:12,66,54:6,3" +
",1,256,54,68,258:3,3,258:5,54,160,258,71,258,74:2,77,264,258,268,258:2,54,7" +
"4,54:3,74,54,3,54,4,54:2,258,54:2,9,54,258:2,165,10,233,3,270,258,54,74,275" +
",258,3,-1,231:32,-1,231,-1,231:15,6,231:4,-1:18,62,-1:38,97:30,26,98,97:2,-" +
"1,97:20,-1:18,64,-1:38,67:2,70:4,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2" +
",70,67:2,12,67,70:3,67,234,67,70:2,67:2,70:2,67,-1,231:11,7,231:20,-1,231,-" +
"1,231:20,-1,67:2,70:4,67,70:2,13,70:2,67,273,70:4,11,70:6,67:7,-1,67,-1,67:" +
"2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,231:11,8,231:20,-1,231,-" +
"1,231:20,-1,67:2,70:4,67,70:5,67,15,70:4,11,70:6,67:7,-1,67,-1,67:2,70,67:2" +
",12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,231:26,38,231:5,-1,231,-1,231:20," +
"-1,67:2,70:4,67,70:5,67,70:5,56,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3," +
"67,70,67,70:2,67:2,70:2,67,-1,67:2,70:4,67,70:5,67,70:5,11,70:6,67:7,-1,67," +
"-1,67:2,70,67:2,12,67,70:2,14,67,70,67,70:2,67:2,70:2,67,-1,231:26,39,231:5" +
",-1,231,-1,231:20,-1,67:2,70:4,67,70:5,67,17,70:4,11,70:6,67:7,-1,67,-1,67:" +
"2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,67:2,70:4,67,70:5,67,70:" +
"5,11,70:4,18,70,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70" +
":2,67,-1,67:2,70:4,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,19,67" +
",70:3,67,70,67,70:2,67:2,70:2,67,-1:18,25,-1:47,108,-1:46,67:2,70:4,67,70:5" +
",67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,21,67," +
"70:2,67,-1:15,109,-1:41,67:2,22:4,67,22:5,67,22:12,67,22,67:3,22,67,-1,67,-" +
"1,67:2,22,67:2,12,67,22:3,67,22,67,22:2,67,22:3,67,-1:24,110,-1:32,67:2,23:" +
"4,67,23:5,67,23:12,67,23,67:3,23,67,-1,67,-1,67:2,23,67:2,12,67,23:3,67,23," +
"67,23:2,67,23:3,67,-1:4,111,-1:51,1,256,54:5,3,54:10,24,54:12,80,54,3,54,4," +
"82,84,54:10,3,54:6,3,-1,67:9,58,67:3,272,67:18,-1,67,-1,67:5,12,67:14,-1:18" +
",62,-1:20,101,-1:17,67:32,-1,67,-1,67:5,12,67:3,59,67:10,-1,97:6,103,97:23," +
"63,98,97,103,104,97:12,103,97:7,-1,99:6,105,99:24,100,99,105,106,27,99:11,1" +
"05,99:7,-1,97:6,103,97:23,26,98,97,103,104,97:12,103,97:7,-1:7,104,-1:24,97" +
",-1,104:2,-1:12,104,-1:8,99:6,105,99:23,-1,100,99,105,106,27,99:11,105,99:7" +
",-1:7,106,-1:24,99,-1,106:2,-1:12,106,-1:7,1,266,54:5,3,54:8,86,54:3,88,54:" +
"2,90,54,92,40,54:6,3,54,4,54:12,3,54:6,3,-1:8,112,-1:51,41,-1:56,113,-1:56," +
"114,-1:70,115,-1:40,42,-1:69,116,-1:57,117,-1:48,118,-1:54,43,-1:52,44,-1:4" +
"4,1,256,54,235,74:3,3,74:5,54,74:2,94,74:3,96,241,74,246,74:2,54,74,54:3,74" +
",54,3,54,4,54:2,74,54:2,45,46,74:2,134,54,162,3,260,74,54,74,274,74,3,-1,67" +
":18,16,67:13,-1,67,-1,67:5,12,67:14,-1,67:13,60,67:18,-1,67,-1,67:5,12,67:1" +
"4,-1,67:23,61,67:8,-1,67,-1,67:5,12,67:14,-1,67:32,-1,67,-1,67:5,19,67:14,-" +
"1,67:32,-1,67,-1,67:5,12,67:9,21,67:4,1,256,54:5,3,54:11,47,54:13,3,54,4,54" +
":12,3,54:6,3,1,48,65:17,49,65:6,50,65:6,3,65,4,65:20,-1:20,128,-1:57,129,-1" +
":47,51,-1:41,1,256,54:5,3,54:25,3,54,4,54:11,135,3,54:6,3,-1,231:10,55,231:" +
"21,-1,231,-1,231:20,-1,67:2,85:4,67,85:5,67,85:12,67,85,67:3,85,67,-1,67,-1" +
",67:2,85,67:2,12,67,85:3,67,85,67,85:2,67,85:3,67,-1,67:2,73,70:3,67,70:5,6" +
"7,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:" +
"2,67,-1,67:9,143,67:22,-1,67,-1,67:5,12,67:14,-1:24,127,-1:32,231:11,69,231" +
":20,-1,231,-1,231:20,-1,67:6,89,67:25,-1,67,-1,67:5,12,67:14,-1,67:2,70:3,7" +
"6,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70" +
":2,67:2,70:2,67,-1,231:11,72,231:20,-1,231,-1,231:20,-1,67:6,91,67:25,-1,67" +
",-1,67:5,12,67:14,-1,67:2,70:4,67,70:5,67,70:5,11,70:2,79,70:3,67:7,-1,67,-" +
"1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,231:26,75,231:5,-1," +
"231,-1,231:20,-1,67:5,120,67:26,-1,67,-1,67:5,12,67:14,-1,67:2,70:4,67,70:5" +
",67,70:5,11,70:3,81,70:2,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:" +
"2,67:2,70:2,67,-1,231:26,78,231:5,-1,231,-1,231:20,-1,67:21,121,67:10,-1,67" +
",-1,67:5,12,67:14,-1,67:2,70:4,67,70,83,70:3,67,70:5,11,70:6,67:7,-1,67,-1," +
"67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,67:22,122,67:9,-1,67," +
"-1,67:5,12,67:14,-1,67:2,70:4,132,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,7" +
"0,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,67:8,123,67:23,-1,67,-1,67:" +
"5,12,67:14,-1,67:2,70:4,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2," +
"12,67,70:3,67,70,67,70:2,137,67,70:2,67,-1,67:6,132,67:25,-1,67,-1,67:5,12," +
"67:14,-1,67:2,70,87,70:2,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2" +
",12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,67:32,-1,67,-1,67:5,12,67:9,137,6" +
"7:4,-1,67:2,70:4,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,7" +
"0:3,67,70,67,70:2,140,67,70:2,67,-1,67:3,124,67:28,-1,67,-1,67:5,12,67:14,-" +
"1,67:32,-1,67,-1,67:5,12,67:9,140,67:4,-1,231:2,131,231:29,-1,231,-1,231:20" +
",-1,67:19,146,67:12,-1,67,-1,67:5,12,67:14,-1,67:2,70:4,67,70:5,67,70:5,11," +
"70:2,133,70:3,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2" +
",67,-1,67:2,70:4,67,70:5,67,70:5,11,141,70:5,67:7,-1,67,-1,67:2,70,67:2,12," +
"67,70:3,67,70,67,70:2,67:2,70:2,67,-1,67:23,159,67:8,-1,67,-1,67:5,12,67:14" +
",-1,231:3,170,231:28,-1,231,-1,231:20,-1,67:3,148,67:28,-1,67,-1,67:5,12,67" +
":14,-1,67:2,70:4,67,70:2,138,70:2,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:" +
"2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,67:2,70,144,70:2,67,70:5,67,70:5" +
",11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-" +
"1,231:14,173,231:17,-1,231,-1,231:20,-1,67:4,150,67:27,-1,67,-1,67:5,12,67:" +
"14,-1,67:2,70:2,147,70,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,1" +
"2,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,231:4,176,231:27,-1,231,-1,231:20,-" +
"1,67:13,152,67:18,-1,67,-1,67:5,12,67:14,-1,67:2,70:4,67,70:5,67,149,70:4,1" +
"1,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1," +
"231:15,179,231:16,-1,231,-1,231:20,-1,67:3,154,67:28,-1,67,-1,67:5,12,67:14" +
",-1,67:2,70,151,70:2,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12," +
"67,70:3,67,70,67,70:2,67:2,70:2,67,-1,231:5,182,231:26,-1,231,-1,231:20,-1," +
"67:21,156,67:10,-1,67,-1,67:5,12,67:14,-1,67:2,70:4,67,70:5,67,70:5,11,70:2" +
",153,70:3,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67," +
"-1,231:6,183,231:25,-1,231,-1,231:20,-1,67:3,157,67:28,-1,67,-1,67:5,12,67:" +
"14,-1,67:2,70,155,70:2,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,1" +
"2,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,231:3,184,231:28,-1,231,-1,231:20,-" +
"1,231:2,185,231:29,-1,231,-1,231:20,-1,231:6,186,231:25,-1,231,-1,231:20,-1" +
",231:3,187,231:28,-1,231,-1,231:20,-1,231:2,188,231:29,-1,231,-1,231:20,-1," +
"231:5,189,231:26,-1,231,-1,231:20,-1,231:3,190,231:28,-1,231,-1,231:20,-1,2" +
"31:7,191,231:24,-1,231,-1,231:20,-1,231:5,192,231:26,-1,231,-1,231:20,-1,23" +
"1:8,193,231:23,-1,231,-1,231:20,-1,231:7,194,231:24,-1,231,-1,231:20,-1,231" +
":3,195,231:28,-1,231,-1,231:20,-1,231:8,196,231:23,-1,231,-1,231:20,-1,231:" +
"9,197,231:22,-1,231,-1,231:20,-1,231:3,198,231:28,-1,231,-1,231:20,-1,231:1" +
"0,199,231:21,-1,231,-1,231:20,-1,231:9,200,231:22,-1,231,-1,231:20,-1,231:5" +
",201,231:26,-1,231,-1,231:20,-1,231:10,202,231:21,-1,231,-1,231:20,-1,231:4" +
",136,231:27,-1,231,-1,231:20,-1,231:5,203,231:26,-1,231,-1,231:20,-1,231:4," +
"139,231:27,-1,231,-1,231:20,-1,231:13,167,231:18,-1,231,-1,231:20,-1,231:13" +
",167,231:12,206,231:5,-1,231,-1,231:20,-1,231:26,236,231:5,-1,231,-1,231:20" +
",-1,231:6,208,231:25,-1,231,-1,231:20,-1,231:32,-1,231,-1,231:7,209,210,231" +
":11,-1,231:13,211,231:18,-1,231,-1,231:20,-1,231:5,212,231:26,-1,231,-1,231" +
":20,-1,231:22,213,231:9,-1,231,-1,231:20,-1,231:9,214,231:22,-1,231,-1,231:" +
"20,-1,231:21,215,231:10,-1,231,-1,231:20,-1,231:10,216,231:21,-1,231,-1,231" +
":20,-1,231:4,261,231:27,-1,231,-1,231:20,-1,231:5,217,231:26,-1,231,-1,231:" +
"20,-1,231:4,218,231:27,-1,231,-1,231:20,-1,231:11,220,231:20,-1,231,-1,231:" +
"20,-1,231:3,221,231:28,-1,231,-1,231:20,-1,231:6,222,231:25,-1,231,-1,231:2" +
"0,-1,231:19,223,231:12,-1,231,-1,231:20,-1,231:26,142,231:5,-1,231,-1,231:2" +
"0,-1,231:9,224,231:22,-1,231,-1,231:20,-1,231:14,225,231:17,-1,231,-1,231:2" +
"0,-1,231:2,226,231:29,-1,231,-1,231:20,-1,231:6,227,231:25,-1,231,-1,231:20" +
",-1,231:26,145,231:5,-1,231,-1,231:20,-1,231:13,167,231:12,242,231:5,-1,231" +
",-1,231:20,-1,231:6,230,231:25,-1,231,-1,231:20,-1,231:32,-1,231,-1,231:8,2" +
"10,231:11,-1,67:19,164,67:12,-1,67,-1,67:5,12,67:14,-1,67:2,70:4,67,70:5,67" +
",70:5,11,70:4,161,70,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67" +
":2,70:2,67,-1,67:2,70:4,67,70:5,67,70:5,11,166,70:5,67:7,-1,67,-1,67:2,70,6" +
"7:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,67:32,-1,67,-1,67:5,12,67:5,23" +
"2,67:8,-1,231:26,207,231:5,-1,231,-1,231:20,-1,231:4,219,231:27,-1,231,-1,2" +
"31:20,-1,231:2,163,231:10,167,231:18,-1,231,-1,231:20,-1,67:13,168,67:18,-1" +
",67,-1,67:5,12,67:14,-1,67:2,70:4,67,70:5,67,169,70:4,11,70:6,67:7,-1,67,-1" +
",67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,67:5,239,67:26,-1,67" +
",-1,67:5,12,67:14,-1,231:26,247,231:5,-1,231,-1,231:20,-1,231,204,231:30,-1" +
",231,-1,231:18,158,231,-1,67:2,171,67:29,-1,67,-1,67:5,12,67:14,-1,67:2,172" +
",70:3,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,6" +
"7,70:2,67:2,70:2,67,-1,67:4,244,67:27,-1,67,-1,67:5,12,67:14,-1,231:26,229," +
"231:5,-1,231,-1,231:20,-1,231,205,231:30,-1,231,-1,231:18,158,231,-1,67:7,1" +
"74,67:24,-1,67,-1,67:5,12,67:14,-1,67:2,70:4,67,175,70:4,67,70:5,11,70:6,67" +
":7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,231,228,2" +
"31:30,-1,231,-1,231:18,158,231,-1,67:7,177,67:24,-1,67,-1,67:5,12,67:14,-1," +
"67:2,70:4,67,178,70:4,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3" +
",67,70,67,70:2,67:2,70:2,67,-1,67:7,180,67:24,-1,67,-1,67:5,12,67:14,-1,67:" +
"2,70:4,67,181,70:4,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67" +
",70,67,70:2,67:2,70:2,67,-1,67:32,-1,67,-1,67:5,12,67:8,249,67:5,-1,67:2,70" +
":4,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,7" +
"0,250,67:2,70:2,67,-1,67:14,257,67:17,-1,67,-1,67:5,12,67:14,-1,231:5,237,2" +
"31:26,-1,231,-1,231:20,-1,248,-1:55,67:4,252,67:27,-1,67,-1,67:5,12,67:14,-" +
"1,67:2,70:3,240,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70" +
":3,67,70,67,70:2,67:2,70:2,67,-1,67:2,70:2,253,70,67,70:5,67,70:5,11,70:6,6" +
"7:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1,251,-1:5" +
"5,67:32,-1,67,-1,67:5,12,67:8,254,67:5,-1,67:2,70:2,245,70,67,70:5,67,70:5," +
"11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,-1" +
",67:2,70:4,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67" +
",70,67,70,255,67:2,70:2,67,-1,67:2,70:4,67,70:5,67,70,259,70:3,11,70:6,67:7" +
",-1,67,-1,67:2,70,67:2,12,67,70:3,67,70,67,70:2,67:2,70:2,67,1,256,54:5,3,5" +
"4:25,3,54,4,54:12,3,54:6,3,-1,67:32,-1,67,-1,67:5,12,67:3,263,67:10,-1,67:2" +
",70:4,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:2,265,67," +
"70,67,70:2,67:2,70:2,67,-1,67:7,276,67:24,-1,67,-1,67:5,12,67:14,-1,67:2,70" +
":4,67,277,70:4,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70," +
"67,70:2,67:2,70:2,67,-1,67:3,267,67:28,-1,67,-1,67:5,12,67:14,-1,67:2,70,26" +
"9,70:2,67,70:5,67,70:5,11,70:6,67:7,-1,67,-1,67:2,70,67:2,12,67,70:3,67,70," +
"67,70:2,67:2,70:2,67");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException, 
StruktorException

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

return (new Symbol(Lsym.EOF));
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
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -3:
						break;
					case 3:
						{ /* ignore white space. */ }
					case -4:
						break;
					case 4:
						{ lineCounter++; }
					case -5:
						break;
					case 5:
						{Tracer.out(yytext()); }
					case -6:
						break;
					case 6:
						{Tracer.out(yytext());
                        return new Symbol(Lsym.ADMSG, new String(yytext().substring(6, yytext().length())));}
					case -7:
						break;
					case 7:
						{ yybegin(YYINITIAL); return new Symbol(Lsym.ENDSTRUK); }
					case -8:
						break;
					case 8:
						{ yybegin(STRUK); return new Symbol(Lsym.STARTSTRUK); }
					case -9:
						break;
					case 9:
						{ yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.COMMAND,new String(""));}
					case -10:
						break;
					case 10:
						{ Tracer.out(yytext());
                         int i = ((Integer)stack.pop()).intValue();
                         // Spezialfall do{}while(); <----- ;!!!
                         if (i == Lsym.DO)
                            yybegin(DOWHILE);
                         else
                            yybegin(STRUK);
                         return new Symbol(Lsym.RSPAR); }
					case -11:
						break;
					case 11:
						{ yybegin(DECSTART);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.STRUKNAME, new String(yytext().substring(0,yytext().length()-1))); }
					case -12:
						break;
					case 12:
						{ yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.COMMAND,new String(yytext()));}
					case -13:
						break;
					case 13:
						{ Tracer.out(yytext());
                         yybegin(BEGIN);
                         lastControl = Lsym.DO;
                         return new Symbol(Lsym.DO); }
					case -14:
						break;
					case 14:
						{ Tracer.out(yytext());
                         lastControl = Lsym.IF;
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.IF); }
					case -15:
						break;
					case 15:
						{ Tracer.out(yytext());
                         lastControl = Lsym.ELSE;
                         yybegin(BEGIN);
                         return new Symbol(Lsym.ELSE); }
					case -16:
						break;
					case 16:
						{ Tracer.out(yytext());
                         tempCond=yytext().substring(4,yytext().length()-1);
                         yybegin(FOR);
                         return new Symbol(Lsym.FOR); }
					case -17:
						break;
					case 17:
						{ Tracer.out(yytext());
                         lastControl = Lsym.WHILE;
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.WHILE); }
					case -18:
						break;
					case 18:
						{ Tracer.out(yytext());
                         lastControl = Lsym.SWITCH;
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.SWITCH); }
					case -19:
						break;
					case 19:
						{ yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.BREAK,new Integer(1));}
					case -20:
						break;
					case 20:
						{ yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.CASE,new String(yytext().substring(4,yytext().length()-1) ));}
					case -21:
						break;
					case 21:
						{ yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.DEFAULT);}
					case -22:
						break;
					case 22:
						{ yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.INPUT,new String(yytext().substring(7,yytext().length()) ));}
					case -23:
						break;
					case 23:
						{ yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.OUTPUT,new String(yytext().substring(7,yytext().length()) ));}
					case -24:
						break;
					case 24:
						{ Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.INTEGER, new Integer(yytext())); }
					case -25:
						break;
					case 25:
						{ Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.DOUBLE, new Double(yytext())); }
					case -26:
						break;
					case 26:
						{ Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.STRING, new String(yytext().substring(1,yytext().length() - 1)));   }
					case -27:
						break;
					case 27:
						{ Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.STRING, new String(yytext().substring(1,yytext().length() - 1)));   }
					case -28:
						break;
					case 28:
						{ Tracer.out(yytext());
                         return new Symbol(Lsym.INTEGER, new Integer(yytext())); }
					case -29:
						break;
					case 29:
						{ Tracer.out(yytext());
                         return new Symbol(Lsym.VARNAME, new String(yytext())); }
					case -30:
						break;
					case 30:
						{ Tracer.out(yytext());
                         return new Symbol(Lsym.RPAR); }
					case -31:
						break;
					case 31:
						{ Tracer.out(yytext());
                         return new Symbol(Lsym.POINTER);}
					case -32:
						break;
					case 32:
						{ Tracer.out(yytext());
                         return new Symbol(Lsym.LEPAR); }
					case -33:
						break;
					case 33:
						{ Tracer.out(yytext());
                         return new Symbol(Lsym.REPAR); }
					case -34:
						break;
					case 34:
						{ Tracer.out(yytext());
                         yybegin(VALUE); return new Symbol(Lsym.ASSIGN); }
					case -35:
						break;
					case 35:
						{ Tracer.out(yytext());
                         stack.push(new Integer(lastControl));
                         yybegin(DECSTART);
                         return new Symbol(Lsym.COMMA); }
					case -36:
						break;
					case 36:
						{ yybegin(DECSTART);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.SEMI); }
					case -37:
						break;
					case 37:
						{ Tracer.out(yytext());
                         stack.push(new Integer(lastControl));
                         return new Symbol(Lsym.LSPAR); }
					case -38:
						break;
					case 38:
						{ Tracer.out(yytext());
                         yybegin(STRUK);}
					case -39:
						break;
					case 39:
						{ Tracer.out(yytext());
                         yybegin(DECSTART); }
					case -40:
						break;
					case 40:
						{
                         Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.RPAR);}
					case -41:
						break;
					case 41:
						{ Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.VARTYPE, new Integer(INTEGER));}
					case -42:
						break;
					case 42:
						{ Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.VARTYPE, new Integer(CHARACTER));}
					case -43:
						break;
					case 43:
						{ Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.VARTYPE, new Integer(DOUBLE));}
					case -44:
						break;
					case 44:
						{
                         Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.VARTYPE, new Integer(STRING));}
					case -45:
						break;
					case 45:
						{ /* extra für Do-While(bla); */
                         Tracer.out(yytext());
                         yybegin(STRUK);
                         return new Symbol(Lsym.SEMI); }
					case -46:
						break;
					case 46:
						{ Tracer.out(yytext());
                         stack.push(new Integer(lastControl));
                         yybegin(STRUK);
                         return new Symbol(Lsym.LSPAR); }
					case -47:
						break;
					case 47:
						{ tempCond = "";
                         Tracer.out(yytext());
                         yybegin(CONDITION); }
					case -48:
						break;
					case 48:
						{ tempCond = tempCond + yytext();}
					case -49:
						break;
					case 49:
						{ tempCond = tempCond + yytext();
                         paraCount++;}
					case -50:
						break;
					case 50:
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
					case -51:
						break;
					case 51:
						{ Tracer.out(yytext());
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.WHILE); }
					case -52:
						break;
					case 52:
						{ // matcht immer !!
                         lastControl = Lsym.FOR;
                         yybegin(BEGIN);
                         return new Symbol(Lsym.CONDITION,tempCond); }
					case -53:
						break;
					case 54:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -54:
						break;
					case 55:
						{Tracer.out(yytext()); }
					case -55:
						break;
					case 56:
						{ yybegin(DECSTART);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.STRUKNAME, new String(yytext().substring(0,yytext().length()-1))); }
					case -56:
						break;
					case 57:
						{ yybegin(STRUK);
                         Tracer.out(yytext());
                         return new Symbol(Lsym.COMMAND,new String(yytext()));}
					case -57:
						break;
					case 58:
						{ Tracer.out(yytext());
                         yybegin(BEGIN);
                         lastControl = Lsym.DO;
                         return new Symbol(Lsym.DO); }
					case -58:
						break;
					case 59:
						{ Tracer.out(yytext());
                         lastControl = Lsym.IF;
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.IF); }
					case -59:
						break;
					case 60:
						{ Tracer.out(yytext());
                         lastControl = Lsym.WHILE;
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.WHILE); }
					case -60:
						break;
					case 61:
						{ Tracer.out(yytext());
                         lastControl = Lsym.SWITCH;
                         yybegin(CONDITIONPARA);
                         return new Symbol(Lsym.SWITCH); }
					case -61:
						break;
					case 62:
						{ Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.DOUBLE, new Double(yytext())); }
					case -62:
						break;
					case 63:
						{ Tracer.out(yytext());
                         yybegin(DEC);
                         return new Symbol(Lsym.STRING, new String(yytext().substring(1,yytext().length() - 1)));   }
					case -63:
						break;
					case 64:
						{ Tracer.out(yytext());
                         return new Symbol(Lsym.INTEGER, new Integer(yytext())); }
					case -64:
						break;
					case 65:
						{ tempCond = tempCond + yytext();}
					case -65:
						break;
					case 66:
						{ // matcht immer !!
                         lastControl = Lsym.FOR;
                         yybegin(BEGIN);
                         return new Symbol(Lsym.CONDITION,tempCond); }
					case -66:
						break;
					case 68:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -67:
						break;
					case 69:
						{Tracer.out(yytext()); }
					case -68:
						break;
					case 71:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -69:
						break;
					case 72:
						{Tracer.out(yytext()); }
					case -70:
						break;
					case 74:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -71:
						break;
					case 75:
						{Tracer.out(yytext()); }
					case -72:
						break;
					case 77:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -73:
						break;
					case 78:
						{Tracer.out(yytext()); }
					case -74:
						break;
					case 80:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -75:
						break;
					case 82:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -76:
						break;
					case 84:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -77:
						break;
					case 86:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -78:
						break;
					case 88:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -79:
						break;
					case 90:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -80:
						break;
					case 92:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -81:
						break;
					case 94:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -82:
						break;
					case 96:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -83:
						break;
					case 131:
						{Tracer.out(yytext()); }
					case -84:
						break;
					case 134:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -85:
						break;
					case 135:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -86:
						break;
					case 136:
						{Tracer.out(yytext()); }
					case -87:
						break;
					case 139:
						{Tracer.out(yytext()); }
					case -88:
						break;
					case 142:
						{Tracer.out(yytext()); }
					case -89:
						break;
					case 145:
						{Tracer.out(yytext()); }
					case -90:
						break;
					case 158:
						{Tracer.out(yytext()); }
					case -91:
						break;
					case 160:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -92:
						break;
					case 162:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -93:
						break;
					case 163:
						{Tracer.out(yytext()); }
					case -94:
						break;
					case 165:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -95:
						break;
					case 167:
						{Tracer.out(yytext()); }
					case -96:
						break;
					case 170:
						{Tracer.out(yytext()); }
					case -97:
						break;
					case 173:
						{Tracer.out(yytext()); }
					case -98:
						break;
					case 176:
						{Tracer.out(yytext()); }
					case -99:
						break;
					case 179:
						{Tracer.out(yytext()); }
					case -100:
						break;
					case 182:
						{Tracer.out(yytext()); }
					case -101:
						break;
					case 183:
						{Tracer.out(yytext()); }
					case -102:
						break;
					case 184:
						{Tracer.out(yytext()); }
					case -103:
						break;
					case 185:
						{Tracer.out(yytext()); }
					case -104:
						break;
					case 186:
						{Tracer.out(yytext()); }
					case -105:
						break;
					case 187:
						{Tracer.out(yytext()); }
					case -106:
						break;
					case 188:
						{Tracer.out(yytext()); }
					case -107:
						break;
					case 189:
						{Tracer.out(yytext()); }
					case -108:
						break;
					case 190:
						{Tracer.out(yytext()); }
					case -109:
						break;
					case 191:
						{Tracer.out(yytext()); }
					case -110:
						break;
					case 192:
						{Tracer.out(yytext()); }
					case -111:
						break;
					case 193:
						{Tracer.out(yytext()); }
					case -112:
						break;
					case 194:
						{Tracer.out(yytext()); }
					case -113:
						break;
					case 195:
						{Tracer.out(yytext()); }
					case -114:
						break;
					case 196:
						{Tracer.out(yytext()); }
					case -115:
						break;
					case 197:
						{Tracer.out(yytext()); }
					case -116:
						break;
					case 198:
						{Tracer.out(yytext()); }
					case -117:
						break;
					case 199:
						{Tracer.out(yytext()); }
					case -118:
						break;
					case 200:
						{Tracer.out(yytext()); }
					case -119:
						break;
					case 201:
						{Tracer.out(yytext()); }
					case -120:
						break;
					case 202:
						{Tracer.out(yytext()); }
					case -121:
						break;
					case 203:
						{Tracer.out(yytext()); }
					case -122:
						break;
					case 204:
						{Tracer.out(yytext()); }
					case -123:
						break;
					case 205:
						{Tracer.out(yytext()); }
					case -124:
						break;
					case 206:
						{Tracer.out(yytext()); }
					case -125:
						break;
					case 207:
						{Tracer.out(yytext()); }
					case -126:
						break;
					case 208:
						{Tracer.out(yytext()); }
					case -127:
						break;
					case 209:
						{Tracer.out(yytext()); }
					case -128:
						break;
					case 210:
						{Tracer.out(yytext()); }
					case -129:
						break;
					case 211:
						{Tracer.out(yytext()); }
					case -130:
						break;
					case 212:
						{Tracer.out(yytext()); }
					case -131:
						break;
					case 213:
						{Tracer.out(yytext()); }
					case -132:
						break;
					case 214:
						{Tracer.out(yytext()); }
					case -133:
						break;
					case 215:
						{Tracer.out(yytext()); }
					case -134:
						break;
					case 216:
						{Tracer.out(yytext()); }
					case -135:
						break;
					case 217:
						{Tracer.out(yytext()); }
					case -136:
						break;
					case 218:
						{Tracer.out(yytext()); }
					case -137:
						break;
					case 219:
						{Tracer.out(yytext()); }
					case -138:
						break;
					case 220:
						{Tracer.out(yytext()); }
					case -139:
						break;
					case 221:
						{Tracer.out(yytext()); }
					case -140:
						break;
					case 222:
						{Tracer.out(yytext()); }
					case -141:
						break;
					case 223:
						{Tracer.out(yytext()); }
					case -142:
						break;
					case 224:
						{Tracer.out(yytext()); }
					case -143:
						break;
					case 225:
						{Tracer.out(yytext()); }
					case -144:
						break;
					case 226:
						{Tracer.out(yytext()); }
					case -145:
						break;
					case 227:
						{Tracer.out(yytext()); }
					case -146:
						break;
					case 228:
						{Tracer.out(yytext()); }
					case -147:
						break;
					case 229:
						{Tracer.out(yytext()); }
					case -148:
						break;
					case 230:
						{Tracer.out(yytext()); }
					case -149:
						break;
					case 231:
						{Tracer.out(yytext()); }
					case -150:
						break;
					case 233:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -151:
						break;
					case 235:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -152:
						break;
					case 236:
						{Tracer.out(yytext()); }
					case -153:
						break;
					case 237:
						{Tracer.out(yytext()); }
					case -154:
						break;
					case 238:
						{Tracer.out(yytext()); }
					case -155:
						break;
					case 241:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -156:
						break;
					case 242:
						{Tracer.out(yytext()); }
					case -157:
						break;
					case 243:
						{Tracer.out(yytext()); }
					case -158:
						break;
					case 246:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -159:
						break;
					case 247:
						{Tracer.out(yytext()); }
					case -160:
						break;
					case 248:
						{Tracer.out(yytext()); }
					case -161:
						break;
					case 251:
						{Tracer.out(yytext()); }
					case -162:
						break;
					case 256:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -163:
						break;
					case 258:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -164:
						break;
					case 260:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -165:
						break;
					case 261:
						{Tracer.out(yytext()); }
					case -166:
						break;
					case 262:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -167:
						break;
					case 264:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -168:
						break;
					case 266:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -169:
						break;
					case 268:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -170:
						break;
					case 270:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -171:
						break;
					case 274:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -172:
						break;
					case 275:
						{ throw new StruktorException("Illegal character: \""+yytext()+"\" in Line "+lineCounter); }
					case -173:
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
