// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import struktor.Presets;
import struktor.StruktorException;
import struktor.Utils;

/** Eine Klasse (-nnhierarchie) um elegant MouseEreignisse behandeln zu k�nnen */
class StrukElementEvt extends MouseAdapter
implements MouseMotionListener, MouseListener, Constants
{
	/** Sensibilit�t der Mouse (ab wann reagiert zb der Drag-Point)*/
	static int sensible=10;
	static boolean debug = false;
	static Struktogramm struktogramm;
	
	// Objektvariablen
	/** MeAsLogicalElement ;-) */
	StrukElement male;
	
	public StrukElementEvt(Struktogramm sg)
	{
		struktogramm=sg;
	}
	void delete()
	{
		male.removeMouseMotionListener(this);
		male.removeMouseListener(this);
		male = null;
	}
	

	public void mousePressed(MouseEvent e) 
	{	
		switch (mouseOnCorner(e))
		{
			case 1:
				dragPointPressed(e);
				break;
			case 3:
				break;
		}
		
		switch (mouseOnEdge(e))
		{
			case 2:
				rightEdgePressed(e);
			break;
			case 3:
				bottomEdgePressed(e);
			break;
		}
		
		if (e.isMetaDown())
		{
			if (struktogramm.presets.enabSePopUp)
				new StrukElementEvtPopUp(e);	
		}	
	}
	
	void dragPointPressed(MouseEvent e)
	{
		if (struktogramm.presets.enabSeDragging)
			male.addMouseMotionListener(this);
	}
	
	void bottomEdgePressed(MouseEvent e)
	{
	  if (struktogramm.presets.enabSeResizing)
	  {
		  male.resizeHeight=true;
		  male.addMouseMotionListener(this);
	  }
	}
	
	void rightEdgePressed(MouseEvent e)
	{
	  if (struktogramm.presets.enabSeResizing)
	  {
		  male.resizeWidth=true;
		  male.addMouseMotionListener(this);
	  }
	}
	
	public void mouseDragged(MouseEvent e) {
	
	   	// resize Breite ?
		if (male.resizeWidth==true)
	  		// Fenstergrenzen beachten (Sicherheitsabstand 10Pixel)
			if (male.getX()+e.getX()+10 < struktogramm.getView().getWidth())
				male.setWidth(e.getX());
	  	// oder resize H�he ?
		if (male.resizeHeight==true)
		{
			if (male.getHeight()>e.getY())
				StrukElement.gettingSmaller = true;
			else
				StrukElement.gettingSmaller = false;
			StrukElement.resizingElement=male;	
			male.setHeightByUser(e.getY());
			// Das erspart aus nicht erfindlichen Gr�nden �rger !
			StrukElement.gettingSmaller=false;
			StrukElement.resizingElement=null;	
		}
	  	// oder Element Moven ?
		if ((male.resizeWidth==false) & (male.resizeHeight==false))
		{
			// Fenstergrenzen beachten (Sicherheitsabstand 10Pixel): oben und links :
			if (male.getX()+e.getX() > 10 && male.getY()+e.getY() > 10)
				// rechts und unten:
				if (male.getX()+e.getX()+male.getWidth() +10  < struktogramm.getView().getWidth() && male.getY()+e.getY()+10 < struktogramm.getView().getHeight())
					male.setLocation(male.getX()+e.getX(),male.getY()+e.getY());
		}
		e.consume();	        
	}
	
	
	
	public void mouseReleased(MouseEvent e) {
	        male.removeMouseMotionListener(this);
	        male.resizeWidth=false;
	        male.resizeHeight=false;
			e.consume();
	}
	

	/** befindet sich die Mouse auf einer Ecke ?
	 * @param   e  
	 * @return Die Nummer der Ecke (sollte gekonstantet werden)    
	 */
	int mouseOnCorner(MouseEvent e)
	{
		// Left Top Corner (Drag Point)
		if((Math.abs(e.getX()) <= 10) && (Math.abs(e.getY()) <= 10))
			return 1;
		// Right Top Corner
		if( (Math.abs(e.getX()-male.getWidth()+10) <= 10) && (Math.abs(e.getY()-10) <= 10))
			return 2;
		// Left Bottom Corner
		if( (Math.abs(e.getX()-10) <=10 ) && (Math.abs(e.getY()-male.getWidth()+10) <= 10))
			return 4;
		return 0;
	}

	/** befindet sich die Mouse auf einer Kante ?
	 * @param   e  
	 * @return Die Nummer der Kante (sollte gekonstantet werden)
	 */
	int mouseOnEdge(MouseEvent e)
	{
	   // Top Edge
	   if( (e.getX()<0) && (e.getX()<male.getWidth()) && (Math.abs(e.getY())<=sensible) )
	     return 1;
	   // Right Edge
	   if( (e.getY()>0) && (e.getY()<=male.getHeight()) && (Math.abs(e.getX()-male.getWidth())<=sensible))
	     return 2;
	   // Bottom Edge (koorigiert)
	   if( e.getX()>0 && (e.getX()<male.getWidth()) && Math.abs(e.getY()-male.getHeight())<=sensible)
	     return 3;
	   // Left Edge
	   if( (e.getY()>male.getY()) && (e.getY()<male.getY()+male.getHeight()) && (Math.abs(e.getX()-male.getX())<=sensible))
	     return 4;
	   return 0;
	}

	/** Befindet sich die Maus innerhalb des Elements ?
	 * @param   e  
	 * @return ja oder nein
	 */
	boolean mouseInsideElement(MouseEvent e)
	{
		if (e.getX()>0 && e.getX()<male.getWidth() && e.getY()>0 && e.getY()<male.getHeight())
			return true;
		return false;
	}
	
	/** leer !*/
	public void mouseMoved(MouseEvent e) {
	}
	
   	
}

/** Die Klasse f�r das Pop-Up-Fenster
 */
class StrukElementEvtPopUp 
extends JDialog
implements ActionListener, ItemListener, Constants
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StrukElement element;
	public Presets presets;
	// F�r (fast) alle Elemente
	private JMenuItem properties;
	private JMenuItem delete, swop;
	private JMenuItem insCommand, insHeadLoop, insTailLoop, insForLoop, insCondition, insSwitch;
	private JMenuItem appCommand, appHeadLoop, appTailLoop, appForLoop, appCondition, appSwitch;
	private JMenuItem moveOutsideBlock;
	private JMenu insert, append, add; 
	private JMenuItem addToLoop, addToConditionFalsePart, addToConditionTruePart;
	private JCheckBox breakpoint;
	// Nur Condition UND Loop
	private JMenu deleteM;
	private JMenuItem deleteAll;
	// Nur Loop
	private JMenuItem delKeepInside;
	// Nur Condition
	private JMenuItem delKeepTrue, delKeepFalse;
	
	public StrukElementEvtPopUp(MouseEvent event)
	{	
		element = (StrukElement)event.getSource();
		this.presets=element.presets;
		JPopupMenu popup = new JPopupMenu();
		//properties hinzuf�gen
		if (presets.enabSePuProperties)
		{
			properties = new JMenuItem("properties");
			properties.addActionListener(this);
			popup.add(properties);
			//Separator hinzuf�gen
			popup.addSeparator();
		}
		
		//insert Men� erzeugen, hinzuf�gen
		if (presets.enabSePuInsert)
		{
			insert = makeInsertMenu();
			popup.add(insert);
		}
	
		//append Men� erzeugen, hinzuf�gen
		if (presets.enabSePuAppend)
		{
			append = makeAppendMenu();
			popup.add(append);
		}
		
		if (presets.enabSePuSwop)
		{
			// Tauschen mit oberen Element
			if (element.getUpConnectionType() == NEXT)
			{
				swop = new JMenuItem("swop (with above)");
				swop.addActionListener(this);
				popup.add(swop);
			}
		}
		
		if (presets.enabSePuAdd)
		{
			// in Condition oder Loop integrieren
			if (element.getUpConnectionType() == NEXT)
			{
				if (element.getUpConnection() instanceof Loop)
				{
					addToLoop = new JMenuItem("add to Loop");
					addToLoop.addActionListener(this);
					popup.add(addToLoop);
				}
				if (element.getUpConnection() instanceof Condition)
				{
					add = makeAddMenu();
					popup.add(add);
				}
			}
		}
		
		if (presets.enabSePuMove)
		{
			// aus Block herausschieben
			if (StrukElement.getConnectionTypeOfBlock(element) != NEXT)
			{
				moveOutsideBlock = new JMenuItem("move outside Block");
				moveOutsideBlock.addActionListener(this);
				popup.add(moveOutsideBlock);
			}
		}
		
		if (presets.enabSePuDelete)
		{
			// delete Men� oder delete erzeugen und hinzuf�gen 
			if (element instanceof Command)
			{
				delete = new JMenuItem("delete");
				delete.addActionListener(this);
				popup.add(delete);
			}
			else
			{
				deleteM = makeDeleteMenu();
				popup.add(deleteM);
			}
		}
		
		if (presets.enabSePuBreakpoints)
		{		
			// Breakpoints setzen
			breakpoint = new JCheckBox("Breakpoint", element.getBreakpoint());
			breakpoint.addItemListener(this);
			popup.add(breakpoint);
		}
			
		//Men� anzeigen
		popup.show(element, event.getX(), event.getY());
			
	}
	
	/** Hilfsmethode damit der Konstruktor nicht so fett wird
	 * @return     
	 */	
	private JMenu makeInsertMenu()
	{
		JMenu tempinsert = new JMenu("insert");
		
		if (presets.enabSePuCommand)
		{
			insCommand = new JMenuItem("Command");
			insCommand.addActionListener(this);
			tempinsert.add(insCommand);
		}
		
		if (presets.enabSePuHeadLoop)
		{
			insHeadLoop = new JMenuItem("HeadLoop");
			insHeadLoop.addActionListener(this);
			tempinsert.add(insHeadLoop);
		}
		
		if (presets.enabSePuTailLoop)
		{
			insTailLoop = new JMenuItem("TailLoop");
			insTailLoop.addActionListener(this);
			tempinsert.add(insTailLoop);
		}
		
		if (presets.enabSePuForLoop)
		{
			insForLoop = new JMenuItem("ForLoop");
			insForLoop.addActionListener(this);
			tempinsert.add(insForLoop);
		}
		
		if (presets.enabSePuCondition)
		{
			insCondition = new JMenuItem("Condition");
			insCondition.addActionListener(this);
			tempinsert.add(insCondition);
		}
		
		if (presets.enabSePuSwitch)
		{
			insSwitch = new JMenuItem("Switch");
			insSwitch.addActionListener(this);
			tempinsert.add(insSwitch);
		}
		
		return tempinsert;
	}
	
	/** Hilfsmethode damit der Konstruktor nicht so fett wird
	 * @return     
	 */
	private JMenu makeAppendMenu()
	{
		JMenu tempappend = new JMenu("append");
		if (presets.enabSePuCommand)
		{
			appCommand = new JMenuItem("Command");
			appCommand.addActionListener(this);
			tempappend.add(appCommand);
		}
		
		if (presets.enabSePuHeadLoop)
		{	
			appHeadLoop = new JMenuItem("HeadLoop");
			appHeadLoop.addActionListener(this);
			tempappend.add(appHeadLoop);
		}
		
		if (presets.enabSePuTailLoop)
		{	
			appTailLoop = new JMenuItem("TailLoop");
			appTailLoop.addActionListener(this);
			tempappend.add(appTailLoop);
		}
		
		if (presets.enabSePuForLoop)
		{	
			appForLoop = new JMenuItem("ForLoop");
			appForLoop.addActionListener(this);
			tempappend.add(appForLoop);
		}
		
		if (presets.enabSePuCondition)
		{	
			appCondition = new JMenuItem("Condition");
			appCondition.addActionListener(this);
			tempappend.add(appCondition);
		}
		
		if (presets.enabSePuSwitch)
		{	
			appSwitch = new JMenuItem("Switch");
			appSwitch.addActionListener(this);
			tempappend.add(appSwitch);
		}
		return tempappend;
	}	
	
	/** Hilfsmethode damit der Konstruktor nicht so fett wird
	 * @return     
	 */
	private JMenu makeDeleteMenu()
	{
		JMenu tempdelete = new JMenu("delete");
		deleteAll = new JMenuItem("deleteAll");
		deleteAll.addActionListener(this);
		tempdelete.add(deleteAll);
		if (element instanceof Condition)
		{
			delKeepTrue = new JMenuItem("keep True-part");
			delKeepTrue.addActionListener(this);
			tempdelete.add(delKeepTrue);
			delKeepFalse = new JMenuItem("keep False-part");
			delKeepFalse.addActionListener(this);
			tempdelete.add(delKeepFalse);
		}
		if (element instanceof Loop)
		{
			delKeepInside = new JMenuItem("keep inside");
			delKeepInside.addActionListener(this);
			tempdelete.add(delKeepInside);
		}
		return tempdelete;
	}

	/** Hilfsmethode damit der Konstruktor nicht so fett wird
	 * @return     
	 */
	private JMenu makeAddMenu()
	{
		JMenu tempadd = new JMenu("add");
		addToConditionTruePart = new JMenuItem("to True-Part");
		addToConditionTruePart.addActionListener(this);
		tempadd.add(addToConditionTruePart);
		
		addToConditionFalsePart = new JMenuItem("to False-Part");
		addToConditionFalsePart.addActionListener(this);
		tempadd.add(addToConditionFalsePart);
	
		return tempadd;
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		element.setBreakpoint(breakpoint.isSelected());
		element.repaint();
	}
	
	public void actionPerformed(ActionEvent event)
	{
		Object src = event.getSource();
		if (src == properties)
			new Properties(Utils.getFrame(element), element);
			
		if (src == insCommand)
			element.insert(COMMAND);	
		if (src == insHeadLoop)
			element.insert(HEADLOOP);
		if (src == insTailLoop)
			element.insert(TAILLOOP);
		if (src == insForLoop)
			element.insert(FORLOOP);		
		if (src == insCondition)
			element.insert(CONDITION);
		if (src == insSwitch)
			element.insert(SWITCH);	
		
		if (src == appCommand)
			element.append(COMMAND);	
		if (src == appHeadLoop)
			element.append(HEADLOOP);
		if (src == appTailLoop)
			element.append(TAILLOOP);
		if (src == appForLoop)
			element.append(FORLOOP);	
		if (src == appCondition)
			element.append(CONDITION);	
		if (src == appSwitch)
			element.append(SWITCH);		
				
		if (src == swop)
			element.swop();
			
		if (src == addToLoop)
			element.addToLoop();
		if (src == addToConditionTruePart)
			element.addToCondition(true);
		if (src == addToConditionFalsePart)
			element.addToCondition(false);	
		
		if (src == moveOutsideBlock)
			element.moveOutsideBlock();		
			
		if (src == delete)
			element.delete(false);
		if (src == deleteAll)
		{
			if (element instanceof Condition)
				((Condition)element).deleteAll();
			if (element instanceof Loop)
				((Loop)element).deleteAll();
			if (element instanceof Switch)
				((Switch)element).deleteAll();	
		}	
		if (src == delKeepInside)
			((Loop)element).delKeepInside();
		if (src == delKeepTrue)
			((Condition)element).delKeepTrue();	
		if (src == delKeepFalse)
			((Condition)element).delKeepFalse();		
				
		element.tare();	
		element.repaint();				
	}

	/** Klasse f�r das Property-Menu
	 */		
	class Properties
	extends JDialog implements ActionListener, ItemListener, Runnable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private StrukElement element;
		private JPanel prop;
		private JComboBox action;
		private JLabel rigid = new JLabel("          ");
		private JLabel adMessageLabel;
		private JTextField adMessage;
		private JTextField label;
		private JButton ok;
		// Nur f�r ForLoop
		private JTextField initLabel;
		private JTextField stepLabel;
		// Nur f�r Switch
		private JButton newAlt;
		
		Properties(Frame parent, StrukElement s)
		{		
			super(parent, "Element-Properties", true);
	    	Container contentPane = getContentPane();
			element = s;
			//Fenster
	    	setBackground(Color.lightGray);
		    Point parloc = parent.getLocation();
		    setLocation(parloc.x + 30, parloc.y + 30);
	    
			//Properties
	   		prop = new JPanel();
			prop.setLayout(new GridLayout(0,2,20,0));
			prop.add(new JLabel("Type of Element: ",JLabel.RIGHT));
			prop.add(new JLabel(element.getClass().getName()));
			if (element instanceof ForLoop)
			{
				prop.add(new JLabel("InitCommand: ", JLabel.RIGHT));
				initLabel = new JTextField(((ForLoop)element).getInitLabel(), 15);
				initLabel.addActionListener(this);
				prop.add(initLabel);
			}
			if (element instanceof Command)
				prop.add(new JLabel("Command: ", JLabel.RIGHT));
			else
				prop.add(new JLabel("Condition: ", JLabel.RIGHT));
			label = new JTextField(element.getLabel(), 15);
			label.addActionListener(this);
			prop.add(label);
			if (element instanceof ForLoop)
			{
				prop.add(new JLabel("StepCommand: ", JLabel.RIGHT));
				stepLabel = new JTextField(((ForLoop)element).getStepLabel(), 15);
				stepLabel.addActionListener(this);
				prop.add(stepLabel);
			}
			
			if (element instanceof Command)
			{
				action = new JComboBox();
				action.addItem("Input");
				action.addItem("Calculation");
				action.addItem("Output");
				action.setSelectedIndex(((Command)(element)).getActionType());
				action.addItemListener(this);
				prop.add(new JLabel("Action:  ",JLabel.RIGHT));
				prop.add(action);
				adMessageLabel = new JLabel("Additional Text:", JLabel.RIGHT);
				adMessage = new JTextField(((Command)(element)).getAdMessage());
				adMessage.addActionListener(this);
				if (action.getSelectedIndex()==0 | action.getSelectedIndex()==2)
				{ 
					prop.add(adMessageLabel) ; prop.add(adMessage);
				}
			}
			
			if (element instanceof Switch)
			{
				Vector altList = ((Switch)element).getAltList();
				for (Enumeration el=altList.elements(); el.hasMoreElements(); )
    				{
					SwitchValue a=(SwitchValue)el.nextElement();
					prop.add(a.getPanel());
    				}
				newAlt = new JButton("newAlternative");
				newAlt.addActionListener(this);
				prop.add(newAlt);
				
			}
			prop.add(rigid);
			contentPane.add(prop, BorderLayout.CENTER);
			
			//Buttons
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER));
			ok = new JButton("Ok");
			ok.addActionListener(this);
			panel.add(ok);	
			contentPane.add(panel, BorderLayout.SOUTH);
	    		pack();
			setVisible(true);
		}
		
		public void actionPerformed(ActionEvent event)
		{
			if (event.getSource() == ok || event.getSource() == label) 
			{
				try {
					element.setLabel(label.getText());
					if (element instanceof Command)
						((Command)(element)).setAdMessage(adMessage.getText());
					if (element instanceof ForLoop)
					{
						((ForLoop)(element)).setInitLabel(initLabel.getText());
						((ForLoop)(element)).setStepLabel(stepLabel.getText());
					}
					setVisible(false);
		    		dispose();
				}
				catch (StruktorException se)
				{
					se.showMsg(this);
				}
			}
			else if (event.getSource() == newAlt)
			{
				((Switch)element).addAlt();
				new Properties(Utils.getFrame(element), element);
				dispose();
			}
			
		}
		public void itemStateChanged(ItemEvent e)
		{
			((Command)(element)).setActionType(action.getSelectedIndex());
			SwingUtilities.invokeLater(this);
		}
		
		/** Ist n�tig weil sonst nicht Threadsicher !!!! F�gt das Feld f�r AdditionalMessage hinzu oder entfernt es
		 */
		public void run()
		{
			if (action.getSelectedIndex()==1)
			{
				prop.remove(adMessageLabel);
				prop.remove(adMessage);
				prop.add(rigid);
				prop.invalidate();
				prop.validate();
			}
			else
			{
				prop.remove(rigid);
				prop.add(adMessageLabel);
				prop.add(adMessage);
				prop.invalidate();
				prop.validate();
			}
		}
	}
}
	
