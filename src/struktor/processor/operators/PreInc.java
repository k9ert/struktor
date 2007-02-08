package struktor.processor.operators;

import struktor.processor.Memory;
import struktor.processor.datatypes.Datatype;

/** Diese Operator Klasse entspricht dem "++Wert"-Operator
 */



public class PreInc extends UnaryExpr
implements Datatype {

	Memory memory;
	public PreInc(Expr operand, Memory memory) {
		super(operand);
		this.memory = memory;
	}



	public Object eval()
	throws struktor.processor.ProcessorException
	{
		Object value = new Assign(operand,new BinaryPlus(operand,new Const(new Integer(1))),memory).eval();
		return value;
	}

	public String toString() {
		return "++"+operand.toString();
	}
}
