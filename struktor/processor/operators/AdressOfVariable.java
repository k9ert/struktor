package struktor.processor.operators;

import struktor.processor.Memory;
import struktor.processor.ProcessorException;
import struktor.processor.datatypes.Pointer;
    

/** Diese Operator Klasse entspricht dem "&"-Operator
 */



public class AdressOfVariable extends Expr {

	Object value;
	Memory memory;
	int typeOfPointer;

	public AdressOfVariable(Object value, Memory memory) {
		this.value = value;
		this.memory = memory;
	}



	public Object eval() 
	throws struktor.processor.ProcessorException
	{
 		if (!(value instanceof LValue))
			value = ((Expr)value).eval(); 
		if (value instanceof LValue)
			return ((LValue)value).getAdress();
		// Falls die Adresse vom Benutzer angefordert wurde, soll
		// Pointer Stress machen (oder nicht)
		if (value instanceof String)
			return memory.getAdressOfVariable((String)value);
		if(value instanceof Pointer)
			return value;
		throw new ProcessorException("adress-operator: Operand must be a L-Value !\n Type: "+ value.getClass().getName());
	}
	
	
    public String toString() {
		return "&" + value;  
	}
    
}
