package struktor.processor.operators;

import struktor.processor.datatypes.*;
import struktor.processor.*;   

/** Diese Operator Klasse entspricht dem "--Wert"-Operator
 */



public class PreDec extends UnaryExpr
implements Datatype {

	Memory memory;
	public PreDec(Expr operand, Memory memory) {
		super(operand);
		this.memory = memory;
	}



	public Object eval()
	throws struktor.processor.ProcessorException
	{
		Object value = new Assign(operand,new BinaryMinus(operand,new Const(new Integer(1))),memory).eval();
		return value;
	}

	public String toString() {
		return "--"+operand.toString();
	}
}