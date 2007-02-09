package struktor.util.gifencoder;
/*
 * @(#)GIFEncoder.java    0.90 4/21/96 Adam Doppelt
 */
import java.io.*;
import java.awt.*;
import java.awt.image.*;

/**
 * GIFEncoder is a class which takes an image and saves it to a stream
 * using the GIF file format (<A
 * HREF="http://www.dcs.ed.ac.uk/%7Emxr/gfx/">Graphics Interchange
 * Format</A>). A GIFEncoder
 * is constructed with either an AWT Image (which must be fully
 * loaded) or a set of RGB arrays. The image can be written out with a
 * call to <CODE>Write</CODE>.<P>
 *
 * Three caveats:
 * <UL>
 *   <LI>GIFEncoder will convert the image to indexed color upon
 *   construction. This will take some time, depending on the size of
 *   the image. Also, actually writing the image out (Write) will take
 *   time.<P>
 *
 *   <LI>The image cannot have more than 256 colors, since GIF is an 8
 *   bit format. For a 24 bit to 8 bit quantization algorithm, see
 *   Graphics Gems II III.2 by Xialoin Wu. Or check out his <A
 *   HREF="http://www.csd.uwo.ca/faculty/wu/cq.c">C source</A>.<P>
 *
 *   <LI>Since the image must be completely loaded into memory,
 *   GIFEncoder may have problems with large images. Attempting to
 *   encode an image which will not fit into memory will probably
 *   result in the following exception:<P>
 *   <CODE>java.awt.AWTException: Grabber returned false: 192</CODE><P>
 * </UL><P>
 *
 * GIFEncoder is based upon gifsave.c, which was written and released
 * by:<P>
 * <CENTER>
 *                                  Sverre H. Huseby<BR>
 *                                   Bjoelsengt. 17<BR>
 *                                     N-0468 Oslo<BR>
 *                                       Norway<P>
 *
 *                                 Phone: +47 2 230539<BR>
 *                                 sverrehu@ifi.uio.no<P>
 * </CENTER>
 * @version 0.90 21 Apr 1996
 * @author <A HREF="http://www.cs.brown.edu/people/amd/">Adam Doppelt</A> */
public class GIFEncoder {
    short width_, height_;
    int numColors_;
    byte pixels_[], colors_[];
    
    ScreenDescriptor sd_;
    ImageDescriptor id_;
    
/**
 * Construct a GIFEncoder. The constructor will convert the image to
 * an indexed color array. <B>This may take some time.</B><P>
 * 
 * @param image The image to encode. The image <B>must</B> be
 * completely loaded.
 * @exception AWTException Will be thrown if the pixel grab fails. This
 * can happen if Java runs out of memory. It may also indicate that the image
 * contains more than 256 colors.
 * */
    public GIFEncoder(Image image) throws AWTException {
	width_ = (short)image.getWidth(null);
	height_ = (short)image.getHeight(null);

	int values[] = new int[width_ * height_];
	PixelGrabber grabber = new PixelGrabber(
	    image, 0, 0, width_, height_, values, 0, width_);
	
	try {
	    if(grabber.grabPixels() != true)
		throw new AWTException("Grabber returned false: " +
				       grabber.status());
	}
	catch (InterruptedException e) { ; }
	
	byte r[][] = new byte[width_][height_];
	byte g[][] = new byte[width_][height_];
	byte b[][] = new byte[width_][height_];
	int index = 0;
	for (int y = 0; y < height_; ++y)
	    for (int x = 0; x < width_; ++x) {
		r[x][y] = (byte)((values[index] >> 16) & 0xFF);
		g[x][y] = (byte)((values[index] >> 8) & 0xFF);
		b[x][y] = (byte)((values[index]) & 0xFF);  
		++index;
	    }
	ToIndexedColor(r, g, b);
    }

/**
 * Construct a GIFEncoder. The constructor will convert the image to
 * an indexed color array. <B>This may take some time.</B><P>
 *
 * Each array stores intensity values for the image. In other words,
 * r[x][y] refers to the red intensity of the pixel at column x, row
 * y.<P>
 *
 * @param r An array containing the red intensity values.
 * @param g An array containing the green intensity values.
 * @param b An array containing the blue intensity values.
 *
 * @exception AWTException Will be thrown if the image contains more than
 * 256 colors.
 * */
    public GIFEncoder(byte r[][], byte g[][], byte b[][]) throws AWTException {
	width_ = (short)(r.length);
	height_ = (short)(r[0].length);

	ToIndexedColor(r, g, b);
    }

/**
 * Writes the image out to a stream in the GIF file format. This will
 * be a single GIF87a image, non-interlaced, with no background color.
 * <B>This may take some time.</B><P>
 *
 * @param output The stream to output to. This should probably be a
 * buffered stream.
 *
 * @exception IOException Will be thrown if a write operation fails.
 * */
    public void Write(OutputStream output) throws IOException {
	BitUtils.WriteString(output, "GIF87a");
	
	ScreenDescriptor sd = new ScreenDescriptor(width_, height_,
						   numColors_);
	sd.Write(output);

	output.write(colors_, 0, colors_.length);

	ImageDescriptor id = new ImageDescriptor(width_, height_, ',');
	id.Write(output);

	byte codesize = BitUtils.BitsNeeded(numColors_);
	if (codesize == 1)
	    ++codesize;
	output.write(codesize);

	LZWCompressor.LZWCompress(output, codesize, pixels_);
	output.write(0);

	id = new ImageDescriptor((byte)0, (byte)0, ';');
	id.Write(output);
	output.flush();
    }

    void ToIndexedColor(byte r[][], byte g[][],
			byte b[][]) throws AWTException {
	pixels_ = new byte[width_ * height_];
	colors_ = new byte[256 * 3];
	int colornum = 0;
	for (int x = 0; x < width_; ++x) {
	    for (int y = 0; y < height_; ++y) {
		int search;
		for (search = 0; search < colornum; ++search)
		    if (colors_[search * 3]     == r[x][y] &&
			colors_[search * 3 + 1] == g[x][y] &&
			colors_[search * 3 + 2] == b[x][y])
			break;
		
		if (search > 255)
		    throw new AWTException("Too many colors.");

		pixels_[y * width_ + x] = (byte)search;
		
		if (search == colornum) {
		    colors_[search * 3]     = r[x][y];
		    colors_[search * 3 + 1] = g[x][y];
		    colors_[search * 3 + 2] = b[x][y];
		    ++colornum;
		}
	    }
	}
	numColors_ = 1 << BitUtils.BitsNeeded(colornum);
	byte copy[] = new byte[numColors_ * 3];
	System.arraycopy(colors_, 0, copy, 0, numColors_ * 3);
	colors_ = copy;
    }
    
}









