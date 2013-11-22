package ui.swing.panels;

import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel {
	ActionListener controller;
	
	JButton btnDelete;
	JButton btnPrevious;
	JButton btnNext;
	JButton btnNew;
	JTextField tfIndex;
	JTextField tfMax;

	/**
	 * Create the panel.
	 */
	public NavigationPanel() {

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnDelete.setActionCommand("delete");
		btnDelete.addActionListener(controller);
		
		btnPrevious = new JButton("Previous");
		btnPrevious.setEnabled(false);
		btnPrevious.setActionCommand("previous");
		btnPrevious.addActionListener(controller);
		
		tfIndex = new JTextField();
		tfIndex.setHorizontalAlignment(JTextField.CENTER);
		tfIndex.setActionCommand("index");
		tfIndex.addActionListener(controller);
		
		tfMax = new JTextField();
		tfMax.setHorizontalAlignment(JTextField.CENTER);
		tfMax.setEditable(false);
		
		btnNext = new JButton("Next");
		btnNext.setEnabled(false);
		btnNext.setActionCommand("next");
		btnNext.addActionListener(controller);
		
		btnNew = new JButton("New");
		btnNew.setEnabled(false);
		btnNew.setActionCommand("new");
		btnNew.addActionListener(controller);
		
		setLayout(new BoxLayout( this, BoxLayout.X_AXIS));
		add(Box.createHorizontalGlue());
		add(btnDelete);
		add(Box.createHorizontalStrut(10));
		add(btnPrevious);
		add(Box.createHorizontalStrut(10));
		add(tfIndex);
		add(Box.createHorizontalStrut(10));
		add(new JLabel("of"));
		add(tfMax);
		add(Box.createHorizontalStrut(10));
		add(btnNext);
		add(Box.createHorizontalStrut(10));
		add(btnNew);
		add(Box.createHorizontalGlue());
	}
	
	public void setNavigationController(ActionListener controller) {
		this.controller = controller;
	}
}
