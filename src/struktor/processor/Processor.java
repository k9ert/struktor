// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;

// Driver for parser
import java.io.StringReader;
import java.util.Vector;

import java_cup.runtime.Symbol;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import struktor.Struktor;
import struktor.Tracer;
import struktor.TurtleCanvas;
import struktor.Utils;
import struktor.processor.datatypes.Datatype;
import struktor.processor.datatypes.Pointer;
import struktor.strukelements.CommandTypes;

/** Diese Klasse modelliert den Prozessor. Wichtigste Methoden sind
 *  die unterschiedlichen Parse-Methoden denen jeweils (unter anderem)
 *  ein Ausdruck übergeben wird, der dann ausgewertet wird.
 *  @see Memory 
 */
public class Processor 
implements CommandTypes, Datatype
{
  	static public Struktor struktor;
	static private Processor actualProcessor;
	private ProcParser parser_obj = new ProcParser();
	// Nur für ExprCalc (Wird sonst nicht benötigt)
	private Memory mem;
	java.applet.Applet applet;
	TurtleCanvas gOutput;
	JTextArea tOutput;
		
	private boolean valueChanged = true;
	
	static Processor getActualProcessor()
	{
		return actualProcessor;
	}
		
	public Processor(Struktor struktor, Vector decList, TurtleCanvas gOutput, JTextArea tOutput)
	throws InterruptedException
	{
		if (false)
			;
		else
		{
			Processor.struktor = struktor;
			actualProcessor = this;
			this.gOutput = gOutput;
			this.tOutput = tOutput;
			/* create a Memory-Object (Referenz ist über statische Methode erreichbar) */
	    	try {
				mem = new Memory(decList);
	    	} catch (ProcessorException pe) 
	    	{	pe.addToMsg(" \n(correct your Declaration!)"); 
				pe.showMsg(Utils.getFrame(struktor));
				throw new InterruptedException();
			}
		}
	}
	
	public Memory getMemoryByExprCalc(struktor.ExprCalc temp)
	{
		return mem;
	}


	/** Die private interne parse-Methode, die alle Exceptions weitergibt. Wird
	 *  von allen anderen Parse-Methode (direkt oder indirekt) aufgerufen
	 * @param   s  
	 * @return Das Resultat des geparsten Ausdrucks    
	 * @exception   Exception  
	 */
	private Object internalParse(String s) 
	throws Exception
	{
  		/* Set up new scanner */
    	parser_obj.setScanner(new Yylex(new StringReader(s)));

		/* open input files, etc. here */
		Symbol parse_tree = null;
		if (Struktor.idebug)
		  parse_tree = parser_obj.debug_parse();
		else
		  parse_tree = parser_obj.parse();
		Tracer.out("Processor: Return value is "+parse_tree.value);
		return parse_tree.value;
	}
	
	/** Eine spezielle Parse-Methode für die Watchlist. Werden
	 *  bei den geparsten Ausdrücken Werte geändert, gibts Ärger
	 * @param   s  
	 * @return Der Wert der Variablen    
	 * @exception   StruktorException  
	 */
	public String watchlistParse(String s) 
	throws struktor.StruktorException
	{
		Object result = null;
		resetValuesChangedFlag();
		try {
			if (! (  (s == null) | s.equals("")) )
				result = internalParse(s + ";");
			else
				return "(not calcuable)";	
		} catch (ProcessorException ex) { 
				return "(not calcuable)"; }
		catch (BreakException be) { throw new struktor.StruktorException("break-Statement not allowed in Watchlist !");}
		catch (ContinueException ce) { throw new struktor.StruktorException("continue-Statement not allowed in Watchlist !");}
		catch (ReturnException re) { throw new struktor.StruktorException("return-Statement not allowed in Watchlist !");}
		catch (Exception e) { throw new struktor.StruktorException("undefined error:\n"+e.toString());}
		if (getValuesChangedFlag())
			throw new struktor.StruktorException("Changing Values is not allowed in Watchlist !");
		return result.toString();
	}
	

	/** Diese Parse-Methode wird nur von der For Loop (und Klassenintern)
	 *  aufgerufen. Kann glaub ich noch geändert werden, dann kann Sie 
	 *  private werden
	 * @param   s  
	 * @return Der Wert des Ausdrucks   
	 * @exception   InterruptedException  
	 * @exception   LoopControlException  
	 * @exception   ReturnException  
	 */
	public Object parse(String s) 
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException
	{
  		Object result=null;
		try {
			result=internalParse(s);
		// das catchen und anschließende rethrown muss sein !
		} catch (ProcessorException pe) {throw pe;}
		catch (LoopControlException lce) {throw lce;}
		catch (InterruptedException ie) {throw ie;}
		catch (ReturnException re) {throw re;}
		catch (Exception e) {throw new ProcessorException(e.toString());}
		return result;
	}
	
	/** Eine von Zwei (öffentlichen) parse-Methoden. Diese hier wird von den StrukElementen aufgerufen um Input/Output implementieren zu können. ruft seinerseits die andere parse-Methode auf
	 * @see parse
	 * @param   label  
	 * @param   action (INPUT,OUTPUT,CALCULATION)  
	 * @param   adMessage zusätzliche Meldung für INPUT/OUTPUT
	 * @return  Das Resultat des Ausdrucks im Double-Format (eigentlich nur für Loop/Condition (SteuerungsInfos))
	 * @exception   InterruptedException  
	 * @exception   LoopControlException  
	 * @exception   ReturnException  
	 */
	public double parse(String label, int action, String adMessage)
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException
	{
  		if (label.equals("") | label.startsWith("//"))
			return 0;
		else if (action == INPUT)
			label = input(label, adMessage);
		else if (action == OUTPUT)
			label = label + ";";
			
		Object result=null;
		result = parse(label);
		
		if (action == OUTPUT)
			output(adMessage+(label.equals("") ? "" : result.toString()));
				
		if (result instanceof Double)
			return ((Double)result).doubleValue();
		else if (result instanceof Integer)
			return new Double(((Integer)result).intValue()).doubleValue();
		else return 0;	
  	}
	

	/** Kapselt die Eingabe in der parse()-Methode, wurde extra eingeführt um verschiedene Datentypen eingeben zu können
	 * @param   label  
	 * @param   adMessage  
	 * @return das geänderte label (a wird z.b. (bei String) zu a = "blabla";)     
	 * @exception   InterruptedException  
	 */
	private String input(String label, String adMessage)
	throws InterruptedException
	{
		String saveLabel = label;
		String input = getValueFromUser(adMessage);
		try {
			// ist das nötig ? --> resetValuesChangedFlag();
			Pointer pointerToLValue = (Pointer)parse("&("+label+");");
			int typeOfVariable = pointerToLValue.getTypeOfPointer();	
			switch (typeOfVariable)
			{
			case INTEGER:
				// Schau mer mal ob das als Integer durchgeht ! (Bei Text würde es ansonsten als Variable indentifiziert werden)
				Integer.parseInt(input);
				label = label + "=" + input + ";";
				break;
			case DOUBLE:
				// Schau mer mal ob das als Double durchgeht ! (Bei Text würde es ansonsten als Variable indentifiziert werden)
				Double.parseDouble(input);
				label = label + "=" + input + ";";
				break;
			case STRING:
				label = label + "= \"" +input+"\";";
				break;
			case CHARACTER:
				if (input.length() == 1)
					label = label + "='" + input.charAt(0)+"';";
				else
					label = label + "=\"" + input +"\";";	
				break;	
			}
		}
		catch (NumberFormatException nfe) {
			// Benutzer hat wohl eher Text eingegeben !! (Exception wird später geworfen)
			label = label + "=\"" + input + "\";";}
		catch (Exception e) { new ProcessorException("Syntax Error in Input-Statement: "+saveLabel).showMsg(Utils.getFrame(struktor)); throw new InterruptedException();}
			
		return label;
	
	
	}

	/** Hilfsmethode: wird von input() verwendet. Gibt die Eingabe den Benutzers als String zurück
	 * @param   adMessage (die angezeigte Meldung)  
	 * @return Die Eingabe des Benutzers als String
	 * @exception   InterruptedException  
	 */
	private String getValueFromUser(String adMessage)
	throws InterruptedException
	{
		String input;
		do {
			input = JOptionPane.showInputDialog(
				Utils.getFrame(struktor),
				adMessage,
				"Input",
				JOptionPane.QUESTION_MESSAGE);
			if (input==null)
				throw new InterruptedException();
		} while (input.equals(""));			
		return input;
	}
	

	 /** Kapselt die Ausgabe in der parse()-Methode
	 * @param   Message  
	 * @exception   InterruptedException  
	 */
	private void output(String Message)
	throws InterruptedException
	{
		int choose = JOptionPane.showConfirmDialog(
				Utils.getFrame(struktor),
		  		Message,
				"Output",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (choose == JOptionPane.CANCEL_OPTION)
				throw new InterruptedException();
	}
	

	/** Getter-Methode: Gibt zurück ob ein Wert während des Parsens geändert wurde. 
	 * @see resetValuesChangedFlag
	 * @see valueChanged, resetValuesChangedFlag
	 * @return     
	 */
	private boolean getValuesChangedFlag()
	{
		return valueChanged;
	}
	

	/** Muß vorher aufgerufen werden, damit man eindeutig festellen kann ob im Ausdruck,
	 *  der der watchlist-parse-Methode übergeben wurde, eine Variable ändert 
	 *  @see valueChanged, getValuesChangedFlag
	 */
	public void resetValuesChangedFlag()
	{
		valueChanged = false;
	}
	

	/** Wird vom Parser aufgerufen, wenn Variablen geändert wurden
	 *  @see resetValuesChangedFlag, getValuesChangedFlag
	 */
	void valueChanged()
	{
		valueChanged = true;
	}
	

	/** Getter-Methode die grafische Ausgabe möglich wird
	 * @return Das GraphicalOutput-Objekt    
	 */
	TurtleCanvas getGOutput()
	{
		return gOutput;
	}
	
	/** Getter-Methode die Text-Ausgabe (Console) möglich wird
	 * @return Das TextOutput-Objekt    
	 */
	JTextArea getTOutput()
	{
		return tOutput;
	}
 
}

