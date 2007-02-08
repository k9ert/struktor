// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;

/** Eine Klasse f�r Breaks 
 *  @see LoopControlException
 */

public class BreakException extends LoopControlException
{
	public BreakException()
	{
	}

	public BreakException(Integer level)
	{
		super(level);
	}
	
}