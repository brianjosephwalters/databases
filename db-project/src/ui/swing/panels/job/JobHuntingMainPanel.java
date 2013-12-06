package ui.swing.panels.job;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import ui.swing.panels.person.PersonCertificatePanel;
import ui.swing.panels.person.PersonCloselyQualifiedPanel;
import ui.swing.panels.person.PersonCurrentJobPanel;
import ui.swing.panels.person.PersonFormPanel;
import ui.swing.panels.person.PersonNavigationPanel2;
import ui.swing.panels.person.PersonQualifiedJobsPanel;
import ui.swing.panels.person.PersonSkillPanel;
import db.DBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class JobHuntingMainPanel extends JPanel {
	private Connection connection;
	
	// GUI Instance Variables
	private JPanel mainPanel;
	private PersonNavigationPanel2 navigationPanel;
	private PersonFormPanel personFormPanel;
	private PersonSkillPanel personSkillPanel;
	private PersonCertificatePanel personCertificatePanel;
	
	private PersonCurrentJobPanel personCurrentJobPanel;
	private PersonQualifiedJobsPanel personQualifiedJobsPanel;
	private PersonCloselyQualifiedPanel personCloselyQualifiedPanel;
	private JPanel panel;
	
	/**
	 * Create the panel.
	 */
	public JobHuntingMainPanel(Connection connection) {
		this.connection = connection;

		initializeGUIComponents();
	}
	
	private void initializeGUIComponents() {
		setLayout(new BorderLayout());
		initializeNavigationPanel();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,3));
		
		initializePersonPanel();
		initializeQualifiedPanel();
		initializeNearlyQualifiedPanel();
		add(navigationPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);	
	}
	
	private void initializeNavigationPanel() {
		navigationPanel = new PersonNavigationPanel2(connection);
	}
	
	private void initializePersonPanel() {
		JPanel personPanel = new JPanel();
		personPanel.setAlignmentX(TOP_ALIGNMENT);
		personPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Person Summary", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		personFormPanel = new PersonFormPanel(connection);
		navigationPanel.addPropertyChangeListener(personFormPanel.new PersonListener());
		personPanel.setLayout(new BorderLayout(0, 0));
		
		personPanel.add(personFormPanel, BorderLayout.NORTH);
		
		mainPanel.add(personPanel);
		
		panel = new JPanel();
		personPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		personSkillPanel = new PersonSkillPanel(connection);	
		navigationPanel.addPropertyChangeListener(personSkillPanel.new PersonListener());

		panel.add(personSkillPanel);
		personCertificatePanel = new PersonCertificatePanel(connection);
		navigationPanel.addPropertyChangeListener(personCertificatePanel.new PersonListener());

		panel.add(personCertificatePanel);
	}
	
	private void initializeQualifiedPanel() {
		JPanel jobQualifiedPanel = new JPanel();
		jobQualifiedPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Jobs Qualified", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		jobQualifiedPanel.setLayout(new BoxLayout(jobQualifiedPanel, BoxLayout.Y_AXIS));
		
		personCurrentJobPanel = new PersonCurrentJobPanel(connection);
		navigationPanel.addPropertyChangeListener(personCurrentJobPanel.new PersonListener());
		
		personQualifiedJobsPanel = new PersonQualifiedJobsPanel(connection);
		navigationPanel.addPropertyChangeListener(personQualifiedJobsPanel.new PersonListener());
		
		
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
		navigationPanel.addPropertyChangeListener(personCloselyQualifiedPanel.new PersonListener());
		jobNearlyQualifiedPanel.add(personCloselyQualifiedPanel);
	}
	
	public static void main (String[] args) {
		Connection connection = null;
		try {
			connection = DBConnection.getConnection2();
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
