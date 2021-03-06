// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;

/** Diese abstrakte Klasse ermöglicht analog zu den LoopControlExceptions
 *  einen Return im Struktogrammcode. (Eigentlich müsste
 *  die Klasse eher "Throwable" implementieren anstatt von Exception
 *  abgeleitet zu sein, aber das war mir zu kompliziert)
 */

public class ReturnException 
extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object returnValue = new Object();
	
	public ReturnException()
	{
	}

	public ReturnException(Object returnValue)
	{
		this.returnValue = returnValue;
	}
	
	public Object getReturnValue()
	{
		return returnValue;
	}
	
	public String toString()
	{
		return "return-Statement in main-Function ? \n(this is forbidden)\nReturning Value: "+returnValue;
	}
}