package struktor.processor.operators;

import struktor.processor.*;


/** Diese Operator Klasse entspricht dem "(int)doubleWert"-Operator
 */

public class IntToDouble extends UnaryExpr {

	public IntToDouble(Expr operand) {
		super(operand);
	}

	// eval wertet den Operanden aus
	// und konvertiert ihn nach double

	public Object eval()
	throws struktor.processor.ProcessorException
	{

		// operand auswerten

		Object value = operand.eval();

		// int to double Konversion

		if ( value instanceof Integer )
			return new Double( ((Integer)value).doubleValue() );
		
		if ( value instanceof Double ) {
			return	value;
	}

	// nicht erlaubtes Argument

	throw new ProcessorException("double-cast: illegal operand type");
	}

	public String toString() {
		return "((double)" + operand.toString() + ")";
	}
}