package struktor.processor.operators;

/** Diese Operator Klasse entspricht dem "(double)intWert"-Operator
 */
import struktor.processor.*;



public class DoubleToInt extends UnaryExpr {

	public DoubleToInt(Expr operand) {
		super(operand);
	}



	// eval wertet den Operanden aus
	// und konvertiert ihn nach int

	public Object eval() 
	throws struktor.processor.ProcessorException
	{

		// operand auswerten
        Object value = operand.eval();

		// double to int Konversion

		if ( value instanceof Double ) {
			return new Integer( ((Double)value).intValue() );
        }


		if ( value instanceof Integer ) {
			return value;
        }

        // nicht erlaubtes Argument

        throw new ProcessorException("int-cast: illegal operand type");
      }

	public String toString() {
		return "((int)" + operand.toString() + ")";

	}
}