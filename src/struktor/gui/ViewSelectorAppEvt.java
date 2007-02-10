// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;

import struktor.Struktor;
import struktor.StruktorException;

class ViewSelectorAppEvt extends ViewSelector
implements ItemListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenu view;
	Struktor struktor;
	private ButtonGroup group = new ButtonGroup();
	
	JRadioButton strukView = new JRadioButton("StructView");
	JRadioButton decView = new JRadioButton("DeclarationView");
	JRadioButton structView = new JRadioButton("StructView");
	
	
	ViewSelectorAppEvt(Struktor struktor)
	{
		this.struktor=struktor;
		group.add(strukView);
		group.add(decView);
		group.add(structView);
		view = new JMenu("View");
		view.add(strukView);
		view.add(decView);
		view.add(structView);
		view.setToolTipText("Choose your View");
		strukView.setSelected(true);
		strukView.addItemListener(this);
		decView.addItemListener(this);
		structView.addItemListener(this);
	}
	
	public void addYourselfTo(JMenuBar menuBar)
	{
		menuBar.add(view);
	}
	
	public void addYourselfTo(Box box)
	{
		new StruktorException("Programmfehler !").showErrorMsg(struktor);
		
	}
	
	
	
	public void itemStateChanged(ItemEvent e)
	{
		Object src = e.getSource();
		if (src == strukView)
			struktor.showStruktogramm();
		else if (src == decView)
			struktor.showDecList();
		else
			struktor.showStructList();	
	}
	
	public void viewChanged(int change)
	{
		switch (change)
		{
			case 0:
				strukView.setSelected(true);
				break;
			case 1:
				decView.setSelected(true);
				break;
			case 2:
				structView.setSelected(true);
		}
	}
}
