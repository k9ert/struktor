package struktor.processor.operators;

import struktor.processor.ProcessorException;

    

/** Diese Operator Klasse entspricht dem "/"-Operator
 */



public class Divide extends BinaryExpr {

	public Divide(Expr left, Expr right) {
		super(left, right);
	}

	public Object eval()
	throws struktor.processor.ProcessorException
	{

	    // beide Operanden auswerten
        value1 = left.eval();

        value2 = right.eval();
		implicitCast();
		try {
	        // / für int
	        if ( value1 instanceof Integer && value2 instanceof Integer ) {
	          return new Integer( ((Integer)value1).intValue()
			  						/ ((Integer)value2).intValue() );
			}
			// / für double
			if ( value1 instanceof Double && value2 instanceof Double ) {
				return new Double( ((Double)value1).doubleValue()
									/ ((Double)value2).doubleValue() );
	        }
		} catch (ArithmeticException aee) {throw new ProcessorException("not allowed: Division by zero !");}
			

        // nicht erlaubtes Argument
        throw new ProcessorException("divide-operator: illegal operand types");
      }



      public String toString() {
        return "(" + left.toString() + " / " + right.toString() + ")";
      }
}
