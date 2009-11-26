// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import struktor.Presets;
import struktor.processor.datatypes.Datatype;

/** Eine Klasse für Deklarationen */
public class Dec extends JPanel
implements ItemListener, ActionListener, Datatype
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Object Variables
	JButton delete = new JButton("delete");
	JButton incDim = new JButton("incDim");
	JCheckBox parameter;
	JCheckBox pointer;
	JCheckBox array;
	JComboBox Type;
	JLabel nameLabel;
	JTextField name;
	
	int dim=1;
	
	JLabel indizesLeft = new JLabel("[");
	JTextField indizes;
	JLabel indizesRight = new JLabel("]");
	
	JLabel indizesLeft2 = new JLabel("[");
	JTextField indizes2 ;
	JLabel indizesRight2 = new JLabel("]");
	
	JLabel indizesLeft3 = new JLabel("[");
	JTextField indizes3 ;
	JLabel indizesRight3 = new JLabel("]");
	
	JLabel textValueLabel = new JLabel("Value:");
	JTextField textValue;
		
	JComboBox type;
	
	DecList decList;
	public Presets presets;
	
	public Dec(Presets presets,boolean isPointer, boolean isParameter, boolean isArray, int index, int temptype, String tempname, String value)
	{	
		this.presets = presets;
		if (presets.enabDecDelete)
			add(delete);
		delete.addActionListener(this);
		incDim.addActionListener(this);
		parameter = new JCheckBox("Parameter", isParameter);
		if (presets.enabParameter)
			add(parameter);
		pointer = new JCheckBox("Pointer", isPointer);
		if (presets.enabPointer)
			add(pointer);
		array = new JCheckBox("Array", isArray);
		array.addItemListener(this);
		if (presets.enabArrays)
			add(array);
		pointer.addItemListener(this);
		add(new JLabel("Type:"));
		type = new JComboBox();
		if (presets.enabInteger)
			type.addItem("Integer");
		if (presets.enabDouble)
			type.addItem("Double");
		if (presets.enabCharacter)
			type.addItem("Character");
		if (presets.enabStrings)
			type.addItem("String");
		// Und Strukturen dazu ...
		for (Enumeration el=struktor.processor.StructPanel.nameList.elements(); el.hasMoreElements(); )
        {
			 String s=(String)el.nextElement();
             type.addItem(s);
        }	
		type.setSelectedIndex(temptype-1);
		type.setVisible(true);
		add(type);
		
		add(new JLabel("Name :"));
		name = new JTextField(tempname,7);
		add(name);
		indizes = new JTextField(new Integer(index).toString(),3);
		indizes2 = new JTextField("1",3);
		indizes3 = new JTextField("1",3);
		
		add(textValueLabel);
		textValue = new JTextField(value,7);
		textValue.setVisible(true);
		add(textValue);
		if (isArray())
			arraySelected();
		else
			arrayDeSelected();
		if (isPointer())
			pointerSelected();
		else
			pointerDeSelected();
		if (isParameter())
			parameterSelected();
		else
			parameterDeSelected();			
	}
	
	void setDecList(DecList decList)
	{
		this.decList = decList;
	}
	
	/** Ist die Variable ein Pointer ?*/
	public boolean isPointer()
	{
		return pointer.isSelected();
	}
	
	/** Eine Variable zum Pointer machen */
	void setPointer(boolean temp)
	{
		pointer.setSelected(temp);
		if (isPointer())
			pointerSelected();
		else
			pointerDeSelected();	
	}
	
	/** Ist die Variable ein Parameter */
	boolean isParameter()
	{
		return parameter.isSelected();	
	}
	
	/** Eine Variable zum Parameter (für das zugehörige Struktogramm) machen */
	void setParameter(boolean temp)
	{
		parameter.setSelected(temp);
		if (isParameter())
			parameterSelected();
		else
			parameterDeSelected();
	}
	
	/** Ist die Variable ein Array ? */
	public boolean isArray()
	{
		return array.isSelected();
	}
	
	/** Ein Variable zum Array machen */
	void setArray(boolean temp)
	{
		array.setSelected(temp);
		if (isArray())
			arraySelected();
		else
			arrayDeSelected();	
	}
		
	/** Index eines Arrays zurückgeben */
	public int getIndex()
	{
		return new Integer(indizes.getText()).intValue()*
				new Integer(indizes2.getText()).intValue()*
				new Integer(indizes3.getText()).intValue();
	}
	
	/** Index eines Arrays gestlegen */
	void setIndex(int i)
	{
		indizes.setText(new Integer(i).toString());
	}
	
	/** Datentyp zurückgeben */
	public int getType()
	{
		String typeString = (String)type.getSelectedItem();
		if (typeString.equals("Integer"))
			return INTEGER;
		else if (typeString.equals("Double"))
			return DOUBLE;	
		else if (typeString.equals("Character"))
			return CHARACTER;
		else if (typeString.equals("String"))
			return STRING;
		else
			return STRUCT;					
	}
	
	public String getStructType()
	{
		if (getType()==STRUCT)
			return (String)type.getSelectedItem();
		return null;
	}	
	
	/** Datentyp festlegen */
	void setType(int type)
	{
		switch (type)
		{
		case INTEGER:
			this.type.setSelectedItem("Integer");
			break;
		case DOUBLE:
			this.type.setSelectedItem("Double");
			break;
		case CHARACTER:
			this.type.setSelectedItem("Character");
			break;
		case STRING:
			this.type.setSelectedItem("String");
			break;
		}
	}
	
	/** VariablenName zurückgeben */
	public String getName()
	{
		return name.getText();
	}
	
	/** VariablenName festlegen */
	public void setName(String name)
	{
		this.name.setText(name);
	}
	
	/** Wert (der bei der Deklaration festgelegt wurde) zurückgeben */
	public String getValue()
	{
		return textValue.getText();
	}
	
	/** Wert festlegen */
	void setValue(String value)
	{
		textValue.setText(value);
	}
	
	/** Format-Code für printf und scanff zurückgeben */
	String getCFormatCode()
	{
		if (((String)type.getSelectedItem()).equals("Integer"))
			return "%i";
		else if (((String)type.getSelectedItem()).equals("Double"))
			return "%d";
		else if (((String)type.getSelectedItem()).equals("Character"))
			return "%c";
		else if (((String)type.getSelectedItem()).equals("String"))
			return "%s";
		else 
			return null;	
	}	
	
	// Ereignisverarbeitung	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==delete)
			decList.delete(this);
		else
			incDim();	
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		Object src = e.getSource();
		if (src == pointer)
		{
			if (pointer.isSelected())
			{
				pointerSelected();
			}
			else
			{
				pointerDeSelected();
			}
		}
		if (src == array)
		{
			if (array.isSelected())
			{
				arraySelected();
			}
			else
			{
				arrayDeSelected();
			}
		}
	}
	
	/** Aktualisiert den View (im Moment leer ?!) */
	private void parameterSelected()
	{
	
	}
	
	/** Aktualisiert den View (im Moment leer ?!) */
	private void parameterDeSelected()
	{
	
	}
	
	/** Aktualisiert den View */
	private void pointerSelected()
	{
		textValue.setText("0x0");
		remove(textValue);
		remove(textValueLabel);
		revalidate();
	}
	
	/** Aktualisiert den View */
	private void pointerDeSelected()
	{
		add(textValueLabel);
		add(textValue);
		textValue.setText("");
		revalidate(); 
	
	}
	
	/** Aktualisiert den View */
	private void arraySelected()
	{
		parameter.setSelected(false);
		parameter.setEnabled(false);
		remove(textValue);
		remove(textValueLabel);
		add(indizesLeft);
		add(indizes);
		add(indizesRight);
		if (dim>=2)
		{
			add(indizesLeft2);
			add(indizes2);
			add(indizesRight2);
		}
		if (dim>=3)
		{
			add(indizesLeft3);
			add(indizes3);
			add(indizesRight3);
		}
		name.setHorizontalAlignment(JTextField.RIGHT);
		add(incDim);
		revalidate();
	}
	
	/** Aktualisiert den View */
	private void arrayDeSelected()
	{
		parameter.setEnabled(true);
		remove(incDim);
		remove(indizesLeft);
		remove(indizes);
		remove(indizesRight);
		if (dim==3||dim==2)
		{
			remove(indizesLeft2);
			remove(indizes2);
			remove(indizesRight2);
		}
		if (dim==3)
		{
			remove(indizesLeft3);
			remove(indizes3);
			remove(indizesRight3);
		}
		add(textValueLabel);
		add(textValue);
		name.setHorizontalAlignment(JTextField.LEFT);
		revalidate();
	}
	
	/** Aktualisiert den View (Dimension eines Arrays erhöht)*/
	private void incDim()
	{
		remove(incDim);
		switch (++dim)
		{
		case 2:
			add(indizesLeft2);
			add(indizes2);
			add(indizesRight2);
			add(incDim);
			break;
		case 3:
			add(indizesLeft3);
			add(indizes3);
			add(indizesRight3);
			break;
		}
		revalidate();
	}
	
	
	/** Speichern */
	public void save(struktor.Save saveObject)
	{
		int type = getType();
		String typeAsString = "";
		String name = "";
		String value = "";
		switch (type)
		{
		case INTEGER:
			typeAsString = (isPointer() ? "int* " : "int ");
			break;
		case DOUBLE:
			typeAsString = (isPointer() ? "double* " : "double ");
			break;
		case CHARACTER:
			typeAsString = (isPointer() ? "char* " : "char ");
			break;
		case STRING:
			typeAsString = "String ";
			break;
		}
		name = (isArray() ? getName()+"["+getIndex()+"]" : getName());   
		switch (type)
		{
		case INTEGER:
		case DOUBLE:
			if (!isPointer() && !isArray())
				value = (getValue().equals("") ? "" : " = "+getValue());
			break;
		case CHARACTER:
			if (!isPointer())
				if (!isArray())
					value = (getValue().equals("") ? "" : " = '"+getValue().charAt(0)+"'");
				else
					value = (getValue().equals("") ? "" : " = \""+getValue()+"\"");
			break;
		case STRING:
			if (!isPointer())
				value = (getValue().equals("") ? "" : " = \""+getValue()+"\"");
			break;
		}
		if (isParameter())
			value = "";
		saveObject.print(typeAsString+name+value);
	}		
}