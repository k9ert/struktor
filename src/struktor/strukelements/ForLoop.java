// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.Color;
import java.awt.Graphics;

import struktor.StruktorException;
import struktor.Tracer;
import struktor.processor.BreakException;
import struktor.processor.ContinueException;
import struktor.processor.LoopControlException;
import struktor.processor.Processor;
import struktor.processor.ProcessorException;
import struktor.processor.ReturnException;

class ForLoop extends Loop 
implements CommandTypes
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String initLabel=new String(" ");
	private String stepLabel=new String(" ");
	
	private String printString = new String(" ; ; ");
	
	
	ForLoop(Struktogramm s, String label)
	{
	 	super(s, label);
		Tracer.out("Leaving setPrintString()");
		setPrintString(label);
	}
		
	void execute(Processor p)
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException
	{
		try {
			delayAndPaint(p);
			for (p.parse(initLabel);p.parse(getLabel()+";", CALCULATION, "") != 0; p.parse(stepLabel))
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
       super.paintComponent(g); // evtl. Dragpoint + Topline
	   int width = getSize().width;
       int height = getSize().height;
       // Die Figur (ohne Topline)
       // kleines St�ck runter
       g.drawLine(width-1, 0, width-1, getVerHeight());
       // wieder nach links fast soweit
       g.drawLine(width-1, getVerHeight(), getHorWidth(), getVerHeight());
       // runter
       g.drawLine(getHorWidth(), getVerHeight(), getHorWidth(), height-1);
       // kleines St�ck links)
       g.drawLine(getHorWidth(), height-1, 0, height-1);
       // und wieder rauf
       g.drawLine(0, height-1, 0, 0);
	   
	   int labelWidth = g.getFontMetrics().stringWidth(printString);
	   g.drawString(printString,width/2-labelWidth/2,15);
		g.setColor(Color.black);
	}
	
	void paintTopLine(Graphics g)
	{
		int width = getSize().width;
		g.drawLine(0, 0, width-1, 0);
	}	
	
	void setInitLabel(String text)
	{
		initLabel=text;
	}
	
	void setLabel(String label)
	throws StruktorException
	{
		super.setLabel(label);
		setPrintString();
	}
	
	void setStepLabel(String text)
	{
		stepLabel=text;
		setPrintString();
	}
	
	String getInitLabel()
	{
		return initLabel;
	}
	
	String getStepLabel()
	{
		return stepLabel;
	}
	
	private void setPrintString()
	{
		printString = new String(initLabel+getLabel()+";");
		int i;
		for(i=0;i<getStepLabel().length();i++)
			if (stepLabel.charAt(i)==';')
				break;
		printString+= stepLabel.substring(0,i);	
	}	
	
	private void setPrintString(String printString)
	{
		
		int i,isave;
		for(i=0;i<printString.length();i++)
			if (printString.charAt(i)==';')
			{
				try {
					initLabel=printString.substring(0,i+1);
				} catch (StringIndexOutOfBoundsException sioobe) {Tracer.out(sioobe+"in setPrintString (first loop)");}
				break;
			}
		isave=++i;
		for(;i<printString.length();i++)
			if (printString.charAt(i)==';')
			{
				try {
					super.setLabel(printString.substring(isave,i));
				} catch (StringIndexOutOfBoundsException sioobe) {Tracer.out(sioobe+"in setPrintString (second loop)");}
				catch (StruktorException se) {}
				break;
			}
		try {
			stepLabel=printString.substring(i+1,printString.length())+";";
		} catch (StringIndexOutOfBoundsException sioobe) {Tracer.out(sioobe+"in setPrintString (third no loop)");}
		setPrintString();
	}		
	
	void save(struktor.Save saveObject)
	{
		saveObject.println("for("+printString+")");
		super.save(saveObject);
		if (getNext()!=null)
			getNext().save(saveObject);
	}

	

}