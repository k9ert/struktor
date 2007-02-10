// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.processor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import struktor.Struktor;
import struktor.Tracer;
import struktor.Utils;
import struktor.strukelements.Dec;

/** Eine Klasse f�r den "Struct-Editor" 
 * Sch�n ist er nicht geworden aber was solls ...
 */
public class StructPanel extends JPanel
implements ActionListener, ItemListener, struktor.processor.datatypes.Datatype
{
	// Static Variables
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Vector structList = new Vector();
	static int count=0;
	public static Vector nameList=new Vector();
	
	static Struct addStruct(Struct s)
	{
		structList.addElement(s);
		return s;
	}
	static Struct findStruct(String name)
	{
		for (Enumeration el=structList.elements(); el.hasMoreElements(); )
        {
			 Struct s=(Struct)el.nextElement();
             if (s.getName().equals(name))
                return s;
        }
		Tracer.out("OhOh ... no Struct found !");    
		return null;
	}
	
	static public void deleteStruct(String name)
	{
        structList.remove(findStruct(name));
	}
	

	// Object Variables
	public Struktor struktor;
	private JButton newStruct = new JButton("newStruct");
	private JComboBox structSelect = new JComboBox();
	private Struct struct;
	private JPanel tempPanel;
		
	public StructPanel(Struktor struktor, JPanel tempPanel)
	{
		super(new FlowLayout());
		this.tempPanel = tempPanel;
		this.struktor = struktor;
		count++;
		add(new Label("this feature is not yet implemented properly!!"));
		if (true)
			add(newStruct);
		if (true)
			add(structSelect);	
		
		newStruct.addActionListener(this);
		newStruct.setVisible(true);
		
		structSelect.addItemListener(this);
		
		structList = new Vector();
		
	}
	

	
	Struct newStruct()
	{
		String name = JOptionPane.showInputDialog(
							Utils.getFrame(struktor),
							"Please Input Name of new Struct:",
							"Input Name",
							JOptionPane.QUESTION_MESSAGE);
		if (name != null)
			return (newStruct(name));
		return null;			
	}
	
	Struct newStruct(String name)
	{
		struct = new Struct(name, this);
		try {
			addStruct(struct);
			nameList.add(new String(name));
			tempPanel.removeAll();
		} catch(NullPointerException npe) {}
		
		tempPanel.add(struct);
		newStructCreated(name);
		return struct;
	}
	
	public void showStruct(String name)
	{
		tempPanel.removeAll();
		tempPanel.add(findStruct(name));
	}
	
	public void newStructCreated(String name)
	{
		structSelect.addItem(name);
		structSelect.setSelectedItem(name);
	}	
		
	public void actionPerformed(ActionEvent e) 
	{
		Struct temp = newStruct();
		if (temp != null)
			tempPanel.add(temp);
		revalidate();	
	}
	
	public void itemStateChanged(ItemEvent ie)
	{
		Object src = ie.getSource();
		if (src == structSelect)
			showStruct(structSelect.getSelectedItem().toString());
		revalidate();
	}
	
	Vector getStructList()
	{
		return structList;
	}
}

class Struct extends JPanel
implements ActionListener, struktor.processor.datatypes.Datatype
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton newDeclaration;
	Vector decList;
	String name;
	StructPanel structPanel;
	Box decListPanel=new Box(BoxLayout.Y_AXIS);
	
	Struct(String name, StructPanel structPanel)
	{
		super(new BorderLayout());
		this.structPanel = structPanel;
		this.name=name;
		newDeclaration=new JButton("new Declaration");
		add(newDeclaration, BorderLayout.NORTH);
		add(decListPanel, BorderLayout.CENTER);
		newDeclaration.addActionListener(this);
		newDeclaration.setVisible(true);
		
		decList = new Vector();
		
		setVisible(true);
		validate();
	}
	
	public String getName()
	{
		return name;
	}
	
	Integer getSpace()
	{
		int space=0;
		for (Enumeration el=decList.elements(); el.hasMoreElements(); )
        {
			 Dec s=(Dec)el.nextElement();
             switch (s.getType())
			 {
			 case INTEGER:
				space +=4;
				break;
			case DOUBLE:
				space +=8;
			 }
        }
		return new Integer(space);
	}
	
	String getIdentifierAtOffset(int offset)
	{
	
		int iOffset=0;
		for (Enumeration el=decList.elements(); el.hasMoreElements(); )
        {
			Dec s=(Dec)el.nextElement();
            if (iOffset==offset)
				return s.getName();
			switch (s.getType())
			{
			case INTEGER:
				iOffset +=4;
				break;
			case DOUBLE:
				iOffset +=8;
			}
        }
		return null;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		newDeclaration(false, false, false, 10, INTEGER, "name", "");
	}
	
	Dec newDeclaration(boolean isPointer, boolean isParameter, boolean isArray, int index, int temptype, String tempname, String value)
	{
		Dec dec = new Dec(structPanel.struktor.presets, isPointer, isParameter, isArray, index, temptype, tempname, value);
		
		decList.addElement(dec);
		Tracer.out("New Dec set ...");
		decListPanel.add(dec);
		decListPanel.validate();
		dec.setVisible(true);
		revalidate();
		return dec;
	}
	
	Vector getDecList()
	{
		return decList;
	}
}



