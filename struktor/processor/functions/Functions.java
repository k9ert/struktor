// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor.functions;

import java.util.*;
import struktor.processor.*;
import struktor.*;

/** des is a Schmarrn */
public class Functions
{
	static Integer sizeof(Object[] parameter)
	throws ProcessorException
	{
		if(!(parameter[0] instanceof String))
			throw new ProcessorException("sizeof-Operator: Illegal Argument!"); 
		return new Integer(5);
	}
}
