package struktor.util.gifencoder;

import java.io.IOException;
import java.io.OutputStream;

class ImageDescriptor {
    public byte separator_;
    public short leftPosition_, topPosition_, width_, height_;
    private byte byte_;

    public ImageDescriptor(short width, short height, char separator) {
	separator_ = (byte)separator;
	leftPosition_ = 0;
	topPosition_ = 0;
	width_ = width;
	height_ = height;
	SetLocalColorTableSize((byte)0);
	SetReserved((byte)0);
	SetSortFlag((byte)0);
	SetInterlaceFlag((byte)0);
	SetLocalColorTableFlag((byte)0);
    }
    
    public void Write(OutputStream output) throws IOException {
	output.write(separator_);
	BitUtils.WriteWord(output, leftPosition_);
	BitUtils.WriteWord(output, topPosition_);
	BitUtils.WriteWord(output, width_);
	BitUtils.WriteWord(output, height_);		
	output.write(byte_);
    }

    public void SetLocalColorTableSize(byte num) {
	byte_ |= (num & 7);
    }

    public void SetReserved(byte num) {
	byte_ |= (num & 3) << 3;
    }

    public void SetSortFlag(byte num) {
	byte_ |= (num & 1) << 5;
    }
    
    public void SetInterlaceFlag(byte num) {
	byte_ |= (num & 1) << 6;
    }

    public void SetLocalColorTableFlag(byte num) {
	byte_ |= (num & 1) << 7;
    }
}
