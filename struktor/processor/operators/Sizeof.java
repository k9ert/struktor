package struktor.processor.operators;

import struktor.processor.datatypes.*;
import struktor.processor.*;
    

/** Diese Operator Klasse entspricht dem "sizeof()"-Operator
 */



public class Sizeof extends Expr {

	Object value;
	Memory memory;
	

	public Sizeof(String value, Memory memory) {
		this.value = value;
		this.memory = memory;
	}



	public Object eval() 
	throws struktor.processor.ProcessorException
	{
		if(value.equals("int"))
			return new Integer(4);
		else if(value.equals("double"))
			return new Integer(8);
		else if(value.equals("char"))
			return new Integer(1);
		else if(value.equals("int*"))
			return new Integer(4);	
		else if(value.equals("double*"))
			return new Integer(4);	
		else if(value.equals("char*"))
			return new Integer(4);	
		else 
			throw new struktor.processor.ProcessorException(" sizeof-Operator: Illegal Argument!");
	}
	
	
    public String toString() {
		return "sizeof(" + value+")";  
	}
    
}
