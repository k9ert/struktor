package struktor.processor.operators;

    

    /** $Id: Const.java 2 2003-05-21 22:36:06 +0000 (Mi, 21 Mai 2003) kneunert $
     * eine Klasse für das binäre Plus
     *
     */



public class Const extends Expr {

	Object value;

	public Const(Object value) {
		this.value = value;
	}



	public Object eval() 
	throws struktor.processor.ProcessorException
	{
 		if (value instanceof Expr)
			return ((Expr)value).eval();
		else
			return value;
	}



    public String toString() {
		return value.toString() ;  }
    
}

