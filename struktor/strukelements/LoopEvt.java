// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.event.MouseEvent;

class LoopEvt extends StrukElementEvt
{



	LoopEvt(Loop l, Struktogramm sg)
	{
		super(sg);
		male=l;
	}

	
	public void mousePressed(MouseEvent e) {
     super.mousePressed(e);
	 e.consume();
     
	}
	
	boolean mouseInsideElement(MouseEvent e)
	{
		Loop male = ((Loop)(this.male));
		boolean temp;
		temp = super.mouseInsideElement(e);
		if (temp)
		{
			// Oder nur inside der Schleife ?
			if ((male.getX()+e.getX())>male.insidex() && (male.getY()+e.getY())>male.insidey())
				return false;
		}
		return temp;
	}


}