// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor;

/** Die Tracerklasse hat bis jetzt nur ganz rudimentäres Verhalten 
 */
 
public class Tracer 
{
	public static void out(String output)
	{
		if (Struktor.idebug)
			if (!output.startsWith("#"))
				System.out.println(output);
	}
	
	public static void err(String output)
	{
		System.out.println(output);
	}
	
	public static void out(Exception e)
	{
		out(e.toString());
	}
	// System.out
}