package ui.swing.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import models.Job;
import models.Person;
import models.queries.PersonQueries;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class JobEligiblePeoplePanel extends JPanel {
	private Connection connection;
	private Job job;
	private PersonQueries personQueries;
	private List<Person> list;
	
	private ButtonController buttonController;
	
	private JTextArea taSkills;
	private JScrollPane scrollPane;
	
	private JButton editButton;

	public JobEligiblePeoplePanel(Connection connection) {
		this.connection = connection;
		this.personQueries = new PersonQueries(connection);
		this.list = new ArrayList<Person>();
		
		this.buttonController = new ButtonController();
		
		initializeGUIComponents();
	}
	
	public void initializeGUIComponents() {
		
		//Setup Data Panel
		editButton = new JButton("Edit");
		editButton.addActionListener(buttonController);
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		buttonPanel.add(editButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
			
		// Setup the Main Panel.
		setBorder(BorderFactory.createTitledBorder( 
			BorderFactory.createEtchedBorder(), "Eligible People"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Setup TextField
		scrollPane = new JScrollPane();
		taSkills = new JTextArea();
		taSkills.setLineWrap(true);
		taSkills.setWrapStyleWord(true);
		taSkills.setEditable(false);
		scrollPane.setViewportView(taSkills);
		
		add(scrollPane);
		add(buttonPanel);
	}

	public void displayJob(Job job) {
		this.job = job;
		setPersonTextArea();
	}
	
	private void generatePersonList() {
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
		try {
			this.list = personQueries.getPeopleFullyQualifiedForJob(jobCode, jobProfileCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setPersonTextArea() { 
		generatePersonList();		
		StringBuilder sb = new StringBuilder();
		for (Person person: list) {
			sb.append(person.getLastName() + ", " + person.getFirstName() + "\n");
		}
		taSkills.setText(sb.toString());
	}
		
	private class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			editButton();
		}
		
		private void editButton() {
			
		}
	}
	
	public class JobListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			displayJob((Job)evt.getNewValue());
		}
	}
	
	public class EditWindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			setPersonTextArea();
		}
	}
}
