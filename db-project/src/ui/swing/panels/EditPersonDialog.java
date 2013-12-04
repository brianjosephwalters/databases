package ui.swing.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Person;
import models.queries.PersonQueries;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import java.awt.Component;

@SuppressWarnings("serial")
public class EditPersonDialog extends JDialog{
	Connection connection;
	Person person;
	
	ButtonController controller;
	
	PersonQueries personQueries;
	
	private JTextField tfPersonCode;
	private JTextField tfLastName;
	private JTextField tfFirstName;
	private JTextField tfGender;
	private JTextField tfEmail;
	
	private JButton applyButton;
	private JButton closeButton;

	private JPanel panel;
	private Component horizontalStrut;
	private Component horizontalStrut_1;
	
	public EditPersonDialog(Connection connection, Person person) {
		this.connection = connection;
		this.person = person;
		this.personQueries = new PersonQueries(connection);
		this.controller = new ButtonController();
		
		initializeGUIComponents();
		
		this.setBounds(100, 100, 400, 400);
		this.setTitle("New Person");
		this.setVisible(true);
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}
	
	private void initializeGUIComponents() {
		initializeDisplayPanel();
		initializeButtonPanel();
		displayPerson();
	}
	
	private void initializeDisplayPanel() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		horizontalStrut = Box.createHorizontalStrut(5);
		panel.add(horizontalStrut);
		tfPersonCode = new JTextField(10);
		tfPersonCode.setEditable(false);
		tfLastName = new JTextField(15);
		tfFirstName = new JTextField(15);
		tfGender = new JTextField(5);
		tfEmail = new JTextField(15);
		
		JPanel displayPanel = new JPanel();
		panel.add(displayPanel);
		displayPanel.setLayout(new GridLayout(5, 2));
		displayPanel.add(new JLabel("Person Code:"));
		displayPanel.add(tfPersonCode);
		displayPanel.add(new JLabel("Last Name:"));
		displayPanel.add(tfLastName);
		displayPanel.add(new JLabel("First Name:"));
		displayPanel.add(tfFirstName);
		displayPanel.add(new JLabel("Gender:"));
		displayPanel.add(tfGender);
		displayPanel.add(new JLabel("Email:"));
		displayPanel.add(tfEmail);
		
		horizontalStrut_1 = Box.createHorizontalStrut(5);
		panel.add(horizontalStrut_1);
	}
	
	private void initializeButtonPanel() {
		applyButton = new JButton("Apply");
		applyButton.addActionListener(controller);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(controller);
		
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		
		
		buttonPanel.add(closeButton);
		buttonPanel.add(applyButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void setPerson(Person person) {
		this.person = person;
		displayPerson();
	}
	
	private void displayPerson() {
		tfPersonCode.setText(person.getPersonCode());
		tfLastName.setText(person.getLastName());
		tfFirstName.setText(person.getFirstName());
		tfGender.setText(person.getGender());
		tfEmail.setText(person.getEmail());
	}
	
	public class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == applyButton) {
				applyButton();
			} else if (e.getSource() == closeButton) {
				closeButton();
			}
		}
		
		public void applyButton() {
			person.setPersonCode(tfPersonCode.getText());
			person.setLastName(tfLastName.getText());
			person.setFirstName(tfFirstName.getText());
			person.setGender(tfGender.getText());
			person.setEmail(tfEmail.getText());
			int count = 0;
			try {
				count = personQueries.updatePerson(person);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (count == 1) {
				setPerson(person);
			} else {
				
			}
		}
		public void closeButton() {
			dispose();
		}
	}
	
	public class PersonListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("currentPerson")) {
				setPerson((Person)evt.getNewValue());
			}
		}
	}
}