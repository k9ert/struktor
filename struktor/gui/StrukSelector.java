// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;
import struktor.*;
import struktor.strukelements.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/** Abstrakte Klasse f�r den Struktogramm-Ausw�hler. 
 * Wird einmal f�r das Applet (simple JComboBox) und einmal f�r 
 * Application implementiert (Men�eintrag in der Menubar)
 * Die ganze Geschichte k�nnte evtl. auch mit Interfaces gel�st werden
 */
abstract class StrukSelector extends JComponent
implements ItemListener
{
	/** Aktualisiert den View */
	public abstract void newStruktogrammCreated(String name);
	
	/** Aktualisiert den View */
	public abstract void struktogrammDeleted(String name);
	
	/** Aktualisiert den View */
	public abstract void struktogrammRenamed(String oldName, String newName);
	
	/** Aktualisiert den View */
	public abstract void struktogrammSelected(String name);
	
	/** Nur f�r StrukSelectorAppEvt (.add(...) geht nicht)*/
	public abstract void addYourselfTo(JMenuBar menuBar);
	
	/** Nur f�r StrukSelectorEvt (.add(...) geht nicht)*/
	public abstract void addYourselfTo(Box box);
}