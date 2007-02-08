package struktor.processor.operators;
    
/** eine abstrakte Klasse f�r un�re Ausdr�cke
 */



abstract class UnaryExpr extends Expr {

	// der Operanden-Ausdruck

	Expr operand;



	UnaryExpr(Expr operand) {
		this.operand = operand;
	}

}

