// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.Color;
import java.awt.Graphics;

import struktor.processor.LoopControlException;
import struktor.processor.Processor;
import struktor.processor.ProcessorException;
import struktor.processor.ReturnException;

/** Eine Klasse für die Condition */
class Condition extends StrukElement
implements CommandTypes, Constants
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Kann leider nicht private gemacht werden! alt1 und alt2 wird EINMAL von StrukElement.delConnection direkt ge�ndert */
	StrukElement alt1=null;
	
	/** Kann leider nicht private gemacht werden! alt1 und alt2 wird EINMAL von StrukElement.delConnection direkt ge�ndert */
	StrukElement alt2=null;
	
	/** Die grafische Relation von True-Part zu False-Part */ 
	private double relation = 0.5;
	
	Condition(Struktogramm s, String label)
	{
		super(s, label);
		mEvt = new ConditionEvt(this, struktogramm);
	 	addMouseListener(mEvt);
	}

	void recalculate()
	{
		calcAlt();
		try {
			calcHeight();
		} catch (ResizeException re) {}
		getAlt1().recalculate();
		getAlt2().recalculate();
		super.recalculate();
	}
	
	void execute(Processor p)
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException
	{
		delayAndPaint(p);
		delayAndPaint(p);
		if (p.parse(getLabel()+";",CALCULATION,"") == 0)
			getAlt2().execute(getAlt2(),p);
		else 
			getAlt1().execute(getAlt1(),p);
	}

	
	void setAlt1(StrukElement alt1)
	{
       if (alt1!=null)
          alt1.delConnection();
       if (this.alt1!=null)
          this.alt1.delConnection();
       this.alt1=alt1;
       if (this.alt1!=null)
       {
          this.alt1.setUpConnection(this,ALT1);
       }
	}

	void setAlt2(StrukElement alt2)
	{
       if (alt2!=null)
          alt2.delConnection();
       if (this.alt2!=null)
          this.alt2.delConnection();
       this.alt2=alt2;
       if (this.alt2!=null)
          this.alt2.setUpConnection(this,ALT2);
	}

	StrukElement getAlt1()
	{
       return alt1;
	}

	StrukElement getAlt2()
	{
	       return alt2;
	}

	void calcNext()
	{
	  
		if (getNext()!=null)
		{
			getNext().setX(getX());
			try {
				if (alt1!=null)
			    	getNext().setY(getY()+getHeight()+alt1.calcHeight());
			  	else
			  		getNext().setY(getY()+getHeight()+50);
		    
				getNext().setWidth(this.getWidth());
			} catch (ResizeException re) {}
		}
	
	}

	void calcAlt()
	{
		if (alt1!=null)
		{
			alt1.setX(this.getX());
			alt1.setY(this.getY()+this.getHeight());
			alt1.setWidth((int)(this.getWidth()*getRelation()));
		}
		if (alt2!=null)
		{
			alt2.setX((int)(this.getX()+this.getWidth()*getRelation())-1);
			alt2.setY(this.getY()+this.getHeight());
			alt2.setWidth(getWidth() % 2 == 0 ? (int)(getWidth()*(1-getRelation()))+1 : (int)(getWidth()*(1-getRelation())+2));
		}
	}

	int calcHeight()
	throws ResizeException
	{
 		int h=getHeight();
		
		int alt1Height=alt1.calcHeight();
		int alt2Height=alt2.calcHeight();
		// links grösser wie rechts ?
		if (alt1Height > alt2Height)
		{
			// Differenz ausrechnen
			int diff = alt1Height-alt2Height;
			// Wenn der Benutzer grad zusammenschiebt
			if (gettingSmaller)
				// links kleiner machen
				makeBlockSmaller(alt1,diff);
			else
				// sonst rechts grösser machen
				makeBlockLarger(alt2,diff);
			h = h + alt1Height;
		}
		// rechts grösser wie links
		else if (alt1Height < alt2Height)
		{
			int diff = alt2Height-alt1Height;
			if (gettingSmaller)
				makeBlockSmaller(alt2,diff);
			else
				makeBlockLarger(alt1,diff);
			
			h = h + alt2Height;
		}
		else // alt1height == alt2Height
		{
			h = h + alt2Height; // (is ja wurscht !)
		}
		if (getNext()!=null)
	 		return (h+getNext().calcHeight());
	 	else
	  		return h;
	}
	
	void heightSet(int connectionType)
	throws ResizeException
	{
		if (connectionType == ALT1 || connectionType == ALT2)
			calcHeight();
		super.heightSet(this.getUpConnectionType());
	}	

	void deleteAll()
	{
		StrukElement saveNext = null;
		StrukElement next = getAlt1();
		while (next != null)
		{
			saveNext = next.getNext();
			next.deleteAll();
			next = saveNext;
		}
		next = getAlt2();
		while (next != null)
		{
			saveNext = next.getNext();
			next.deleteAll();
			next = saveNext;
		}
		if (getAlt1()!=null) // Kann vorkommen weil beim l�schen (siehe z.b unten) auch wieder Elemente produziert werden k�nnen
			getAlt1().delete(true);
		if (getAlt2()!=null)
			getAlt2().delete(true);
		if (getUpConnectionType() != NEXT & hasUpConnection())
			connect(getUpConnectionType(), getUpConnection(), struktogramm.addElement(COMMAND));
		delete(true);
	}
		
	void delKeepTrue()
	{
		StrukElement alt1 = getAlt1();
		connect(getUpConnectionType(),getUpConnection(), alt1);
		connect(NEXT, getLastOfBlock(alt1), getNext());
		StrukElement alt2 = getAlt2();
		StrukElement saveAlt2 = null;
		while (alt2 != null)
		{
			saveAlt2 = alt2.getNext();
			alt2.deleteAll();
			alt2 = saveAlt2;
		}
		delete(true);
	}
	
	void delKeepFalse()
	{
		StrukElement alt2 = getAlt2();
		connect(getUpConnectionType(),getUpConnection(), alt2);
		connect(NEXT, getLastOfBlock(alt2), getNext());
		StrukElement alt1 = getAlt1();
		StrukElement saveAlt1 = null;
		while (alt1 != null)
		{
			saveAlt1 = alt1.getNext();
			alt1.deleteAll();
			alt1 = saveAlt1;
		}
		delete(true);
	}

	public void paintComponent(Graphics g) 
	{
		int width = getSize().width;
		int height = getSize().height;
		int labelWidth = g.getFontMetrics().stringWidth(getLabel());
		int trueWidth = g.getFontMetrics().stringWidth("true");
		int falseWidth = g.getFontMetrics().stringWidth("false");
		super.paintComponent(g);
		
		// Die Figur (ohne Topline)
		g.drawLine(getWidth()-1,0,getWidth()-1,getHeight()-1);
		g.drawLine(getWidth()-1,getHeight()-1, 0, getHeight()-1);
		g.drawLine(0, getHeight()-1, 0,0);
		g.drawLine(0, 0, (int)(width*getRelation()),height-1);
		g.drawLine((int)(width*getRelation()),height-1,width,0);
		g.setColor(Color.black);
		
		// Der Relation-Point
		g.fillRect((int)(width*getRelation())-3,height-6,5,5);	
		
		// Das Label
		g.drawString(getLabel(),width/2-labelWidth/2,15);
		g.drawString("True",(int)(width*getRelation()/2-trueWidth/2),height-5);
		g.drawString("False",(int)(width*getRelation()+width*(1-getRelation())/2-falseWidth/2),height-5);
	}
	
	void paintTopLine(Graphics g)
	{
		g.drawLine(0,0,getWidth()-1,0);
	}
	
	
	double getRelation()
	{
		return relation;
	}
	
	void setRelation(double newRelation)
	{
		relation = newRelation;
		if (newRelation > 0.8)
			relation = 0.8;
		if (newRelation < 0.2)
			relation = 0.2;	
	}
	
	void save(struktor.Save saveObject)
	{
		// Bei CCode müssen auf jeden Fall geschweifte Klammern gesetzt werden, da evtl. ein Command 
		// mehr als ein Kommando produziert
		boolean printPara1 = blockPrint(getAlt1());
		boolean printPara2 = blockPrint(getAlt2());
		saveObject.println("if ("+getLabel()+")");
		if (printPara1)
			saveObject.println("{");
		saveObject.addTab(1);
		alt1.save(saveObject);
		saveObject.addTab(-1); 
		if (printPara1)
			saveObject.println("}");
		// lohnt sich ein else ?
		if (getAlt2().getBlockCount() >1 || (!getAlt2().getLabel().equals("")))
		{
			saveObject.println("else");
			if (printPara2)
				saveObject.println("{");
			saveObject.addTab(1);
			alt2.save(saveObject);
			saveObject.addTab(-1);
			if (printPara2)
				saveObject.println("}");
		}
		try {
			getNext().save(saveObject);
		} catch (NullPointerException npe) {}
		
	}

}