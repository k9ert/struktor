// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JToolBar;
import javax.swing.event.ChangeListener;

import struktor.Struktor;

/** Die Event-Klasse für Struktogramme, wird im Moment nicht verwendet (Application)
 */
public class StruktogrammAppEvt extends StruktogrammEvt
implements ActionListener, ItemListener, ChangeListener
{
	// Object Variables
	private JToolBar toolBar;
	
	public StruktogrammAppEvt(Struktor s, JToolBar toolBar)
	{
		super(); // muss leider sein
		this.struktor = s;
		this.toolBar = toolBar;
		
		// Elemente die Preset-Informationen brauchen werden erst jetzt instanziert !
		debugMode = new JCheckBox("debugMode", struktor.presets.statDebugMode);
		pause.addActionListener(this);
		step.addActionListener(this);
		if (struktor.presets.enabExecute)
			toolBar.add(execute);
		execute.addActionListener(this);
		if (struktor.presets.enabDebugMode)
			toolBar.add(debugMode);
		debugMode.addItemListener(this);
		delay.addChangeListener(this);
	}
	// Event Handling
	private void strukStarted()
	{
		execute.setText("stop");
		toolBar.add(pause);
		toolBar.add(step);
		step.setVisible(true);
		toolBar.add(delay);
		
		toolBar.revalidate();
		toolBar.validate();
		toolBar.repaint();
		
	}
	
	public void strukStopped()
	{
		toolBar.remove(delay);
		toolBar.remove(pause);
		toolBar.remove(step);
		execute.setText("execute");
		pause.setText("pause");
		toolBar.invalidate();
		toolBar.validate();
		struktor.selectStruktogramm(Struktogramm.main.getName());
		toolBar.repaint();
	}
	
	
	
}