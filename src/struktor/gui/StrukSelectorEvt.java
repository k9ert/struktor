// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;

import struktor.Struktor;
import struktor.StruktorException;

class StrukSelectorEvt extends StrukSelector
implements ItemListener
{
	JComboBox selector;
	Struktor struktor;
	private ButtonGroup group = new ButtonGroup();

	StrukSelectorEvt(Struktor struktor)
	{
		this.struktor = struktor;
		selector = new JComboBox();
		selector.setPreferredSize(new Dimension(100,30));
		selector.addItemListener(this);
	}
	
	public void addYourselfTo(JMenuBar menubar)
	{
		new StruktorException("Programmfehler !").showErrorMsg(struktor);
	}
	
	public void addYourselfTo(Box box)
	{
		box.add(selector);
	}
	
	
	
	public void itemStateChanged(ItemEvent e)
	{
		try {
			struktor.selectStruktogramm(selector.getSelectedItem().toString());	
		} catch (NullPointerException ex) {}
	}
	
	public void newStruktogrammCreated(String name)
	{
		selector.addItem(name);
		selector.setSelectedItem(name);
	}
	
	public void struktogrammDeleted(String name)
	{
		selector.removeItem(name);
	}
	
	public void struktogrammRenamed(String oldName, String newName)
	{
		selector.removeItem(oldName);
		selector.addItem(newName);
		selector.setSelectedItem(newName);
	}
	
	public void struktogrammSelected(String name)
	{
		if (selector.getSelectedItem()!=name)
			selector.setSelectedItem(name);
	}
}
