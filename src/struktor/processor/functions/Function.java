// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor.functions;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTextArea;

import struktor.Struktor;
import struktor.StruktorException;
import struktor.Tracer;
import struktor.TurtleCanvas;
import struktor.processor.Memory;
import struktor.processor.ProcessorException;
import struktor.processor.datatypes.Datatype;
import struktor.processor.datatypes.Pointer;
import struktor.processor.operators.Expr;
import struktor.strukelements.Struktogramm;

/** Ermöglicht das Aufrufen von Funktionen. Wird im Prinzip genauso wie die Operatoren 
 * verwendet
 */

public class Function extends Expr
implements Datatype
{
	private Vector parameter;
	private Memory memory;
	private Object returnValue;
	private TurtleCanvas gOutput;
	private JTextArea tOutput;
	private Struktor struktor;
	
	
	private String nameOfFunction;
	
	

	/** Die Konstruktor-Methode. Wesentlich sind die Parameter "name" und "parameter"
	 * @param   struktor  
	 * @param   name (der Name der Funktion)
	 * @param   parameter (Die Parameter der Funktion)
	 * @param   gOutput (ermöglicht grafische Ausgabe)
	 * @param   tOutput (ermöglich textuelle Ausgabe)
	 * @param   memory (damit ein neues Formular angelegt werden kann ...)
	 */
	public Function(Struktor struktor, String name, Vector parameter, TurtleCanvas gOutput, JTextArea tOutput, Memory memory)
	{
		Tracer.out("Entering: "+getClass()+"Kostruktor");
		this.struktor = struktor;
		nameOfFunction = name;
		this.parameter = parameter;
		this.gOutput = gOutput;
		this.tOutput = tOutput;
		this.memory = memory; 
	}
	
	public Object eval()
	throws ProcessorException
	{
		Struktogramm struktogramm;
		Method method;
				
		// Argumente auswerten
		for (Enumeration el=parameter.elements(); el.hasMoreElements(); )
		{
			Object o=el.nextElement();
			Object osave=o;
			while(o instanceof Expr)
				o=((Expr)o).eval();
			parameter.setElementAt(o, parameter.indexOf(osave));
		}
		
		// vielleicht TextOut
		if (nameOfFunction.equals("println"))
		{
			println();
			return new Integer(0);
		}
		if (nameOfFunction.equals("print"))
		{
			print();
			return new Integer(0);
		}
		// Vielleicht calloc
		if (nameOfFunction.equals("calloc"))
		{
			return calloc();
		}

		// vielleicht ein anderes Struktogramm ?
		else if ((struktogramm = struktor.findStruktogramm(nameOfFunction)) != null)
		{
			Struktogramm caller=null;
			try {
				caller = struktor.getActualStruktogramm();
				try {
					if (Struktogramm.delay > 10)
						struktor.selectStruktogramm(nameOfFunction);
				} catch (NullPointerException npe) {}
				returnValue = struktogramm.startStruk(parameter);
				Memory.popForm();
				try {
					if (Struktogramm.delay > 10)
						Struktor.staticSelectStruktogramm(caller.getName());
				} catch (NullPointerException npe) {}
				if (returnValue instanceof Expr)
					returnValue = ((Expr)returnValue).eval();
				return returnValue;
			} catch (StruktorException se) { 
				Tracer.err(se.getMsg()); 
				Struktor.staticSelectStruktogramm(Struktogramm.main.getName());
				throw new ProcessorException(se.getMsg());}
		}
		
		// Vielleicht eine mathematische Funktion ?
		else if ((method = getFunctionOfClass("java.lang.Math")) != null)
			return executeMathFunction(method);
		
		// Vielleicht eine Turtle Funktion ?
		else if ((method = getFunctionOfClass("struktor.TurtleCanvas")) != null)
			return executeTurtleFunction(method);
			
		// Vielleicht eine grafische Funktion ?
		else if ((method = getFunctionOfClass("java.awt.Graphics")) != null)
			return executeGraphicsFunction(method);
		
		// vielleicht eine hinzukopierte Funktion ? 
		else if ((method = getFunctionOfClass("struktor.processor.functions.Functions")) != null)
			return executeInternalFunction(method);
		
		else
			throw new ProcessorException("Function "+nameOfFunction+" not found!\n (Struktogramm, Math, Graphics)");
	}
	

	/** Eine mathematische Funktion ausführen (java.Math)
	 * @param   method  
	 * @return     
	 * @exception   ProcessorException  
	 */
	private Object executeMathFunction(Method method)
	throws ProcessorException
	{
		//Nun kann die Methode aufgerufen werden 
		try {
			return method.invoke(new Object(), parameter.toArray());
		} 
		catch (IllegalArgumentException iae) 
		{ 
			throw new ProcessorException("Error at the call of Math-Function: "+nameOfFunction+"\n"+iae.toString());
		}
		catch (Exception e) 
		{ 
			throw new ProcessorException("undefined Error at call of Function: "+nameOfFunction+".\n"+e.toString());
		}
	}
	

	/** Eine grafische Funktion ausführen (java.awt.Graphics)
	 * @param   method  
	 * @return     
	 * @exception   ProcessorException  
	 */
	private Object executeGraphicsFunction(Method method)
	throws ProcessorException
	{
		Graphics output = gOutput.getGraphics();
		if (output == null)
			throw new ProcessorException("Error: Output-Window is closed \n(activate left-upper checkbox!)");
		// Extrawurscht für setColor(Color color); (geht aber leider nicht)
		if (method.getName().equals("setColor"))
		{
			Integer[] rgb = new Integer[3];
			Object[] rgb2 = parameter.toArray();
			rgb[0] = (Integer)rgb2[0];
			rgb[1] = (Integer)rgb2[1];
			rgb[2] = (Integer)rgb2[2];
			Color color = new Color(rgb[0].intValue(), rgb[1].intValue(), rgb[2].intValue());
			parameter.removeAllElements();
			parameter.add(color);
		}
		
		try {
			Object returnValue = method.invoke(gOutput.getGraphics(), parameter.toArray());
			gOutput.repaint();
			return returnValue;
		} 
		catch (IllegalArgumentException iae) 
		{ 
			throw new ProcessorException("Error at the call of Graphics-Function: "+nameOfFunction+"\n"+iae.toString());
		}
		catch (Exception e) 
		{ 
			throw new ProcessorException("undefined Error at call of Function: "+nameOfFunction+".\n"+e.toString());
		}
	}
	

	/** Eine Turtle Funktion ausführen (struktor.TurtleCanvas)
	 * @param   method  
	 * @return     
	 * @exception   ProcessorException  
	 */
	private Object executeTurtleFunction(Method method)
	throws ProcessorException
	{
		try {
			Object returnValue = method.invoke(gOutput, parameter.toArray());
			gOutput.repaint();
			return returnValue;
		} 
		catch (IllegalArgumentException iae) 
		{ 
			throw new ProcessorException("Error at the call of Turtle-Function: "+nameOfFunction+"\n"+iae.toString());
		}
		catch (Exception e) 
		{ 
			throw new ProcessorException("undefined Error at call of Function: "+nameOfFunction+".\n"+e.toString());
		}
	}
	
	/** Funktioniert noch nicht. Der Gedanke ist der, dass jeder 
	 *  beliebige Funktionen (Methoden) in die Klasse Functions
	 *  reinschreiben kann. Die sollen dann auch ausführbar sein
	 */
	private Object executeInternalFunction(Method method)
	throws ProcessorException
	{
		try {
			return method.invoke(new Object(), parameter.toArray());
		} 
		catch (IllegalArgumentException iae) 
		{ 
			throw new ProcessorException("Illegal Arguments of Internal-Function: "+nameOfFunction+"\n"+iae.toString());
		}
		catch (Exception e) 
		{ 
			throw new ProcessorException("undefined Error at call of Function: "+nameOfFunction+".\n"+e.toString());
		}
	}
	
	

	/** Ermittelt, ob die Funktion in der Klasse "classs" enthalten ist
	 * @param   classs  
	 * @return die Methode zur Funktion    
	 */
	private Method getFunctionOfClass(String classs)
	{
		Class claz;
		try {
			claz = Class.forName(classs);
		} catch (ClassNotFoundException cnfe) {Tracer.out(cnfe);return null;}
		Method[] methods = claz.getMethods();
		for (int i = 0; i < methods.length; ++i)
		{
      		String name = methods[i].getName();		  
      		if (name.equals(nameOfFunction))
				return methods[i];
		}
		return null;	
	}
	

	/** Analog zu Java .... (aber auf der "Console" tOutput"
	 */
	private void println()
	{
		print();
		tOutput.append("\n");
	}
	
	/** println ohne newLine ....
	 */
	private void print()
	{
		String string = new String();
		try {
			for (int i=0; i< parameter.size();i++)
				string = string + parameter.elementAt(i).toString();
		} catch (ArrayIndexOutOfBoundsException aioobe) {}
		tOutput.append(string);
	}

	/** MemmoryAllocation, reserviert Speicher bei Memory Objekt
	 * @return     
	 * @exception   ProcessorException  
	 */
	private Pointer calloc()
	throws ProcessorException
	{
		int numberOfBytes=4;
		int length;
		try {
			numberOfBytes=((Integer)parameter.elementAt(1)).intValue();
			length = ((Integer)parameter.elementAt(0)).intValue();
		} catch (ArrayIndexOutOfBoundsException aioobe) 
		{throw new ProcessorException("Illegal Arguments of calloc-Function: \n"+aioobe.toString());}
		Pointer start=memory.declareVariable(numberOfBytes);
		for(int i=1; i<length;i++)
			memory.declareVariable(numberOfBytes);
		return start;	
	}	
}