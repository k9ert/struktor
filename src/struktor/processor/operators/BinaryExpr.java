package struktor.processor.operators;

import struktor.processor.ProcessorException;

/** eine abstrakte Klasse für binäre Ausdrücke
 */



abstract class BinaryExpr extends Expr 
{

	// die Operanden

	Expr left;


	Expr right;
	
	// für alle Operatoren ausser assign (bis jetzt)
	Object value1;
	Object value2;


	// der Konstruktor ist default
	// da er nur von Subklassen aus aufgerufen wird

	BinaryExpr(Expr left, Expr right) {
		this.left  = left;
		this.right = right;
	}
	
	void implicitCast()
	throws ProcessorException
	{
		/* Die ersten zwei Umwandlungen sind keine wirklichen impliziten
			Casts! Aber hier hab ich Sie trotzdem erstmal untergebracht !
			*/
		if (value1 instanceof LValue)
			value1 = ((LValue)value1).eval();
		if (value2 instanceof LValue)
			value2 = ((LValue)value2).eval();	
		if (value1 instanceof Integer && value2 instanceof Double)
			value1 = new Double(((Integer)value1).intValue());
		if (value1 instanceof Double && value2 instanceof Integer)
			value2 = new Double(((Integer)value2).intValue());
		if (value1 instanceof Character && value2 instanceof Integer)
			value1 = new Integer(((Character)value1).charValue());
		if (value1 instanceof Integer && value2 instanceof Character)
			value2 = new Integer(((Character)value2).charValue());		
	}

}

