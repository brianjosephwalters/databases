package ui.swing.panels.person;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import db.DBConnection;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class PersonMainPanel extends JPanel {
	private Connection connection;
	
	// GUI Instance Variables
	private PersonNavigationPanel2 navigationPanel;
	private PersonFormPanel personFormPanel;
	private PersonAddressPanel personAddressPanel;
	private PersonPhonePanel personPhonePanel;
	private PersonSkillPanel personSkillPanel;
	private PersonCertificatePanel personCertificatePanel;
	private PersonCoursePanel personCoursePanel;
	private PersonEmploymentPanel personEmploymentPanel;
	
	private PersonQualifiedJobsPanel personQualifiedJobsPanel;
	private PersonCloselyQualifiedPanel personCloselyQualifiedPanel;
	
	private JPanel personInfoTab;
	private JPanel qualificationsTab;
	private JPanel panel;
	private JTabbedPane tabbedPane;
	private JPanel employmentTab;
	private JPanel courseTab;
	private JPanel panel_1;
	private JPanel qualifiedTab;
	private JPanel closelyQualifiedTab;

	public PersonMainPanel(Connection connection) {
		this.connection = connection;
		
		initializeGUIComponents();
	}
	
	private void initializeGUIComponents() {
		setLayout(new BorderLayout(0, 0));
		
		// Setup Info Panel
		JPanel infoPanel = new JPanel();
		add(infoPanel, BorderLayout.CENTER);
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.setLayout(new BorderLayout(0, 0));
		
		
		personFormPanel = new PersonFormPanel(connection);
		infoPanel.add(personFormPanel, BorderLayout.NORTH);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		infoPanel.add(tabbedPane);
		
		personInfoTab = new JPanel();
		tabbedPane.addTab("Person Info", null, personInfoTab, null);
		personInfoTab.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		personInfoTab.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		personPhonePanel = new PersonPhonePanel(connection);
		panel.add(personPhonePanel);
		personAddressPanel = new PersonAddressPanel(connection);
		panel.add(personAddressPanel);
		
		qualificationsTab = new JPanel();
		tabbedPane.addTab("Qualifications", null, qualificationsTab, null);
		qualificationsTab.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		qualificationsTab.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		personSkillPanel = new PersonSkillPanel(connection);
		panel_1.add(personSkillPanel);
		personCertificatePanel = new PersonCertificatePanel(connection);
		panel_1.add(personCertificatePanel);
		
		employmentTab = new JPanel();
		tabbedPane.addTab("Employment History", null, employmentTab, null);
		employmentTab.setLayout(new BorderLayout(0, 0));
		
		personEmploymentPanel = new PersonEmploymentPanel(connection);
		employmentTab.add(personEmploymentPanel);
		
		courseTab = new JPanel();
		tabbedPane.addTab("Course History", null, courseTab, null);
		courseTab.setLayout(new BorderLayout(0, 0));
		
		personCoursePanel = new PersonCoursePanel(connection);
		courseTab.add(personCoursePanel);
		
		qualifiedTab = new JPanel();
		tabbedPane.addTab("Qualified Jobs", null, qualifiedTab, null);
		
		personQualifiedJobsPanel = new PersonQualifiedJobsPanel(connection);
		qualifiedTab.add(personQualifiedJobsPanel);
		
		closelyQualifiedTab = new JPanel();
		tabbedPane.addTab("Closely Qualified", null, closelyQualifiedTab, null);
		
		personCloselyQualifiedPanel = new PersonCloselyQualifiedPanel(connection);
		closelyQualifiedTab.add(personCloselyQualifiedPanel);
		
		// Setup Navigation Panel
		navigationPanel = new PersonNavigationPanel2(connection);
		add(navigationPanel, BorderLayout.NORTH);
		
		navigationPanel.addPropertyChangeListener(
			personFormPanel.new PersonListener());
		navigationPanel.addPropertyChangeListener(
			personAddressPanel.new PersonListener());
		navigationPanel.addPropertyChangeListener(
			personPhonePanel.new PersonListener());
		navigationPanel.addPropertyChangeListener(
			personSkillPanel.new PersonListener());
		navigationPanel.addPropertyChangeListener(
			personCertificatePanel.new PersonListener());
		navigationPanel.addPropertyChangeListener(
			personCoursePanel.new PersonListener());
		navigationPanel.addPropertyChangeListener(
			personEmploymentPanel.new PersonListener());
		navigationPanel.addPropertyChangeListener(
			personQualifiedJobsPanel.new PersonListener());
		navigationPanel.addPropertyChangeListener(
			personCloselyQualifiedPanel.new PersonListener());
		
		navigationPanel.resetPersonList();
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
