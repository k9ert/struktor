// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor.datatypes;

import struktor.processor.*;
import struktor.Tracer;

/** Modelliert einen Zeiger (Pointer). Sehr zentraler Datentyp !!
 * wird ständig beim ausführen instanziert da fast alles über Zeiger abgewickelt wird
 */
public class Pointer implements Datatype
{
	/** zum bequemen ein und aussschalten von String-Pointern */
	static private final boolean allowStringPointers = false; 
		
	private int typeOfPointer;
	private int adress;
	
	/** Construktor-Methode
	 * @param   typeOfPointer  
	 * @param   adress 
	 * @exception   ProcessorException   
	 */
	public Pointer(int typeOfPointer, Integer adress)
	throws ProcessorException
	{
		this(typeOfPointer, adress.intValue());
	}
	

	/** Construktor-Methode
	 * @param   typeOfPointer  
	 * @param   adress 
	 * @exception   ProcessorException   
	 */
	public Pointer(int typeOfPointer, int adress)
	throws ProcessorException
	{
		checkType(typeOfPointer);
		this.typeOfPointer = typeOfPointer;
		this.adress = adress;
	}
	

	/** Construktor-Methode: Dubliziert einen Pointer (könnte wohl auch über .clone implementiert werden)
	 * @param   pointer
	 * @exception   ProcessorException    
	 */
	public Pointer(Pointer pointer)
	throws ProcessorException
	{
		this(pointer.getTypeOfPointer(), pointer.getAdress());
	}
	
	

	/** Constructor-Methode: Erzeugen eines Pointers mithilfe einer "Hexzahl" als String
	 * @param   typeOfPointer  
	 * @param   adress  
	 * @exception   ProcessorException  
	 */
	public Pointer(int typeOfPointer, String adress)
	throws ProcessorException
	{
		checkType(typeOfPointer);
		this.typeOfPointer = typeOfPointer;
		// "0x" wegschneiden
		String temp = adress.substring(2, adress.length());
		// Hexzahl parsen ...
		Integer temp2 = new Integer(Integer.parseInt(temp,16));
		this.adress = temp2.intValue();
	}
	
	
	

	/** Gibt den Typ des Pointers zurück
	 * @return Typ des Pointers
	 */
	public int getTypeOfPointer()
	{
		return typeOfPointer;
	}
	

	/** Gibt den Wert an der Adresse zurück: Delegiert an Memory
	 * @return Object an Adresse     
	 * @exception   ProcessorException  
	 */
	public Object getValueAtAdress()
	throws ProcessorException
	{
		try {
		return Memory.getValueAtAdress(new Pointer(this));	
		} catch (ProcessorException pe) 
		{Tracer.out(pe.toString()+ "(Pointer.getValueAtAdress()"); throw pe;}
		catch (NullPointerException npe) 
		{Tracer.out(npe.toString()+ "(Pointer.getValueAtAdress()");throw npe;}
	}
	

	/** Speichert das Object an der Adresse auf die der Pointer weist: Delegation an Memory
	 * @param   value  
	 * @exception   ProcessorException  
	 */
	public void setValueAtAdress(Object value)
	throws ProcessorException
	{
		Memory.setValueAtAdress(new Pointer(this), value);	
	}
		
	

	/** Gibt die Adresse als int zurück. Ist Default und wird nur von anderen Pointern in der Equals-Methode benutzt
	 * @return die Adresse als int     
	 */
	int getAdress() 
	{
		return adress;
	}
	

	/** Gibt true zurück, wenn das Pointerargument den gleichen Typ und die gleiche Adresse haben
	 * @param   pointer  
	 * @return true bei gleicher Adresse und Typ     
	 */
	public boolean equals(Pointer pointer)
	{
		if (pointer.getAdress() == this.adress)
		{
			if (pointer.getTypeOfPointer() == this.typeOfPointer)
				return true;
			else
				return false;	
		}
		else
			return false;
	}
	
	public boolean isBiggerAs(Pointer pointer)
	{
		if (pointer.getAdress() < this.adress)
			return true;
		else
			return false;
	}	
			
	/** Pointerarithemtik: addiert entsprechend des Typs des Pointers den offset
	 * @param   offset  
	 */
	public void addToAdress(int offset)
	{
		switch (typeOfPointer)
		{
		case INTEGER:
			adress = adress+offset*4;
			break;
		case DOUBLE:
			adress = adress+offset*8;
			break;
		case CHARACTER:
			adress = adress+offset;
			break;
		default:
			adress += offset;	
		}
	}
	

	/** Pointerarithemtik: subtrahiert entsprechend des Typs des Pointers den offset
	 * @param   offset  
	 */
	public void subFromAdress(int offset)
	{
		switch (typeOfPointer)
		{
		case INTEGER:
			adress = adress-offset*4;
			break;
		case DOUBLE:
			adress = adress-offset*8;
			break;
		case CHARACTER:
			adress = adress-offset;
			break;	
		}
	}
	
	
	
	private void checkType(int type)
	throws ProcessorException
	{
		if (!allowStringPointers)
			if (typeOfPointer == STRING)
				throw new ProcessorException("No Pointer Arithmetics with Strings !");	
	}
	
	/** Gibt die Adresse im Hexformat (vorangestelltes '0x') als String aus
	 * @return Adresse im Hexformat     
	 */
	public String toString()
	{
		return "0x"+Integer.toString(adress, 16);
	}
}	
				
	
	
