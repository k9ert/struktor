package struktor.processor.operators; 

import struktor.processor.ProcessorException;


/** Diese Operator Klasse entspricht dem "!"-Operator
 */



public class LogNot extends UnaryExpr {



	public LogNot(Expr operand) {
		super(operand);
	}



	// eval wertet den Operanden aus
	// und negiert den Wert
	// Operation erlaubt für int und double Werte

	public Object eval()
	throws struktor.processor.ProcessorException
	{

		// operand auswerten

		Object value = operand.eval();

		// NOT für int

		if ( value instanceof Integer )
		{
			if ( ((Integer)value).intValue() != 0)
				return new Integer(0);
			else
				return new Integer(1);
		}	

		// NOT für double

		if ( value instanceof Double ) 
		{
			if ( ((Double)value).doubleValue() != 0)
				return new Double(0);
			else
				return new Double(1);
		}
		
		//NOT für Character
		
		if ( value instanceof Character ) 
		{
			if ( ((Character)value).charValue() != 0)
				return new Character('\0');
			else
				return new Character('\1');
		}
			

		// nicht erlaubtes Argument

		throw new ProcessorException("logical not : illegal operand type");
	}



	public String toString() {
		return "!" + operand.toString() ;
	}
}


