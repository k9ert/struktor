// Copyright 2000 Kim Neunert (k9ert@gmx.de), this is free Software (GNU Public License)
package struktor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * Eine Klasse f�r das Logo das am Anfang �blicherweise gezeigt wird
 */

class Copyright extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean closed = false;

	private ImageIcon icon;

	Copyright(Struktor struktor) {
		super(Utils.getFrame(struktor),
				"Copyright 2000, Kim Neunert, All Rights Reserved", true);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		Border bd1 = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		Border bd2 = BorderFactory.createEtchedBorder();
		Border bd3 = BorderFactory.createCompoundBorder(bd1, bd2);
		((JPanel) contentPane).setBorder(bd3);
		if (Struktor.isApplet)
			// only works when packaged as Jar
			icon = new ImageIcon(struktor.getImage(Struktor.class
					.getResource("Logo.jpg")));
		else
			icon = new ImageIcon("struktor/Logo.jpg");

		JButton ok = new JButton("Ok");
		ok.addActionListener(this);
		contentPane.add(new JLabel(icon, JLabel.CENTER), BorderLayout.CENTER);
		contentPane.add(ok, BorderLayout.SOUTH);
		setContentPane(contentPane);
		pack();
	}

	void showFor(int millis) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 3, dim.height / 3);
		setSize(dim.width / 3, dim.height / 3);
		setVisible(true);
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
		setVisible(false);
	}

	void showIt() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 3, dim.height / 3);
		setSize(dim.width / 3, dim.height / 3);
		setVisible(true);
		while (!closed)
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}

	}

	public void actionPerformed(ActionEvent event) {
		closed = true;
		setVisible(false);
		try {
			Thread.sleep(100);
		} catch (Exception e) {
		}
		dispose();
	}
}
