package struktor.processor.operators; 

import struktor.Tracer;
import struktor.processor.Memory;
import struktor.processor.ProcessorException;
import struktor.processor.datatypes.Pointer;

/** Diese Operator Klasse entspricht einem LValue
 */



public class LValue extends Expr {
	
	struktor.processor.Memory memory;

	Pointer pointer;
	String variableName;
	
	public LValue(Pointer pointer, Memory mem) {
		this.pointer = pointer;
		this.memory = mem;
	}
	
	public LValue(String variable, Memory mem) 
	throws ProcessorException
	{
		Tracer.out("Entering lvalue.Constructor ...");
		this.memory = mem;
		// Ein LValue ist eigentlich ein Pointer auf Speicherplatz (wurde aber jetzt geändert)
		// Dieser Pointer wird aber aus irgendwelchen Gründen erst bei eval initialisiert
		// Auf diese Weise kann zur Laufzeit damit elegant operiert werden
		variableName=variable;
	}




	// eval wertet den Operanden aus
	// Wert des LValues wird zurückgegeben
	public Object eval() 
	throws struktor.processor.ProcessorException
	{
		try {
			if (pointer==null)
				pointer = memory.getAdressOfVariable(variableName);
			Object temp=pointer.getValueAtAdress();
			return temp;
		} catch (NullPointerException npe) 
		{
			Tracer.out("Nullpointer catched in LValue.eval");
			throw new ProcessorException(npe.toString()+"in in LValue.eval");
		}	
		 catch (ProcessorException pe) 
		{Tracer.out(pe.toString()+ "(Lvalue.eval"); throw pe;}
		
	}
	
	public Pointer getAdress()
	throws struktor.processor.ProcessorException
	{
		try {
			if (pointer==null)
				pointer = memory.getAdressOfVariable(variableName);
			try {
				return new Pointer((Pointer)pointer);
			} catch (NullPointerException npe) 
			{ System.err.println("Nullpointer in lvalue.getadress !");} 
			return null;
		} catch (ProcessorException pe) {Tracer.out(pe.toString()+ "(Lvalue.eval"); throw pe;}
			
	}
	
	public String getNameOfVariable()
	{
		return variableName;
	}
	
	public void setValue(Object newValue)
	throws struktor.processor.ProcessorException
	{
		Tracer.out("Entering LValue.setValue... "+pointer);
		if (pointer==null)
			pointer = memory.getAdressOfVariable(variableName);
		try {
			pointer.setValueAtAdress(newValue);
		} catch (NullPointerException npe) 
		{ System.err.println("Nullpointer in lvalue.setvalue !");} 	
	}

	public String toString()
	{
		
		try {
			if (pointer==null)
			pointer = memory.getAdressOfVariable(variableName);
		} catch (ProcessorException pe) { }
		if (pointer==null)
			return "null(Pointer;-)";
		else
			return pointer.toString();
	}
}



