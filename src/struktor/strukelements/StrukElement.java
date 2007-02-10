// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import struktor.Presets;
import struktor.Save;
import struktor.StruktorException;
import struktor.Tracer;
import struktor.Utils;
import struktor.processor.LoopControlException;
import struktor.processor.Processor;
import struktor.processor.ProcessorException;
import struktor.processor.ReturnException;

/** Diese abstrakte Klasse (StruktogrammElement) stellt einen Grossteil der Funktionalit�t f�r die Unterklassen Command, Loop und Condition zur Verf�gung. Die Kommunikation mit dem Anwender wurde in eine gleichartige Klassenhierarchie gekapselt. Die Klasse enth�lt vorwiegend package-Methoden, da sie von den Unterklassen aufgerufen werden 
 * @see StrukElementEvt
 */
abstract class StrukElement extends JComponent
implements Constants
{
	//statische Variablen
	// wird verwendet um festzustellen ob der Benutzer ein Element gerade vergr��ert oder verkleinert
	static boolean gettingSmaller = true;
	static StrukElement resizingElement=null;
	public final static int standardLayer = -30000;
	
	/** Liefert das letzte Element eines Blocks
	 * @param   first  
	 * @return das letzte Element des Blocks  
	 */
	static StrukElement getLastOfBlock(StrukElement first)
	{
		while (first.getNext() != null)
			first = first.getNext();
		return first;
	}
	
	static int getConnectionTypeOfBlock(StrukElement element)
	throws NullPointerException
	{
			return getFirstOfBlock(element).connectionType;
	}
	
	/** Liefert das Element �ber dem Block
	 * @param   first  
	 * @return das letzte Element des Blocks  
	 */
	static StrukElement getTopOfBlock(StrukElement element)
	{
		return getFirstOfBlock(element).connectedFrom;	
	}
	
	/** Liefert das erste Element eines Blocks
	 * @param   first  
	 * @return das erste Element des Blocks  
	 */
	static StrukElement getFirstOfBlock(StrukElement element)
	{
		while (element.connectionType == NEXT && element.connectedFrom != null)
			element = element.connectedFrom;
		return element;	
	}

	/** Macht den Block in dem startElement (hoffentlich) das erste Element ist um diff kleiner
	 * @param   startElement  
	 * @param   diff  
	 */
	static void makeBlockSmaller(StrukElement startElement, int diff)
	{	
		if (diff==0)
			return;
		diff=Math.abs(diff);
		if (startElement.getHeight()>diff)
		try {
			startElement.setHeight(startElement.getHeight()-diff);
		} catch (ResizeException re) 
		{
			if (startElement.getNext()!=null)
				makeBlockSmaller(startElement.getNext(),re.getRest());
		}
	}	
	
	/** Macht einen den Block in dem startElement (hoffentlich) das erste Element ist um diff gr��er
	 * @param   startElement  
	 * @param   diff  
	 */
	static void makeBlockLarger(StrukElement startElement, int diff)
	{	
		if (diff==0)
			return;
		diff=Math.abs(diff);
		try {
			startElement.setHeight(startElement.getHeight()+diff);
		} catch (ResizeException re) {/* kann nicht auftreten ! */}
	}	
		
	// Objektvariablen
	Struktogramm struktogramm;
	public Presets presets;
	private boolean executing;
	private boolean breakpoint;
	private String label;
	private StrukElement next=null;
	private StrukElement connectedFrom=null;
	// Sollte eigentlich eine Getter/Setter-Methode bekommen
	private int connectionType;
	boolean resizeWidth=false;
	boolean resizeHeight=false;
	StrukElementEvt mEvt;
	
	StrukElementEvt getmEvt()
	{
		return mEvt;
	}
	
	StrukElement(Struktogramm s, String label)
	{
	 	if (false)
			;
		else
		{
			struktogramm=s;
			this.presets=struktogramm.presets;
			setX(struktogramm.presets.SeXPos); 
			setY(struktogramm.presets.SeYPos); 
			try {
				setWidth(struktogramm.presets.SeWidth); 
			 	setHeight(struktogramm.presets.SeHeight);
			}catch (ResizeException re) {/* Kann eigentlich nicht vorkommen*/}
			this.label=Utils.convertString(label);
		}
	}
	
	protected void finalize()
	{
		Utils.getApplet(this).removeMouseMotionListener(mEvt);
		Utils.getApplet(this).removeMouseListener(mEvt);
		mEvt = null;
	}
	
	/** Berechnet das Element se neu durch (X,Y,Width)
	 * Wird normalerweise rekursiv vom ersten Element aus aufgerufen 
	 */
	void recalculate() 
	{		
		tare();
		calcNext();
		if (getNext()!=null)
			getNext().recalculate();
	}
	
	/** war urspr�nglich static, wurde aber speziell f�r Switch wieder Objektgebunden implementiert. Verkn�pft 2 Elemente auf die angegebene Art
	 * @param   connectionType  
	 * @param   father  
	 * @param   child  
	 */
	void connect(int connectionType, StrukElement father, StrukElement child)
	{
		switch (connectionType)
		{
		case NEXT:
			father.setNext(child);
			break;
		case INSIDE:
			((Loop)(father)).setInside(child);
			break;
		case ALT1:
			((Condition)(father)).setAlt1(child);
			break;
		case ALT2:
			((Condition)(father)).setAlt2(child);
			break;
		case ALT:
			((Switch)(father)).setAlt(this, child);
			break;
		}
	}
	

	/** Kapselt die Funktionalit�t die alle Elemente beim Ausf�hren gemeinsam haben um den Programmflu� (au�erhalb der Funktionalit�t der Elemente) steuern zu k�nnen
	 * @param   element  
	 * @param   processor  
	 * @exception   InterruptedException  
	 */
	final void execute(StrukElement element, Processor processor)
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException
	{
		try {
			if (Struktogramm.stopExecuting)
			{
				if (executing == true)
					delayAndPaint(processor);
				throw new InterruptedException();
			}
			if (element == null)
			{
				if (executing == true)
					delayAndPaint(processor);
				throw new ProcessorException("Struktogramm: "+struktogramm.getName()+"\nElement does not exist !");	
			}
			element.execute(processor);
			if (getNext() != null)
				getNext().execute(getNext(),processor);
			executing = false;
		} finally
		{
			if (executing == true)
					delayAndPaint(processor);
			executing = false;
		}	
	}
	

	/** Zeichnet das Element in rot, wartet die entsprechende Zeit ab und aktualisiert die Watchlist
	 * @param   processor  
	 * @exception   InterruptedException  
	 */
	final void delayAndPaint(Processor processor)
	throws InterruptedException
	{
		int layer = struktogramm.getLayer(this);
		try {
			executing = !executing;
			if (!(Struktogramm.delay < 100 & executing == true))
			{
				struktogramm.getView().remove(this);
				struktogramm.setLayer(this, 100);
				struktogramm.getView().add(this);
				try {paint(getGraphics());}
				catch (NullPointerException npe) 
				{// kommt manchmal vor, ich wei� nur nicht warum !
				}
			}
			// nicht doppelt warten ...
			if (executing==true)
			{
				WatchList.findWatchList(struktogramm).update(processor);
				if (Struktogramm.debugMode == true & breakpoint == true)
					struktogramm.triggerPause();
				if (Struktogramm.delay > 100) // Performancesteigernd !
					Thread.sleep(Struktogramm.delay);
				while (Struktogramm.pauseMode == true)
					Thread.sleep(100);
			}
			
		} finally {
			if (!(Struktogramm.delay < 100 & executing == true))
			{
				struktogramm.getView().remove(this);
				struktogramm.setLayer(this, layer);
				struktogramm.getView().add(this);
			}
		}
	}		
	
	/** �ndert das n�chste Element. Wurde etwas komplexer um zirkul�re Verkn�fungen vermeiden zu k�nnen
	 * @param next  
	 */
	private void setNext(StrukElement next)
	{
		// Wenn das n�chste Element nicht null ist, dann erst mal seine Connection l�schen !
	  if (next!=null)
	     next.delConnection();
	  // Wenn das aktuelle n�chste Element nicht null ist, auch die Connection l�schen !
	  if (this.next!=null)
	     this.next.delConnection();
	  // Jetzt den pot next zum aktuellen next machen
	  this.next = next;
	  // und mich bei Ihm bekannt machen !
	  if (this.next!=null)
	     this.next.setUpConnection(this,NEXT);
	}
	
	/** Legt eine Up-Connection fest. Wird vor allem (oder nur?) von den 
	 * unterschiedlichen set-Methoden der Elemente benutzt (setAlt,setInside ...)
	 */
	void setUpConnection(StrukElement s, int connectionType) {
	  connectedFrom=s;
	  this.connectionType = connectionType;
	}
	
	StrukElement getUpConnection() {
	  return connectedFrom;
	}
	
	int getUpConnectionType() {
		return connectionType;
	}
	
	boolean hasUpConnection()
	{
	 if (connectedFrom!=null)
	    return true;
	 else
	    return false;
	}
	
	/** Wird von next, inside und alt benutzt um den Vorg�nger zu loeschen �ndert DIREKT die next-Variable des Vorg�ngers !!!!
	 */
	void delConnection() {
		if (hasUpConnection()) // ist bereits verkn�pft !!
		{
		switch(connectionType)
		{
		case NEXT:
			connectedFrom.next=null;
			break;
		case INSIDE:
			Loop l;
			l = (Loop)connectedFrom;
			l.inside=null;
			break;
		case ALT1:
			Condition c1;
			c1 = (Condition)connectedFrom;
			c1.alt1=null;
			break;
		case ALT2:
			Condition c2;
			c2 = (Condition)connectedFrom;
			c2.alt2=null;
		break;
		}
		connectedFrom=null;
	  }
	}
	

	/** Ein Element einf�gen
	 * @param   typeOfElement  
	 */
	void insert(int typeOfElement)
	{
		StrukElement element = struktogramm.addElement(typeOfElement);
		try {
			connect(connectionType, connectedFrom, element);
		} catch (NullPointerException e) {}
		element.setNext(this);
		struktogramm.setLayer(element,element.getLayer(standardLayer));
		if (this == struktogramm.getFirst())
			struktogramm.setFirst(element);
	}
	
	/** Ein Element anh�ngen
	 * @param   typeOfElement  
	 */	
	void append(int typeOfElement)
	{
		StrukElement element = struktogramm.addElement(typeOfElement);
		element.setNext(getNext());
		setNext(element);
		struktogramm.setLayer(element,element.getLayer(standardLayer));
		Tracer.out("Layer: "+element.getLayer(0));
	}

	/** Dieses Element mit dem Vorg�nger tauschen
	 */
	void swop()
	{
		StrukElement topElement = connectedFrom.connectedFrom;
		StrukElement bottomElement = getNext();
		StrukElement upper = connectedFrom;
		if (upper != struktogramm.getFirst())
			connect(upper.connectionType, topElement, this);
		else
		{
			struktogramm.setFirst(this);
			setX(upper.getX());
			setY(upper.getY());	
			upper.setNext(null);
		}	
		connect(NEXT, this, upper);
		if (bottomElement != null)
			connect(NEXT, upper, bottomElement);
		else
			upper.setNext(null);
	}
	
	/** Zu einer Schleife (die direkt davorh�ngt) hinzuf�gen
	 */
	void addToLoop()
	{
		Loop loop = (Loop)connectedFrom;
		connect(NEXT, loop, getNext());
		connect(NEXT, getLastOfBlock(loop.getInside()), this);
		struktogramm.setLayer(loop,loop.getLayer(standardLayer));
	}
	
	/** Zu einer Condition (die direkt davorh�ngt) hinzuf�gen
	 * @param   part (gibt an zu welchem Teil der Condition es hinzugef�gt werden soll)
	 */
	void addToCondition(boolean part)
	{
		Condition condition = (Condition)connectedFrom;
		connect(NEXT, condition, getNext());
		if (part == true)
			connect(NEXT, getLastOfBlock(condition.getAlt1()), this);
		if (part == false)
			connect(NEXT, getLastOfBlock(condition.getAlt2()), this);
		tare();			
	}
	
	/** Au�erhalb einer Condition oder Loop schieben
	 */
	void moveOutsideBlock()
	{
		StrukElement topOfBlock = getTopOfBlock(this);
		if (getNext() != null)
			connect(connectionType, connectedFrom, getNext());
		else
			if (connectedFrom == topOfBlock)
				connect(connectionType, topOfBlock, struktogramm.addElement(COMMAND));	
		connect(NEXT, this, topOfBlock.getNext());
		connect(NEXT, topOfBlock, this);
	
	}
	
	/** Ein Element l�schen
	 * @param   must (Gibt an ob die L�schung unter bestimmten Bedingungen auch
	 * "simuliert" werden darf indem einfach der Inhalt gel�scht wird)
	 * Die Abfrage innterhalb von (must==false) geh�rt aber eigentlich nach Evt da must==false eh nur dort vorkommt ?! 
	 */
	void delete(boolean must)
	{
		if (must == false)
		{
			if (connectionType != NEXT && getNext() == null)
			// z.b. einzigstes Element unter dem true-part einer Condition
			// ==> Darf nicht gel�scht werden
			label = ""; // L�schung wird "simuliert"
			else
			{
				try {
					connect(connectionType, connectedFrom, getNext());
				} catch (NullPointerException e) {}
				mEvt.delete();
				mEvt=null;
				struktogramm.delStrukElement(this);
			}
		}
		else
		// Hier wird die L�schung explizit verlangt ! Die Folgen tr�gt der Aufrufer
		{
		
			try {
				connect(connectionType, connectedFrom, getNext());
			} catch (NullPointerException e) {}
			mEvt.delete();
			mEvt=null;
			struktogramm.delStrukElement(this);
		}
		struktogramm.getView().repaint();
	}
	
	/** Methode um ganze Loops oder Conditions samt Inhalt l�schen zu k�nnen */
	abstract void deleteAll();
	
	 	
	/** Rekursive Funktion um die H�he eines Blocks auszurechnen. Wird von Loop und Condition benutzt
	 * @return Gesamth�he des Elements und aller Ihrer Folgeelemente    
	 */
	int calcHeight()
	throws ResizeException
	{
		if (next!=null)
	 		return (getHeight()+next.calcHeight());
	 	else
	  		return (getHeight());
	}

	/** Rechnet die Koordinaten des n�chsten Elements aus und setzt diese. Wird in den Subelementen Loop und Condition durch entsprechende Methoden erg�nzt.
	 *	@see Loop#calcInside
	 *	@see Condition#calcAlt
	 */
	void calcNext()
	{
	  if (next!=null)
	  {
	     next.setX(nextX());
	     next.setY(nextY());
	     next.setWidth(this.getWidth());
	  }
	}
	

	/** Die Paint-Methode des Elements. Wird nur �ber die paint-Methoden (super.paint()) der Subelemente aufgerufen. Zeichnet, wenn n�tig drag-Point. Mu� in den Subelementen konkretisiert werden
	 * @see Command#paint 
	 * @see Loop#paint 
	 * @see Condition#paint  
	 * @param g, das Graphics-Object  
	 */
	public void paintComponent(Graphics g)
	{
	   super.paintComponent(g);
	   //g.setColor(Color.black);
	   // Das eigentliche Zeichnen wird (fast) den Unterklassen �berlasssen
		if (struktogramm.presets.enabSeDragging)
		{
			if (!hasUpConnection())
				g.fillRect(0,0,5,5);
		}
		
		if (executing == true)
			g.setColor(Color.red);
		else
			g.setColor(Color.black);
			
		// die obere Linie des Elements (zur besseren Pflege hierher verlegt)
		if ((!hasUpConnection() | executing | (connectedFrom instanceof TailLoop & connectedFrom==struktogramm.getFirst())))
			paintTopLine(g);	
				
		if (breakpoint)
		{
			g.setColor(Color.red);
			int diameter = getHeight()<14 ? getHeight()-4 : 10;
			g.fillOval(15,getHeight()/2-diameter/2,diameter,diameter);
			g.setColor(Color.black);	
		}	
	}


	/** Es hat irgendeinen Grund das das ausgelagert wurde !!!?
	 * @param   g  
	 */
	abstract void paintTopLine(Graphics g);
	
	/** Die Elemente sind in Layern geschichtet. 
	 * Keine Loop oder Condition darf Ihre Unterelemente verdecken 
	 * (Auch wenn das Element trotzdem sichtbar w�re w�rde sonst immer 
	 * das falsche PopUpMen� zum Vorschein kommen. Diese Methode ist 
	 * (unter anderem) f�r diese Funktionalit�t zust�ndig 
	 */
	int getLayer(int tempLayer)
	{
		if (connectedFrom != null)
		{
			if (connectionType != INSIDE)
				return connectedFrom.getLayer(tempLayer);
			else
				return connectedFrom.getLayer(tempLayer+1);
		}
		else 
			return tempLayer;	
	}
	
	/** Z�hlen wie viele Elemente in einem Block enthalten sind (f�r die Formatierung beim Speichern)
	 */
	int getBlockCount()
	{
		return getBlockCount(1);
	}
	
	/** Z�hlen wie viele Elemente in einem Block enthalten sind (f�r die Formatierung beim Speichern)
	 */
	int getBlockCount(int i)
	{
		if (getNext()!=null)
			return(getNext().getBlockCount(i)+1);
		else
			return i; 
	}
	

	/** Gibt zur�ck, ob geschweifte Klammern verwendet werden sollen (nur Loop und Condition)
	 * @param   se  
	 * @return     
	 */
	boolean blockPrint(StrukElement se)
	{
		// Das Element ist als erstes Element eines Blocks anzusehen wenn eines der boolean-Werte wahr ist
		boolean blockByCCode = false;
		if (se instanceof Command)
		{
			if (((Command)se).getActionType() != Command.CALCULATION && struktogramm.presets.makeCCode)
				blockByCCode = true;
		}
		boolean blockByBlockCount = se.getBlockCount() > 1;
		boolean blockByElementType = (se instanceof Condition) || (se instanceof Loop);
		return blockByCCode || blockByBlockCount || blockByElementType ;	
	}
		
	/** rekursive Methode um Kommandos ausf�hren zu k�nnen. Mu� in allen StrukElementen vorhanden sein. Wird in den jeweiligen Strukelementen entsprechend ihrer Funktionalit�t implementiert
	 * @param   p, das Processor-Objekt (wird zum parsen etc. verwendet)  
	 */
	abstract void execute(Processor p) 
	throws ProcessorException, InterruptedException, LoopControlException, ReturnException;
	
	// getter/setter-Methoden


	/** Getter-Methode
	 * @return Das Label des Elements    
	 */
	String getLabel()
	{
		return label;
	}
	/** Setter-Methode
	 * @param   newLabel das neue Label des Elements  
	 */
	void setLabel(String newLabel)
	throws StruktorException
	{
		if (Utils.containsVovelMutation(newLabel))
			throw new StruktorException("No VovelMutations ! \n (keine Umlaute !)");
		else
			label=newLabel;
	}
	

	/** Getter-Methode
	 * @return das n�chste Element des Elements    
	 */
	StrukElement getNext()
	{
	  return next;
	}
	
	/** Setter-Methode
	 * Die Y-Koordinate setzen    
	 */
	void setY(int y)
	{
	  setLocation((int)getLocation().getX(),y);
	}
	
	/** Setter-Methode
	 * Die X-Koordinate setzen     
	 */
	void setX(int x)
	{
	  setLocation(x,(int)getLocation().getY());
	}

	/** Setter-Methode
	 * Die Breite des Elements ver�ndern
	 * @param   width  
	 */
	void setWidth(int width)
	{
		try {
			int minWidth = getGraphics().getFontMetrics().stringWidth(getLabel());
			if (width > minWidth+20)
		   		setSize(new Dimension(width,getSize().height));
			else
			{
				setSize(new Dimension(minWidth+20,getSize().height));
			}
		} catch (NullPointerException e)
		{ 
			if (width > 20)
				setSize(new Dimension(width,getSize().height));
			else
				setSize(new Dimension(20,getSize().height));	
		}	
			
	}
	

	/** Setter-Methode
	 * Die H�he des Elements ver�ndern
	 * @param   height  
	 */
	void setHeight(int height)
	throws ResizeException
	{
		if (height > 20)
			setSize(new Dimension(getSize().width,height));
		else
		{
			setSize(new Dimension(getSize().width,20));
			throw new ResizeException(height-20, ResizeException.HEIGHT);
		}
	}
	
	/** Due H�he eines Elements vom User ver�ndern
	 * @param   height  
	 */
	void setHeightByUser(int height)
	{
		// jetzige H�he ermitteln
		// Probiern wir es
		try {
			setHeight(height);
			if (hasUpConnection())
				connectedFrom.heightSet(this.connectionType); 
		} catch (ResizeException re) {
			// Ok, keine Ver�nderung, evtl austarieren ...
			//tare();
			Tracer.out("resizeException catched !");
		}
	}
	

	/** Falls sich das Element in einem Block einer Condition befindet, 
	 * gleicht diese Funktion die Gr��en der Bl�cke aneinander an
	 * Etwas �hnliches geschieht bei @seeCondition#calcHeight allerdings
	 * wird das dort unter dem Gesichtspunkt des Users, der gerade eine
	 * Gr��e ver�ndert implementiert. Beide Funktionalit�ten sind zwar
	 * �hnlich funktionieren aber nach etwas anderen Prinzipien.
	 */
	void tare()
	{
		int connecType = getConnectionTypeOfBlock(this); 
				
		// befinden uns in einem Block einer Condition ?
		if (connecType==ALT1 || connecType==ALT2)
		{
			// ... denn dann mu� (evtl) entsprechend austariert werden ...
			try {
				// Anfang der Gr��enberechnung ....
				int ourHeight=getFirstOfBlock(this).calcHeight();
				int othersHeight;
				StrukElement othersFirst;
				if (connecType==ALT1)
					othersFirst=((Condition)getTopOfBlock(this)).getAlt2();
				else
					othersFirst=((Condition)getTopOfBlock(this)).getAlt1();
				
				othersHeight=othersFirst.calcHeight();
				// Ende der Gr��enberechnung
				int diff=Math.abs(ourHeight-othersHeight);
				if (ourHeight==othersHeight)
					return;
				else if (ourHeight < othersHeight)
					// machen wir uns gr��er
					setHeight(getHeight()+diff);
				else
				{
					// machen wir uns kleiner
					try {
						setHeight(getHeight()-diff);
					} catch (ResizeException re2) 
					{
						// na gut, machen wir halt den anderen gr��er
						othersFirst.setHeight(othersFirst.getHeight()+diff);	
					}
				}
					
			} catch (ResizeException re) {// Da wei� ich auch nicht weiter ! Mu� man da weiterwissen ? 
			}	
		}
	}
	
	/** meldet rekursiv zum Vorg�ngerElement, da� die H�he ver�ndert wurde. Eine Loop oder Condition mu� darauf reagieren ... */
	void heightSet(int connectionType)
	throws ResizeException
	{
		if (connectedFrom != null)
			connectedFrom.heightSet(this.connectionType); 
	}
	
	
	/** wird verwendet, um die X-Koordinate des Nachfolgeelements 
	 * in Abh�ngigkeit vom eigenen Element zu errechnen
	 * @return (Soll)X-Koordinate des n�chsten Elements    
	 */
	private int nextX()
	{
		return getX();
	}
	
	/** wird verwendet, um die Y-Koordinate des Nachfolgeelements 
	 * in Abh�ngigkeit vom eigenen Element zu errechnen
	 * @return (Soll)Y-Koordinate des n�chsten Elements    
	 */
	private int nextY()
	{
		return getY()+getSize().height;
	}
	
	/** Breakpoint setzen oder reseten */
	void setBreakpoint(boolean flag)
	{
		breakpoint = flag;
	}
	
	boolean getBreakpoint()
	{
		return breakpoint;
	}
	
	/** Speichert das Element (als "Quelltext") im saveObject */
	abstract void save(Save saveObject);
}