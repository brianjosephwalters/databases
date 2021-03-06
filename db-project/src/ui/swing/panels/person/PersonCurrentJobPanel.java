package ui.swing.panels.person;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import models.Job;
import models.Person;
import models.queries.JobQueries;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class PersonCurrentJobPanel extends JPanel {
	private Connection connection;
	private Person person;
	private JobQueries jobQueries;
	private List<Job> list;
	
	private ButtonController buttonController;
	private CheckBoxController checkBoxController;
	
	private JTextArea taJobs;
	private JScrollPane scrollPane;
	private JButton editButton;
	private JCheckBox chckbxShowAll;
	
	public PersonCurrentJobPanel (Connection connection) {
		this.connection = connection;
		this.jobQueries = new JobQueries(connection);
		this.list = new ArrayList<Job>();
		
		this.buttonController = new ButtonController();
		this.checkBoxController = new CheckBoxController();
		
		initializeGUIComponents();
	}

	private void initializeGUIComponents() {
		//Setup Data Panel
		editButton = new JButton("Edit");
		editButton.addActionListener(buttonController);
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		
		chckbxShowAll = new JCheckBox("Show All");
		chckbxShowAll.addActionListener(checkBoxController);
		
		buttonPanel.add(chckbxShowAll);
		buttonPanel.add(editButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		
		// Setup the Main Panel.
		setBorder(BorderFactory.createTitledBorder( 
			BorderFactory.createEtchedBorder(), "Job Held"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		scrollPane = new JScrollPane();
		taJobs = new JTextArea();
		taJobs.setLineWrap(false);
		taJobs.setWrapStyleWord(false);
		taJobs.setEditable(false);
		scrollPane.setViewportView(taJobs);
		
		add(scrollPane);
		add(buttonPanel);
	}
	
	private void displayPerson(Person person) {
		this.person = person;
		setJobsTextArea();
	}
	
	private void generateJobList() {
		String personCode = person.getPersonCode();
		if (!chckbxShowAll.isSelected()) {
			try {
				this.list = jobQueries.getCurrentJobsOfPerson(personCode);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		} else {
			try {
				this.list = jobQueries.getJobsOfPerson(personCode);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setJobsTextArea() {
		generateJobList();
		StringBuilder sb = new StringBuilder();
		for (Job job : list) {
			sb.append(job.getJobProfileTitle() + " at " + job.getCompanyName() + "\n");
		}
		taJobs.setText(sb.toString());
	}
	
	private class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
				editButton();
		}
		
		private void editButton() {
			
		}
	}
	
	private class CheckBoxController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			setJobsTextArea();
		}
		
	}
	
	public class PersonListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("currentPerson")) {
				displayPerson((Person)evt.getNewValue());
			}
		}
	}
}
