package struktor.processor.operators;

import struktor.processor.ProcessorException;

/** eine Klasse f�r das un�re Plus
 */



public
class UnaryPlus extends UnaryExpr {



	public UnaryPlus(Expr operand) {
		super(operand);
	}



	// eval wertet den Operanden aus
	// und bel��t den Wert
	// Operation erlaubt f�r int und double Werte

	public Object eval()
	throws struktor.processor.ProcessorException
	{

		// operand auswerten

		Object value = operand.eval();

		// + f�r int

		if ( value instanceof Integer )
			return new Integer( ((Integer)value).intValue());

		// + f�r double

		if ( value instanceof Double ) 
			return new Double( ((Double)value).doubleValue());

		// nicht erlaubtes Argument

		throw new ProcessorException("unaryPlus-operator : illegal operand type");
	}



	public String toString() {
		return "+(" + operand.toString() + ")";
	}
}


