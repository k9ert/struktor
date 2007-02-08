// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.Color;
import java.awt.Graphics;

import struktor.Tracer;
import struktor.processor.BreakException;
import struktor.processor.ContinueException;
import struktor.processor.LoopControlException;
import struktor.processor.Processor;
import struktor.processor.ProcessorException;
import struktor.processor.ReturnException;

class HeadLoop extends Loop 
implements CommandTypes
{

	HeadLoop(Struktogramm s, String label)
	{
	 	super(s, label);
	}
		
	void execute(Processor p)
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException
	{
		try {
			double result;
			delayAndPaint(p);
			while (p.parse(getLabel()+";", CALCULATION, "") != 0)
			{
				Tracer.out("Execute Loop (true) ...");
				delayAndPaint(p);
				try {
					getInside().execute(getInside(),p);
				} catch (ContinueException ce) { ce.catched(); if (ce.getLevel() != 0) throw ce;}				
				delayAndPaint(p);		
			}
			delayAndPaint(p);
		} catch (BreakException be) { be.catched(); if (be.getLevel() != 0) throw be;}	
	}

	// Ab Hier Grafik + Events
	int insidey(){
       return getY()+getVerHeight();
	}
	
	public void paintComponent(Graphics g)
	{
       super.paintComponent(g);
	   int width = getSize().width;
       int height = getSize().height;
       
	   // Die Figur (ohne Topline)
       
       // kleines Stück runter
       g.drawLine(width-1, 0, width-1, getVerHeight());
       // wieder nach links fast soweit
       g.drawLine(width-1, getVerHeight(), getHorWidth(), getVerHeight());
       // runter
       g.drawLine(getHorWidth(), getVerHeight(), getHorWidth(), height-1);
       // kleines Stück links)
       g.drawLine(getHorWidth(), height-1, 0, height-1);
       // und wieder rauf
       g.drawLine(0, height-1, 0, 0);
	   
	   int labelWidth = g.getFontMetrics().stringWidth(getLabel());
	   g.drawString(getLabel(),width/2-labelWidth/2,15);
		g.setColor(Color.black);
	}
	
	void paintTopLine(Graphics g)
	{
		int width = getSize().width;
		g.drawLine(0, 0, width-1, 0);
	}
	
	void save(struktor.Save saveObject)
	{
		saveObject.println("while ("+getLabel()+")");
		super.save(saveObject);
		if (getNext()!=null)
			getNext().save(saveObject);
	}

	

}