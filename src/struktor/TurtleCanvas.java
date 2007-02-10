

/*
 * Class    : Turtle
 * Copyright: (c) Gerhard R�hner
 *
 * Historie
 * --------
 * 1.00 10.11.89 Urfassung in Anlehnung an AppleGraf konzipiert
 * 1.01 17.11.89 wesentliche Module fertiggestellt
 * 1.02 16.02.90 Umlaute entfernt: GRAFTABL laden! Show- und HideCursor
 * 1.03 14.09.90 Entfernung AppleGraf spezifischer Elemente
 * 1.05 15.09.90 Leave- und EnterGraphic
 * 1.06 26.10.90 generelle �berarbeitung, Get/Set-Cursor
 * 1.07 02.11.90 R�ckkehr zu �blichem Koordinatensystem
 * 1.08 16.11.90 WaitKey
 * 1.09 18.11.90 �berarbeitung der Druckroutinen
 * 1.10 26.11.90 GraphMode mit HideCursor, TextMode mit ShowCursor
 * 1.11 26.04.95 �berarbeitung f�r VGA-Grafik
 * 1.12 08.05.95 Inverse Darstellung
 * 2.00 28.10.97 Erste Fassung f�r Delphi, als Unit, auf Turtle reduziert
 * 2.01 19.01.98 Realisierung als Komponente
 * 2.02 19.09.98 Erg�nzung der Methode Clear
 * 2.03 27.09.98 Erg�nzung der Methode Print und des Ereignisses OnMouseUp
 * 2.04 07.10.98 PrintSize und DrawDynamic
 * 3.00 10.10.00 Erste Fassung als Java-Klasse
 */

package struktor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;


/**
 * Diese Klasse stellt eine Turtle-Komponente f�r die Grafikprogrammierung
 * zur Verf�gung.
 *
 * @see <a href="http://www.bildung.hessen.de/abereich/inform/skii/material/index.html">
 * @author Gerhard R�hner
 * @version 3.00 10/10/00
 */



public class TurtleCanvas extends JComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// -- private Attribute ------------------------------------------------------
	private Graphics Grafik;
	private BufferedImage Bild;
	private int Width;
	private int Height;
	private Color foreground;
	private Color background;

	private final double PIdurch180 = 0.0174533;

	// -- �ffentliche Attribute --------------------------------------------------

	/**
	* TurtleX ist die X-Koordinate der Turtle.
	* <P>
	* Beispiel: <PRE>TurtleX = 100;</PRE>
	*/

	private double TurtleX;

	/**
	* TurtleY ist die Y-Koordinate der Turtle.
	* <P>
	* Beispiel: <PRE>TurtleY = 200;</PRE>
	*/

	private double TurtleY;

	/**
	* TurtleW ist der aktuelle Winkel der Turtle im Bereich 0 bis 360 Grad.
	* <P>
	* Beispiel: <PRE>TurtleW = 180;</PRE>
	*/

	public double TurtleW;

	// --- Konstruktor -----------------------------------------------------------


	/**
	* Erzeugt eine Turtle mit einer Zeichenfl�che, die Breite x H�he gro� ist.
	* Die Turtle wird anfangs in die Mitte der Zeichenfl�che gesetzt.
	* Der Anfangswinkel ist 0 Grad, was Blickrichtung nach rechts entspricht.
	* Die Hintergrundfarbe ist Wei�, die Zeichenfarbe ist Schwarz.
	* <P>
	* Die Turtleposition kann interaktiv durch Anklicken der Zeichenfl�che
	* festgelegt werden.
	* <P>
	* Beispiel: <PRE>Turtle myTurtle = new Turtle(640, 480);</PRE>
	*/

	public TurtleCanvas (int Breite, int Hoehe)
	{

		super();
		Width = Breite;
		Height = Hoehe;
		foreground = new Color(0,0,0);
		background = new Color(255,255,255);
		Bild = new BufferedImage( Width, Height, BufferedImage.TYPE_INT_RGB);
		Grafik = Bild.createGraphics();
		
		setBackcolor(background);
		setColor(foreground);
		
		tmoveto(Breite/2, Hoehe/2);
		tturnto(0);
	}






	/**
	* Erm�glicht die automatische Gr��eneinstellung der Turtle-Zeichenfl�che
	* durch pack().
	*/

	public Dimension getPreferredSize()
	{
		return new Dimension(Width, Height);
	}


	/**
	* Setzt die Gr��e der Turtle-Zeichenfl�che auf Breite x Hoehe.
	* <P>
	* Beispiel: <PRE>myTurtle.setSize(640, 480);</PRE>
	*/


	public void setSize(int Breite, int Hoehe)
	{
		super.setSize(Breite, Hoehe);
		Bild = new BufferedImage(Breite, Hoehe, BufferedImage.TYPE_INT_RGB);
	    Grafik = Bild.getGraphics();
	}





	// --- Winkel und Drehungen --------------------------------------------------

	private void wTurtleMod360()
	{

		while (TurtleW >= 360)
			TurtleW = TurtleW - 360;

		while (TurtleW < 0)
			TurtleW = TurtleW + 360;
	}


	/**
	* Setzt den Richtungswinkel der Turtle absolut auf den Winkel angle.
	* Der Richtungswinkel nimmt gegen den Uhrzeigersinn zu, also gilt
	* Rechts = 0, Oben = 90, Links = 180, Unten = 270.
	* <P>
	* Beispiel: <PRE>myTurtle.turnto(270);</PRE>
	*/

	public void tturnto(double angle)
	{

		TurtleW = angle;
		wTurtleMod360();
	}



	/**
	* Dreht den Richtungswinkel der Turtle relativ um den Winkel angle.
	* Positive Werte bedeuten eine Rechtsdrehung, negative eine Linksdrehung.
	* <P>
	* Beispiel: <PRE>myTurtle.turn(-90);</PRE>
	*/

	public void tturn (double angle)
	{

		TurtleW = TurtleW + angle;
		wTurtleMod360();
	}

	// --- Zeichnen --------------------------------------------------------------

	/**
	* Die Turtle zeichnet eine Linie von der aktuellen Position (TurtleX, TurtleY)
	* zur Position (x, y).
	* <P>
	* Beispiel: <PRE>myTurtle.drawto(200, 300);</PRE>
	*/

	public void tdrawto (double x, double y)
	{
		Grafik.drawLine((int)TurtleX, (int)TurtleY, (int)x, (int)y);
		TurtleX= x; TurtleY= y;
	}

	/**
	* Die Turtle zeichnet eine Linie der angegebenen L�nge in die aktuelle
	* Richtung.
	* <P>
	* Beispiel: <PRE>myTurtle.draw(100);</PRE>
	*/

	public void tdraw(double Laenge)
	{

		tdrawto (TurtleX + Laenge * Math.cos (TurtleW * PIdurch180),
			TurtleY - Laenge * Math.sin (TurtleW * PIdurch180));

	}





	// --- Bewegen ohne Zeichnen -------------------------------------------------


	/**
	* Die Turtle bewegt sich ohne Zeichnen zur Position (x/y).
	* <P>
	* Beispiel: <PRE>myTurtle.moveto(100, 200);</PRE>
	*/

	public void tmoveto (double x, double y)
	{

		TurtleX = x;
		TurtleY = y;
	}

	/**
	* Die Turtle bewegt sich ohne Zeichnen in der aktuellen Richtung
	* um eine Strecke der angegebenen L�nge.
	* <P>
	* Beispiel: <PRE>myTurtle.move(100);</PRE>
	*/


	public void tmove (double length)
	{

		TurtleX = TurtleX + length * Math.cos (TurtleW * PIdurch180);
		TurtleY = TurtleY - length * Math.sin (TurtleW * PIdurch180);
	}



	// --- Vorder- und Hintergrundfarbe ------------------------------------------

	/**
	* Setzt die Zeichenfarbe der Turtle auf die Farbe c.
	* <P>
	* Beispiel: <PRE>myTurtle.setColor(Color.red);</PRE>
	*/


	// F�r den Aufruf aus Struktogrammen
	public void setColor(int r, int g, int b)
	{
		setColor(new Color(r,g,b));
	}  
	
	public void setColor(Color c)
	{
		Grafik.setColor(c);
	}
	
	/**
	 * Setzt die Farbe der Zeichenfl�che auf die Farbe c.
	 * <P>
	 * Beispiel: <PRE>myTurtle.setBackcolor(Color.blue); </PRE>
	 */
	
	
	public void setBackcolor(Color c) 
	{
		setColor(c);
		Grafik.fillRect(0, 0, Width, Height);
		setColor(foreground);
		repaint();
	}
	


	// --- Anzeigen --------------------------------------------------------------

	public void clear()
	{
		tmoveto(Width/2, Height/2);
		tturnto(0);
		setBackcolor(background);
		setColor(foreground);
	}
	
	public void paintComponent( Graphics g )
	{
	    g.drawImage( Bild, 0, 0, this );
	}
	
	public void update( Graphics g ) 
	{
	    paintComponent( g );
	}
	
	public Graphics getGraphics()
	{
		return Grafik;
	}
}



