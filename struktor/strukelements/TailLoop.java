// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.Color;
import java.awt.Graphics;

import struktor.processor.BreakException;
import struktor.processor.ContinueException;
import struktor.processor.LoopControlException;
import struktor.processor.Processor;
import struktor.processor.ProcessorException;
import struktor.processor.ReturnException;

class TailLoop extends Loop 
implements CommandTypes
{

	TailLoop(Struktogramm s, String label)
	{
	 	super(s, label);	
	}
		
	void execute(Processor p)
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException
	{
		try {
			double result;
			do 
			{
				try {
					getInside().execute(getInside(),p);
				} catch (ContinueException ce) { ce.catched(); if (ce.getLevel() != 0) throw ce;}				
				delayAndPaint(p);
				delayAndPaint(p);		
			} while (p.parse(getLabel()+";", CALCULATION, "") != 0);
		} catch (BreakException be) { be.catched(); if (be.getLevel() != 0) throw be;}		
	}
	// Ab Hier Grafik + Events

	int insidey(){
       return getY();
	}
		
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int width = getSize().width;
		int height = getSize().height;
		// Die Figur (ohne Topline)
		// nach unten
		g.drawLine(getHorWidth(), 0, getHorWidth(), height-1-getVerHeight());
		// nach rechts
		g.drawLine(getHorWidth(), height-1-getVerHeight(), width-1, height-1-getVerHeight());
		// kleines Stück nach unten	
		g.drawLine(width-1, height-1-getVerHeight(), width-1, height-1);
		// nach links
		g.drawLine(width-1, height-1, 0, height-1);
		// und wieder rauf
		g.drawLine(0, height-1, 0, 0);

		int labelWidth = g.getFontMetrics().stringWidth(getLabel());
		g.drawString(getLabel(),width/2-labelWidth/2,height-15);

		g.setColor(Color.black);
	
	}
	
	void paintTopLine(Graphics g)
	{
		g.drawLine(0, 0, getHorWidth(), 0);
	}
	
	void save(struktor.Save saveObject)
	{
		saveObject.println("do {");
		saveObject.addTab(1);
		getInside().save(saveObject);
		saveObject.addTab(-1);
		saveObject.println("} while("+getLabel()+");");
		try {
			getNext().save(saveObject);
		} catch (NullPointerException npe) {}
		
	}

	

}