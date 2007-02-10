// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import struktor.processor.LoopControlException;
import struktor.processor.Memory;
import struktor.processor.Processor;
import struktor.processor.ProcessorException;
import struktor.processor.ReturnException;
import struktor.strukelements.Dec;
import struktor.strukelements.DecList;

public class ExprCalc extends JApplet
implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Presets presets = new Presets();
	DecList decList;
	Processor processor;
	Memory memory;
	Box decPanel=new Box(BoxLayout.Y_AXIS);
	JPanel control=new JPanel();
	JTextField expr= new JTextField(20);
	JTextField value= new JTextField("",20);
	JButton calculate = new JButton("auswerten");
	Box box = new Box(BoxLayout.X_AXIS);
	Container contentPane;
	
	public void init()
	{
		decList = new DecList(presets);
		load();
		Struktor.idebug = false;
		try {
			processor = new Processor(null, decList.getDecList(), null, null);
			memory = processor.getMemoryByExprCalc(this);
		} catch (InterruptedException ie) {}
		contentPane=getContentPane();
		contentPane.setLayout(new BorderLayout());
		control.add(calculate);
		control.add(expr);
		control.add(value);
		value.setEditable(false);
		contentPane.add(control,BorderLayout.CENTER);
		contentPane.add(decPanel,BorderLayout.NORTH);
		
		calculate.addActionListener(this);
		expr.addActionListener(this);
		
	}
	
	public void actionPerformed(java.awt.event.ActionEvent ae) 
	{
		try {
			Memory.setActualFormByExprCalc(this,memory);
			value.setText(processor.parse(expr.getText()).toString());
		} catch (InterruptedException ie) {}
		catch (ProcessorException pe) {value.setText(pe.getMsg());}
		catch (LoopControlException lce) {new StruktorException("\"break;\" oder \"continue;\" macht hier keinen Sinn !").showMsg(this);}
		catch (ReturnException re) {new StruktorException("\"return;\" macht hier keinen Sinn !").showMsg(this);}
	}
	
	
	public void load()
	{
		String loadString;
		try {
			if (getParameter("load")!=null)
			{
				loadString = getParameter("load");
				try {
					while (!loadString.equals(""))
						loadString = loadDec(loadString);
				} catch (StruktorException se) {}
			}
		} catch (NullPointerException npe) {}	
	}
	
	String loadDec(String string)
	throws StruktorException
	{
		int decType;
		boolean isPointer=false;
		boolean isArray=false;
		String index= new String();
		int iindex=0;
		String name = "";
		String value = new String();
		
		// Typ ermitteln
		if (string.startsWith("int"))
		{
			decType=Dec.INTEGER;
			string=string.substring(3,string.length());
		}
		else if (string.startsWith("double"))
		{
			decType=Dec.DOUBLE;
			string=string.substring(6,string.length());
		}
		else if (string.startsWith("char"))
		{
			decType=Dec.CHARACTER;
			string=string.substring(4,string.length());
		}
		else if (string.startsWith("String"))
		{
			decType=Dec.STRING;
			string=string.substring(6,string.length());
		}
		else
			throw new StruktorException("Cant read dec");
		
		while (string.charAt(0) == ' ')	
			string=string.substring(1,string.length());
		
		// Pointer ?
		if (string.charAt(0)=='*')
		{
			isPointer = true;
			string=string.substring(1,string.length());
		}
		
		while (string.charAt(0) == ' ')	
			string=string.substring(1,string.length());
		// Name der Variablen
		StringBuffer sb = new StringBuffer(string);
		while (sb.charAt(0) != ';' && sb.charAt(0) !='=' && sb.charAt(0) !='*' && sb.charAt(0) !='[')
		{
			name += new Character(sb.charAt(0)).toString();
			sb = sb.delete(0,1);
		}
		if (sb.charAt(0) =='[')
		{
			isArray=true;
			sb=sb.delete(0,1);
			while (sb.charAt(0) != ']')
			{
				index += new Character(sb.charAt(0)).toString();
				sb = sb.delete(0,1);
			}		
			sb=sb.delete(0,1);
			iindex = new Integer(index).intValue();
		}
		System.out.println(sb);
		
		if (sb.charAt(0) =='=')
			sb=sb.delete(0,1);
		
		
		
		// Wert der Variablen
		while (sb.charAt(0) != ';')
		{
			value += new Character(sb.charAt(0)).toString();
			sb = sb.delete(0,1);
		}	
		sb = sb.delete(0,1);
		
		Tracer.out("Typ:"+type2String(decType, isPointer)+"Wert:"+value+"Name:"+name+(isArray ? "["+index+"[" :""));
		
		decPanel.add(new JLabel(type2String(decType, isPointer)+" "+name+(isArray ? "["+index+"]" :"")));
		decList.newDeclaration(isPointer,false,isArray,iindex,decType, name.trim(),value);
		return sb.toString();
			
	}
	
	String type2String(int type, boolean isPointer)
	{
		String string;
		switch (type)
		{
		case Dec.INTEGER:
			string = "Integer";
			break;
		case Dec.DOUBLE:
			string = "Double";
			break;
		case Dec.CHARACTER:
			string = "Character";
			break;
		case Dec.STRING:
			string = "String";
			break;
		default:
			return "Fehler";
		}
		if (isPointer)
			string += "*";
		return string;
	}
			
}