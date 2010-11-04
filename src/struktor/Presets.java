// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

/** Die Variablen dieser Klasse speichern den Zustand des Systems.
 *  Mittlererweile kann man bei der Application-Class die Variablen
 *  ändern. Dort kann man auch als "Fingertip" anschauen, was die
 *  einzelnen Variablen für Bedeutung haben. (direkt im Quelltext 
 *  nachschauen). Alle Variablen sind public damit sie von überall 
 *  her zugreifbar sind ...
 */


public class Presets implements ItemListener, ActionListener, Serializable
{
	
	static public final String version="0.65";
	
	/** Die Bedeutung der einzelnen Variablen kann im Quelltext nachgeschaut werden. */
	//enab-Variablen geben an, ob ein Element überhaupt erscheinen soll
	
		//StruktorPanel:
		public boolean enabOutput =			true;
		String enabOutputTT = "ob die Output-checkbox angezeigt wird";
		public boolean enabNew = 			true;
		String enabNewTT = "ob der New-Button angezeigt wird";
		public boolean enabRemove = 		true;
		String enabRemoveTT = "ob der Remove-Button angezeigt wird";
		public boolean enabRename = 		true;
		String enabRenameTT = "ob der Rename-Button angezeigt wird";
		public boolean enabSave = 			true;
		String enabSaveTT = "ob der Save-Button angezeigt wird";
		public boolean enabLoad = 			true;
		String enabLoadTT = "ob der Load-Button angezeigt wird";
		public boolean enabSwitchStruk = 	true;
		String enabSwitchStrukTT = "ob man zwischen den Struktogrammen wechseln kann";
		public boolean enabSwitchView = 	true;
		String enabSwitchViewTT = "ob man in den Declaration-View wechseln kann";
		public boolean enabStructs =		true;
		String enabStructsTT = "ob man Strukturen erzeugen und editieren kann";
		public boolean enabWatchlist = 		true;
		String enabWatchlistTT = "ob man sich die watchlist anzeigen lassen kann";
		//StruktogrammPanel:
		public boolean enabExecute = 		true;
		String enabExecuteTT = "ob der Execute-Button angezeigt wird";
		public boolean enabDebugMode = 		true;
		String enabDebugModeTT = "ob die Debug-Checkbox angezeigt wird";
		public boolean enabTitle = 			true;
		String enabTitleTT = "ob ein Titel für jedes Struktogramm angegeben wird";
		
		//Dec und DecList
		public boolean enabParameter = 		true;
		String enabParameterTT = "ob Parameter erlaubt sind";
		public boolean enabPointer = 		true;
		String enabPointerTT = "ob Pointer erlaubt sind";
		public boolean enabArrays = 		true;
		String enabArraysTT = "ob Arrays erlaubt sind";
		public boolean enabInteger = 		true;
		String enabIntegerTT = "ob Integer erlaubt sind";
		public boolean enabDouble = 		true;
		String enabDoubleTT = "ob Double erlaubt sind";
		public boolean enabCharacter = 		true;
		String enabCharacterTT = "ob Character erlaubt sind";
		public boolean enabStrings = 		true;
		String enabStringsTT = "ob Strings erlaubt sind";
		public boolean enabDecDelete = 		true;
		String enabDecDeleteTT = "ob Deklarationen gelöscht werden können";
		public boolean enabNewDec =			true;
		String enabNewDecTT = "ob neue Deklarationen erstellt werden können";
		
		//StrukElements
		public boolean enabSeDragging =		true;
		String enabSeDraggingTT = "ob die Struktogramme verschiebbar sind";
		public boolean enabSeResizing = 	true;
		String enabSeResizingTT = "ob die Struktogramme größenveränderbar sind";
		public boolean enabSePopUp =		true;
		String enabSePopUpTT = "ob man das PopUp-Menu aufrufen kann";
		public int		SeXPos =			100;
		public int		SeYPos =			100;
		public int		SeWidth =			400;
		public int		SeHeight =			40;
		
		//StrukElements PopUp
		public boolean enabSePuProperties =	true;
		String enabSePuPropertiesTT = "ob die properties aufrufbar sind";
		public boolean enabSePuInsert =		true;
		String enabSePuInsertTT = "ob man Elemente einfügen kann";
		public boolean enabSePuAppend =		true;
		String enabSePuAppendTT = "ob man Elemente anhängen kann";
		public boolean enabSePuCommand =		true;
		String enabSePuCommandTT = "ob Commands erzeugbar sind";
		public boolean enabSePuHeadLoop =		true;
		String enabSePuHeadLoopTT = "ob HeadLoops erzeugbar sind";
		public boolean enabSePuTailLoop =		true;
		String enabSePuTailLoopTT = "ob TailLoops erzeugbar sind";
		public boolean enabSePuForLoop =		true;
		String enabSePuForLoopTT = "ob ForLoops erzeugbar sind";
		public boolean enabSePuCondition =		true;
		String enabSePuConditionTT = "ob Conditions erzeugbar sind";
		public boolean enabSePuSwitch =			true;
		String enabSePuSwitchTT = "ob Switchs erzeugbar sind";
		public boolean enabSePuMove =			true;
		String enabSePuMoveTT = "ob Elemente hierarchich aufgestuft werden können \"move outside Block\"";
		public boolean enabSePuAdd =			true;
		String enabSePuAddTT = "ob Elemente hierarchich abgestuft werden können (z.B. \"add to Loop\")";
		public boolean enabSePuSwop =			true;
		String enabSePuSwopTT = "ob Elemente getauscht werden können";
		public boolean enabSePuDelete =			true;
		String enabSePuDeleteTT = "ob Elemente gelöscht werden können";
		public boolean enabSePuBreakpoints =	true;
		String enabSePuBreakpointsTT = "ob Breakpoints erlaubt sind";
	
	//stat-Variablen geben den Zustand an, den ein Element hat, wenn es angezeigt wird
	
		//StruktorPanel:
		public boolean statOutput =			false;
		public boolean statWatchlist = 		false;
		//public String showAtStart = 		"";
		
		//StruktogrammPanel:
		public boolean statDebugMode = 		false;
		public boolean makeCCode = 			false;
			
	//Allgemeine Voreinstellungen
	
	public boolean showLogo = 				true;
	public boolean isApplet = 				true;
	public boolean loadAndRun =				false;
	
	transient Applet applet;
	transient JDialog dialog;
	
	Presets(Applet applet)
	{
		this.applet = applet;
		if (Struktor.isApplet)
			initPresets();
	}
	
	/** nur für ExprCalc*/
	Presets()
	{
		presetAllFeatures();
	}
	
	
	
	void initPresets()
	{
		setPresets();
		// Enab-Variablen
		initEnabs();
		// Stat-Variablen
		initStats();
		
		// Allgemeine Voreinstellungen
		showLogo = initBoolVar("showLogo");
	}
	
	/** Alle preset-Methoden werden im Dialog als Button angezeigt
	 *  und können so ausgeführt werden um bestimmte Gruppen von
	 *  Eigenschaften ändern zu können
	 */
	void presetAllEnabFalse()
	{
		//StruktorPanel:
		enabOutput =			false;
		enabNew = 				false;
		enabRename = 			false;
		enabRemove = 			false;
		enabSave = 				false;
		enabLoad = 				false;
		enabSwitchStruk = 		false;
		enabSwitchView = 		false;
		enabWatchlist = 		false;
		//StruktogrammPanel:
		enabExecute = 			false;
		enabDebugMode = 		false;
		enabTitle = 			false;
		
		//Dec und DecList
		enabParameter = 		false;
		enabPointer = 			false;
		enabArrays = 			false;
		enabInteger = 			false;
		enabDouble = 			false;
		enabCharacter = 		false;
		enabStrings = 			false;
		enabDecDelete = 		false;
		enabNewDec =			false;
		
		//StrukElements
		enabSeDragging =		false;
		enabSeResizing = 		false;
		enabSePopUp =			false;
	}
	
	/** Alle preset-Methoden werden im Dialog als Button angezeigt
	 *  und können so ausgeführt werden um bestimmte Gruppen von
	 *  Eigenschaften ändern zu können
	 */
	public void presetAsImage()
	{
		presetAllEnabFalse();
		showLogo = false;
		enabInteger = true;
		enabDouble = true;
		enabCharacter = true;
		enabStrings = true;
		SeXPos = 30;
		SeYPos = 30;
		SeWidth = 150;
		SeHeight = 50;
	}
	
	/** Alle preset-Methoden werden im Dialog als Button angezeigt
	 *  und können so ausgeführt werden um bestimmte Gruppen von
	 *  Eigenschaften ändern zu können
	 */
	public void presetAsExeutableImage()
	{
		presetAsImage();
		enabExecute = true;
	}
	
	/** Alle preset-Methoden werden im Dialog als Button angezeigt
	 *  und können so ausgeführt werden um bestimmte Gruppen von
	 *  Eigenschaften ändern zu können
	 */
	public void presetAllFeatures()
	{
		//StruktorPanel:
		enabOutput =			true;
		enabNew = 				true;
		enabRename = 			true;
		enabRemove = 			true;
		enabSave = 				true;
		enabLoad = 				true;
		enabSwitchStruk = 		true;
		enabSwitchView = 		true;
		enabWatchlist = 		true;
		//StruktogrammPanel:
		enabExecute = 			true;
		enabDebugMode = 		true;
		enabTitle = 			true;
		
		//Dec und DecList
		enabParameter = 		true;
		enabPointer = 			true;
		enabArrays = 			true;
		enabInteger = 			true;
		enabDouble = 			true;
		enabCharacter = 		true;
		enabStrings = 			true;
		enabDecDelete = 		true;
		enabNewDec =			true;
		
		//StrukElements
		enabSeDragging =		true;
		enabSeResizing = 		true;
		enabSePopUp =			true;
		SeXPos =				100;
		SeYPos =				100;
		SeWidth =				300;
		SeHeight =				50;
		
		//enabSeSwitch = 			true;
	
	//stat-Variablen geben den Zustand an, den ein Element hat, wenn es angezeigt wird
	
		//StruktorPanel:
		statOutput =			false;
		statWatchlist = 		false;
		
		//StruktogrammPanel:
		statDebugMode = 		false;
			
	//Allgemeine Voreinstellungen
		showLogo = 				true;
		
	}
		
		
	// -------------------------------------------------------------------------
	// Die folgenden Methoden sind nur Hilfsmethoden um die Voreinstellungen elegant
	// verändern zu können
	// -------------------------------------------------------------------------
	

	/** Zeigt ein Fenster an um die Eigenschaften interaktiv ändern zu
	 *  können.
	 */
	public void showDialog()
	{
		dialog=new JDialog();
		Class clazz = getClass();
	    JPanel enabProp = new JPanel(new GridLayout(4,6));
		JPanel enabSeProp = new JPanel(new GridLayout(10,1));
		JPanel enabSePuProp = new JPanel(new GridLayout(6,5));
		JPanel statProp = new JPanel(new GridLayout(5,3));
		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BorderLayout(30,30));
		Field[] fields = clazz.getFields();
		// Enab-Variablen
		for (int i = 0; i < fields.length; ++i) 
		{
			try {
				JCheckBox checkBox = new JCheckBox(fields[i].getName(),fields[i].getBoolean(this));
				checkBox.setToolTipText(getToolTip(fields[i].getName()));
				checkBox.addItemListener(this);
				if (fields[i].getName().startsWith("enabSePu"))
					enabSePuProp.add(checkBox);
				else if (fields[i].getName().startsWith("enabSe"))
					enabSeProp.add(checkBox);
				else if (fields[i].getName().startsWith("enab"))
					enabProp.add(checkBox);
				else if (fields[i].getName().startsWith("stat"))
					statProp.add(checkBox);
	        } catch (IllegalAccessException iae) {}
			catch (IllegalArgumentException iAe) {}
		}
		
		// Algemeine Voreinstellungen
		JCheckBox chShowLogo = new JCheckBox("showLogo",showLogo);
		chShowLogo.setToolTipText(getToolTip("showLogo"));
		chShowLogo.addItemListener(this);
		statProp.add(chShowLogo);
		
		JCheckBox chLoadAndRun = new JCheckBox("loadAndRun",loadAndRun);
		chLoadAndRun.setToolTipText(getToolTip("loadAndRun"));
		chLoadAndRun.addItemListener(this);
		statProp.add(chLoadAndRun);
		
		JCheckBox chMakeCCode = new JCheckBox("makeCCode",makeCCode);
		chMakeCCode.setToolTipText(getToolTip("makeCCode"));
		chMakeCCode.addItemListener(this);
		statProp.add(chMakeCCode);
		
		// Methoden zum einfacheren Anpassen
		Class claz = this.getClass();
		Method[] methods = claz.getMethods();
		for (int i = 0; i < methods.length; ++i)
		{
      		String name = methods[i].getName();		  
      		if (name.startsWith("preset"))
			{
				JButton button = new JButton(name);
				button.addActionListener(this);
				statProp.add(button);
			}
		}
		
		// Zambauen
		contentPane.add(enabProp, BorderLayout.NORTH);
		contentPane.add(enabSeProp, BorderLayout.WEST);
		contentPane.add(enabSePuProp, BorderLayout.EAST);
		contentPane.add(statProp, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		String name = ((JCheckBox)e.getSource()).getText();
		
		Class clazz = getClass();
	    Field field=null;
		try {
			field = clazz.getField(name);
			field.setBoolean(this, ((JCheckBox)e.getSource()).isSelected());
	    } catch(NoSuchFieldException nsfe) {}
		catch (IllegalAccessException iae) {}
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		String name = ((JButton)e.getSource()).getText();
		executePresetMethod(getPresetMethod(name));
		dialog.dispose();
		showDialog();
		
	}
	
	private void initEnabs()
	{
		Class clazz = getClass();
	    
		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; ++i) 
		{
	        try {		
				if (fields[i].getName().startsWith("enab"))
					fields[i].setBoolean(this,initBoolVar(fields[i].getName()));
	        } catch (IllegalAccessException iae) {}
		}
	
	}
	
	private void initStats()
	{
		Class clazz = getClass();
	    
		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; ++i) 
		{
	        try {		
				if (fields[i].getName().startsWith("stat"))
					fields[i].setBoolean(this,initBoolVar(fields[i].getName()));
	        } catch (IllegalAccessException iae) {}
		}
	}
	
	
	
	private boolean initBoolVar(String variable)
	{
		if (applet.getParameter(variable) != null)
		{
			if (applet.getParameter(variable).equals("true"))
				return true;
			else if (applet.getParameter(variable).equals("false"))
				return false;
		} 
		else
		{
			Class clazz = this.getClass();
			try {
				Field var = clazz.getField(variable);
				return ((Boolean)var.get(this)).booleanValue();
			} catch (NoSuchFieldException nsfe) { return false; }
			catch (IllegalAccessException iae) { return false; }
			
		}
		return false;		
	}
	
	private int initIntVar(String variable)
	{
		if (applet.getParameter(variable) != null)
			return Integer.parseInt(applet.getParameter(variable));
		else
		{
			Class clazz = this.getClass();
			try {
				Field var = clazz.getField(variable);
				return ((Integer)var.get(this)).intValue();
			} catch (NoSuchFieldException nsfe) { return 0; }
			catch (IllegalAccessException iae) { return 0; }
			
		}		
	}
	
	private void setPresets()
	{
		Method method;
		for (int i=1; i<11; i++)
		{
			if (applet.getParameter("Preset"+i) != null)
			{
				method = getPresetMethod(applet.getParameter("Preset"+i));
				executePresetMethod(method);
			} 
		}
	}
	
	private Method getPresetMethod(String preset)
	{
		Class claz = this.getClass();
		Method[] methods = claz.getMethods();
		for (int i = 0; i < methods.length; ++i)
		{
      		String name = methods[i].getName();		  
      		if (name.equals(preset))
				return methods[i];
		}
		return null;	
	}
	
	private void executePresetMethod(Method method)
	{
		try {
			method.invoke(this, (Object[])null);
		} 
		catch (IllegalArgumentException iae) {}
		catch (Exception e) {}
	}
	

	/** Serialisiert sich selber in einem File
	 * @param   file  
	 */
	public void saveToFile(File file)
	throws StruktorException
	{
		// Noch ein paar Sachen nachtragen bevors auf Disk geht ...
		SeWidth = ((Struktor)applet).getActualStruktogramm().getWidth();
		SeHeight = ((Struktor)applet).getActualStruktogramm().getHeightOfFirst()+5;
		
		try {
			FileOutputStream fs = new FileOutputStream(file);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(this);
			os.close();
		} catch (IOException e) {System.err.println(e.toString()); throw new StruktorException("Cannot save \"ser\"-File !");}	
	}
	
	
	public String toString()
	{
		String string=new String();
		Class clazz = getClass();
	    
		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; ++i) 
		{
	        try {		
				string = string + fields[i].getName()+"="+fields[i].get(this)+"\n";
	        } catch (IllegalAccessException iae) {}
		}
		return string;
	}
	

	/** Sucht den entsprechenden Erklärenden Text zu den Eigenschaften
	 * @param   name  
	 * @return     
	 */
	private String getToolTip(String name)
	{
		try {
			Class clazz = getClass();
	    	Field field = clazz.getDeclaredField(name+"TT");
			return ((String)field.get(this));
		} catch (java.lang.NoSuchFieldException nsfe) {
			return "no ToolTip :-(";
		} catch (java.lang.IllegalAccessException iae) {
			return "no ToolTip :-(";
		}
	}
}
