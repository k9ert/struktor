// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

public interface Constants
{
	// Kopiert nach ProcParser.cup (weil man im Parser nicht implementen kann)
	static public final int COMMAND = 0;
	static public final int HEADLOOP = 1;
	static public final int TAILLOOP = 2;
	static public final int FORLOOP = 3;
	static public final int CONDITION = 4;
	static public final int SWITCH = 5; // Achtung gleicher Wert wie in CommandTypes
	
	static public final int NEXT = 0;
	static public final int INSIDE = 1;
	static public final int ALT1 = 2;
	static public final int ALT2 = 3;
	static public final int ALT = 4;
	
}
	