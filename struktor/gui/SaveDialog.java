// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.util.*;
import struktor.strukelements.*;
import struktor.*;

/** Eine Klasse mit der der saveMark jedes Struktogramms interaktiv verändert werden kann 
 * Funktioniert vom Aufruf ähnlich wie ein FileChooser
 */
 
public class SaveDialog
extends JDialog implements ActionListener, ItemListener
{
	static public int OK=1;
	static public int CANCEL=2;
	boolean result;
	String format;
	int returnVal=CANCEL;
	Struktor struktor;
	struktor.Presets presets;
	Vector struktogrammList;
	boolean proceed=false;
	private JButton ok, cancel;
	private JCheckBox cCode;
	
	public SaveDialog(Frame parent, Struktor struktor, Vector struktogrammList, String format)
	{		
		super(parent, "Which Struktogramms should be saved ?", true);
    	this.struktor=struktor;
		this.format = format;
		this.struktogrammList = struktogrammList;
		presets = struktor.presets;
	}
	

	/** Zeigt das Fenster an und gibt OK oder CANCEL zurück
	 */
	public int showDialog()
	{
		addWindowListener(
			new WindowAdapter() {
				public void windowClosed (WindowEvent e) {proceed=true; }
				public void windowClosing (WindowEvent e) {proceed=true; }
			}
		);
				
		Container contentPane = getContentPane();
		//Fenster
    	setBackground(Color.lightGray);
		JPanel list = new JPanel();
		list.setLayout(new GridLayout(0,1,0,0));
			    
		for (Enumeration el=struktogrammList.elements(); el.hasMoreElements(); )
        {
             Struktogramm s=(Struktogramm)el.nextElement();
			 
			 StruktogrammCheck temp = new StruktogrammCheck(s);
             list.add(temp);
        }
		
		contentPane.add(list, BorderLayout.CENTER);
		contentPane.add(new JLabel("Select Struktogramms to be saved"), BorderLayout.NORTH);
        
		//Buttons
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		ok = new JButton("Ok");
		ok.addActionListener(this);
		panel.add(ok);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		panel.add(cancel);	
		cCode = new JCheckBox("produce C-Code", struktor.presets.makeCCode);
		cCode.addItemListener(this);
		panel.add(cCode);
		//setResizable(false);
		contentPane.add(panel, BorderLayout.SOUTH);
    	pack();
		setVisible(true);
		while (!proceed)
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie) {}
		return returnVal;	
	}
	
	public void actionPerformed(ActionEvent event)
	{
		Object src = event.getSource();
		if (src == ok)
		{
			returnVal=OK;
			proceed = true;
			setVisible(false);
			return;
		}
		else
		{
			returnVal=CANCEL;
			proceed=true;	
			setVisible(false);
		}
	}
		
	public void itemStateChanged(ItemEvent e)
	{
		if (cCode.isSelected())
			if (!struktor.containsMainFunction())
			{
				new StruktorException("You need a main-Function if you want to produce C-Code !").showErrorMsg(struktor);
				presets.makeCCode=false;
				cCode.setSelected(false);
				return;
			}
		presets.makeCCode = cCode.isSelected();
	}
	
	class StruktogrammCheck extends JPanel
	implements ItemListener
	{
		
		Struktogramm struktogramm;
		JCheckBox checkBox;
		
		StruktogrammCheck(Struktogramm struktogramm)
		{
			this.struktogramm = struktogramm;
			checkBox = new JCheckBox(struktogramm.getName(), struktogramm.saveMark);
			checkBox.addItemListener(this);
			setLayout(new BorderLayout());
			add(checkBox, BorderLayout.WEST);
		}
		
		public void itemStateChanged(ItemEvent e)
		{
			struktogramm.saveMark = checkBox.isSelected();
		}
	
	}
	
}
	