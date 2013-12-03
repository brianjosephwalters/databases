package ui.swing.panels.person;

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
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

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
	private PersonCoursePanel personCoursePanel;
	private PersonEmploymentPanel personEmploymentPanel;
	
	private JPanel leftPanel;
	private JPanel centerPanel;
	private JPanel panel;
	private JTabbedPane tabbedPane;

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
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.setLayout(new BorderLayout(0, 0));
		
		// Setup Info Panel
		personFormPanel = new PersonFormPanel(connection);
		infoPanel.add(personFormPanel, BorderLayout.NORTH);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		infoPanel.add(tabbedPane);
		
		leftPanel = new JPanel();
		tabbedPane.addTab("Person Info", null, leftPanel, null);
		leftPanel.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		leftPanel.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		personPhonePanel = new PersonPhonePanel(connection);
		panel.add(personPhonePanel);
		personAddressPanel = new PersonAddressPanel(connection);
		panel.add(personAddressPanel);
		
		centerPanel = new JPanel();
		tabbedPane.addTab("Qualifications", null, centerPanel, null);
		centerPanel.setLayout(new GridLayout(5, 1, 0, 0));
		personSkillPanel = new PersonSkillPanel(connection);	
		centerPanel.add(personSkillPanel);
		personCertificatePanel = new PersonCertificatePanel(connection);
		centerPanel.add(personCertificatePanel);
		
		personEmploymentPanel = new PersonEmploymentPanel(connection);
		tabbedPane.addTab("Employment History", null, personEmploymentPanel, null);
		
		personCoursePanel = new PersonCoursePanel(connection);
		tabbedPane.addTab("Course History", null, personCoursePanel, null);
		this.addPropertyChangeListener(personFormPanel.new PersonListener());
		this.addPropertyChangeListener(personAddressPanel.new PersonListener());
		this.addPropertyChangeListener(personPhonePanel.new PersonListener());
		
		// Setup Main Panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(10,10));
		mainPanel.add(navPanel, BorderLayout.NORTH);
		mainPanel.add(infoPanel, BorderLayout.CENTER);
		this.addPropertyChangeListener(personSkillPanel.new PersonListener());
		this.addPropertyChangeListener(personCertificatePanel.new PersonListener());
		this.addPropertyChangeListener(personCoursePanel.new PersonListener());
		this.addPropertyChangeListener(personEmploymentPanel.new PersonListener());

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
		frame.getContentPane().add(new PersonMainPanel(connection));
		frame.setBounds(60, 0, 600, 700);
		frame.setTitle("Person Program");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
