// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;

import struktor.StruktorApplication;

public class StruktorAppEvt extends StruktorEvt
implements ActionListener, ItemListener
{
	JFrame frame;
	JMenu file;
	JPanel struktorInput;
		
	// View exisiert bereits als Combobox
		
	JMenuItem newStruktogrammApp;
	JMenuItem removeStruktogrammApp;
	JMenuItem renameStruktogrammApp;
	JMenuItem saveApp;
	JMenuItem loadApp;
	JMenuItem saveAsBitmap;
	JMenuItem properties;
	JMenuItem info;
	
	
	
	// Die Ausführgschichten
	JButton execute = new JButton("execute");
	JCheckBox debugMode = new JCheckBox("debugMode");
	JButton pause = new JButton("pause");
	JButton step = new JButton("step");
	JSlider delay = new JSlider(JSlider.HORIZONTAL, 0 , 1500, 200);

	public StruktorAppEvt(StruktorApplication struktor)
	{
		super(struktor);
		
		this.struktor = struktor;
		menuBar.setBorderPainted(true);
				
		addFileHandling(null);
		
		// und alles zusammenfügen ...
		
		menuBar.add(file);
		struktogrammSelect.addYourselfTo(menuBar);
		view.addYourselfTo(menuBar);
		menuBar.add(output);
		menuBar.add(watchlist);
		struktor.setJMenuBar(menuBar);
	}
	
	void addFileHandling(Box box)
	{
		// Filehandling
		file = new JMenu("File");
		newStruktogrammApp = new JMenuItem("new");
		removeStruktogrammApp = new JMenuItem("remove");
		renameStruktogrammApp = new JMenuItem("rename");
		saveApp = new JMenuItem("save");
		loadApp = new JMenuItem("load");
		saveAsBitmap = new JMenuItem("saveAsBitmap");
		properties = new JMenuItem("properties");
		info = new JMenuItem("info");
	
		file.add(newStruktogrammApp);
		file.add(removeStruktogrammApp);
		file.add(renameStruktogrammApp);	
		file.add(saveApp);
		file.add(loadApp);
		file.addSeparator();
		file.add(saveAsBitmap);
		file.add(properties);
		file.add(info);
		newStruktogrammApp.addActionListener(this);
		removeStruktogrammApp.addActionListener(this);
		renameStruktogrammApp.addActionListener(this);
		saveApp.addActionListener(this);
		loadApp.addActionListener(this);
		saveAsBitmap.addActionListener(this);
		properties.addActionListener(this);
		info.addActionListener(this);
	}
	
	void addStruktogrammSelect(Box box)
	{
		struktogrammSelect = new StrukSelectorAppEvt(struktor, menuBar);
	}
							
	void addViewSelect(Box box)
	{
		view = new ViewSelectorAppEvt(struktor);
	}
	
	void addOutputAndWatchlist(Box box)
	{
		watchlist = new JCheckBox("WatchList", struktor.presets.statWatchlist);
		watchlist.addItemListener(this);
		output = new JCheckBox("output", struktor.presets.statOutput);
		output.addItemListener(this);
	}
	
	/** Erstellt ein neues Struktogramm
	 * @param   a, ActionEvent  
	 */
	public void actionPerformed(ActionEvent a)
	{
		Object source = a.getSource();
		if (source == newStruktogrammApp)
			newStruktogramm();
		else if (source == removeStruktogrammApp)
			deleteStruktogramm();
		else if (source == renameStruktogrammApp)
			renameStruktogramm();	
		else if (source == saveApp)
			struktor.save();
		else if (source == loadApp)
			struktor.load();	
		else if (source == saveAsBitmap)
			((StruktorApplication)struktor).saveAsBitmap();
		else if (source == properties)
			struktor.presets.showDialog();		
		else if (source == info)
			showLicenseInfo();
		// den Rest erledigt StruktorEvt
		super.actionPerformed(a);
	}	
}