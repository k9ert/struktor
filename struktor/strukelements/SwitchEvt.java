// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import struktor.processor.*;

import java.awt.event.*;

class SwitchEvt extends StrukElementEvt
{
	private boolean resizeRelation = false;
	SwitchEvt(Switch c, Struktogramm sg)
	{
		super(sg);
		male=c;
	}
		
	// Event-Handling
	

	private void relationPointPressed(MouseEvent e)
	{
		resizeRelation = true;
		male.addMouseMotionListener(this);
	}

	public void mousePressed(MouseEvent e) {
		switch (mouseOnCorner(e))
		{
			case 23:
				relationPointPressed(e);
			break;
		}
	  super.mousePressed(e); 
	  e.consume();
	}
	
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);   
	}
	
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		resizeRelation = false;
	} 
}