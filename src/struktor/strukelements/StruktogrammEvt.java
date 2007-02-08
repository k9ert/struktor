// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import struktor.Struktor;

/** Eine Klasse für die AusführungsSteuerung der Struktogramme (execute-Button, Geschwindigkeitsregler ...)
 * Wird nur EINMAL instanziert und steuert alle Struktogramme
 */
public class StruktogrammEvt extends Box
implements ActionListener, ItemListener, ChangeListener
{
	// Static Variables
	static private final int COMMAND=0;
	static private final int HEADLOOP=1;
	static private final int TAILLOOP=2;
	static private final int CONDITION=3;
	
		
	// Object Variables
	JButton execute = new JButton("execute");
	JCheckBox debugMode;
	JButton pause = new JButton("pause");
	JButton step = new JButton("step");
	JSlider delay = new JSlider(JSlider.HORIZONTAL, 0 , 1500, 200);
	Struktor struktor;

	public StruktogrammEvt(Struktor s)
	{
		super(BoxLayout.X_AXIS);
		this.struktor = s;
		
		// Elemente die Preset-Informationen brauchen werden erst jetzt instanziert !
		debugMode = new JCheckBox("debugMode", struktor.presets.statDebugMode);
		pause.addActionListener(this);
		step.addActionListener(this);
		if (struktor.presets.enabExecute)
			add(execute);
		execute.addActionListener(this);
		if (struktor.presets.enabDebugMode)
			add(debugMode);
		debugMode.addItemListener(this);
		delay.addChangeListener(this);
	}
	
	// Für die Subklasse
	public StruktogrammEvt()
	{
		super(BoxLayout.X_AXIS);
	}
	/** Aktualisiert den View */
	private void strukStarted()
	{
		execute.setText("stop");
		add(pause);
		add(step);
		step.setVisible(true);
		add(delay);
		invalidate();
		validate();
		repaint();
		
	}
	
	/** Aktualisiert den View */
	public void strukStopped()
	{
		remove(delay);
		remove(pause);
		remove(step);
		execute.setText("execute");
		pause.setText("pause");
		invalidate();
		validate();
		//struktor.selectStruktogramm(Struktogramm.main.getName());
		this.repaint();
	}
	
	/** Aktualisiert den View */
	void strukPaused()
	{
		pause.setText("resume");
	}
	
	/** Aktualisiert den View */
	void strukUnpaused()
	{
		pause.setText("pause");
	}
			
	public void actionPerformed(ActionEvent e) 
	{
		Object src = e.getSource();
		        
		if (src == execute)
		{
			if (execute.getText().equals("stop"))
			{
				Struktogramm.stopStruktogramm(); // (static)
				strukStopped();
			}
			else
			{
				struktor.getActualStruktogramm().startStruktogramm();
				strukStarted();
			}
		}
		if (src == pause)
		{
			struktor.getActualStruktogramm().triggerPause();
		}
		if (src == step)
		{
			struktor.getActualStruktogramm().step();
		}

	}
	public void itemStateChanged(ItemEvent e)
	{
		Object src = e.getSource();
		if (src == debugMode)
			struktor.getActualStruktogramm().setDebugMode(debugMode.isSelected());
		struktor.getActualStruktogramm().getView().repaint();
	}
	
	public void stateChanged(ChangeEvent e)
	{
		JSlider source = (JSlider)e.getSource();
		Struktogramm.delay = (int)source.getValue();
	}
	
	/** Aktualisiert den View */
	void delayChanged(int newDelay)
	{
		delay.setValue(newDelay);
	}
	
	
}