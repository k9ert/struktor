// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;
import struktor.*;
import struktor.strukelements.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/** Abstrakte Klasse für den Struktogramm-Auswähler. 
 * Wird einmal für das Applet (simple JComboBox) und einmal für 
 * Application implementiert (Menüeintrag in der Menubar)
 * Die ganze Geschichte könnte evtl. auch mit Interfaces gelöst werden
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
	
	/** Nur für StrukSelectorAppEvt (.add(...) geht nicht)*/
	public abstract void addYourselfTo(JMenuBar menuBar);
	
	/** Nur für StrukSelectorEvt (.add(...) geht nicht)*/
	public abstract void addYourselfTo(Box box);
}