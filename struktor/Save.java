// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/** Eine Klasse zum Struktogramme Saven. 
 * Abgeleitet von JFrame, damit man im Applet den "Quellcode" da 
 * rein schreiben kann. Im Applet bleibt das Teil invisible dafür
 * wird @see saveToFile aufgerufen.
 */

public class Save
extends JFrame implements ActionListener
{
	JTextArea save = new JTextArea(50,50);
	/** speichert die Anzahl der Tabs damit man schön formatieren kann */
	int tabs=0;
	Struktor struktor;

	public Save(Struktor struktor)
	{
		super("Save all Struktogramms");
		this.struktor = struktor;
		save.setTabSize(2);
		save.setEditable(false);
		JPanel contentPane = new JPanel(); 
		contentPane.setLayout(new BorderLayout());
		Border bd1 = BorderFactory.createBevelBorder( BevelBorder.RAISED);
		Border bd2 = BorderFactory.createEtchedBorder();
		Border bd3 = BorderFactory.createCompoundBorder(bd1, bd2);
		((JPanel)contentPane).setBorder(bd3);

		JButton ok = new JButton("Ok");
		ok.addActionListener(this);
		contentPane.add(new JLabel("Copy Text to Clipboard and paste in Editor", JLabel.CENTER), BorderLayout.NORTH);
		contentPane.add(new JScrollPane(save), BorderLayout.CENTER);
		contentPane.add(ok, BorderLayout.SOUTH);
		setContentPane(contentPane); 
		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(50, 50);
		setSize((int)(dim.width/1.5), (int)(dim.height / 1.5));

	}

	/** Speichert den Inhalt der JTextArea in einem File */
	public void saveToFile(File file)
	throws StruktorException
	{
		try {
			file.createNewFile();
			FileOutputStream fr = new FileOutputStream(file);
			StringReader saveText = new StringReader(save.getText());
			int temp;
			while ((temp = saveText.read()) != -1)
				fr.write(temp);
			fr.close(); 
		} catch (IOException ioe)
		{System.err.println(ioe); throw new StruktorException("Cannot Save \".str\"-File !");
		}
		try
		{
			String name = file.getCanonicalPath();
			String iniName = name.substring(0,name.length()-4)+".ser";
			Tracer.out(iniName);
			struktor.presets.saveToFile(new File(iniName));	
		} catch (IOException ioe)
		{System.err.println(ioe); throw new StruktorException("Cannot Save \".ser\"-File !");
		}
	}

	public void addTab(int offset)
	{
		tabs = tabs + offset;
	}


	/** Fenster schließen */
	public void actionPerformed(ActionEvent event)
	{
		setVisible(false);
		dispose();
	}

	/** Die Methode mit der die Elemente in das JTextArea reinschreiben können */
	public void println(String string)
	{  	
		printTabs();
		print(string);
		print("\n");
	}
	/** is wohl klar */
	public void print(String string)
	{
		save.append(string);
	}

	/** Wird normalerweise nur intern vor jeder Zeile aufgerufen. Einmal aber auch von DecList weil die save()-Methode von Dec genauso auch für Parameter (die keine Tabs brauchen) benutzt wird */
	public void printTabs()
	{
		for (int i=0; i<tabs; i++)
			print("\t");
	}
	
	/** Momentan unbenutzt (für Später)*/	
	String getCode()
	{
		return save.getText();
	}

}
