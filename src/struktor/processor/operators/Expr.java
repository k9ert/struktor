package struktor.processor.operators;

/** $Id: Expr.java 16 2005-02-10 19:36:29 +0000 (Do, 10 Feb 2005) kneunert $
     * eine abstrakte Klasse für die Konstruktion
     * beliebiger Ausdrücke
     * und zum Auswerten dieser Ausdrücke
     *
     */


public abstract class Expr {

	// die eval Funktion
	// liefert ein beliebiges Objekt als Resultat
	// damit können nicht nur arithmetische Ausdrücke
	// sondern Ausdrücke mit beliebigem Resultat
	// behandelt werden
	

	/** Die eval Funktion liefert ei beliebiges Objekt als Resultat.
	 * In der abgeänderten Version kann das auch ein "Expr"-Object sein.
	 * @return das Resultat der Berechnung (beliebiges Objekt)    
	 * @exception   ProcessorException  
	 */
	public abstract Object eval()
	throws struktor.processor.ProcessorException;

}