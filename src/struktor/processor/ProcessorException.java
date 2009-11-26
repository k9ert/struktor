// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;


/** Eine Exception die angibt, dass was beim Processor schief gelaufen ist 
 */

public class ProcessorException extends struktor.StruktorException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessorException(String msg)
	{
		super(msg);
	}
	
	public void addToMsg(String additional)
	{
		super.addToMsg(super.getMsg());
	}
	
}