package struktor.processor.operators;

import struktor.processor.datatypes.Pointer;

    

/** Diese Operator Klasse entspricht dem "+"-Operator
 */



public class BinaryPlus extends BinaryExpr {

	public BinaryPlus(Expr left, Expr right) {
		super(left, right);
	}

	public Object eval()
	throws struktor.processor.ProcessorException
	{

		// beide Operanden auswerten
		value1 = left.eval();

		value2 = right.eval();
		
		implicitCast();

		// + für int
		if ( value1 instanceof Integer && value2 instanceof Integer ) 
			return new Integer( ((Integer)value1).intValue()
								+ ((Integer)value2).intValue() );

		// + für double
		if ( value1 instanceof Double && value2 instanceof Double ) 

			return new Double( ((Double)value1).doubleValue()
								+ ((Double)value2).doubleValue() );
		
		// + für double+int
		if ( value1 instanceof Double && value2 instanceof Integer ) 

			return new Double( ((Double)value1).doubleValue()
								+ ((Integer)value2).intValue() );
		
		// + für int+double
		if ( value1 instanceof Integer && value2 instanceof Double ) 

			return new Double( ((Integer)value1).intValue()
								+ ((Double)value2).doubleValue() );	

		// + für Pointer
		if ( value1 instanceof Pointer && value2 instanceof Integer )
		{  
			((Pointer)value1).addToAdress(((Integer)value2).intValue());
			return value1;
		}
		
		// + für String
		if ( value1 instanceof String && value2 instanceof String ) 

			return new String( ((String)value1).toString()
								+ ((String)value2).toString() );
		
		// + für String+Number
		if ( value1 instanceof String && value2 instanceof Number ) 

			return new String( ((String)value1).toString()
								+ ((Number)value2).toString() );
		
		// + für String+Number
		if ( value1 instanceof Number && value2 instanceof String ) 

			return new String( ((Number)value1).toString()
								+ ((String)value2).toString() );		

		// Andere Kombinationen sollen gecastet werden

		// nicht erlaubtes Argument
		throw new IllegalArgumentException("plus-operator : illegal operand types");
	}

	public String toString() {
		return "(" + left.toString() + " + " + right.toString() + ")";
	}
}
