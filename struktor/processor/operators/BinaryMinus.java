package struktor.processor.operators;

import struktor.processor.datatypes.*; 
import struktor.processor.*;


/** Diese Operator Klasse entspricht dem "-"-Operator
 */



public class BinaryMinus extends BinaryExpr {

	public BinaryMinus(Expr left, Expr right) {
	super(left, right);
	}



	public Object eval()
	throws struktor.processor.ProcessorException
	{

	// beide Operanden auswerten
	value1 = left.eval();

	value2 = right.eval();
	
	implicitCast();

	// - f�r int
	if ( value1 instanceof Integer && value2 instanceof Integer ) 
	{
		return new Integer( ((Integer)value1).intValue()
							- ((Integer)value2).intValue() );
	}
	
	// - f�r double-int
	if ( value1 instanceof Double && value2 instanceof Integer ) 

		return new Double( ((Double)value1).doubleValue()
							- ((Integer)value2).intValue() );
	
	// - f�r int-double
	if ( value1 instanceof Integer && value2 instanceof Double ) 

		return new Double( ((Integer)value1).intValue()
							- ((Double)value2).doubleValue() );

	// - f�r double
	if ( value1 instanceof Double && value2 instanceof Double ) 
	{
		return new Double( ((Double)value1).doubleValue()
							- ((Double)value2).doubleValue() );
	}
	
	// - f�r Pointer
	if ( value1 instanceof Pointer && value2 instanceof Integer )
	{  
		((Pointer)value1).subFromAdress(((Integer)value2).intValue());
		return value1;
	}
	
	// Bei anderen Kombinationen soll explizit gecastet werden
	
	// nicht erlaubtes Argument
	throw new ProcessorException("minus-operator : illegal operand types");
}

	public String toString() {
		return "("+ left.toString() + " - " + right.toString() + ")";
	}
}
