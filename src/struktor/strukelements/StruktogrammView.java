// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

import struktor.Presets;
import struktor.Struktor;
import struktor.util.BMPFile;


public class StruktogrammView extends JLayeredPane
{

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
		BMPFile file = new BMPFile();
		file.saveBitmap(model.getName()+".bmp",i,getWidth(),getHeight());
	}
}