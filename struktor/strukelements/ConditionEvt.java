// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import struktor.processor.*;

import java.awt.event.*;

class ConditionEvt extends StrukElementEvt
{
	boolean resizeRelation = false;
	ConditionEvt(Condition c, Struktogramm sg)
	{
		super(sg);
		male=c;
	}
		
	// Event-Handling
	int mouseOnCorner(MouseEvent e)
	{
		 Condition male = (Condition)(this.male);
	     // RelationChange Point at half bottom Edge
		 if(Math.abs(e.getX()-male.getWidth()*male.getRelation()) < sensible && (Math.abs(e.getY()-male.getHeight()) < sensible))
	         return 23;
	     return super.mouseOnCorner(e);
	}

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
	
	  	Condition male = (Condition)this.male;
	  	if (resizeRelation == true)
		{
			male.setRelation((double)(e.getX())/(double)male.getWidth());
		}
		super.mouseDragged(e);   
	}
	
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		resizeRelation = false;
	} 
}