package struktor.processor.operators;

import struktor.processor.datatypes.*;
import struktor.processor.*;   

/** Diese Operator Klasse entspricht dem "Wert--"-Operator
 */



public class PostDec extends UnaryExpr
implements Datatype {

	Memory memory;
	public PostDec(Expr operand, Memory memory) {
		super(operand);
		this.memory = memory;
	}



	public Object eval()
	throws struktor.processor.ProcessorException
	{
		Object result = operand.eval();
		Object value = new Assign(operand,new BinaryMinus(operand,new Const(new Integer(1))),memory).eval();
		return result;
	}

	public String toString() {
		return operand.toString()+"--";
	}
}