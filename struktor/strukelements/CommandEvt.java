// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import struktor.processor.*;
import java.awt.event.*;

class CommandEvt extends StrukElementEvt
{
	
	CommandEvt(Command c, Struktogramm sg)
	{
		super(sg);
		male=c;
	}
		
	public void mousePressed(MouseEvent e) {
       super.mousePressed(e);
       e.consume();
	}
	
	int mouseOnCorner(MouseEvent e)
	{
		// Right Bottom Corner (Eventuell gleich Right Top Corner eines anderen Elementes ! )
		if( (Math.abs(e.getX()-male.getX()-male.getWidth()+10) <= sensible) && (Math.abs(e.getY()-male.getY()-male.getHeight()+10) <= sensible) )
		  	return 3;
		int i=0;
		i=super.mouseOnCorner(e);
		if (i!=0)
			return i;
		return 0;
	}
}