package struktor.processor.operators;

import struktor.processor.*;


/** Diese Operator Klasse entspricht dem "*"-Operator
 */



public class Mult extends BinaryExpr {

	public Mult(Expr left, Expr right) {
		super(left, right);
	}



	public Object eval()
	throws struktor.processor.ProcessorException
	{

		// beide Operanden auswerten
		value1 = left.eval();

		value2 = right.eval();

		implicitCast();

		// * für int
		if ( value1 instanceof Integer && value2 instanceof Integer ) {
			return new Integer( ((Integer)value1).intValue()
								* ((Integer)value2).intValue() );
		}

		// * für double
		if ( value1 instanceof Double && value2 instanceof Double ) {
			return new Double( ((Double)value1).doubleValue()
								* ((Double)value2).doubleValue() );
		}
		
		// * für double*int
			if ( value1 instanceof Double && value2 instanceof Integer ) 

				return new Double( ((Double)value1).doubleValue()
								* ((Integer)value2).intValue() );
		
		// * für int*double
		if ( value1 instanceof Integer && value2 instanceof Double ) 

			return new Double( ((Integer)value1).intValue()
								* ((Double)value2).doubleValue() );

		// nicht erlaubtes Argument
		throw new ProcessorException("multiply-operator : illegal operand types");
	}



	public	String toString() {
		return "(" + left.toString() + " * " + right.toString() + ")";
	}
}
