package ui.swing.panels.person;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DBConnection;
import models.Person;
import models.queries.PersonQueries;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;

import java.awt.Component;

@SuppressWarnings("serial")
public class PersonFormPanel extends JPanel{
	Connection connection;
	Person person;
		
	private JTextField tfPersonCode;
	private JTextField tfLastName;
	private JTextField tfFirstName;
	private JTextField tfGender;
	private JTextField tfEmail;
	
	private JPanel panel;
	private Component horizontalStrut;
	private Component horizontalStrut_1;
	
	public PersonFormPanel(Connection connection) {
		this.connection = connection;
		
		initializeGUIComponents();
	}
	
	private void initializeGUIComponents() {
		initializeDisplayPanel();
	}
	
	private void initializeDisplayPanel() {
		setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		horizontalStrut = Box.createHorizontalStrut(5);
		panel.add(horizontalStrut);
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
	
	public class PersonListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("currentPerson")) {
				setPerson((Person)evt.getNewValue());
			} else if (evt.getPropertyName().equals("currentList")) {
				
			}
		}
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
			System.out.println("Unable to connect to database");
		}
		
		JFrame frame = new JFrame();
		PersonFormPanel personPanel = new PersonFormPanel(connection);
		frame.getContentPane().add(personPanel);
		personPanel.setPerson(list.get(0));
		
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("Test Person Panel");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}