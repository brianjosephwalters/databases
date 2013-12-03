package ui.swing.test;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import models.Job;
import models.Person;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Font;

public class HirePanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public HirePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);
		
		JLabel lblNewLabel = new JLabel("Hire:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
		add(lblNewLabel);
		
		JPanel personPanel = new JPanel();
		add(personPanel);
		personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.X_AXIS));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		personPanel.add(horizontalStrut);
		
		JLabel lblPerson = new JLabel("Person:");
		personPanel.add(lblPerson);
		
		JComboBox<Person> cbPerson = new JComboBox<Person>();
		personPanel.add(cbPerson);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		personPanel.add(horizontalStrut_3);
		add(Box.createVerticalStrut(20));
		JPanel jobPanel = new JPanel();
		add(jobPanel);
		jobPanel.setLayout(new BoxLayout(jobPanel, BoxLayout.X_AXIS));
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		jobPanel.add(horizontalStrut_1);
		
		JLabel lblJob = new JLabel("New label");
		jobPanel.add(lblJob);
		
		JComboBox<Job> cbJob = new JComboBox<Job>();
		jobPanel.add(cbJob);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		jobPanel.add(horizontalStrut_2);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		add(verticalGlue_1);

	}

}
