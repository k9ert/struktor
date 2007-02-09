package struktor.util.gifencoder;

import java.io.IOException;
import java.io.OutputStream;

class BitFile {
    OutputStream output_;
    byte buffer_[];
    int index_, bitsLeft_;

    public BitFile(OutputStream output) {
	output_ = output;
	buffer_ = new byte[256];
	index_ = 0;
	bitsLeft_ = 8;
    }

    public void Flush() throws IOException {
	int numBytes = index_ + (bitsLeft_ == 8 ? 0 : 1);
	if (numBytes > 0) {
	    output_.write(numBytes);
	    output_.write(buffer_, 0, numBytes);
	    buffer_[0] = 0;
	    index_ = 0;
	    bitsLeft_ = 8;
	}
    }

    public void WriteBits(int bits, int numbits) throws IOException {
	int bitsWritten = 0;
	int numBytes = 255;
	do {
	    if ((index_ == 254 && bitsLeft_ == 0) || index_ > 254) {
		output_.write(numBytes);
		output_.write(buffer_, 0, numBytes);

		buffer_[0] = 0;
		index_ = 0;
		bitsLeft_ = 8;
	    }

	    if (numbits <= bitsLeft_) {
		buffer_[index_] |= (bits & ((1 << numbits) - 1)) <<
		    (8 - bitsLeft_);
		bitsWritten += numbits;
		bitsLeft_ -= numbits;
		numbits = 0;
	    }
	    else {
		buffer_[index_] |= (bits & ((1 << bitsLeft_) - 1)) <<
		    (8 - bitsLeft_);
		bitsWritten += bitsLeft_;
		bits >>= bitsLeft_;
		numbits -= bitsLeft_;
		buffer_[++index_] = 0;
		bitsLeft_ = 8;
	    }
	} while (numbits != 0);
    }
}
