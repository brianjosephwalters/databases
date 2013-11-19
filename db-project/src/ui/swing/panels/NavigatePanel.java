package ui.swing.panels;

import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NavigatePanel extends JPanel {

	JButton btnPrevious;
	JButton btnNext;
	JTextField tfIndex;
	JTextField tfMax;
	/**
	 * Create the panel.
	 */
	public NavigatePanel(ActionListener controller) {
		setLayout(new BoxLayout( this, BoxLayout.X_AXIS));
		
		btnPrevious = new JButton("Previous");
		btnPrevious.setEnabled(false);
		btnPrevious.setActionCommand("previous");
		btnPrevious.addActionListener(controller);
		
		tfIndex = new JTextField();
		tfIndex.setHorizontalAlignment(JTextField.CENTER);
		
		tfMax = new JTextField();
		tfMax.setHorizontalAlignment(JTextField.CENTER);
		tfMax.setEditable(false);
		
		btnNext = new JButton("Next");
		btnNext.setEnabled(false);
		btnPrevious.setActionCommand("next");
		btnNext.addActionListener(controller);
		
		add(btnPrevious);
		add(Box.createHorizontalStrut(10));
		add(tfIndex);
		add(Box.createHorizontalStrut(10));
		add(new JLabel("of"));
		add(tfMax);
		add(Box.createHorizontalStrut(10));
		add(btnNext);
	}
}
