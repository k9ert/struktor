// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor;

import javax.swing.*;
import java.awt.*;

/** Eine Klasse für generell alle internen Exceptions. 
 * Mithilfe der Methode show...Msg läßt sich die Meldung elegant als Fenster anzeigen
 */

public class StruktorException extends Exception
{
	private String msg = "Undefined Runtime Exception";
	public boolean showed=false;
	
	public StruktorException()
	{
	}
	
	
	public StruktorException(String msg)
	{
		this.msg = msg;
	}
	
	public void addToMsg(String additional)
	{
		msg = msg + additional;
	}
	
	public void showErrorMsg(final Frame frame)
	{
		showed = true;
		Runnable showM = new Runnable() {
            public void run() { 
				JOptionPane.showMessageDialog(
					frame,
					msg, 
					"Error", 
					JOptionPane.ERROR_MESSAGE);}
        };
        SwingUtilities.invokeLater(showM);
		struktor.strukelements.Struktogramm.stopStruktogramm();
		
	}
	
	public void showMsg(final Frame frame)
	{
		showed = true;
		Runnable showM = new Runnable() {
            public void run() { 
				JOptionPane.showMessageDialog(
					frame,
					msg, 
					"Information", 
					JOptionPane.INFORMATION_MESSAGE);}
        };
        SwingUtilities.invokeLater(showM);
		struktor.strukelements.Struktogramm.stopStruktogramm();	
	}
	
	public void showMsg(Container container)
	{
		showMsg(Utils.getFrame(container));
	}
	
	public void showErrorMsg(Container container)
	{
		showErrorMsg(Utils.getFrame(container));
	}
	
	public String getMsg()
	{
		return msg;
	}
}