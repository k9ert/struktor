// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;

import java.awt.Component;
import java.awt.Container;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import struktor.Presets;
import struktor.Struktor;
import struktor.StruktorException;
import struktor.Tracer;
import struktor.TurtleCanvas;
import struktor.Utils;
import struktor.processor.LoopControlException;
import struktor.processor.Memory;
import struktor.processor.Processor;
import struktor.processor.ReturnException;


/** Die Verwaltungsinstanz f�r alle StrukElemente des Struktogramms. Ein Teil der Funktionalit�t k�nnten evtl. statische Methoden der StrukElement-Klasse �bernehmen. Dann h�tte man aber ein Problem mit der GUI-Funktionalit�t (mehrere Applets auf einer HTML-Seite.
 * @see ent#dissolve
 */
public class Struktogramm
implements Runnable, Constants
{
	static public Struktogramm main;
	static public boolean stopExecuting = false;
	static boolean debugMode=false;
	static boolean pauseMode=false;
	static boolean debug = true;
	static public int delay = 500;
	
	
	static public void stopStruktogramm()
	{
		stopExecuting = true;
		pauseMode = false;	
	}
	
	static void programmStarted()
	{
	}
	
	// Object Variables
	private StruktogrammEvt myEvents;
	private JLayeredPane view;
	private String name = "main";
	private Object returnValue;
	private boolean returned = false;
	private StrukElement first=null;
	private Vector StrukList = new Vector();
	//Ausschlie�lich f�r die �bergabe an Processor !!!
	public Struktor struktor;
	public Presets presets;
	public boolean saveMark;
	
		
	public Struktogramm(String name, StruktogrammEvt event, Struktor struktor)
	{
		this.struktor = struktor;
		presets = struktor.presets;
		this.name = Utils.convertString(name);
		myEvents = event;
		view = new StruktogrammView(this, struktor);
		//if (!struktor.isApplet)
		//	viewApp = new StruktogrammAppView(this, struktor, view);
		view.setVisible(true);
		addCommand("");
	}
	
	void recalculate()
	{
		getFirst().recalculate();
	}

	StrukElement addElement(int typeOfElement)
	{
		return addElement(typeOfElement,"");
	}

	StrukElement addElement(int typeOfElement, String label)
	{
		StrukElement element=null;
		switch (typeOfElement)
		{
		case COMMAND:
			element = addCommand(label);
			break;
		case HEADLOOP:
			element = addHLoop(label);
			break;
		case TAILLOOP:
			element = addTLoop(label);
			break;	
		case FORLOOP:
			if (label.equals(""))
				element = addFLoop(" ; ; ");
			else
				element = addFLoop(label);
			break;
		case CONDITION:
			element = addCondition(label);
			break;
		case SWITCH:
			element = addSwitch(label);
		} 
		return element;
	}
	
	/** F�gt ein Command ein. 
	 * @return Das Element     
	 */
	private Command addCommand(String label)
	{
		Command command = new Command(this, label);
		StrukList.addElement(command);
		if (getFirst() == null)
			setFirst(command);
		view.add(command, new Integer(StrukElement.standardLayer+20)); // vorher new Integer
		return command;					
	}
	
	/** F�gt eine Loop und ein Command ein. Die Parameter werden im Moment noch alle ignoriert
	 * @return Das Element selber     
	 */
	private Loop addHLoop(String label)
	{
		HeadLoop loop = new HeadLoop(this, label);
		StrukList.addElement(loop);
		if (getFirst() == null)
			setFirst(loop);
		int layer = loop.getLayer(StrukElement.standardLayer); 
		view.add(loop, new Integer(layer));
		Command command = new Command(this, "");
		StrukList.addElement(command);
		loop.setInside(command);
		view.add(command, new Integer(StrukElement.standardLayer+20));
		return loop;
	}
	
	/** F�gt eine Loop und ein entsprechende Command ein. Die Parameter werden im Moment noch alle ignoriert
	 * @return Das Element selber     
	 */
	private Loop addTLoop(String label)
	{
		TailLoop loop = new TailLoop(this, label);
		StrukList.addElement(loop);
		if (getFirst() == null)
			setFirst(loop);
		int layer = loop.getLayer(StrukElement.standardLayer);
		view.add(loop, new Integer(layer));
		Command command = new Command(this, "");
		StrukList.addElement(command);
		loop.setInside(command);
		view.add(command, new Integer(StrukElement.standardLayer+20));
		return loop;
	}
	
	/** F�gt eine For-Loop und ein Command ein. Die Parameter werden im Moment noch alle ignoriert
	 * @return Das Element selber     
	 */
	private Loop addFLoop(String label)
	{
		ForLoop loop = new ForLoop(this, label);
		StrukList.addElement(loop);
		if (getFirst() == null)
			setFirst(loop);
		int layer = loop.getLayer(StrukElement.standardLayer); 
		view.add(loop, new Integer(layer));
		Command command = new Command(this, "");
		StrukList.addElement(command);
		loop.setInside(command);
		view.add(command, new Integer(StrukElement.standardLayer+20));
		return loop;
	}
	
	

	/** F�gt eine Condition und entsprechende Commands ein. Die Parameter werden im Moment noch alle ignoriert
	 * @return Das Element selber     
	 */
	private Condition addCondition(String label)
	{
    	Condition condition = new Condition(this, label);
    	StrukList.addElement(condition);
		if (getFirst() == null)
			setFirst(condition);
		int layer = condition.getLayer(StrukElement.standardLayer);
		view.add(condition, new Integer(layer));
		Command command1 = new Command(this, "");
		StrukList.addElement(command1);
		view.add(command1, new Integer(StrukElement.standardLayer+20));
		condition.setAlt1(command1);
		Command command2 = new Command(this, "");
		StrukList.addElement(command2);
		view.add(command2, new Integer(StrukElement.standardLayer+20));
		condition.setAlt2(command2);
		return condition;
	}
	
	private Switch addSwitch(String label)
	{
		Switch switchh = new Switch(this, label);
		StrukList.addElement(switchh);
		if (getFirst() == null)
			setFirst(switchh);
		int layer = switchh.getLayer(StrukElement.standardLayer);
		view.add(switchh, new Integer(layer));
		Command command1 = new Command(this, "");
		StrukList.addElement(command1);
		view.add(command1, new Integer(StrukElement.standardLayer+20));
		switchh.setDefault(command1);
		Command command2 = new Command(this, "");
		StrukList.addElement(command2);
		view.add(command2, new Integer(StrukElement.standardLayer+20));
		switchh.addAlt(command2);
		command2 = new Command(this, "");
		StrukList.addElement(command2);
		view.add(command2, new Integer(StrukElement.standardLayer+20));
		switchh.addAlt(command2);
		return switchh;
	}	
		
	public void triggerPause()
	{
		pauseMode = !pauseMode;
		if (pauseMode == true)
			myEvents.strukPaused();
		else
			myEvents.strukUnpaused();	
	}
	public void step()
	{
		int tempdelay = delay;
		if (delay < 100)
			delay = 100;
		if (pauseMode == true)
		{
			pauseMode = false;
			try { Thread.sleep(100); } catch (InterruptedException e) {}
			pauseMode = true;
		}
		delay = tempdelay;
	}

	public Object startStruk(Vector parameter)
	throws StruktorException
	{
		DecList.findDecList(this).initializeParameters(parameter);
		run();
		if (returned == false && !stopExecuting)
			throw new StruktorException("Struktogramm "+name+" : Missing return-Statement !");
		else
			return returnValue;
	}
	
	
	
	public void startStruktogramm()
	{
		main = this;
		stopExecuting = false;
		
		((Struktor)Utils.getApplet((Container)view)).textOutput.setText("");
		
		TurtleCanvas canvas = (TurtleCanvas)((Struktor)Utils.getApplet((Container)view)).canvas;
		canvas.clear();
		
		Memory.initializeMemory();
		Thread runner = new Thread(this);
		runner.start();
	}
		
	
	
	/** St��t die Ausf�hrung des Struktogramms an, indem das erste Element ausgef�hrt wird. Vorher mu� ein Prozessor erzeugt werden, der seinerseits einen Parser und Memory (nach den Deklarationen des Struktogramms) erzeugt.
	 *	@see StrukElement#execute
	 *  @see Processor
	 *	@see Memory
	 */
	public void run()
	{
		Tracer.out("Entering Struktogramm.run");		
		returned = false;
		try{
			try {
				Tracer.out("befor new Processor");		
				Processor processor = new Processor(struktor, DecList.findDecList(this).decList, struktor.canvas, struktor.textOutput);
				Tracer.out("before first.execute");		
				first.execute(first,processor);
			} 
			catch (ReturnException re) { Tracer.out("returnEx catched !"); returned = true; returnValue = re.getReturnValue(); if (main == this) throw new StruktorException(re.toString());}
			catch (InterruptedException e) {Tracer.out("Interrupted !");}
			catch (LoopControlException be) {throw new StruktorException("Break or Continue without Loop or \nBreak or Continue with wrong level !");}
		} 
		catch (StruktorException se) { 
			Tracer.out("StruktorException catched in Struktogramm.run");
			se.showMsg((Container)view);
		} 
		//catch (Exception e) {Tracer.out(e + " in Struktogramm.run()");}
		finally { 
			view.repaint(); 
			if (main == this)
			{
				myEvents.strukStopped();
				delay=500;
				myEvents.delayChanged(delay);
			}
		}		
	}

	
	/** L�scht ein Element indem in allen Elementen die Verkn�pfungen entsprechend abge�ndert werden. Anschlie�en wird das Element aus der Liste entfernt.   
	 * @param   e  
	 */
	void delStrukElement(StrukElement e)
	{
			if (e==first)
				try {
					first=e.getNext();
				} catch (NullPointerException npe) { first = null; }
			for (Enumeration el=StrukList.elements(); el.hasMoreElements(); )
			{
				StrukElement r=(StrukElement)el.nextElement();
				if (r.getNext()==e)
	                r.connect(NEXT,r,e);
				if (r.getUpConnection() == e)
					r.setUpConnection(null,-1);
				if (r instanceof Loop)
				{
					Loop l = (Loop)r;
					if (l.getInside()==e)
					   l.setInside(null);
				}
				if (r instanceof Condition)
				{
					Condition c = (Condition)r;
					if (c.getAlt1()==e)
					   c.setAlt1(null);
					if (c.getAlt2()==e)
					   c.setAlt2(null);
				}
	       }
	       if (StrukList.removeElement(e) == true)
		   	if (StrukList.removeElement(e) == true)
				if (StrukList.removeElement(e) == true)
					StrukList.removeElement(e);
		   view.remove(e);
	}
	
	//Getter-Setter Methoden

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	throws StruktorException
	{
		if (Utils.containsVovelMutation(name))
			throw new StruktorException("No VovelMutations ! \n (keine Umlaute !)");
		else
			this.name = name;
	}
	
	/** Setter-Methode
	 * Legt das erste Element des Struktogramms fest.
	 * @param   e, das erste Element  
	 */
	void setFirst(StrukElement first)
	{
		this.first=first;
	}
	
	/** Getter-Methode
	 * @return das erste Element    
	 */
	StrukElement getFirst()
	{
		return first;
	}
	
	void setDebugMode(boolean newMode)
	{
		debugMode = newMode;
	}
	
	public void save(struktor.Save saveObject)
	{
		if (!presets.makeCCode)
		{
			saveObject.println("//@start struktogramm "+name);
			saveObject.println("// Struktogramm could be a Function");
		}
		saveObject.print(name+"( ");
		DecList.findDecList(this).saveParam(saveObject);
		saveObject.println(" )");
		saveObject.println("{");
		saveObject.addTab(1);
		DecList.findDecList(this).saveDecs(saveObject);
		if (!presets.makeCCode)
			saveObject.println("//@*** Program ***");
		getFirst().save(saveObject);
		saveObject.addTab(-1);
		saveObject.println("}");
		if (!presets.makeCCode)
			saveObject.println("//@end struktogramm");
	}
	
	public void saveAsImage()
	{
		((StruktogrammView)view).saveAsImage();
	}
	
	public Container getView()
	{
		return (Container)view;	
	}
		
	int getLayer(JComponent element)
	{
		return   JLayeredPane.getLayer(element);
	}
	void setLayer(Component element, int layer)
	{
		view.setLayer(element, layer);
	}
	
	public int getWidth()
	{
		return getFirst().getWidth();
	}
	
	public int getHeightOfFirst()
	{
		return getFirst().getHeight();
	}
}
