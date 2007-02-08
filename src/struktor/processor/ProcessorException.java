// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;


/** Eine Exception die angibt, daﬂ was beim Processor schief gelaufen ist 
 */

public class ProcessorException extends struktor.StruktorException
{
	public ProcessorException(String msg)
	{
		super(msg);
	}
	
	public void addToMsg(String additional)
	{
		super.addToMsg(super.getMsg());
	}
	
}