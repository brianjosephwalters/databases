package ui.swing.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DBConnection;

import models.Person;
import models.queries.PersonQueries;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class PersonFormPanel extends JPanel{
	Connection connection;
	Person person;
	
	private JTextField tfPersonCode;
	private JTextField tfLastName;
	private JTextField tfFirstName;
	private JTextField tfGender;
	private JTextField tfEmail;
	
	private JButton editButton;
	
	public PersonFormPanel(Connection connection) {
		this.connection = connection;
		setLayout(new BorderLayout());
		initializeDisplayPanel();
		initializeButtonPanel();
		add(Box.createVerticalStrut(10), BorderLayout.NORTH);
		add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		add(Box.createHorizontalStrut(10), BorderLayout.WEST);
	}
	
	private void initializeDisplayPanel() {
		tfPersonCode = new JTextField(10);
		tfPersonCode.setEditable(false);
		tfLastName = new JTextField(15);
		tfLastName.setEditable(false);
		tfFirstName = new JTextField(15);
		tfFirstName.setEditable(false);
		tfGender = new JTextField(5);
		tfGender.setEditable(false);
		tfEmail = new JTextField(15);
		tfEmail.setEditable(false);

		JPanel displayPanel = new JPanel();
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
		add(displayPanel, BorderLayout.CENTER);
	}
	
	private void initializeButtonPanel() {
		editButton = new JButton("Edit");
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		buttonPanel.add(editButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void displayPerson(Person person) {
		this.person = person;
		tfPersonCode.setText(person.getPersonCode());
		tfLastName.setText(person.getLastName());
		tfFirstName.setText(person.getFirstName());
		tfGender.setText(person.getGender());
		tfEmail.setText(person.getEmail());
	}
	
	public static void main(String[] args) {
		Connection connection = null;
		PersonQueries queries;
		List<Person> list = null;
		try {
			connection = DBConnection.getConnection();
			queries = new PersonQueries(connection);
			list = queries.getAllPeople();
		} catch (SQLException e){
			
		}
		
		JFrame frame = new JFrame();
		PersonFormPanel personPanel = new PersonFormPanel(connection);
		frame.getContentPane().add(personPanel);
		personPanel.displayPerson(list.get(0));
		
		//
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("Test Person Panel");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}