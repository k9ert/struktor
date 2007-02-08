// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;

/** Eine Klasse für Continues
 *  @see LoopControlException
 */


public class ContinueException extends LoopControlException
{
	public ContinueException()
	{
	}

	public ContinueException(Integer level)
	{
		super(level);
	}
	
}