// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;

/** Diese abstrakte Klasse b�ndelt Break und Continue Exceptions die
 *  Im Struktogramm zb. bei "break;" geworfen werden (Eigentlich m��ten
 *  Die Klassen eher "Throwable" implementieren anstatt von Exception
 *  abgeleitet zu sein, aber das war mir zu kompliziert)
 */

public abstract class LoopControlException extends struktor.StruktorException
{
	private int level=1;
	
	public LoopControlException()
	{
	}

	public LoopControlException(Integer level)
	{
		this.level = level.intValue();
	}
	
	public void catched()
	{
		--level;
	}
	
	public int getLevel()
	{
		return level;
	}

}