package ui.swing.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import models.Person;
import models.queries.PersonQueries;
import db.DBConnection;

@SuppressWarnings("serial")
public class PersonMainPanel extends JPanel {
	private Connection connection;
	private PersonQueries personQueries;

	// Data Instance Variables
	private List<Person> personList;
	private Person currentPerson;

	private PropertyChangeSupport pcS;

	// GUI Instance Variables
	private PersonNavigationPanel navPanel;
	private PersonFormPanel personFormPanel;
	private PersonAddressPanel personAddressPanel;
	private PersonPhonePanel personPhonePanel;
	private PersonSkillPanel personSkillPanel;
	private PersonCertificatePanel personCertificatePanel;

	public PersonMainPanel(Connection connection) {
		this.connection = connection;
		personQueries = new PersonQueries(connection);
		
		pcS = new PropertyChangeSupport(this);
		
		try {
			personList = personQueries.getAllPeople();
		} catch (SQLException e) {
			System.out.println("Unable to get people!");
		}
		
		initializeGUIComponents();
		setCurrentPerson(getPersonList().get(0));
	}
	
	private void initializeGUIComponents() {
		// Setup Navigation Panel
		navPanel = new PersonNavigationPanel(personList);
		navPanel.addPropertyChangeListener(new NavigationListener());
		this.addPropertyChangeListener(navPanel.new ListListener());
		
		// Setup Info Panel
		personFormPanel = new PersonFormPanel(connection);
		this.addPropertyChangeListener(personFormPanel.new PersonListener());
		personAddressPanel = new PersonAddressPanel(connection);
		this.addPropertyChangeListener(personAddressPanel.new PersonListener());
		personPhonePanel = new PersonPhonePanel(connection);
		this.addPropertyChangeListener(personPhonePanel.new PersonListener());
		personSkillPanel = new PersonSkillPanel(connection);	
		this.addPropertyChangeListener(personSkillPanel.new PersonListener());
		personCertificatePanel = new PersonCertificatePanel(connection);
		this.addPropertyChangeListener(personCertificatePanel.new PersonListener());
		
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(personFormPanel);
		infoPanel.add(personAddressPanel);
		infoPanel.add(personPhonePanel);
		infoPanel.add(personSkillPanel);
		infoPanel.add(personCertificatePanel);
		
		// Setup Main Panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(10,10));
		mainPanel.add(navPanel, BorderLayout.NORTH);
		mainPanel.add(infoPanel, BorderLayout.CENTER);

		setLayout(new FlowLayout(10,10,10));
		add(mainPanel);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public List<Person> getPersonList() {
		return this.personList;
	}
	
	public void setPersonList(List<Person> newPersonList) {
		List<Person> oldPersonList = this.personList;
		this.personList = newPersonList;
		pcS.firePropertyChange("personList", oldPersonList, newPersonList);
	}
	
	public Person getCurrentPerson() {
		return this.currentPerson;
	}
	
	public void setCurrentPerson(Person newPerson) {
		Person oldPerson = this.currentPerson;
		this.currentPerson = newPerson;
		pcS.firePropertyChange("currentPerson", oldPerson, newPerson);
	}	
	
	public class NavigationListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("currentIndex")) {
				int index = (Integer)evt.getNewValue();
				setCurrentPerson(getPersonList().get(index));
			}
		}
	}

	public static void main (String[] args) {
		Connection connection = null;
		try {
			connection = DBConnection.getConnection2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame();
		frame.add(new PersonMainPanel(connection));
		frame.setBounds(10, 10, 650, 500);
		frame.setTitle("Person Program");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
