package struktor.processor.operators;

import struktor.processor.ProcessorException;

/** eine Klasse für das unäre Plus
 */



public
class UnaryPlus extends UnaryExpr {



	public UnaryPlus(Expr operand) {
		super(operand);
	}



	// eval wertet den Operanden aus
	// und beläßt den Wert
	// Operation erlaubt für int und double Werte

	public Object eval()
	throws struktor.processor.ProcessorException
	{

		// operand auswerten

		Object value = operand.eval();

		// + für int

		if ( value instanceof Integer )
			return new Integer( ((Integer)value).intValue());

		// + für double

		if ( value instanceof Double ) 
			return new Double( ((Double)value).doubleValue());

		// nicht erlaubtes Argument

		throw new ProcessorException("unaryPlus-operator : illegal operand type");
	}



	public String toString() {
		return "+(" + operand.toString() + ")";
	}
}


