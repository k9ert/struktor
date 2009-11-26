// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import struktor.StruktorException;
import struktor.Utils;
import struktor.processor.LoopControlException;
import struktor.processor.Processor;
import struktor.processor.ProcessorException;
import struktor.processor.ReturnException;

/** Eine Klasse für das simple Kommando (Rechteck)
 */
class Command extends StrukElement
implements CommandTypes
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Standardmässig auf CALCULATION (Verarbeitung) */
	private int action = CALCULATION;
	
	/** Die AdditionalMessage (Für INPUT/OUTPUT*/
	private String adMessage = new String("");

	Command(Struktogramm s, String label)
	{
		super(s, label);
		mEvt = new CommandEvt(this, struktogramm);
	 	addMouseListener(mEvt);
	}

	void execute(Processor p)
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException
	{
		delayAndPaint(p);		
		p.parse(getLabel(),action, adMessage);
		delayAndPaint(p);
	}

	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);// evtl. Dragpoint+Topline 
		
		// Die Figur (ohne Topline)
		g.drawLine(getWidth()-1,0,getWidth()-1,getHeight()-1);
		g.drawLine(getWidth()-1,getHeight()-1, 0, getHeight()-1);
		g.drawLine(0, getHeight()-1, 0,0);
		g.setColor(Color.black);
		  
		int labelWidth = g.getFontMetrics().stringWidth(getLabel());
		switch (action)
		{
		case INPUT:
			g.drawString("Input:",getWidth()/4,15);
			break;
		case OUTPUT:
			g.drawString("Output:",getWidth()/4,15);
			break;
		}
		if (action == CALCULATION)
			g.drawString(getLabel(),getWidth()/2-labelWidth/2,15);
		else
			g.drawString(getLabel(),getWidth()/2-labelWidth/2,30);
	}
	
	void paintTopLine(Graphics g)
	{
		g.drawLine(0,0,getWidth()-1,0);
	}	
	
	// Getter-Setter-Methoden
	int getActionType()
	{
		return action;
	}
	
	void setActionType(int actionType)
	{
		action = actionType;
	}
	
	String getAdMessage()
	{
		return (adMessage);
	}
	
	void setAdMessage(String newMessage)
	throws StruktorException
	{
		if (Utils.containsVovelMutation(newMessage))
			throw new StruktorException("No VovelMutations ! \n (keine Umlaute !)");
		else
			adMessage = newMessage;
	}
	
	/** Setter-Methode
	 * Die Höhe des Elements verändern (Spezialfall wegen Input/Output)
	 * @param   height  
	 */
	void setHeight(int height)
	throws ResizeException
	{
		if (action == CALCULATION)
			super.setHeight(height);
		else
		{
			if (height > 35)
				setSize(new Dimension(getSize().width,height));
			else
			{
				setSize(new Dimension(getSize().width,35));
				throw new ResizeException(height-35, ResizeException.HEIGHT);
			}
		}
			
	}

	/** das selbe wie delete(true) */
	void deleteAll()
	{
		delete(true); // im Falle des Commands wirklich simpel 
	}
	
	/** Speichert das Command */
	void save(struktor.Save saveObject)
	{
		if (struktogramm.presets.makeCCode)
			saveC(saveObject);
		else
		{
			switch (action)
			{
			case INPUT:
				if (!adMessage.equals(""))
					saveObject.println("//Msg:"+adMessage);
				saveObject.println("Input: "+getLabel());
				break;
			case OUTPUT:
				if (!adMessage.equals(""))
					saveObject.println("//Msg:"+adMessage);
				saveObject.println("Output: "+getLabel());
				break;
			default:
				if (getLabel().equals(""))
					saveObject.println(";");
				else
					saveObject.println(getLabel());	
				break;
			}
			try {
				getNext().save(saveObject);
			} catch (NullPointerException npe) {}
		}
	}

	/** Speichert das Command als CCode */
	private void saveC(struktor.Save saveObject)
	{
		
		switch (action)
			{
			case INPUT:
				if (!adMessage.equals(""))
					saveObject.println("printf(\""+adMessage+"\");");
				saveObject.println(produceScanf());
				saveObject.println("fflush(stdin);");
				break;
			case OUTPUT:
				if (!adMessage.equals(""))
					saveObject.println("printf(\""+adMessage+"\");");
				saveObject.println(producePrintf());
				break;
			default:
				if (getLabel().equals(""))
					saveObject.println(";");
				else
				{
					if (getLabel().trim().startsWith("print"))
						saveObject.println(producePrint2Printf());
					else	
						saveObject.println(getLabel());	
				}
				break;
			}
			try {
				getNext().save(saveObject);
			} catch (NullPointerException npe) {}
	}
	
	/** das Input-Command als scanf */
	private String produceScanf()
	{
		DecList variables = DecList.findDecList(struktogramm);
		String label = getLabel();
		String preString= new String("scanf(\"");
		String postString = new String();
		try {
			preString += variables.getCFormatCode(label)+"\"";
			postString = " ,&" + label + ");";
		} catch (ProcessorException pe) {}
		return preString + postString;
	}
	
	/** das Output-Command als printf*/
	private String producePrintf()
	{
		boolean isInsideString=false;
		DecList variables = DecList.findDecList(struktogramm);
		String label = getLabel();
		// preString = erstes Argument von printf (String)
		String preString= new String("\"");
		// postString = alle weiteren Argumente von printf
		String postString = new String();
		
		// mit dieser Schleife gehen wir das komplette Label char f�r char durch
		for(int i = 0 ; i < label.length(); i++)
		{
			if (label.charAt(i)== '"')
			{
				isInsideString = !isInsideString;
				continue;
			}
			else if ((label.charAt(i)== '+' & !isInsideString) || (!isInsideString & Character.isLetter(label.charAt(i))))
			{
				String temp = new String();
				for (; (i < label.length()) && (Character.isLetterOrDigit(label.charAt(i)));i++)
				{
					if (label.charAt(i)== ' ')
						continue;
					temp += new String(new Character(label.charAt(i)).toString());
				}
				try {
					preString += variables.getCFormatCode(temp);
				} catch (ProcessorException pe) {}
				if (!temp.equals(""))
					postString += ", "+temp;	
			}	
			if (isInsideString)
				preString += label.charAt(i);
		}
		preString += "\\n\"";
		return "printf(" + preString + postString + ");";
	}
	
	/** das Calculation-Command (mit print oder println) als printf */
	private String producePrint2Printf()
	{
		boolean isInsideString=false;
		DecList variables = DecList.findDecList(struktogramm);
		String label = getLabel().trim();
		boolean newLine = label.startsWith("println(");
		if (newLine)
			label = label.substring(8, label.length()-2);
		else
			label = label.substring(6, label.length()-2);
		// preString = erstes Argument von printf (String)
		String preString= new String("\"");
		// postString = alle weiteren Argumente von printf (incl. Kommas)
		String postString = new String();
		
		for(int i = 0 ; i < label.length(); i++)
		{
			if (label.charAt(i)== '"')
			{
				isInsideString = !isInsideString;
				continue;
			}
			else if ((label.charAt(i)== ',' & !isInsideString) || (!isInsideString & Character.isLetter(label.charAt(i))))
			{
				String temp = new String();
				for (; (i < label.length()) && (Character.isLetterOrDigit(label.charAt(i)));i++)
				{
					if (label.charAt(i)== ' ')
						continue;
					temp += new String(new Character(label.charAt(i)).toString());
				}
				try {
					preString += variables.getCFormatCode(temp);
				} catch (ProcessorException pe) {}
				if (!temp.equals(""))
					postString += ", "+temp;	
			}	
			if (isInsideString)
				preString += label.charAt(i);
		}
		if (newLine)
			preString += "\\n\"";
		else
			preString += "\"";
		return "printf(" + preString + postString + ");";	
	}
	
}