// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/** Abstrakte Klasse für den View-Auswähler. 
 * Wird einmal für das Applet (simple JComboBox) und einmal für 
 * Application implementiert (Menüeintrag in der Menubar)
 * Die ganze Geschichte könnte evtl. auch mit Interfaces gelöst werden
 */
abstract class ViewSelector extends JComponent
implements ItemListener
{
	/** Aktualisiert den View */
	public abstract void viewChanged(int change);
	
	/** Nur für ViewSelectorAppEvt (.add(...) geht nicht)*/
	public abstract void addYourselfTo(JMenuBar menuBar);
	
	/** Nur für ViewSelectorEvt (.add(...) geht nicht)*/
	public abstract void addYourselfTo(Box box);
}