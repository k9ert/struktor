package struktor.processor.operators;

import struktor.processor.ProcessorException;


    

/** Diese Operator Klasse entspricht dem "<="-Operator
 */



public class LessOrEqual extends BinaryExpr {

	public LessOrEqual(Expr left, Expr right) {
		super(left, right);
	}



	public Object eval()
	throws struktor.processor.ProcessorException
	{

	// beide Operanden auswerten
	value1 = left.eval();

	value2 = right.eval();
	
	implicitCast();

	// - für int
	if ( value1 instanceof Integer && value2 instanceof Integer ) 
	{
		if ( ((Integer)value1).intValue() <= ((Integer)value2).intValue())
			return new Integer(1);
		else
			return new Integer(0);	
	}

	// - für double
	if ( value1 instanceof Double && value2 instanceof Double ) 
	{
		if ( ((Double)value1).doubleValue() <= ((Double)value2).doubleValue())
			return new Double(1);
		else
			return new Double(0);
	}
	
	// Bei anderen Kombinationen soll explizit gecastet werden
	
	// nicht erlaubtes Argument
	throw new ProcessorException("lessOrEqual-operator : illegal operand types");
}

	public String toString() {
		return "("+ left.toString() + " <= " + right.toString() + ")";
	}
}
