// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;

import struktor.Struktor;
import struktor.StruktorException;

class ViewSelectorEvt extends ViewSelector
implements ItemListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JComboBox view;
	Struktor struktor;
	
	ViewSelectorEvt(Struktor struktor)
	{
		this.struktor=struktor;
		view = new JComboBox();
		view.addItem("StruktogrammView");
		view.addItem("DeclarationView");
		if (struktor.presets.enabStructs)
			view.addItem("StructView");
		view.setToolTipText("Choose your View");
		view.setSelectedIndex(0);
		view.addItemListener(this);
	}
	
	public void addYourselfTo(JMenuBar menubar)
	{
		new StruktorException("Programmfehler !").showErrorMsg(struktor);
	}
	
	public void addYourselfTo(Box box)
	{
		box.add(Box.createHorizontalGlue());
		box.add(view);
	}
	
	
	
	public void itemStateChanged(ItemEvent e)
	{
		Object src = e.getSource();
		if (src == view)
		{
		  int i = view.getSelectedIndex();
			if (i==Struktor.STRUKVIEW)
				struktor.showStruktogramm();
			else if(i==Struktor.DECVIEW)
				struktor.showDecList();
			else
				struktor.showStructList();	
		}
	}
	
	public void viewChanged(int change)
	{
		view.setSelectedIndex(change);
	}
}
