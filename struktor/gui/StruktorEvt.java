// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;

import struktor.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class StruktorEvt extends JPanel
implements ActionListener, ItemListener
{
	Struktor struktor;
	// Struktor InputElements
	JPanel struktorInput;
	
	/** Das gehört hier eigentlich gar nicht hin sondern in StruktorAppEvt. Geht aber nicht anders
	 * weil sonst NullPointerException
	 */
	JMenuBar menuBar = new JMenuBar();;
	
	KeyPresses keyPresses;
	
	ViewSelector view;
	StrukSelector struktogrammSelect;
	JButton newStruktogramm;
	JButton removeStruktogramm;
	JButton renameStruktogramm;
	JButton save;
	JButton load;
	JCheckBox watchlist;
	JCheckBox output;
	
	public StruktorEvt(Struktor struktor)
	{
		this.struktor = struktor;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		Box box = new Box(BoxLayout.X_AXIS);
		
		//Bedienung des Programms mittels Keyboard ...
		//keyPresses = new KeyPresses(struktor, this, struktorPanel);
		addKeyListener(keyPresses);
		
		
		addFileHandling(box);
		addStruktogrammSelect(box);
		addViewSelect(box);		
		addOutputAndWatchlist(box);
		
		// und alles zusammenfügen ...
		if (struktor.isApplet)
			add(new JLabel("Copyright 2000, Kim Neunert (k9ert@gmx.de), All Rights Reserved"), BorderLayout.NORTH);
		// Der andere Kram ist bereits in Box drin
		add(box, BorderLayout.CENTER);
		revalidate();
	}
	
	/** Wird in der App-Klasse überschrieben ! */
	void addFileHandling(Box box)
	{
		newStruktogramm = new JButton("new");
		removeStruktogramm = new JButton("remove");
		renameStruktogramm = new JButton("rename");
		save = new JButton("save");
		load = new JButton("load");
		
		// Abstandshalter ...
		box.add(Box.createRigidArea(new Dimension(10, 0)));
		if (struktor.presets.enabNew)
			box.add(newStruktogramm);
		if (struktor.presets.enabRemove)
			box.add(removeStruktogramm);
		if (struktor.presets.enabRename)
			box.add(renameStruktogramm);	
		box.add(Box.createRigidArea(new Dimension(10, 0)));
		if (struktor.presets.enabSave)
			box.add(save);
		if (struktor.presets.enabLoad)
			box.add(load);
		newStruktogramm.addActionListener(this);
		removeStruktogramm.addActionListener(this);
		renameStruktogramm.addActionListener(this);
		save.addActionListener(this);
		load.addActionListener(this);	
	}
	
	/** Wird in der App-Klasse überschrieben */
	void addStruktogrammSelect(Box box)
	{
		struktogrammSelect = new StrukSelectorEvt(struktor);
		if (struktor.presets.enabSwitchStruk)
		{
			box.add(Box.createRigidArea(new Dimension(10, 0)));
			struktogrammSelect.addYourselfTo(box);
		}
	}
	
	/** Wird in der App-Klasse überschrieben ! */
	void addViewSelect(Box box)
	{
		view = new ViewSelectorEvt(struktor);
		if (struktor.presets.enabSwitchView)
			view.addYourselfTo(box);
	}
	
	/** Wird in der App-Klasse überschrieben ! */
	void addOutputAndWatchlist(Box box)
	{
		watchlist = new JCheckBox("WatchList", struktor.presets.statWatchlist);
		watchlist.addItemListener(this);
		output = new JCheckBox("output", struktor.presets.statOutput);
		output.addItemListener(this);
		box.add(Box.createRigidArea(new Dimension(10, 0)));
		
		if (struktor.presets.enabOutput)
			add(output, BorderLayout.WEST);
		if (struktor.presets.enabWatchlist)
			add(watchlist, BorderLayout.EAST);
	
	}
	
	/** Verteilt je nach Klick
	 * @param   a, ActionEvent  
	 */
	public void actionPerformed(ActionEvent a)
	{
		Object source = a.getSource();
		if (source == newStruktogramm)
			newStruktogramm();
		else if (source == removeStruktogramm)
			deleteStruktogramm();
		else if (source == renameStruktogramm)
			renameStruktogramm();	
		else if (source == save)
			struktor.save();
		else if (source == load)
			struktor.load();	
	}
	
	/** im Moment nur Listener für den View-Selector. Ruft die entsprechende show-Methode auf.
	 * @see Struktor#showStruktogramm
	 * @see Struktor#showDecList
	 * @param   e  
	 */
	public void itemStateChanged(ItemEvent e)
	{
		Object src = e.getSource();
		// Der ViewSelector und der StruktogrammSelector managen das intern !
		if (src == watchlist)
			updateWatchlistView();
		if (src == output)
			struktor.showOutput(output.isSelected());	
				
	}
	
	public void updateWatchlistView()
	{
		struktor.showWatchList(watchlist.isSelected());
	}
	
	void newStruktogramm()
	{
		String name = JOptionPane.showInputDialog(
							Utils.getFrame(struktor),
							"Please Input Name of new Struktogramm:",
							"Input Name",
							JOptionPane.QUESTION_MESSAGE);
		if (name != null)
			struktor.newStruktogramm(name);
	}
	
	/** Aktualisiert den View */
	public void newStruktogrammCreated(String name)
	{
		struktogrammSelect.newStruktogrammCreated(name);
	}
	
	public void deleteStruktogramm()
	{
		int choose = JOptionPane.showConfirmDialog(
				Utils.getFrame(struktor),
		  		"Struktogramm will be deleted !\n Save ?",
				"Delete Struktogramm",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (choose == JOptionPane.YES_OPTION)
			struktor.save();
		if (choose == JOptionPane.NO_OPTION)	
		{
			struktor.deleteStruktogramm(); //aktuelles Struktogramm 
			
		}
	}
	
	/** Aktualisiert den View */
	public void struktogrammDeleted(String name)
	{
		struktogrammSelect.struktogrammDeleted(name);
	}
	
	void renameStruktogramm()
	{
		String newName = JOptionPane.showInputDialog(
							Utils.getFrame(struktor),
							"Please Input new Name:",
							"Input Name",
							JOptionPane.QUESTION_MESSAGE);
		if (newName != null)
			struktor.renameStruktogramm(newName);
	}
	
	/** Aktualisiert den View */	
	public void struktogrammRenamed(String oldName, String newName)
	{
		struktogrammSelect.struktogrammRenamed(oldName, newName);
	}
	
	/** Aktualisiert den View */
	public void struktogrammSelected(String name)
	{
		struktogrammSelect.struktogrammSelected(name);
	}
	
	/** Aktualisiert den View */
	public void viewChanged(int change)
	{
		view.viewChanged(change);
	}
	
	void showLicenseInfo()
	{
		String version= Presets.version;
		JOptionPane.showMessageDialog(
					Utils.getFrame(struktor),
					"Struktor "+version+ "\nCopyright 2000-2004, Kim Neunert\nvisit http://www.learn2prog.de", 
					"Info:", 
					JOptionPane.INFORMATION_MESSAGE);
	}
}
