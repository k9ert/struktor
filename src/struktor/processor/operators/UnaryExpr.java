package struktor.processor.operators;
    
/** eine abstrakte Klasse für unäre Ausdrücke
 */



abstract class UnaryExpr extends Expr {

	// der Operanden-Ausdruck

	Expr operand;



	UnaryExpr(Expr operand) {
		this.operand = operand;
	}

}

