// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor;

import struktor.*;
import struktor.strukelements.*;
import struktor.gui.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class StruktorApplication extends Struktor
implements WindowListener
{
	JDesktopPane desktop;
	JFrame frame;
	String saveLocation = "D:\\programmieren\\java\\Struktor\\StruktorDevelop";
	
	public static void main (String[] args) {
		try {
		    if (args[0]!=null){
			if (args[0].toLowerCase().endsWith("--help")){
			    	    System.out.println("Struktor - A Nassi-Shneiderman Editor with an integrated Interpreter");
				    System.out.println("usage: struktor [struktogram] | --help");
				    System.out.println("struktogram \t\tAn .str or .ser file");
				    System.out.println("--help \t\t\tdisplay this help-screen");
				    System.exit(0);
			}
		    }
		} catch (ArrayIndexOutOfBoundsException aioobe) {}
		isApplet = false;
		StruktorApplication a = new StruktorApplication();
		JFrame frame = new JFrame("Struktor - Copyright 2000-2004, Kim Neunert (k9ert@gmx.de), All Rights Reserved");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(a, BorderLayout.CENTER);
		frame.setSize(800,600);
		frame.addWindowListener( a );
		frame.setVisible(true);
		a.init();
		a.presets.isApplet=false;
		a.start();
		try {
			if (args[0]!=null){
				if (args[0].toLowerCase().endsWith("str")){
					Tracer.out(args[0].substring(0,args[0].length()-3)+"ser");
					try {
						a.loadPreset(args[0].substring(0,args[0].length()-3)+"ser");
					} catch (StruktorException se) {}
					a.loadStruktogramm(args[0]);
				}
				else if (args[0].toLowerCase().endsWith("ser"))	{
					try {
						a.loadPreset(args[0]);
					} catch (StruktorException se) {}
					a.loadStruktogramm(args[0].substring(0,args[0].length()-3)+"str");
				}
				else
					new StruktorException("Illegal File-Type (only .str or .ser) - try struktor --help").showMsg(a);
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {Tracer.out(aioobe.toString());}
		if (!a.containsMainFunction())
			a.selectStruktogramm(a.getActualStruktogramm().getName());
			
	}
	
	public void load(){
		//Create a file chooser
        final JFileChooser fc = new JFileChooser(saveLocation);
        fc.setDialogTitle("choose File to Load !");
		fc.setFileFilter(
			new javax.swing.filechooser.FileFilter()
			{
				public boolean accept(java.io.File file)
				{
					String fileName = file.getName();
					int nameLength = fileName.length();
					try {
						if (fileName.substring(nameLength-3, nameLength).equals("str") || file.isDirectory())
							return true;
						else
							return false;
					} catch (StringIndexOutOfBoundsException sioobe) {return true;}	
				}
				public String getDescription()
				{
					return "Struktogramme (*.str)";
				}
			}
		);	
				
		int returnVal = fc.showOpenDialog(struktor);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			java.io.File file = fc.getSelectedFile();
			try {
				saveLocation = file.getCanonicalPath();
				loadPreset(file.getCanonicalPath());
			} catch (java.io.IOException ioe) {Tracer.out("SER-File not Loaded");}
			catch (StruktorException se) {Tracer.out("SER-File not Loaded");}
			Load loadObject = new Load(struktor, file);
		}
	}
	
	public void loadStruktogramm(String filename)
	{
		struktor.strukelements.Load load;
		try {
			load = new Load(this, (InputStreamReader)(new FileReader(filename)));				
		} catch(FileNotFoundException fnfe) {new StruktorException(fnfe.toString()).showMsg(this);}
	}
	
	public void save()
	{
		//Create a file chooser
        final JFileChooser fc = new JFileChooser(saveLocation);
        fc.setDialogTitle("choose File to Save !");
		fc.setFileFilter(
			new javax.swing.filechooser.FileFilter()
			{
				public boolean accept(java.io.File file)
				{
					String fileName = file.getName();
					int nameLength = fileName.length();
					try {
						if (fileName.substring(nameLength-3, nameLength).equals("str") || file.isDirectory())
							return true;
						else
							return false;
					} catch (StringIndexOutOfBoundsException sioobe) {return true;}	
				}
				public String getDescription()
				{
					return "Struktogramme (*.str)";
				}
			}
		);	
				
		int returnVal = fc.showSaveDialog(struktor);
		if (returnVal != JFileChooser.APPROVE_OPTION) 
			return;
			
		java.io.File file = fc.getSelectedFile();
		if (!file.getName().endsWith(".str"))
		file = new File(file.toString()+".str");
		
		SaveDialog saveDialog = new SaveDialog(frame,this,getStruktogrammList(), "str");
		if (saveDialog.showDialog()==SaveDialog.CANCEL)
			return;
		try {
			saveLocation = file.getCanonicalPath();
		} catch (java.io.IOException ioe) {}
		Save saveObject = new Save(this);
		for (Enumeration el=getStruktogrammList().elements(); el.hasMoreElements(); )
    	{
			Struktogramm s=(Struktogramm)el.nextElement();
			if (s.saveMark)
				s.save(saveObject);
    	}
		try {
			saveObject.saveToFile(file);
		} catch (StruktorException se) {se.showErrorMsg(frame); return;}
		new StruktorException("Sucessfully Saved !").showMsg(this);
	}
	
	public void saveAsBitmap()
	{
		SaveDialog saveDialog = new SaveDialog(Utils.getFrame(this),this,getStruktogrammList(), "str");
		if (saveDialog.showDialog()==SaveDialog.CANCEL)
			return;
			
		for (Enumeration el=getStruktogrammList().elements(); el.hasMoreElements(); ) {
			Struktogramm s=(Struktogramm)el.nextElement();
			if (s.saveMark)
			{
				struktor.selectStruktogramm(s.getName());	
				s.saveAsImage();
			}
		}
		new StruktorException("Die Funktionen wurden unter Ihrem Namen im aktuellen Verzeichnis als BMP-Datei gespeichert").showMsg(this);
	}
	

	public void windowClosing (WindowEvent e) {					
	    this.stop();
	    System.exit(0);
	}
	public void windowClosed (WindowEvent e) { }
	public void windowOpened (WindowEvent e) { }
	public void windowIconified (WindowEvent e) { }
	public void windowDeiconified (WindowEvent e) { }
	public void windowActivated (WindowEvent e) { }
	public void windowDeactivated (WindowEvent e) { }

	StruktorEvt getStruktorInput(Struktor struktor) {
		return new StruktorAppEvt(this);
	}
}
