// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import struktor.processor.*;
import struktor.Utils;
import struktor.Presets;


class Switch extends StrukElement
implements CommandTypes, Constants
{

	private StrukElement defaultt=null;
	private Vector altList=null;
	
	Switch(Struktogramm s, String label)
	{
		super(s, label);
		altList = new Vector();
		mEvt = new SwitchEvt(this, struktogramm);
	 	addMouseListener(mEvt);
	}
	
	void recalculate()
	{
		calcAlt();
		recalculateChilds();
		super.recalculate();
	}

	void execute(Processor p)
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException
	{
		boolean somethingExecuted=false;
		delayAndPaint(p);
		delayAndPaint(p);
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			SwitchValue a=(SwitchValue)el.nextElement();
			if (p.parse(getLabel()+"=="+a.value+";",CALCULATION,"") == 0)
				continue;
			else
			{
				somethingExecuted=true;
				a.alt.execute(a.alt,p);
				break;
			}
    	} 
		if (!somethingExecuted)
			defaultt.execute(defaultt,p);
	}
	
	Vector getAltList()
	{
		return altList;
	}
	
	void setDefault(StrukElement defaultt)
	{
		defaultt.delConnection();
		defaultt.setUpConnection(this,ALT);
		if (this.defaultt!=null)
			this.defaultt.delete(true);
		this.defaultt=defaultt;
	}

	void addAlt(StrukElement alt)
	{
		alt.delConnection();
		alt.setUpConnection(this,ALT);
		altList.add(new SwitchValue(alt, "value",this));
	}
	
	void addAlt()
	{
		addAlt(struktogramm.addElement(COMMAND));
	}
	
	void delAlt(SwitchValue switchValue)
	{
		switchValue.alt.deleteAll();
		altList.remove(switchValue);
		repaint();
	}
	
	void setAlt(StrukElement formallyAlt, StrukElement alt)
	{
		SwitchValue a;
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			a=(SwitchValue)el.nextElement();
			if (a.alt == formallyAlt)
			{
				a.alt=alt;
				if (alt!=null)
				alt.delConnection();
				if (formallyAlt!=null)
          			formallyAlt.delConnection();
		  		alt.setUpConnection(this,ALT);
				break;
			}
    	}	
	}
	void setAltList(Vector altList)
	{
		for (Enumeration el=this.altList.elements(); el.hasMoreElements(); )
    	{
			SwitchValue a=(SwitchValue)el.nextElement();
			a.alt.delete(true);
    	} 
		this.altList=altList;
	}
	
	void calcNext()
	{	
		try {
			int maxHeight=getAltMaxHeight();
					
			if (getNext()!=null)
			{
				getNext().setX(getX());
				
				getNext().setY(getY()+calcHeight());
			  	getNext().setWidth(this.getWidth());
				
			}
		} catch (ResizeException re) {}
	}
	
	private void recalculateChilds()
	{
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			SwitchValue a=(SwitchValue)el.nextElement();
			a.alt.recalculate();
    	}
	
	}

	void calcAlt()
	{
		defaultt.setX(this.getX());
		defaultt.setY(this.getY()+this.getHeight());
		defaultt.setWidth(getAltWidth());
		int count=0;
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			count++;
			SwitchValue a=(SwitchValue)el.nextElement();
			if (a.alt!=null)
			{
				a.alt.setX((int)(this.getX()+getAltWidth()*count)-count);
				a.alt.setY(this.getY()+this.getHeight());
				a.alt.setWidth(getAltWidth());
			}
		}
	}

	int calcHeight()
	throws ResizeException
	{
		// Diese Scheißmethode ist nur deswegen so lang weil alles noch mal extra für defaultt implementiert werden muß :-( Es läßt sich ernsthaft überlegen ob man die nicht evtl. auch noch in den Verktor verschiebt !
		int h=getHeight();
		int maxHeight=getAltMaxHeight();
		int minHeight=getAltMinHeight();
		// Extrawurscht für default :-(
		if (defaultt.calcHeight()<minHeight)
			minHeight=defaultt.calcHeight();
		if (defaultt.calcHeight()>maxHeight)
			maxHeight=defaultt.calcHeight();	
		boolean flag=true;
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			// Extrawurscht für Defaultt:
			if (defaultt!=resizingElement && flag==true)
			{
				flag=false;
				if (gettingSmaller)
				{
					if (defaultt.calcHeight()>minHeight)
						makeBlockSmaller(defaultt, defaultt.calcHeight()-minHeight);	
					else
						makeBlockLarger(defaultt, minHeight-defaultt.calcHeight());
				}
				else
				{
					if (defaultt.calcHeight()<maxHeight)
						makeBlockLarger(defaultt, defaultt.calcHeight()-maxHeight);
					else
						makeBlockSmaller(defaultt, maxHeight-defaultt.calcHeight());	
				}
			}
			SwitchValue a=(SwitchValue)el.nextElement();
			if (a.alt!=null)
				if (a.alt==resizingElement)
					continue;
			if (gettingSmaller)
			{
				if (a.alt.calcHeight()>minHeight)
					makeBlockSmaller(a.alt, a.alt.calcHeight()-minHeight);	
				else
					makeBlockLarger(a.alt, minHeight-a.alt.calcHeight());
			}
			else
			{
				if (a.alt.calcHeight()<maxHeight)
					makeBlockLarger(a.alt, a.alt.calcHeight()-maxHeight);
				else
					makeBlockSmaller(a.alt, maxHeight-a.alt.calcHeight());	
			}
		}
		return h+maxHeight;
	}
	
	
	
	int getAltWidth()
	{
		int altWidth = Math.round((getWidth())/(altList.size()+1)+1);
		return altWidth;
	}
	
	int getAltMaxHeight()
	throws ResizeException
	{
		int maxHeight=0;
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			SwitchValue a=(SwitchValue)el.nextElement();
			if(a.alt.calcHeight()>maxHeight)
				maxHeight=a.alt.calcHeight();
    	}
		return maxHeight;
	}
	
	int getAltMinHeight()
	throws ResizeException
	{
		int minHeight=300000;
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			SwitchValue a=(SwitchValue)el.nextElement();
			if(a.alt.calcHeight()<minHeight)
				minHeight=a.alt.calcHeight();
    	}
		return minHeight;
	}
	
	void heightSet(int connectionType)
	throws ResizeException
	{
		if (connectionType == ALT)
			calcHeight();
		super.heightSet(this.getUpConnectionType());
	}	

	void deleteAll()
	{
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			SwitchValue a=(SwitchValue)el.nextElement();
			a.alt.deleteAll();
    	}
		defaultt.deleteAll();
		if (getUpConnectionType() != NEXT & hasUpConnection())
			connect(getUpConnectionType(), getUpConnection(), struktogramm.addElement(COMMAND));
		delete(true);
	}
		
	public void paintComponent(Graphics g) 
	{
		int width = getSize().width;
		int height = getSize().height;
		int x = getX();
		int y = getY();
		int labelWidth = g.getFontMetrics().stringWidth(getLabel());
		int defaultWidth = g.getFontMetrics().stringWidth("default");
		super.paintComponent(g);
		
		// Die Figur (ohne Topline)
		g.drawLine(getWidth()-1,0,getWidth()-1,getHeight()-1);
		g.drawLine(getWidth()-1,getHeight()-1, 0, getHeight()-1);
		g.drawLine(0, getHeight()-1, 0,0);
		g.drawLine(0, 0, (int)(width/(altList.size()+1)),height/2);
		g.drawLine((int)(width/(altList.size()+1)),height/2,width,0);
		g.drawLine((int)(width/(altList.size()+1)),height-1,(int)(width/(altList.size()+1)),height/2);
		g.setColor(Color.black);
		
		// Das Label und "default"
		g.drawString(getLabel(),width/2-labelWidth/2,15);
		g.drawString("default",(int)(width/(altList.size()+1)/2-defaultWidth/2),height-5);
		
		int count=1;
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			count++;
			SwitchValue a=(SwitchValue)el.nextElement();
			int labelsWidth = g.getFontMetrics().stringWidth(a.alt.getLabel());
			int partWidth = (int)(width/(altList.size()+1));
			g.drawString(a.value,(int)(partWidth*count-partWidth/2-labelsWidth/2),height-5);
    	}
	}
	
	void paintTopLine(Graphics g)
	{
		g.drawLine(0,0,getWidth()-1,0);
	}
	
	void save(struktor.Save saveObject)
	{
		saveObject.println("switch ("+getLabel()+")");
		saveObject.println("{");
		for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    	{
			SwitchValue a=(SwitchValue)el.nextElement();
			saveObject.println("case "+a.value+":");
			saveObject.addTab(1);
			a.alt.save(saveObject);
			saveObject.println("break;");
			saveObject.addTab(-1); 
    	}
		saveObject.println("default:");
		saveObject.addTab(1);
		defaultt.save(saveObject);
		saveObject.println("break;");
		saveObject.addTab(-1); 
		saveObject.println("}");
		try {
			getNext().save(saveObject);
		} catch (NullPointerException npe) {}
		
	}	  
}

class SwitchValue 
implements ActionListener, DocumentListener, CommandTypes
{
	StrukElement alt;
	JPanel panel;
	String value;
	Switch switchh;
	SwitchValue(StrukElement alt,String value, Switch switchh)
	{
		this.alt=alt;
		this.value=value;
		this.switchh=switchh;
	}
	
	public JPanel getPanel()
	{
		panel = new JPanel();
		JButton delete = new JButton("delete");
		delete.addActionListener(this);
		panel.add(delete);
		JTextField value = new JTextField(this.value,8);
		value.addActionListener(this);
		value.getDocument().addDocumentListener(this);
		panel.add(value);
		return panel;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() instanceof JButton)
		{
			switchh.delAlt(this);
			panel.setVisible(false);
		}
		else
			this.value=((JTextField)ae.getSource()).getText();
	}

	public void changedUpdate(DocumentEvent event) {
		setValueViaDocument((Document)event.getDocument());
	}

	public void insertUpdate(DocumentEvent event) {
		setValueViaDocument((Document)event.getDocument());
	}	
	public void removeUpdate(DocumentEvent event) {
		setValueViaDocument((Document)event.getDocument());
	}

	private void setValueViaDocument(Document document) {
		Segment text = new Segment();
		try {
			document.getText(0,document.getLength(),text);
		} catch (BadLocationException ble) {}
		this.value= text.toString();
	}
	
	public void setSwitch(Switch switchh)
	{
		
		this.switchh=switchh;
	}
	
	
}
