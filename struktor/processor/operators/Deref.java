package struktor.processor.operators;

import struktor.processor.datatypes.*;
import struktor.processor.*;
    

/** Diese Operator Klasse entspricht dem "*Pointer"-Operator
 */



public class Deref extends Expr {

	Object value;
	Memory memory;
	

	public Deref(Object value, Memory memory) {
		this.value = value;
		this.memory = memory;
	}



	public Object eval() 
	throws struktor.processor.ProcessorException
	{
		try {
			do {
				value  = ((Pointer)((Expr)value).eval()); 
			} while (!(value instanceof Pointer));
			Pointer adress = (Pointer)value;
			return new LValue(adress, memory);
		} catch (ClassCastException cce) 
		{
			throw new ProcessorException("dereference-operator: Operand must be a Pointer !\n type: "+value.getClass().getName()+" value: "+value);
		}
	}
	
	
    public String toString() {
		return "*" + value;  
	}
    
}
