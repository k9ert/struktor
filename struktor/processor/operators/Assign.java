package struktor.processor.operators;

import struktor.processor.datatypes.*;
import struktor.processor.*;   

/** Diese Operator Klasse entspricht dem "="-Operator
 */



public class Assign extends BinaryExpr implements Datatype {

	Memory memory;
	public Assign(Expr left, Expr right, Memory memory) {
		super(left, right);
		this.memory = memory;
	}



	public Object eval()
	throws struktor.processor.ProcessorException
	{
	
		// rechten Operanden auswerten (linker muß LValue, Part oder Deref sein)
		Object value2 = right.eval();
		while (value2 instanceof Deref||value2 instanceof LValue || value2 instanceof Part)
			value2=((Expr)value2).eval();	
		
		Object value1;	
		if (left instanceof Deref)
		{
			value1 = (LValue)((Expr)left).eval();	
		}
		else if (left instanceof Part)
			value1 = ((Part)left).getLValue(); 
		else
			value1 = (LValue)left;

		// Assign für LValue
		if ( value1 instanceof LValue)
		{  
			((LValue)value1).setValue(value2);
			return value2;
		}
		if ( value1 instanceof Part)
		{
			((Part)value1).setValue(value2);
			return value2;
		}
	
		// nicht erlaubtes Argument
		throw new ProcessorException("assign: illegal operand types: value1: "+value1.getClass().getName()+" value2: "+value1.getClass().getName());
	}

	public String toString() {
		return left.toString() + " = " + right.toString() ;
	}
}
