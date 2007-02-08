// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import struktor.Tracer;
import struktor.processor.datatypes.Pointer;
import struktor.strukelements.Dec;

/** Diese Klasse modelliert den Speicher. typische (public) Methoden sind
 *  "getValueAtAdress" oder "getAdressOfVariable"
 *	@see Processor
 */
public class Memory
implements struktor.processor.datatypes.Datatype
{

	// Der Formular-Stack
	private static Stack stack;
	private static Memory actualForm = null;
	private static int adress=256;
	
	static Memory getActualForm()
	{
		return actualForm;
	}
	

	/** Wird von der Constructor-Methode aufgerufen. Erledigt die FormularStack-Geschichte
	 * @param   form  
	 */
	private static void setActualForm(Memory form)
	{
		if (stack == null)
			stack = new Stack();
		if (actualForm != null)
			stack.push(actualForm);
		actualForm = form;
	}
	
	/** NUR VON ExprCalc !!!
	 */
	public static void setActualFormByExprCalc(struktor.ExprCalc temp, Memory form)
	{
		// die Übergabe von ExprCalc ist so eine Art "ZugangsCode"
		actualForm = form;
	}
	

	/** Wird bis jetzt nur von der Function-Methode nach dem erfolgreichen durchlaufen eines anderen Struktogramms aufgerufen. Kapselt den Formular-Stack
	 */
	public static void popForm()
	{
		actualForm = (Memory)stack.pop();
	}
	

	/** Wird aufgerufen vom "main-Struktogramm". Der Speicher wird vollständig gelöscht (Stack und evtl noch vorhandenes Memory-Object). Außerdem wird der Adreßraum wieder neu festgelegt
	 */
	public static void initializeMemory()
	{
		stack = null;
		actualForm = null;
		adress = 256;
	}
		
	// Objectvariablen
	private Hashtable variables = new Hashtable();
		
	

	/** Construktor-Methode: Erstellt entsprechende Variables-Objekte die den Speicher repräsentieren. Alle Variables werden in einem Vector gespeichert.
	 * @param   DecList (Die DeklarationsListe) 
	 * @exception   ProcessorException
	 */
	Memory(Vector DecList)
	throws ProcessorException
	{
		if (false)
			;
		else
		{
			setActualForm(this);
			for (Enumeration el=DecList.elements(); el.hasMoreElements(); )
	        {
	         	Dec r=(Dec)el.nextElement();
				Variable var = null;
				Variable temp=null;
				// Variable bereits deklariert ?
				try {
					temp = getVariable(r.getName());	
				} catch (ProcessorException pe) {}
				catch (NullPointerException npe) 
				{	System.err.println("Nullpointer in Memory.init: "+r.getName());
				}
				if (temp != null)
					throw new ProcessorException("Variable "+r.getName()+" allready defined !"); 
				if (r.isArray())
					declareArray(r);
				else
					declareVariable(r);
			}
		}
	}
	

	/** Ausgelagert aus der Constructor-Methode. Erstellt analog zu normalen Variablen ein Array
	 * @param   r, die Deklaration des Arrays  
	 * @exception   ProcessorException  
	 */
	private void declareArray(Dec r)
	throws ProcessorException
	{
		int index = r.getIndex();
		int type = r.getType();
		Variable varPointer = new Variable(r.getName(), type, new Pointer(type,new Integer(0)));
		Pointer tempPointer = varPointer.getAdress();
		tempPointer.addToAdress(1);
		varPointer.setValue(tempPointer);
		variables.put(varPointer.getAdress().toString(),varPointer);
		
		for (int i = 0; i < index; i++)
		{
			Variable var = null;
			switch (type)
			{
			case INTEGER:
				var = new Variable( null, r.getType(), new Integer(0));
				break;
			case DOUBLE:
				var = new Variable( null, r.getType(), new Double(0));
				break;
			case CHARACTER:
				var = new Variable( null, r.getType(), new Character(r.getValue().length() > i ? r.getValue().charAt(i) : 0));
				break;
			case STRING:
				var = new Variable( null, r.getType(), new String(""));
				break;
			}
			variables.put(var.getAdress().toString(),var);
		}
	}
	

	/** Ausgelagert aus der Constructor-Methode. Erstellt eine Variable
	 * @param   r  
	 * @exception   ProcessorException  
	 */
	private void declareVariable(Dec r)
	throws ProcessorException
	{
		int type;
		Variable var = null;
		type = r.getType();
        try { 
			if (r.isPointer())
				var = new Variable(r.getName(), r.getType(), new Pointer(r.getType(), r.getValue()));
			else
			{
				switch (type)
				{
				case INTEGER:
					var = new Variable(r.getName(), type, r.getValue().equals("") ? new Integer(0) : new Integer( r.getValue()));
					break;
				case DOUBLE:
					var = new Variable(r.getName(), type, r.getValue().equals("") ? new Double(0) : new Double(r.getValue()));
					break;
				case CHARACTER:
					var = new Variable(r.getName(), type, r.getValue().equals("") ? new Character(' ') :new Character(r.getValue().charAt(0)) );
					break;
				case STRING:
					var = new Variable(r.getName(), type, new String(r.getValue()));
					break;
				case VOID:
					var = new Variable(r.getName(), type, r.getValue());
					break;
				case STRUCT:
					var = new Variable(r.getName(), type, new StructVar(r.getStructType()));
					break;
				}
			}
			variables.put(var.getAdress().toString(),var);
		} catch (NumberFormatException nfe) 
		{throw new ProcessorException("Type Mismatch !\n type:"+type+"\n value: "+r.getValue());}
	}
	
	/** nur für calloc aus Functions */
	public Pointer declareVariable(int length)
	throws ProcessorException 
	{
		Variable var=null;
		switch (length)
		{
			case 4:
				var = new Variable(null, INTEGER, new Integer(0));
				break;
			case 8:
				var = new Variable(null, DOUBLE, new Double(0));
				break;
			case 1:
				var = new Variable(null, CHARACTER, new Character((char)0));
				break;
		}
		variables.put(var.getAdress().toString(), var);
		return var.getAdress();
	}
		

	/** Wird von Deref und LValue verwendet. Braucht keine entsprechende Setter-Methode, weil diese grundsätzlich über eine Pointer (und damit statisch) angesprochen werden 
	 *  Man könnte auch sagen daß diese Funktion Variablennamen in Pointer auflöst
	 * @param   variable  
	 * @return     
	 * @exception   ProcessorException  
	 */
	public Pointer getAdressOfVariable(String variable)
	throws ProcessorException
	{
		try {
			Variable var = getVariable(variable);
			Pointer poin = var.getAdress();
			return poin;
		} catch (ProcessorException pe) 
		{Tracer.out(pe.toString()+ "(Memory.getAdressOfVariable(String)"); throw pe;}
		
	}

	/** Allgemeine Hilfsmethode um ein Variablen-Object anhand seiner Adresse zu suchen, ist leider durch Structs etwas komplizierter geworden
	 * @param   variable (Adresse)
	 * @return Das gesuchte VariablenObject     
	 * @exception   ProcessorException  
	 */
	private Variable getVariable(Pointer adress)
	throws ProcessorException
	{
		StructVar myStruct=null;
		Tracer.out("Entering private getVariable("+adress+")");
		Variable v=null;
		Variable lastElement=null;
		
		Variable var =(Variable)(variables.get(adress.toString()));
		if (var!=null)
			return var;
		Tracer.out("Adress not found !");
		throw new ProcessorException("No data at Adress "+adress);
	}
	

	/** Allgemeine Hilfsmethode um ein Variablen-Object anhand seines Namens zu suchen
	 * @param   variable (Name)
	 * @return Das gesuchte VariablenObject     
	 * @exception   ProcessorException  
	 */
	private Variable getVariable(String variable)
	throws ProcessorException
	{
		for (Enumeration el=variables.elements(); el.hasMoreElements(); )
		{
    		Variable v=(Variable)el.nextElement();
			try {
				if (v.getName().equals(variable))
				{
					Tracer.out("Variable found !");
					return v;
				}
			// Arrays haben keine Namen -> es kann Nullpointers geben
			} catch (NullPointerException npe) {}
		}
		Tracer.out("Variable not found !("+variable+") in Memory.getVariable");
		//try {
		throw new ProcessorException("Variable not found: "+variable);	
		//} catch (NullPointerException npe) { Tracer.out("Aha ! Da is a !"); return null;}
	}

	/** wird benutzt um eine Speicheradresse für einen entsprechenden Typ anzufordern
	 * @param   type  
	 * @return Speicheradresse     
	 */
	static int getNewAdressFor(Object object)
	{
		adress += getSpaceOfDatatype(object);
		return adress;
	}
	
	static int getSpaceOfDatatype(Object o)
	{
		if (o instanceof Integer)
			return 4;
		else if (o instanceof Double)
			return 8;	
		else if (o instanceof Character)
			return 1;
		else if (o instanceof StructVar)
			return ((StructVar)o).getSpace().intValue();
		else
			return 4;	
	}	
	
	/** Statische Methode, ermöglicht den kompletten Speicher (nicht nur ein Formular) nach einer Adresse zu durchsuchen und den Wert zurückzugeben (wird nur von Pointern benutzt)
	 * @param   adress  
	 * @return den Wert an der Adresse     
	 * @exception   ProcessorException  
	 */
	static public Object getValueAtAdress(Pointer adress)
	throws ProcessorException
	{
		// erstmal bei actualform suchen
		try {
			return actualForm.getVariable(adress).getValue();
		} catch (ProcessorException pe) {} 
		catch (NullPointerException npe) { Tracer.out("Nullpointer in Memory.getValueAtAdress"); }
		
		//Ansonsten den Stack durchsuchen
		for (Enumeration el = stack.elements(); el.hasMoreElements(); )
        {
         	Memory r=(Memory)el.nextElement();
			try {
				return r.getVariable(adress).getValue();
			} catch (ProcessorException pe) {}
			catch (NullPointerException npe) { Tracer.out("Nullpointer in Memory.getValueAtAdress (Loop)"); }
		}
		throw new ProcessorException("No Data at Adress "+adress);
		
	}
	/** Statische Methode, ermöglicht den kompletten Speicher (nicht nur ein Formular) nach einer Adresse zu durchsuchen und den Wert zu setzen (wird nur von Pointern benutzt)
	 * @param   adress  
	 * @exception ProcessorException  
	 */
	static public void setValueAtAdress(Pointer adress, Object value)
	throws ProcessorException
	{
		// erstmal bei actualform suchen
		try {
			actualForm.getVariable(adress).setValue(value);
			return;
		} catch (ProcessorException pe) {}
		
		//Ansonsten den Stack durchsuchen
		for (Enumeration el = stack.elements(); el.hasMoreElements(); )
        {
         	Memory r=(Memory)el.nextElement();
			try {
				r.getVariable(adress).setValue(value);
				return;
			} catch (ProcessorException pe) {}
		}
		throw new ProcessorException("No Data at Adress "+adress+"(Processor)");
		
	}
	
	static public Pointer getAdressOfStructIdentifier(String varName, String identifier)
	throws ProcessorException
	{
		Variable var;
		Tracer.out("entering getAdressOfStructIdentifier("+varName+","+identifier+")");
		var = actualForm.getVariable(varName);
		if (!(var.getValue() instanceof StructVar))
			throw new ProcessorException("Ist kein Struct !");
		int offset = ((StructVar)(var.getValue())).getOffsetOfIdentifier(identifier);
		Tracer.out("offset is:"+offset);
		Pointer pointer=new Pointer(var.getAdress());
		pointer.addToAdress(offset);
		Tracer.out("adress is:"+pointer);
		return pointer;	
	}
}

class Variable 
implements struktor.processor.datatypes.Datatype
{

	/** Constructor-Methode: Erstellt ein entsprechende Variable
	 * @param   name  
	 * @param   type  
	 * @param   value  
	 * @exception   ProcessorException  
	 */
	Variable(String name, int type, Object value)
	throws ProcessorException
	{
		this.name = name;
		this.type = type;
		if (type != STRUCT)
			adress = Memory.getNewAdressFor(value);
		else
			adress = Memory.getNewAdressFor(value);	
		if (value instanceof Pointer)
			isPointer = true;
		setValue(value);
	}
	
	// Name of the Variable
	private String name;
	// Type of Variable
	private int type;
	// The "adress in memory" of the Variable
	int adress;
	// The value of the Variable
	Object value;
	boolean isPointer=false;
		
	String getName()
	{
		return name;
	}
	
	Pointer getAdress()
	throws ProcessorException
	{
		return new Pointer(type, adress);
	}
	
	Object getValue()
	throws ProcessorException
	{
		if (isPointer)
			return new Pointer( (Pointer)value);
		else if (value instanceof StructVar)
			return ((StructVar)value).getVariableAtOffset(0).getValue();
		else	
			switch (type)
			{
			case INTEGER:
				return new Integer(((Integer)value).intValue());
			case DOUBLE:
				return new Double(((Double)value).doubleValue());
			case CHARACTER:
				return new Character( ((Character)value).charValue());
			case STRING:
				return new String( ((String)value));
			default:
				/* Evtl können auch alle anderen Werte direkt übergeben
					werden. der Default sollte aber zunächst nur dann 
					zum Zuge kommen	wenn über eine noch nicht richtig
					implementierte Funktion	malloc() Speicher reserviert 
					wurde.*/
				return value;
			}
	}
	void setValue(Object value)
	throws ProcessorException
	{
		try {
			
			if (isPointer)
			{
				if (!(value instanceof Pointer))
				{
					if(!(value instanceof String))
						throw new ProcessorException("Variable Type Mismatch! \nType: Pointer\nValue: "+value+" !"); 
					else // Spezialfall char-array=string;
						setValueOfCharArray((String)value);
				}
				else
					this.value = value;	
			}
			else if (this.value instanceof StructVar)
			{
				if (value instanceof StructVar)
					this.value = value;
				else
					((StructVar)value).setVariableAtOffset(0, value);
			}
			else
				switch (type)
				{
				case INTEGER:
					if (!(value instanceof Integer))
						throw new ProcessorException("Variable Type Mismatch! \nType: Integer\n Value: "+value+" !"); 
					this.value = value;
					break;
				case DOUBLE:
					// bei Double wird gecastet
					if (!(value instanceof Double | value instanceof Integer))
						throw new ProcessorException("Variable Type Mismatch! \nType: Double\nValue: "+value); 
					if (value instanceof Integer)
						this.value = new Double(((Integer)value).intValue());
					else
						this.value = value;
					break;
				case CHARACTER:
					if (!(value instanceof Character))
						throw new ProcessorException("Variable Type Mismatch! \nType: Character\nValue: "+value+" !"); 
					this.value = value;
					break;
				case STRING:
					if (!(value instanceof String))
						throw new ProcessorException("Variable Type Mismatch! \nType: String\nValue: "+value+" !"); 
					this.value = value;
					break;
				case STRUCT:
					if (!(value instanceof StructVar))
						throw new ProcessorException("Variable Type Mismatch! \nType: Struct\nValue: "+value+" !"); 
					this.value = value;
				case VOID:
					this.value = value;
					break;
				default:
					this.value = value;
				}
		} catch (NumberFormatException e) 
		{ throw new ProcessorException("Number Format Exception !"); 
		}
		catch (ClassCastException cce)
		{ throw new ProcessorException("Class Cast Exception !"); 
		}
	}
	
	// Spezialfall um char-Arrays analog zu C verwenden zu können
	void setValueOfCharArray(String string)
	throws ProcessorException
	{
		Pointer pointer=new Pointer((Pointer)this.value);
		for (int i=0;i<string.length();i++)
		{	
			pointer.setValueAtAdress(new Character(string.charAt(i)));
			pointer.addToAdress(1);
		}
		// Die binäre Null
		pointer.setValueAtAdress(new Character((char)0));
	}
	
	
}
