package ui.swing.panels;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import ui.swing.panels.person.PersonAddressPanel;
import ui.swing.panels.person.PersonCertificatePanel;
import ui.swing.panels.person.PersonCloselyQualifiedPanel;
import ui.swing.panels.person.PersonCurrentJobPanel;
import ui.swing.panels.person.PersonFormPanel;
import ui.swing.panels.person.PersonNavigationPanel;
import ui.swing.panels.person.PersonPhonePanel;
import ui.swing.panels.person.PersonQualifiedJobsPanel;
import ui.swing.panels.person.PersonSkillPanel;
import models.Person;
import models.queries.PersonQueries;
import db.DBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("serial")
public class JobHuntingMainPanel extends JPanel {
	Connection connection;
	private PersonQueries personQueries;
	
	// Data Instance Variables
	List<Person> personList;
	Person currentPerson;
	
	PropertyChangeSupport pcS;
	
	// GUI Instance Variables
	JPanel mainPanel;
	PersonNavigationPanel navPanel;
	PersonFormPanel personFormPanel;
	PersonAddressPanel personAddressPanel;
	PersonPhonePanel personPhonePanel;
	PersonSkillPanel personSkillPanel;
	PersonCertificatePanel personCertificatePanel;
	
	PersonCurrentJobPanel personCurrentJobPanel;
	PersonQualifiedJobsPanel personQualifiedJobsPanel;
	PersonCloselyQualifiedPanel personCloselyQualifiedPanel;
	
	/**
	 * Create the panel.
	 */
	public JobHuntingMainPanel(Connection connection) {
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
		setLayout(new BorderLayout());
		initializeNavigationPanel();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,3));
		
		initializePersonPanel();
		initializeQualifiedPanel();
		initializeNearlyQualifiedPanel();
		add(navPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);	
	}
	
	private void initializeNavigationPanel() {
		navPanel = new PersonNavigationPanel(personList);
		navPanel.addPropertyChangeListener(new NavigationListener());
		this.addPropertyChangeListener(navPanel.new ListListener());
	}
	
	private void initializePersonPanel() {
		JPanel personPanel = new JPanel();
		personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.Y_AXIS));
		personPanel.setAlignmentX(TOP_ALIGNMENT);
		personPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Person Summary", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		personFormPanel = new PersonFormPanel(connection);
		this.addPropertyChangeListener(personFormPanel.new PersonListener());
		personSkillPanel = new PersonSkillPanel(connection);	
		this.addPropertyChangeListener(personSkillPanel.new PersonListener());
		personCertificatePanel = new PersonCertificatePanel(connection);
		this.addPropertyChangeListener(personCertificatePanel.new PersonListener());
		
		personPanel.add(personFormPanel);
		personPanel.add(personSkillPanel);
		personPanel.add(personCertificatePanel);
		
		mainPanel.add(personPanel);
	}
	
	private void initializeQualifiedPanel() {
		JPanel jobQualifiedPanel = new JPanel();
		jobQualifiedPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Jobs Qualified", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		jobQualifiedPanel.setLayout(new BoxLayout(jobQualifiedPanel, BoxLayout.Y_AXIS));
		
		personCurrentJobPanel = new PersonCurrentJobPanel(connection);
		this.addPropertyChangeListener(personCurrentJobPanel.new PersonListener());
		
		personQualifiedJobsPanel = new PersonQualifiedJobsPanel(connection);
		this.addPropertyChangeListener(personQualifiedJobsPanel.new PersonListener());
		
		
		jobQualifiedPanel.add(personCurrentJobPanel);
		jobQualifiedPanel.add(personQualifiedJobsPanel);
		mainPanel.add(jobQualifiedPanel);
	}
	
	private void initializeNearlyQualifiedPanel() {
		JPanel jobNearlyQualifiedPanel = new JPanel();
		jobNearlyQualifiedPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Jobs Closely Qualified", TitledBorder.RIGHT, TitledBorder.TOP, null, null));
		mainPanel.add(jobNearlyQualifiedPanel);
		jobNearlyQualifiedPanel.setLayout(new BoxLayout(jobNearlyQualifiedPanel, BoxLayout.Y_AXIS));
		
		personCloselyQualifiedPanel = new PersonCloselyQualifiedPanel(connection);
		this.addPropertyChangeListener(personCloselyQualifiedPanel.new PersonListener());
		jobNearlyQualifiedPanel.add(personCloselyQualifiedPanel);
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
			connection = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(new JobHuntingMainPanel(connection));
		frame.setBounds(10, 10, 650, 600);
		frame.setTitle("Job Hunting Program");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
