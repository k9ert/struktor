package struktor.util.gifencoder;

import java.io.IOException;
import java.io.OutputStream;

class ScreenDescriptor {
    public short localScreenWidth_, localScreenHeight_;
    private byte byte_;
    public byte backgroundColorIndex_, pixelAspectRatio_;

    public ScreenDescriptor(short width, short height, int numColors) {
	localScreenWidth_ = width;
	localScreenHeight_ = height;
	SetGlobalColorTableSize((byte)(BitUtils.BitsNeeded(numColors) - 1));
	SetGlobalColorTableFlag((byte)1);
	SetSortFlag((byte)0);
	SetColorResolution((byte)7);
	backgroundColorIndex_ = 0;
	pixelAspectRatio_ = 0;
    }

    public void Write(OutputStream output) throws IOException {
	BitUtils.WriteWord(output, localScreenWidth_);
	BitUtils.WriteWord(output, localScreenHeight_);
	output.write(byte_);
	output.write(backgroundColorIndex_);
	output.write(pixelAspectRatio_);
    }

    public void SetGlobalColorTableSize(byte num) {
	byte_ |= (num & 7);
    }

    public void SetSortFlag(byte num) {
	byte_ |= (num & 1) << 3;
    }

    public void SetColorResolution(byte num) {
	byte_ |= (num & 7) << 4;
    }
    
    public void SetGlobalColorTableFlag(byte num) {
	byte_ |= (num & 1) << 7;
    }
}
