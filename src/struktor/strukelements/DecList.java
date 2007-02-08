// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import struktor.Presets;
import struktor.Save;
import struktor.Tracer;
import struktor.processor.ProcessorException;

/** Eine Klasse für den "Deklarationen-Editor" */
public class DecList extends JPanel
implements ActionListener, ItemListener, struktor.processor.datatypes.Datatype
{
	// Static Variables
	private static Vector DecListList = new Vector();
	
	private static DecList addDecList(DecList s)
	{
		DecListList.addElement(s);
		return s;
	}
	
	/** Eine Deklarationsliste zu einem Struktogramm finden */
	static public DecList findDecList(Struktogramm sg)
	{
		for (Enumeration el=DecListList.elements(); el.hasMoreElements(); )
        {
             DecList r=(DecList)el.nextElement();
             if (r.struktogramm == sg)
                return r;
        }
    	
			Tracer.out("OhOh ... no Declist found !");    
		return null;
	}
	
	static public void deleteDecList(Struktogramm sg)
	{
        DecListList.remove(findDecList(sg));
	}
	

	// Object Variables
	JButton newDeclaration = new JButton("newDeclaration");
	public Struktogramm struktogramm;
	public Presets presets;
	Vector decList;
	JPanel tempPanel;
	
	/** Nur für Expr.Calc */
	public Vector getDecList()
	{
		return decList;
	}
	
	public DecList(Struktogramm s, JPanel tempPanel)
	{
		struktogramm = s;
		presets = s.presets;
		addDecList(this);
		
		Box box = new Box(BoxLayout.X_AXIS);
		if (presets.enabNewDec)
			box.add(newDeclaration);		
		add(box);
		newDeclaration.addActionListener(this);
		newDeclaration.setVisible(true);
		
		decList = new Vector();
		this.tempPanel = tempPanel;
	}
	
	/** für ExprCalc */
	public DecList(Presets presets)
	{
		this.presets = presets;
		decList = new Vector();
		tempPanel = new JPanel();
	}
	
	/** Die Parameter beim Aufruf von Struktogrammen werden initialisiert indem der Wert des Arguments in das Textfeld geschrieben wird 
	 * @param   Die Argumente als Vektor  
	 * @exception   ProcessorException  
	 */
	void initializeParameters(Vector parameter)
	throws ProcessorException
	{
		Tracer.out("try to initilize parameters ...");
		int parameterCount = getParameterCount();
		int i=0;
		for (Enumeration el=parameter.elements(); el.hasMoreElements(); )
	    {
			 Object p=(Object)el.nextElement();
			 
			 Tracer.out("Argument "+i+":"+p.toString());
	         if (p != null)
			 	getParameter(i).setValue(p.toString());
			 i++;
	    }
		
		Tracer.out("ok, Variables initialized !");
		if (i != parameterCount)
			throw new ProcessorException("To less Parameters !");
	}
	
	/** Hilfsmethode um den xten Parameter zu finden */
	private Dec getParameter(int index)
	throws ProcessorException
	{
		int counter=0;
		Component decs[] = tempPanel.getComponents();
		int max = tempPanel.getComponentCount();
		for (int i = 0; i < max; i++)
		{
			Dec element = (Dec)decs[i];
			if (element.isParameter())
			{
				if (index == counter)
					return element;
				else
					counter++;	
			}
		}
		throw new ProcessorException("To many Parameters !");
	}
	
	/** Hilfsmethode: Gibt die Anzahl der Parameter zurück */
	private int getParameterCount()
	{
		int counter=0;
		Component decs[] = tempPanel.getComponents();
		int max = tempPanel.getComponentCount();
		for (int i = 0; i < max; i++)
		{
			Dec element = (Dec)decs[i];
			if (element.isParameter())
				counter++;	
		}
		
			Tracer.out("We have " + counter+" parameters here !");
		return counter;
	}
	
	/** Hilfsmethode: Gibt die Anzahl der Deklarationen zurück */
	private int getDeclarationCount()
	{
		return tempPanel.getComponentCount();
	}
	
	public JPanel getTempPanel()
	{
		return tempPanel;
	}
	
	/** Eine neue Deklaration erzeugen (für ExprCalc public) */
	public Dec newDeclaration(boolean isPointer, boolean isParameter, boolean isArray, int index, int temptype, String tempname, String value)
	{
		Dec dec = new Dec(presets, isPointer, isParameter, isArray, index, temptype, tempname, value);
		dec.setDecList(this);
		decList.addElement(dec);
		
		Tracer.out("New Dec set ("+isPointer+isParameter+isArray+index+temptype+tempname+value);
		tempPanel.add(dec);
		dec.setVisible(true);
		tempPanel.validate();
		return dec;
	}
	
	String getCFormatCode(String varName)
	throws ProcessorException
	{
		int counter=0;
		Component decs[] = tempPanel.getComponents();
		int max = tempPanel.getComponentCount();
		for (int i = 0; i < max; i++)
		{
			Dec element = (Dec)decs[i];
			if (element.getName().equals(varName))
				return element.getCFormatCode();
		}
		throw new ProcessorException("Variable not Found: "+varName);
	}
	
	// Event Handling
	void newDeclaration()
	{
		newDeclaration(false, false, false, 10, INTEGER, "name", "");
	}
	
	/** Deklaration loeschen (delete-Button) */
	void delete(Dec dec)
	{
		decList.remove(dec);
		tempPanel.remove(dec);
		
			Tracer.out("dec deleted !");
		revalidate();
		repaint();
	}
		
		
	public void actionPerformed(ActionEvent e) 
	{
	  Object src = e.getSource();
		        
		if (src == newDeclaration) {
			newDeclaration();
	    repaint();
	  }
	}
	public void itemStateChanged(ItemEvent i)
	{
	}
	
	/** Deklarationen speichern (ohne Parameter) */
	void saveDecs(Save saveObject)
	{
		if (!presets.makeCCode)
		{
			saveObject.println("//@*** Declarations ***");
			if (decList.isEmpty() | (getParameterCount()-getDeclarationCount() == 0) )
				saveObject.println("// No Declarations");
		}	
		for (Enumeration el=decList.elements(); el.hasMoreElements(); )
		{
			
			Dec r=(Dec)el.nextElement();
			if (!r.isParameter())
			{
				// Verwendet als einzigster printTabs weil dec.save auch für Parameter verwendet wird (print statt println)
				saveObject.printTabs();
				r.save(saveObject);
				saveObject.print(" ;\n");
			}	
        }
		if (!decList.isEmpty())
			saveObject.println("");
	}
	
	/** Parameter speichern */
	void saveParam(Save saveObject)
	{
		boolean flag = false;
		int parameterCount = getParameterCount();
		int i=0;
		for (Enumeration el=decList.elements(); el.hasMoreElements(); )
		{
			Dec r=(Dec)el.nextElement();
			if (r.isParameter())
			{
				i++;
				r.save(saveObject);
				if (i<parameterCount)
					saveObject.print(", ");
			}
			flag = true;	
		}
	}
}
	
