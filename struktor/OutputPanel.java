// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor;

import javax.swing.*;
import java.awt.*;
/** kombiniert den grafischen und den textuellen Output in einem JPanel
 */
class OutputPanel extends JPanel
{
	Struktor struktor;
	private JComponent canvas;
	private JTextArea textOutput;

	OutputPanel(Struktor struktor, JComponent canvas, JTextArea textOutput)
	{
		setLayout(new BorderLayout());
		this.struktor = struktor;
		this.canvas = canvas;
		this.textOutput = textOutput;
		add(new JLabel("OutputWindow"), BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		textOutput.setLineWrap(true);
		textOutput.setWrapStyleWord(true);
		textOutput.setEditable(false);
		add(new JScrollPane(textOutput), BorderLayout.SOUTH);
	}
}