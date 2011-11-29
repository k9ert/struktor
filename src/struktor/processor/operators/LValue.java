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
	
	/** for inheritance enabling
	 * 
	 */
	public LValue() {
		throw new RuntimeException("not supported");
	}
	
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
			Object temp=getPointer().getValueAtAdress();
			return temp;
		} catch (NullPointerException npe) 
		{
			Tracer.out("Nullpointer catched in LValue.eval");
			throw new ProcessorException(npe.toString()+"in in LValue.eval");
		}	
		 catch (ProcessorException pe) 
		{Tracer.out(pe.toString()+ "(Lvalue.eval"); throw pe;}
		
	}
	
	/**
	 * 
	 * @return a Pointer pointing to the memory-position of this LValue
	 * @throws ProcessorException
	 */
	Pointer getAdressOfLValue() throws ProcessorException {
		return memory.getAdressOfVariable(variableName);
	}
	
	public Pointer getAdress()
	throws struktor.processor.ProcessorException
	{
		try {
			try {
				return new Pointer(getPointer());
			} catch (NullPointerException npe) 
			{ System.err.println("Nullpointer in lvalue.getadress !");} 
			return null;
		} catch (ProcessorException pe) {Tracer.out(pe.toString()+ "(Lvalue.eval"); throw pe;}
			
	}

	public void setValue(Object newValue)
	throws struktor.processor.ProcessorException
	{
		Tracer.out("Entering LValue.setValue... "+getPointer());
		try {
			getPointer().setValueAtAdress(newValue);
		} catch (NullPointerException npe) 
		{ System.err.println("Nullpointer in lvalue.setvalue !");} 	
	}
	
	public String getNameOfVariable()
	{
		return variableName;
	}
	
	public struktor.processor.Memory getMemory() {
		return memory;
	}

	private Pointer getPointer() throws ProcessorException {
		if (pointer==null)
			pointer = getAdressOfLValue();
		return pointer;
	}

	public String toString()
	{
		try {
			if (getPointer()==null)
				return "null(Pointer;-)";
			else
				return pointer.toString();
		} catch (ProcessorException e) {
			return "ProcessorException while toString() of LValue";
		}
	}
}



