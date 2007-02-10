// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import struktor.gui.SaveDialog;
import struktor.gui.StruktorEvt;
import struktor.processor.StructPanel;
import struktor.strukelements.DecList;
import struktor.strukelements.Load;
import struktor.strukelements.Struktogramm;
import struktor.strukelements.StruktogrammEvt;
import struktor.strukelements.WatchList;

public class Struktor extends JApplet
{
	static JApplet applet;
	public static boolean isApplet = true;
	/** interne Debug-Medungen ausgeben ?*/
	public static boolean idebug = true;
	public static Struktor struktor;
	public static final int STRUKVIEW=0;
	public static final int DECVIEW=1;
	public static final int STRUCTVIEW=2;
	public static GregorianCalendar cal = new GregorianCalendar();
	
	/** Wird von Function benutzt, da Function keine Referenz auf Struktor besitzt*/
	public static void staticSelectStruktogramm(String name)
	{
		struktor.selectStruktogramm(name);
	}
	
	/** wird nur bei init() verwendet (soll ganz rausfliegen)
	 * @return Das Frame-Objekt     
	 */	
	public Frame getFrame()
	{
		Container parent = this;
		while (! (parent instanceof Frame) )
			parent = parent.getParent();
		Frame theFrame = (Frame) parent;
		return theFrame;
	}
	// Object Variables
	public Presets presets;
	private Vector struktogrammList;
	private int view;
	private Struktogramm struktogramm;
	private Container contentPane;
	
	/* General Panels */
	private StruktorEvt struktorPanel;
	private StruktogrammEvt struktoPanel;
	private DecList decPanel;
	private StructPanel structPanel;
	private JPanel structs=new JPanel();
	private WatchList watchPanel;
	private JPanel tempPanel;
	private JPanel outputPanel;
	
	public JTextArea textOutput;
	public TurtleCanvas canvas;

	public void init()
	{
		struktor = this;
		if (isApplet)
		{
			if (getParameter("load")!=null)
			{
				try {
					loadPreset(null);
				} catch (StruktorException se) {}
			}
			else
				presets = new Presets(this);
		}
		else
			presets = new Presets(this);	
		Utils.setApplet(this);
		Utils.setFrame(getFrame());
		struktogrammList = new Vector();
		contentPane = getContentPane();
		applet = this;
		
		if (presets.showLogo)
		{
			Copyright intro = new Copyright(this);
			intro.showIt();
		}
			
		//Die Zeichenfl�che
		canvas = new TurtleCanvas(250,250);
		// Textausgabefenster
		textOutput = new JTextArea("", 17, 20);
		// Fasst beide zusammen
		outputPanel = new OutputPanel(this, canvas, textOutput); 
		outputPanel.setSize(100,100);
		outputPanel.setVisible(true);
		
		// die Bedienfelder f�r Structs
		structPanel = new StructPanel(this,structs);
		
		// Die Bedienfelder f�r Struktogramme
		struktoPanel = new StruktogrammEvt(this);
				
		// tempor�re Erstellung eines Struktogramms (ansonsten w�r es ziemlich kompliziert)(Ich wei� ehrlich gesagt auch nicht genau warum das n�tig ist)
		struktogramm = new Struktogramm("temp", struktoPanel, this);
		struktogrammList.remove(findStruktogramm("temp"));
		
		
		//StruktorInput erzeugen und Struktogramm laden
		struktorPanel = getStruktorInput(this);
		
		loadStruktogramm();	
		
		
		// und ois zamf�gn
		contentPane.add(struktorPanel, BorderLayout.NORTH);
		contentPane.add(struktogramm.getView(), BorderLayout.CENTER);
		contentPane.add(struktoPanel, BorderLayout.SOUTH);
		if (presets.enabWatchlist) 
			contentPane.add(watchPanel, BorderLayout.EAST);
		showWatchList(presets.statWatchlist);
		showOutput(presets.statOutput);
		if (presets.loadAndRun)
			struktogramm.startStruktogramm();
		
	}
	
	public void start()
	{
		repaint();
	}
	
	public void stop()
	{
		Struktogramm.stopStruktogramm();
	}


	/** Zeigt die Deklarationsliste an und l��t vorher das Struktogramm verschwinden.
	 */
	public void showDecList() 
	{
		contentPane.remove(struktoPanel);
		struktogramm.getView().setVisible(false);
		contentPane.remove(structPanel);
		contentPane.remove(structs);
		contentPane.add(decPanel, BorderLayout.SOUTH);
		contentPane.add(tempPanel, BorderLayout.CENTER);
		decPanel.setVisible(true);
		repaint();
		validate();
	}


	/** Zeigt das Struktogramm an und l��t vorher die Deklarationsliste verschwinden.
	 */
	public void showStruktogramm()
	{
		contentPane.remove(decPanel);
		contentPane.remove(tempPanel);
		contentPane.remove(structPanel);
		contentPane.remove(structs);
		contentPane.add(struktoPanel, BorderLayout.SOUTH);
		struktoPanel.setVisible(true);
		struktogramm.getView().setVisible(true);
		validate();
		repaint();
	}
	
	/** Zeigt den "Struct-Editor" an
	 */
	public void showStructList()
	{
		contentPane.remove(decPanel);
		contentPane.remove(tempPanel);
		struktogramm.getView().setVisible(false);
		contentPane.remove(struktoPanel);
		contentPane.add(structPanel, BorderLayout.SOUTH);
		contentPane.add(structs, BorderLayout.CENTER);
		structPanel.setVisible(true);
		validate();
		repaint();
	}
	
	/** Zeigt die Watchlist an oder l��t sie verschwinden */
	public void showWatchList(boolean show)
	{
		if (show == true)
			contentPane.add(watchPanel, BorderLayout.EAST);
		if (show == false)
			contentPane.remove(watchPanel);
		validate();	
		repaint();
	}
	
	/** Zeigt das OutputPanel an oder l��t es verschwinden */
	public void showOutput(boolean show)
	{
		if (show == true)
			contentPane.add(outputPanel, BorderLayout.WEST);
		else
			contentPane.remove(outputPanel);
		validate();	
		repaint();
	}
	
	/** Erstellt ein neues Struktogramm */
	public Struktogramm newStruktogramm(String name)
	{
		removePanels();
		// Name schon vorhanden ?
		String saveName = new String(name);
		int i = 2;
		while (findStruktogramm(name) != null)
			name = saveName + new Integer(i++).toString();
		struktogramm = new Struktogramm(name, struktoPanel, this);
		struktogrammList.addElement(struktogramm);
		tempPanel = new JPanel();
		decPanel = new DecList(struktogramm,tempPanel);
		watchPanel = new WatchList(struktogramm);
		addPanels();
		struktorPanel.newStruktogrammCreated(name);	
		selectStruktogramm(name);
		return struktogramm;	
	}
	
	/** L�scht das aktuelle Struktogramm */
	public void deleteStruktogramm()
	{
		removePanels();
		String name = struktogramm.getName();
		struktogrammList.remove(findStruktogramm(name));
		DecList.deleteDecList(struktogramm);
		WatchList.deleteWatchList(struktogramm);
		struktorPanel.struktogrammDeleted(name);
		repaint();
	}
	
	/** benennt das aktuelle Struktogramm um */
	public void renameStruktogramm(String newName)
	{
		String oldName = struktogramm.getName();
		try {
			struktogramm.setName(newName);
			struktorPanel.struktogrammRenamed(oldName,newName);
		} catch(StruktorException se) {	se.showMsg(this); }
	}
	
	/** Zeigt das Load Fenster (in der App-Klasse: Filechooser) */
  	public void load()
	{
		Load loadObject = new Load(this);
	}
			

	/** L�dt das Struktogramm welches in den AppletParametern angegeben wurde
	 */
	public void loadStruktogramm()
	{
		struktor.strukelements.Load load;
		String loadString;
		
		try {
			if (getParameter("load")!=null)
			{
				loadString = getParameter("load");
				// Struktogramm laden	
				load = new Load(this, loadString);
			}
			else
				newStruktogramm("Default");
		} catch (NullPointerException npe) {newStruktogramm("Default");}	
	}
	

	/** L�dt ein serialisiertes PresetObjekt anhand eines Filenamens
	 * @param   loadString  
	 */
	public void loadPreset(String loadString)
	throws StruktorException
	{	
		if (loadString == null)
			loadString = getParameter("load");
		String plainFilename=null;
		if (loadString.toLowerCase().endsWith("str") || loadString.toLowerCase().endsWith("ser"))
			plainFilename = loadString.substring(0,loadString.length()-3);
		FileInputStream fs;
		InputStream is;
		ObjectInputStream ois;
		
		// ini laden
		try {
			if (!isApplet)
			{
				fs = new FileInputStream(plainFilename+"ser");
				ois = new ObjectInputStream(fs);
			}
			else
			{
				is = new java.net.URL(getCodeBase(),plainFilename+"ser").openStream();
				ois = new ObjectInputStream(is);
			}
			
			Presets presets = (Presets)ois.readObject();
			presets.applet=this;
			presets.isApplet= Struktor.isApplet;
			this.presets=presets;
			ois.close();
		} catch (ClassNotFoundException cnfe) {
			Tracer.out(cnfe);
			this.presets=new Presets(this);
			throw new StruktorException("Could not load Preset ("+cnfe+")");
		} catch (IOException ioe) {
			this.presets=new Presets(this);
			Tracer.out(ioe);
			throw new StruktorException("Could not load Preset ("+ioe+")");
		} catch (NullPointerException npe) {
			newStruktogramm("Default");
			Tracer.out(npe);
			throw new StruktorException("Could not load Preset ("+npe+")");
		} 	
	}

	/** Zeigt das Save-Fenster an und l��t da rekursiv den Code reinschreiben */
	public void save()
	{
		SaveDialog saveDialog = new SaveDialog(Utils.getFrame(this),this,struktogrammList, "str");
		if (saveDialog.showDialog()==SaveDialog.CANCEL)
			return;
			
		Save saveObject = new Save(this);
		if (presets.makeCCode)
		{
			saveObject.println("// Der folgende Code ist ann�hernd C-Code !");
			saveObject.println("// Folgende Anpassungen m�ssen vorgenommen werden:");
			saveObject.println("// 1. Keiner der Funktionen besitzt eine R�ckgabetyp, dieser mu� von Hand hinzugef�gt werden");
			saveObject.println("// 2. Falls interne Funktionen verwendet wurden, m�ssen diese entsprechend angepasst werden");
			saveObject.println("// 3. Es werden keine Prototypen ausgegeben, diese m�ssen nachtr�glich hinzugef�gt werden");
			saveObject.println("#include <stdio.h>");
		}	
		for (Enumeration el=struktogrammList.elements(); el.hasMoreElements(); )
	    {
			Struktogramm s=(Struktogramm)el.nextElement();
			if (s.saveMark)
				s.save(saveObject);
	    }
		saveObject.setVisible(true);
	}
	
	/** Zeigt ein bestimmtes Struktogramm an */
	public void selectStruktogramm(String name)
	{
		removePanels();
		struktogramm = findStruktogramm(name);
		decPanel = DecList.findDecList(struktogramm);
		watchPanel = WatchList.findWatchList(struktogramm);
		tempPanel = decPanel.getTempPanel();
		addPanels();
		struktorPanel.viewChanged(0);
		showStruktogramm();
		struktorPanel.struktogrammSelected(name);
		repaint();	
	}
	
	/** Hat die StruktogrammListe ein main-Struktogramm (ohne main-Struktogramm kein CCode)*/
	public boolean containsMainFunction()
	{
		for (int i=0; i<struktogrammList.size()-1;i++)
			if (((Struktogramm)struktogrammList.get(i)).getName().equals("main"))
				return true;
		return false;
	}	
	
	String getCode()
	{
		Save saveObject = new Save(this);
		JOptionPane.showConfirmDialog(
				getFrame(),
		  		"ok",
				"ok",
				JOptionPane.OK_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		for (Enumeration el=struktogrammList.elements(); el.hasMoreElements(); )
        {
			Struktogramm s=(Struktogramm)el.nextElement();
			s.save(saveObject);
        }
		return saveObject.getCode();
	}
	
	private void removePanels()
	{
		try {
			contentPane.remove(struktogramm.getView());
			contentPane.remove(watchPanel);
			contentPane.remove(decPanel);
			contentPane.remove(tempPanel);
		} catch (NullPointerException npe) {}
	}
	
	private void addPanels()
	{
		try {
			struktorPanel.updateWatchlistView();
			contentPane.add(tempPanel, BorderLayout.CENTER);
			contentPane.add(struktogramm.getView());
			contentPane.add(decPanel, BorderLayout.SOUTH);
		} catch (NullPointerException npe) {}
	}
	
	public void updateLookAndFeel()
	{
		SwingUtilities.updateComponentTreeUI(this);
		SwingUtilities.updateComponentTreeUI(struktogramm.getView());
		SwingUtilities.updateComponentTreeUI(struktoPanel);
		SwingUtilities.updateComponentTreeUI(struktorPanel);
		SwingUtilities.updateComponentTreeUI(decPanel);
		SwingUtilities.updateComponentTreeUI(watchPanel);
	}
	
	/** Gibt das gesuchte Struktogramm zur�ck */
	public Struktogramm findStruktogramm(String name)
	{
		for (Enumeration el=struktogrammList.elements(); el.hasMoreElements(); )
        {
             Struktogramm s=(Struktogramm)el.nextElement();
             if (s.getName().equals(name))
                return s;
        }
        return null;
	}
	
	// Getter-Setter-Methoden
	public  Struktogramm getActualStruktogramm()
	{
		return struktogramm;
	}
	
	/** Nicht so sch�n .... */
	Vector getStruktogrammList()
	{
		return struktogrammList;
	}
	
	/** Gibt einen neuen Control-View des Struktors zur�ck und wird in der Application-Klasse �berschrieben */
	StruktorEvt getStruktorInput(Struktor struktor)
	{
		return new StruktorEvt(struktor);
	}
}
