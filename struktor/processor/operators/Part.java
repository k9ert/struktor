package struktor.processor.operators; 

import struktor.Tracer;
import struktor.processor.Memory;
import struktor.processor.ProcessorException;
import struktor.processor.datatypes.Pointer;

/** Diese Operator Klasse entspricht dem "."-Operator (Strukturen)
 */



public class Part extends Expr {
	
	struktor.processor.Memory memory;

	Pointer pointer;
	String identifier;
	String variable;
	
	public Part(Pointer pointer, Memory mem) {
		this.pointer = pointer;
		this.memory = mem;
	}
	
	public Part(Expr variable, String identifier, Memory mem) 
	throws ProcessorException
	{
		Tracer.out("emtering part_Konstruktor("+variable+","+identifier+")");
		this.memory = mem;
		this.variable = ((LValue)variable).getNameOfVariable();
		pointer = ((LValue)variable).getAdress();
		this.identifier = identifier;
		pointer = Memory.getAdressOfStructIdentifier(this.variable,identifier);
		
	}




	// eval wertet den Operanden aus
	// Wert des LValues wird zurückgegeben
	public Object eval() 
	throws struktor.processor.ProcessorException
	{
		Tracer.out("Entering part.eval ...");
		Tracer.out("the Pointers Value: "+pointer);
		Object temp=pointer.getValueAtAdress();
		Tracer.out("(Part)returning value: "+temp);
		return temp;
	}
	
	public Pointer getAdress()
	throws struktor.processor.ProcessorException
	{
		try {
			return new Pointer((Pointer)pointer);
		} catch (NullPointerException npe) 
		{ System.err.println("Nullpointer in lvalue.getadress !");} 
		return null;	
	}
	
	public void setValue(Object newValue)
	throws struktor.processor.ProcessorException
	{
		try {
			Pointer pointer = Memory.getAdressOfStructIdentifier(variable,identifier);
			pointer.setValueAtAdress(newValue);
		} catch (NullPointerException npe) 
		{ System.err.println("Nullpointer in lvalue.setvalue !");} 	
	}
	
	public LValue getLValue()
	throws struktor.processor.ProcessorException
	{
		Pointer pointer = Memory.getAdressOfStructIdentifier(variable,identifier);
		return new LValue(pointer, memory);
	}

	public String toString() {
		return pointer.toString();
	}
}



