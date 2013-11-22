package ui.swing.panels;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;

public class ReadableFormPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public ReadableFormPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel headerPanel = new JPanel();
		add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new GridLayout(1, 2, 0, 0));
		
		JLabel lbTitle = new JLabel("Title");
		lbTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		headerPanel.add(lbTitle);
		
		JPanel textPanel = new JPanel();
		add(textPanel, BorderLayout.CENTER);
		textPanel.setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textPanel.add(textArea, BorderLayout.CENTER);
		
		Component horizontalStrut = Box.createHorizontalStrut(10);
		textPanel.add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		textPanel.add(horizontalStrut_1, BorderLayout.EAST);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		textPanel.add(verticalStrut, BorderLayout.NORTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		textPanel.add(verticalStrut_1, BorderLayout.SOUTH);
		
		JPanel footerPanel = new JPanel();
		add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new GridLayout(0, 3, 0, 0));
		
		JButton btnNew = new JButton("New");
		footerPanel.add(btnNew);
		
		JLabel lblAdd = new JLabel("Add:");
		lblAdd.setHorizontalAlignment(SwingConstants.RIGHT);
		footerPanel.add(lblAdd);
		
		JComboBox comboBox = new JComboBox();
		footerPanel.add(comboBox);

	}

}
