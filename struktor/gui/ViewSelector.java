// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/** Abstrakte Klasse f�r den View-Ausw�hler. 
 * Wird einmal f�r das Applet (simple JComboBox) und einmal f�r 
 * Application implementiert (Men�eintrag in der Menubar)
 * Die ganze Geschichte k�nnte evtl. auch mit Interfaces gel�st werden
 */
abstract class ViewSelector extends JComponent
implements ItemListener
{
	/** Aktualisiert den View */
	public abstract void viewChanged(int change);
	
	/** Nur f�r ViewSelectorAppEvt (.add(...) geht nicht)*/
	public abstract void addYourselfTo(JMenuBar menuBar);
	
	/** Nur f�r ViewSelectorEvt (.add(...) geht nicht)*/
	public abstract void addYourselfTo(Box box);
}