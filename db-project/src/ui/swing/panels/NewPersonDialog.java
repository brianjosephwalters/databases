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
import javax.swing.BoxLayout;
import java.awt.Component;

@SuppressWarnings("serial")
public class NewPersonDialog extends JDialog{
	Connection connection;
	
	ButtonController controller;
	
	PersonQueries personQueries;
	
	private JTextField tfPersonCode;
	private JTextField tfLastName;
	private JTextField tfFirstName;
	private JTextField tfGender;
	private JTextField tfEmail;
	
	private JButton okayButton;
	private JButton cancelButton;
	
	private JPanel panel;
	private Component horizontalStrut;
	private Component horizontalStrut_1;
	
	public NewPersonDialog(Connection connection) {
		this.connection = connection;
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
	}
	
	private void initializeDisplayPanel() {
		setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		horizontalStrut = Box.createHorizontalStrut(5);
		panel.add(horizontalStrut);
		tfPersonCode = new JTextField(10);
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
		okayButton = new JButton("Okay");
		okayButton.addActionListener(controller);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(controller);
		
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		buttonPanel.add(okayButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private Person getPersonFromFields() {
		String personCode = tfPersonCode.getText();
		String lastName = tfLastName.getText();
		String firstName = tfFirstName.getText();
		String gender = tfGender.getText();
		String email = tfEmail.getText();
		Person person = new Person(personCode, lastName, firstName, gender, email);
		return person;
	}
	
	public class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okayButton) {
				okayButton();
			} else if (e.getSource() == cancelButton) {
				cancelButton();
			}
		}
		public void okayButton(){
			int count = 0;
			Person person = getPersonFromFields();
			try {
				count = personQueries.addPerson(person);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (count == 1) {
				dispose();
			}
		}	
		
		public void cancelButton() {
			dispose();
		}
	}
}