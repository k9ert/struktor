package struktor.util.gifencoder;

class LZWStringTable {
    private final static int RES_CODES = 2;
    private final static short HASH_FREE = (short)0xFFFF;
    private final static short NEXT_FIRST = (short)0xFFFF;
    private final static int MAXBITS = 12;
    private final static int MAXSTR = (1 << MAXBITS);
    private final static short HASHSIZE = 9973;
    private final static short HASHSTEP = 2039;

    byte strChr_[];
    short strNxt_[];
    short strHsh_[];
    short numStrings_;

    public LZWStringTable() {
	strChr_ = new byte[MAXSTR];
	strNxt_ = new short[MAXSTR];
	strHsh_ = new short[HASHSIZE];    
    }

    public int AddCharString(short index, byte b) {
	int hshidx;

	if (numStrings_ >= MAXSTR)
	    return 0xFFFF;
	
	hshidx = Hash(index, b);
	while (strHsh_[hshidx] != HASH_FREE)
	    hshidx = (hshidx + HASHSTEP) % HASHSIZE;
	
	strHsh_[hshidx] = numStrings_;
	strChr_[numStrings_] = b;
	strNxt_[numStrings_] = (index != HASH_FREE) ? index : NEXT_FIRST;

	return numStrings_++;
    }
    
    public short FindCharString(short index, byte b) {
	int hshidx, nxtidx;

	if (index == HASH_FREE)
	    return b;

	hshidx = Hash(index, b);
	while ((nxtidx = strHsh_[hshidx]) != HASH_FREE) {
	    if (strNxt_[nxtidx] == index && strChr_[nxtidx] == b)
		return (short)nxtidx;
	    hshidx = (hshidx + HASHSTEP) % HASHSIZE;
	}

	return (short)0xFFFF;
    }

    public void ClearTable(int codesize) {
	numStrings_ = 0;
	
	for (int q = 0; q < HASHSIZE; q++) {
	    strHsh_[q] = HASH_FREE;
	}

	int w = (1 << codesize) + RES_CODES;
	for (int q = 0; q < w; q++)
	    AddCharString((short)0xFFFF, (byte)q);
    }
    
    static public int Hash(short index, byte lastbyte) {
	return ((int)((short)(lastbyte << 8) ^ index) & 0xFFFF) % HASHSIZE;
    }
}

