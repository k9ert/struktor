// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor.datatypes;

public interface Datatype
{
	// Irgendwie ist das nicht sehr konsistent aber ... ??!!
	// Void wird noch nicht benutzt ...
	public static final int VOID=0;
	public static final int INTEGER=1;
	public static final int DOUBLE=2;
	public static final int CHARACTER=3;
	public static final int STRING=4;
	public static final int POINTER=5;
	// Wird im Moment noch nicht genutzt ...
	public static final int STRUCT=6;
	public static final int POINTERTOINT=POINTER+5*INTEGER;
	public static final int POINTERTODOUBLE=POINTER+5*DOUBLE;
	public static final int POINTERTOTOCHAR=POINTER+5*CHARACTER;
	public static final int POINTERTOSTRING=POINTER+5*STRING;
	public static final int POINTERTOPOINTER=POINTER+5*POINTER;
}
