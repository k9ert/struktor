// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import struktor.Struktor;
import struktor.strukelements.Struktogramm;
import struktor.strukelements.StruktogrammEvt;

/** Eine Klasse um die Tasteneingaben entsprechend weiterzugeben
 */

class KeyPresses extends KeyAdapter
{
	Struktor struktor;
	int lookAndFeel=0;
	StruktogrammEvt sEvt;
	struktor.gui.StruktorEvt strukEvt;
	
	KeyPresses(Struktor struktor, StruktogrammEvt sEvt, StruktorEvt strukEvt)
	{
		this.struktor = struktor;
		this.sEvt = sEvt;
		this.strukEvt = strukEvt;
		
	}
	
	public void keyPressed(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) 
		{
			Struktogramm.stopStruktogramm();
			sEvt.strukStopped();
		}
		if (event.getKeyCode() == KeyEvent.VK_F9) 
			changeLookAndFeel();
		if (event.getKeyCode() == KeyEvent.VK_F8) 
			strukEvt.showLicenseInfo();	
		if (event.getKeyCode() == KeyEvent.VK_F2) 
			if (!Struktor.isApplet)
				struktor.presets.showDialog();		
	}
	
	void changeLookAndFeel()
	{
		lookAndFeel++;
		if (lookAndFeel > 2)
			lookAndFeel = 0;
		try {
			String plaf = "unknown";
			switch (lookAndFeel)
			{
			case 0:
				plaf = "javax.swing.plaf.metal.MetalLookAndFeel";
				break;
			case 1:
				plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
				break;
			case 2:
				plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				break;
			}
			//LAF umschalten
			UIManager.setLookAndFeel(plaf);
			struktor.updateLookAndFeel();
		} catch (UnsupportedLookAndFeelException e) {
		System.err.println(e.toString());
		} catch (ClassNotFoundException e) {
		System.err.println(e.toString());
		} catch (InstantiationException e) {
		System.err.println(e.toString());
		} catch (IllegalAccessException e) {
		System.err.println(e.toString());
		}
	}
}
	