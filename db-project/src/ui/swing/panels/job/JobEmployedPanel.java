package ui.swing.panels.job;

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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import models.Job;
import models.Person;
import models.queries.PersonQueries;

@SuppressWarnings("serial")
public class JobEmployedPanel extends JPanel {
	private Connection connection;
	private PersonQueries personQueries;
	private Job job;
	private List<Person> list;
	
	private ButtonController buttonController;
	private CheckBoxController checkBoxController;
	
	private JTextArea taJobs;
	private JScrollPane scrollPane;
	private JButton editButton;
	private JCheckBox chckbxShowAll;
	
	public JobEmployedPanel(Connection connection){
		this.connection = connection;
		this.personQueries = new PersonQueries(connection);
		this.list = new ArrayList<Person>();
		
		this.buttonController = new ButtonController();
		this.checkBoxController = new CheckBoxController();
		
		initializeGUIComponents();
	}
	
	private void initializeGUIComponents () {
		//Setup Data Panel
		editButton = new JButton("Edit");
		editButton.addActionListener(buttonController);
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		
		chckbxShowAll = new JCheckBox("Show History");
		chckbxShowAll.addActionListener(checkBoxController);
		
		buttonPanel.add(chckbxShowAll);
		buttonPanel.add(editButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		
		// Setup the Main Panel.
		setBorder(BorderFactory.createTitledBorder( 
			BorderFactory.createEtchedBorder(), "Current Employment"));
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
	
	private void displayJob(Job job) {
		this.job = job;
		setEmployedTextArea();
	}
	
	private void generateEmployeeList() {
		String jobCode = job.getJobCode();
		if (!chckbxShowAll.isSelected()) {
			try {
				this.list = personQueries.getCurrentPersonEmployedAtJob(jobCode);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		} else {
			try {
				this.list = personQueries.getPeopleEmployedAtJob(jobCode);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void setEmployedTextArea() {
		generateEmployeeList();
		StringBuilder sb = new StringBuilder();
		for (Person person : list) {
			sb.append(person.getLastName() + ", " + person.getFirstName() + "\n");
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
			setEmployedTextArea();
		}
		
	}
	
	public class JobListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			displayJob((Job)evt.getNewValue());
		}
	}
}
