// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)

package struktor;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Frame;

/** Sammelt ein paar nützliche Tools */
public class Utils
{
	static javax.swing.JApplet applet;
	static Frame frame;
	
	static public Frame getFrame(Container element)
	{
		Container parent = element.getParent();
		while (! (parent instanceof Frame) )
			parent = parent.getParent();
		Frame theFrame = (Frame) parent;
		return theFrame;	
	}
	
	/** statische Getter-Methode (ohne entsprechende Setter-Methode) 
	 * Gibt das applet-Objekt zurück. Wird fast in allen Konstruktoren 
	 * verwendet, um repaint() auszulösen. evtl problematisch wenn 
	 * mehrere Applets auf einer Seite versammelt sind.
	 * @return das Applet-Objekt    
	 */
	static public javax.swing.JApplet getApplet(Container element)
	{
		Container parent = element.getParent();
		while (! (parent instanceof Applet) )
			parent = parent.getParent();
		javax.swing.JApplet theApplet = (javax.swing.JApplet) parent;
		return theApplet;
		//return applet;
	}
	static void setApplet(javax.swing.JApplet theApplet)
	{
		applet = theApplet;
	}
	
	static void setFrame(Frame theFrame)
	{
		frame = theFrame;
	}
	
	public static String convertString(String inString)
	{
		StringBuffer outString= new StringBuffer();
		for(int i = 0 ; i < inString.length(); i++)
		{
			if (inString.charAt(i)== 'Ä')
				outString = outString.append("Ae");
			else if (inString.charAt(i)== 'ä')
				outString = outString.append("ae");
			else if (inString.charAt(i)== 'Ö')
				outString = outString.append("Oe");
			else if (inString.charAt(i)== 'ö')
				outString = outString.append("oe");
			else if (inString.charAt(i)== 'Ü')
				outString = outString.append("Ue");
			else if (inString.charAt(i)== 'ü')
				outString = outString.append("ue");
			else if (inString.charAt(i)== 'ß')
				outString = outString.append("ss");
			else if (inString.charAt(i)== ' ')
				outString = outString.append(" ");
			else	
				outString = outString.append(inString.charAt(i));
		}
		return outString.toString();	
	}
	
	public static boolean containsVovelMutation(String inString)
	{
		for(int i = 0 ; i < inString.length(); i++)
		{
			if (inString.charAt(i)== 'Ä')
				return true;
			else if (inString.charAt(i)== 'ä')
				return true;
			else if (inString.charAt(i)== 'Ö')
				return true;
			else if (inString.charAt(i)== 'ö')
				return true;
			else if (inString.charAt(i)== 'Ü')
				return true;
			else if (inString.charAt(i)== 'ü')
				return true;
			else if (inString.charAt(i)== 'ß')
				return true;
		}
		return false;	
	} 
	
	
	
}