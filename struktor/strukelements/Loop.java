// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import struktor.processor.*;
import java.awt.*;

/** Die abstrakte Klasse für alle Loops (Tail-, Head-, For-) Unglaublich wieviel man da reinschreiben kann :-)
 */
abstract class Loop extends StrukElement 
implements Constants
{

	StrukElement inside=null;

	Loop(Struktogramm s, String label)
	{
	 	super(s, label);
		mEvt = new LoopEvt(this, struktogramm);
	 	addMouseListener(mEvt);
	}
	
	void recalculate()
	{
		getInside().recalculate();
		calcInside();
		try { 
			setHeight(getVerHeight()+getInside().calcHeight());
		} catch (ResizeException re) {}
		super.recalculate();
	}
		
	
	void setInside(StrukElement inside)
	{
       if (inside!=null)
          inside.delConnection();
       if (this.inside!=null)
          this.inside.delConnection();
       this.inside=inside;
       if (this.inside!=null)
	   		inside.setUpConnection(this, INSIDE);
	}
	
	StrukElement getInside()
	{
	       return inside;
	}
		
	void deleteAll()
	{
		StrukElement saveNext = null;
		StrukElement next = getInside();
		while (next != null)
		{
			saveNext = next.getNext();
			next.deleteAll();
			next = saveNext;
		}
		if (getInside()!=null) // Kann vorkommen weil beim rekursiven löschen auch wieder Elemente produziert werden können (siehe z.b unten)
			getInside().delete(true);
		if (getUpConnectionType() != NEXT) // Hier könnte wieder ein Element entstehen (nötig da sonst Löcher entstehen)
			connect(getUpConnectionType(), getUpConnection(), struktogramm.addElement(COMMAND));
		delete(true);
	}
	
	void delKeepInside()
	{
		StrukElement inside = getInside();
		connect(getUpConnectionType(),getUpConnection(), inside);
		connect(NEXT, getLastOfBlock(inside), getNext());
		delete(true);
	}

	// Ab Hier Grafik + Events

	int insidex(){
       return getX()+getHorWidth();
	}

	abstract int insidey();
		
	private int insidemaxwidth(){
	       return getSize().width-getHorWidth();
	}
	int getHorWidth() {
	       return (getWidth()/10 > 40 ? 40 : (getWidth()/10 < 20 ? 20 : getWidth()/10));
	}
	int getVerHeight() {
	       return getHorWidth();
	}
	
	void heightSet(int connectionType)
	throws ResizeException
	{
		if (connectionType == INSIDE)
			calcHeight();
		super.heightSet(this.getUpConnectionType());
	}
	
	/** Die Höhe einer Loop zu verändern ist nicht erlaubt
	 * @param   height  
	 */
	void setHeightByUser(int height)
	{
		// Nix da ! Da wird vom Benutzer nix verändert !
	}
	
	void tare()
	{
		// Das lassen wir auch lieber ...
	}

	void calcInside(){
       if (inside!=null)
       {
        inside.setX(this.insidex());
        inside.setY(this.insidey());
        inside.setWidth(this.insidemaxwidth());
       }
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.black);
		super.paintComponent(g);
	}
	
	void save(struktor.Save saveObject)
	{ 
		boolean printPara = blockPrint(getInside());
		
		if (printPara)
			saveObject.println("{");
		saveObject.addTab(1);
		getInside().save(saveObject);
		saveObject.addTab(-1);
		if (printPara)
			saveObject.println("}");
	}
}
