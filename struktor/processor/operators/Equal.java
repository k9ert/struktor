package struktor.processor.operators;

import struktor.processor.datatypes.*;
import struktor.processor.*;


/** Diese Operator Klasse entspricht dem "=="-Operator
 */



public class Equal extends BinaryExpr {

	public Equal(Expr left, Expr right) {
		super(left, right);
	}



	public Object eval() 
	throws struktor.processor.ProcessorException
	{

	// beide Operanden auswerten
	value1 = left.eval();

	value2 = right.eval();
	
	implicitCast();

	// == für int
	if ( value1 instanceof Integer && value2 instanceof Integer ) 
	{
		if ( ((Integer)value1).intValue() == ((Integer)value2).intValue())
			return new Integer(1);
		else
			return new Integer(0);	
	}

	// == für double
	else if ( value1 instanceof Double && value2 instanceof Double ) 
	{
		if ( ((Double)value1).doubleValue() == ((Double)value2).doubleValue())
			return new Double(1);
		else
			return new Double(0);
	}
	
	// == für double == int
	/*else if ( value1 instanceof Double && value2 instanceof Integer ) 
	{
		if ( ((Double)value1).doubleValue() == ((Integer)value2).intValue())
			return new Double(1);
		else
			return new Double(0);
	}
	
	// == für int == double
	else if ( value1 instanceof Integer && value2 instanceof Double ) 
	{
		if ( ((Integer)value1).intValue() == ((Double)value2).doubleValue())
			return new Double(1);
		else
			return new Double(0);
	}*/
	
	// == für Pointer
	if ( value1 instanceof Pointer && value2 instanceof Pointer ) 
	{
		if ( ((Pointer)value1).equals((Pointer)value2))
			return new Double(1);
		else
			return new Double(0);
	}
	
	// == für Characters
	if ( value1 instanceof Character && value2 instanceof Character ) 
	{
		if ( ((Character)value1).charValue() == ((Character)value2).charValue())
			return new Double(1);
		else
			return new Double(0);
	}
	
	// == für Strings
	if ( value1 instanceof String && value2 instanceof String ) 
	{
		if ( ((String)value1).equals((String)value2))
			return new Integer(1);
		else
			return new Integer(0);
	}
	
	
	
	
	// Bei anderen Kombinationen soll explizit gecastet werden
	
	// nicht erlaubtes Argument
	throw new ProcessorException("equal-operator : illegal operand types");
}

	public String toString() {
		return "("+ left.toString() + " = " + right.toString() + ")";
	}
}
