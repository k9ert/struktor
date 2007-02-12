// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor.strukelements;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

import struktor.StruktorException;
import struktor.Tracer;

/** Implementiert die Watchlist als Box in der Ausdr�cke �berwacht werden k�nnen
 * F�r jedes Struktogramm wird eine eigene Watchlist instanziert
 */
public class WatchList extends Box
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Vector watchListList = new Vector();
	private static void addWatchList(WatchList w)
	{
		watchListList.addElement(w);
		
	}
	
	/** Eine Watchlist zum Struktogramm suchen */
	static public WatchList findWatchList(Struktogramm sg)
	{
		for (Enumeration el=watchListList.elements(); el.hasMoreElements(); )
        {
             WatchList w = (WatchList)el.nextElement();
             if (w.struktogramm==sg)
                return w;
        }
    	Tracer.out("OhOh ... no Watchlist found !");    
		return null;
	}
	
	static public void deleteWatchList(Struktogramm sg)
	{
		watchListList.remove(findWatchList(sg));
	}
	
	
	private Struktogramm struktogramm;
	/** unsch�n, ich wei� aber das �ber ein Array zu regeln war irgendwie nicht m�glich. Sollte ge�ndert werden */
	private JTextField expr1 = new JTextField(7);
	private JTextField calc1 = new JTextField(7);
	private JTextField expr2 = new JTextField(7);
	private JTextField calc2 = new JTextField(7);
	private JTextField expr3 = new JTextField(7);
	private JTextField calc3 = new JTextField(7);
	private JTextField expr4 = new JTextField(7);
	private JTextField calc4 = new JTextField(7);
	private JTextField expr5 = new JTextField(7);
	private JTextField calc5 = new JTextField(7);
	private Box expressions = new Box(BoxLayout.Y_AXIS);
	private Box calculations = new Box(BoxLayout.Y_AXIS);
		
	public WatchList(Struktogramm struktogramm)
	{
		super(BoxLayout.X_AXIS);
		//setBorder(BorderFactory.createTitledBorder("WatchList"));
		this.struktogramm = struktogramm;
		addWatchList(this);
		
		expr1.setPreferredSize(new Dimension(70,20));
		expr1.setMaximumSize(new Dimension(70,20));
		expressions.add(expr1);
		
		calc1.setPreferredSize(new Dimension(70,20));
		calc1.setMaximumSize(new Dimension(70,20));
		calculations.add(calc1);
		calc1.setEditable(false);
		
		expr2.setPreferredSize(new Dimension(70,20));
		expr2.setMaximumSize(new Dimension(70,20));
		expressions.add(expr2);
		
		calc2.setPreferredSize(new Dimension(70,20));
		calc2.setMaximumSize(new Dimension(70,20));
		calculations.add(calc2);
		calc2.setEditable(false);
		
		expr3.setPreferredSize(new Dimension(70,20));
		expr3.setMaximumSize(new Dimension(70,20));
		expressions.add(expr3);
		
		calc3.setPreferredSize(new Dimension(70,20));
		calc3.setMaximumSize(new Dimension(70,20));
		calculations.add(calc3);
		calc3.setEditable(false);
		
		expr4.setPreferredSize(new Dimension(70,20));
		expr4.setMaximumSize(new Dimension(70,20));
		expressions.add(expr4);
		
		calc4.setPreferredSize(new Dimension(70,20));
		calc4.setMaximumSize(new Dimension(70,20));
		calculations.add(calc4);
		calc4.setEditable(false);
		
		expr5.setPreferredSize(new Dimension(70,20));
		expr5.setMaximumSize(new Dimension(70,20));
		expressions.add(expr5);
		
		calc5.setPreferredSize(new Dimension(70,20));
		calc5.setMaximumSize(new Dimension(70,20));
		calculations.add(calc5);
		calc5.setEditable(false);
		
		add(expressions);
		add(calculations);
	}
	
	/** Die Ausdr�cke neu berechnen lassen */
	protected void update(struktor.processor.Processor processor)
	throws InterruptedException
	{
		try {
			processor.resetValuesChangedFlag();
			calc1.setText(processor.watchlistParse(expr1.getText()));
			calc2.setText(processor.watchlistParse(expr2.getText()));
			calc3.setText(processor.watchlistParse(expr3.getText()));
			calc4.setText(processor.watchlistParse(expr4.getText()));
			calc5.setText(processor.watchlistParse(expr5.getText()));	
		} catch (StruktorException e) {e.showMsg(this); throw new InterruptedException();}
	}
}
		
		
		