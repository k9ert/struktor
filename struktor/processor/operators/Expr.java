package struktor.processor.operators;

/** $Id$
     * eine abstrakte Klasse f�r die Konstruktion
     * beliebiger Ausdr�cke
     * und zum Auswerten dieser Ausdr�cke
     *
     */


public abstract class Expr {

	// die eval Funktion
	// liefert ein beliebiges Objekt als Resultat
	// damit k�nnen nicht nur arithmetische Ausdr�cke
	// sondern Ausdr�cke mit beliebigem Resultat
	// behandelt werden
	

	/** Die eval Funktion liefert ei beliebiges Objekt als Resultat.
	 * In der abge�nderten Version kann das auch ein "Expr"-Object sein.
	 * @return das Resultat der Berechnung (beliebiges Objekt)    
	 * @exception   ProcessorException  
	 */
	public abstract Object eval()
	throws struktor.processor.ProcessorException;

}