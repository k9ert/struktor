package struktor.util.gifencoder;

import java.io.IOException;
import java.io.OutputStream;

class LZWCompressor {

    public static void LZWCompress(OutputStream output, int codesize,
				   byte toCompress[]) throws IOException {
	byte c;
	short index;
	int clearcode, endofinfo, numbits, limit, errcode;
	short prefix = (short)0xFFFF;

	BitFile bitFile = new BitFile(output);
	LZWStringTable strings = new LZWStringTable();

	clearcode = 1 << codesize;
	endofinfo = clearcode + 1;
    
	numbits = codesize + 1;
	limit = (1 << numbits) - 1;
	
	strings.ClearTable(codesize);
	bitFile.WriteBits(clearcode, numbits);

	for (int loop = 0; loop < toCompress.length; ++loop) {
	    c = toCompress[loop];
	    if ((index = strings.FindCharString(prefix, c)) != -1)
		prefix = index;
	    else {
		bitFile.WriteBits(prefix, numbits);
		if (strings.AddCharString(prefix, c) > limit) {
		    if (++numbits > 12) {
			bitFile.WriteBits(clearcode, numbits - 1);
			strings.ClearTable(codesize);
			numbits = codesize + 1;
		    }
		    limit = (1 << numbits) - 1;
		}
		
		prefix = (short)((short)c & 0xFF);
	    }
	}
	
	if (prefix != -1)
	    bitFile.WriteBits(prefix, numbits);
	
	bitFile.WriteBits(endofinfo, numbits);
	bitFile.Flush();
    }
}

