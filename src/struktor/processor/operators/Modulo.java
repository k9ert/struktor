package struktor.processor.operators;

import struktor.processor.ProcessorException;


    
/** Diese Operator Klasse entspricht dem "%"-Operator
 */



public class Modulo extends BinaryExpr {

	public Modulo(Expr left, Expr right) {
		super(left, right);
	}

	public Object eval()
	throws struktor.processor.ProcessorException
	{

	    // beide Operanden auswerten
        value1 = left.eval();

        value2 = right.eval();

        // / für int
        if ( value1 instanceof Integer && value2 instanceof Integer ) {
          return new Integer( ((Integer)value1).intValue()
		  						% ((Integer)value2).intValue() );
		}


		// nicht erlaubtes Argument
        throw new ProcessorException("modulo-operator : illegal operand types");
      }



      public String toString() {
        return "(" + left.toString() + " % " + right.toString() + ")";
      }
}
