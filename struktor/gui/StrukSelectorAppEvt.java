// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;
import struktor.*;
import struktor.strukelements.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class StrukSelectorAppEvt extends StrukSelector
implements ItemListener
{
	JMenu selector;
	Struktor struktor;
	private ButtonGroup group = new ButtonGroup();
	
	StrukSelectorAppEvt(Struktor struktor, JMenuBar menuBar)
	{
		this.struktor = struktor;
		selector = new JMenu("empty");
		selector.addItemListener(this);
	}
	
	public void addYourselfTo(JMenuBar menubar)
	{
		menubar.add(selector);
	}
	
	public void addYourselfTo(Box box)
	{
		new StruktorException("Programmfehler !").showErrorMsg(struktor);
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		Object source = e.getSource();
		if (source instanceof JRadioButton)
		try {
			struktor.selectStruktogramm(((JRadioButton)source).getText());	
		} catch (NullPointerException ex) {}
	}
	
	public void newStruktogrammCreated(String name)
	{
		JRadioButton temp = new JRadioButton(name);
		group.add(temp);
		selector.add(temp);
		temp.setSelected(true);
		temp.addItemListener(this);
	}
	
	public void struktogrammDeleted(String name)
	{
		Component[] radioButtons = selector.getMenuComponents();
		for (int i=0; i <radioButtons.length; i++)
		{
			if (((JRadioButton)radioButtons[i]).getText().equals(name))
			{
				selector.remove(radioButtons[i]);
				group.remove((JRadioButton)radioButtons[i]);
			}
		}
		// Und das erste Element auswählen (sonst ist gar nichts ausgewählt)
		radioButtons = selector.getMenuComponents();
		if (radioButtons.length > 0)	
			((JRadioButton)radioButtons[0]).setSelected(true);
		else
			selector.setText("empty");	
	}
	
	public void struktogrammRenamed(String oldName, String newName)
	{
		int j = selector.getMenuComponentCount();
		for (int i=0; i <j; i++)
		{
			JRadioButton item = (JRadioButton) selector.getMenuComponent(i);
			if (item.getText().equals(oldName))
			{
				item.setText(newName);
			}
		}
		if (selector.getText().equals(oldName))
			selector.setText(newName);						
	}
	
	public void struktogrammSelected(String name)
	{
		selector.setText(name);	
		/*
		// Warum geht das nicht ????
		int j = selector.getMenuComponentCount();
		for (int i=0; i <j; i++)
		{
			JRadioButton item = (JRadioButton) selector.getMenuComponent(i);
			if ((!item.isSelected()) && item.getText().equals(name))
			{
				System.out.println(i);
				item.removeItemListener(this);
				item.setSelected(true);
				item.addItemListener(this);
				break;
			}
		}*/
	}
	
}
