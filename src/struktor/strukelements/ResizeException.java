// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

/** Wird geschmissen wenn ein Element kleiner gemacht werden soll
 * als es aus irgendeinem Grund gemacht werden darf
 */
public class ResizeException extends struktor.StruktorException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final public int WIDTH = 1;
	static final public int HEIGHT = 2;
	
	/** Der Restbetrag um den ein Element NICHT kleiner gemacht werden konnte
	 */
	private int rest;
	private int type;
	

	/** Konstruktor Methode
	 *
	 *
	 * @param   rest  Restbetrag
	 * @param   type  Typ (WIDTH oder HEIGHT)
	 */
	ResizeException(int rest, int type)
	{
		this.rest = rest;
		this.type = type;
	}
	
	int getRest()
	{
		return rest;
	}
	
	int getType()
	{
		return type;
	}
}