// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

import struktor.Presets;
import struktor.Struktor;
import struktor.util.gifencoder.GIFEncoder;


public class StruktogrammView extends JLayeredPane
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Object Variables
	Struktor struktor;
	public Presets presets;
	Struktogramm model;
		
	public StruktogrammView(Struktogramm struktogramm, Struktor struktor)
	{
		this.struktor = struktor;
		model = struktogramm;
		presets = struktor.presets;
		setVisible(true);
		if (Struktor.isApplet)
			if (struktor.presets.enabTitle)
				setBorder(BorderFactory.createTitledBorder(model.getName()));
	}
	

	/** Zeichnet das Struktogramm rekursiv neu
	 * @param   g  
	 */
	public void paintComponent(Graphics g)
	{
			model.recalculate();
			super.paintComponent(g);
	}
	
	public void saveAsImage()
	{
		repaint();
		Image i = createImage(getWidth(),getHeight());
		paint(i.getGraphics());

		// encode the image as a GIF
		GIFEncoder encode;
		try {
			encode = new GIFEncoder(i);
		} catch (AWTException e) {
			throw new RuntimeException();
		}
		OutputStream output;
		try {
			output = new BufferedOutputStream(
			    new FileOutputStream(model.getName()+".gif"));
			encode.Write(output);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
	}
}
