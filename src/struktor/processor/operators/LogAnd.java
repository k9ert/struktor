package struktor.processor.operators;

import struktor.processor.ProcessorException;
    

/** Diese Operator Klasse entspricht dem "&&"-Operator
 */



public class LogAnd extends BinaryExpr {

	public LogAnd(Expr left, Expr right) {
		super(left, right);
	}



	public Object eval()
	throws struktor.processor.ProcessorException
	{

	// beide Operanden auswerten
	value1 = left.eval();

	value2 = right.eval();
	
	implicitCast();

	// LOGAND für int, int
	if ( value1 instanceof Integer && value2 instanceof Integer ) 
	{
		if ( ((Integer)value1).intValue() != 0 && ((Integer)value2).intValue() != 0)
			return new Integer(1);
		else
			return new Integer(0);	
	}

	// LOGAND für double, double
	if ( value1 instanceof Double && value2 instanceof Double ) 
	{
		if ( ((Double)value1).doubleValue() != 0 && ((Double)value2).doubleValue()!=0)
			return new Double(1);
		else
			return new Double(0);
	}
	
	// LOGAND für int, double
	if ( value1 instanceof Integer && value2 instanceof Double ) 
	{
		if ( ((Integer)value1).intValue() != 0 && ((Double)value2).doubleValue()!=0)
			return new Double(1);
		else
			return new Double(0);
	}
	
	// LOGAND für double, int
	if ( value1 instanceof Double && value2 instanceof Integer ) 
	{
		if ( ((Double)value1).doubleValue() != 0 && ((Integer)value2).intValue()!=0)
			return new Double(1);
		else
			return new Double(0);
	}
	
	
	// nicht erlaubtes Argument
	throw new ProcessorException("logical and: illegal operand types !");
}

	public String toString() {
		return "("+ left.toString() + " && " + right.toString() + ")";
	}
}
