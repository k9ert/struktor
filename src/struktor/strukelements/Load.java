// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import java_cup.runtime.Symbol;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import struktor.Struktor;
import struktor.StruktorException;


public class Load
extends JFrame implements ActionListener
{

	JTextArea save = new JTextArea(50,50);
    Struktor struktor;
	Yylex lexer;
	LoadParser parser;
	
	public Load(Struktor struktor)
	{
		super("Load Struktogramms");
		this.struktor = struktor;
		save.setTabSize(2);
		save.setEditable(true);
		JPanel contentPane = new JPanel(); 
		contentPane.setLayout(new BorderLayout());
		Border bd1 = BorderFactory.createBevelBorder( BevelBorder.RAISED);
		Border bd2 = BorderFactory.createEtchedBorder();
		Border bd3 = BorderFactory.createCompoundBorder(bd1, bd2);
		((JPanel)contentPane).setBorder(bd3);

		JButton ok = new JButton("Ok");
		ok.addActionListener(this);
		contentPane.add(new JLabel("Paste Text from Clipboard and pars", JLabel.CENTER), BorderLayout.NORTH);
		contentPane.add(new JScrollPane(save), BorderLayout.CENTER);
		contentPane.add(ok, BorderLayout.SOUTH);
		setContentPane(contentPane); 
		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(50, 50);
		setSize((int)(dim.width/1.5), (int)(dim.height / 1.5));
		setVisible(true);
	}
	
	public Load(Struktor struktor, InputStreamReader sr)
	{
		super("Load Struktogramms");
		this.struktor = struktor;
		lexer = new Yylex(sr);
		parse();
		if (struktor.containsMainFunction())
			struktor.selectStruktogramm("main");
		dispose();
	}
	
	public Load(Struktor struktor, String sr)
	{
		super("Load Struktogramms");
		this.struktor = struktor;
		URL strukURL=null;
		InputStream is=null;
		try {
			strukURL = new URL(struktor.getDocumentBase(), sr);
			is = strukURL.openStream();
		} catch (Exception e) {new StruktorException("Unexpected Error:\n" + e.toString()).showMsg(this);}
		
		lexer = new Yylex(is);
		parse();
		if (struktor.containsMainFunction())
			struktor.selectStruktogramm("main");
		dispose();
	}
	
	public Load(Struktor struktor, File file)
	{
		super("Load Struktogramms");
		this.struktor = struktor;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException fnfe) {new StruktorException("Cannot open file !" + fnfe.toString()).showMsg(this);}
		lexer = new Yylex(fis);
		parse();
		try {
			fis.close();
		} catch (IOException ioe) {new StruktorException("Cannot Close File!"+ ioe.toString()).showMsg(this);}
		if (struktor.containsMainFunction())
			struktor.selectStruktogramm("main");
		dispose();
	}
  
  
	void parse()
	{
		parser = new LoadParser(lexer, struktor);
		Symbol parse_tree = null;
		try {
			if (Struktor.idebug)
				parser.debug_parse();
			else
				parser.parse();	
		} catch (StruktorException se) { se.showMsg(this);}
		 catch (Exception e) { new StruktorException("Unexpected Error:\n" + e.toString()).showMsg(this);}

	}
	public void actionPerformed(ActionEvent event)
	{
		
		StringReader temp = null;
		temp = new StringReader(save.getText());

		lexer = new Yylex(temp);
		parse();
		setVisible(false);
		dispose();
	}

  
}