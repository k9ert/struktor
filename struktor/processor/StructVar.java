// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;

import struktor.Tracer;
import struktor.processor.ProcessorException;
import struktor.strukelements.Dec;

public class StructVar
implements struktor.processor.datatypes.Datatype
{

	Vector values = new Vector();
	Struct struct;
	String type;
	
	StructVar(String type)
	{
		this.type = type;
		struct = StructPanel.findStruct(type);
		for (Enumeration el=struct.getDecList().elements(); el.hasMoreElements(); )
        {
			 Dec s=(Dec)el.nextElement();
			try {
				switch (s.getType())
				{
				case INTEGER:
					values.add(new Variable(s.getName(), INTEGER, new Integer(0)));
					break;
				case DOUBLE: 
					values.add(new Variable(s.getName(), DOUBLE, new Double(0)));
					break;
				case STRUCT:
					values.add(new Variable(s.getName(), STRUCT, new StructVar(s.getStructType())));
					break;
				}
			} catch(ProcessorException pe) {}
			Tracer.out("StructVar (Construktor)"+getValue(s.getName()));
        }
	
	
	}
	
	int getOffsetOfIdentifier(String name)
	{
		Tracer.out("entering Structvar:getOffsetOfIdentifier("+name+")");
		int offset=0;
		for (Enumeration el=values.elements(); el.hasMoreElements(); )
        {
			 Variable s=(Variable)el.nextElement();
			 Tracer.out("searching: "+s.getName());
			 if (s.getName().equals(name))
			 	return offset;
			else
				offset += Memory.getSpaceOfDatatype(s.value);
        }
		Tracer.out("identifier of struct not found !");
		return 0;
	}
	
	Variable getVariableAtOffset(int offset)
	{
		Tracer.out("entering Structvar:getVariableAtOffset("+offset+")");
		String identifier=struct.getIdentifierAtOffset(offset);
		
		return getVariable(identifier);
	}
	
	void setVariableAtOffset(int offset, Object newValue)
	throws ProcessorException
	{
		Variable var = getVariableAtOffset(offset);
		var.setValue(newValue);
	}

	
	String getStructType()
	{
		return type;
	}
	
	public String toString()
	{
		return "Struct!!";
	}
	
	
	
	Integer getSpace()
	{
		return struct.getSpace();
	}
	
	public Object getValue(String name)
	{
		Tracer.out("entering Structvar:getValue("+name+")");
		for (Enumeration el=values.elements(); el.hasMoreElements(); )
        {
			 Variable s=(Variable)el.nextElement();
			 if (s.getName().equals(name))
			 	return s.value;
        }
		Tracer.out("identifier of struct not found !");
		return null;
	}	
	
	public Variable getVariable(String name)
	{
		Tracer.out("entering Structvar:getVariable("+name+")");
		for (Enumeration el=values.elements(); el.hasMoreElements(); )
        {
			 Variable s=(Variable)el.nextElement();
			 if (s.getName().equals(name))
			  	return s;
        }
		Tracer.out("identifier of struct not found !");
		return null;
	}	
	
	/*class Value
	{
		String name;
		Object value;
		
		Value(String name, Object value)
		{
			this.name=name;
			this.value=value;
		}
	}*/
	
}			
