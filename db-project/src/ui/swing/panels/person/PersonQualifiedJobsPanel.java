package ui.swing.panels.person;

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
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import models.Job;
import models.Person;
import models.queries.JobQueries;

import javax.swing.JCheckBox;

import ui.swing.panels.EditPersonEmployment;
import ui.swing.panels.EditPersonSkillsPanel;
import ui.swing.panels.person.PersonSkillPanel.EditWindowListener;

import java.awt.Component;

@SuppressWarnings("serial")
public class PersonQualifiedJobsPanel extends JPanel{
	private Connection connection;
	private Person person;
	private JobQueries jobQueries;
	private List<Job> list;
	
	private CheckBoxController checkBoxController;
	private ButtonController buttonController;
	
	private JTextArea taJobs;
	private JScrollPane scrollPane;
	private JCheckBox chckbxShowAll;
	private JButton btnHire;
	
	public PersonQualifiedJobsPanel (Connection connection) {
		this.connection = connection;
		this.jobQueries = new JobQueries(connection);
		this.list = new ArrayList<Job>();
		this.checkBoxController = new CheckBoxController();
		this.buttonController = new ButtonController();
		
		initializeGUIComponents();
	}

	private void initializeGUIComponents() {
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		
		chckbxShowAll = new JCheckBox("Include Not Available");
		chckbxShowAll.addActionListener(checkBoxController);
		
		buttonPanel.add(chckbxShowAll);
		
		btnHire = new JButton("Hire!");
		btnHire.addActionListener(buttonController);
		buttonPanel.add(btnHire);
		buttonPanel.add(Box.createHorizontalStrut(10));
		
		// Setup the Main Panel.
		setBorder(BorderFactory.createTitledBorder( 
			BorderFactory.createEtchedBorder(), "Qualified For"));
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
				this.list = jobQueries.getAvailableJobsQualifiedForByPerson(personCode);
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		} else {
			try {
				this.list = jobQueries.getJobsQualifiedForByPerson(personCode);
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
		public void actionPerformed(ActionEvent arg0) {
			hirePerson();
		}
		
		public void hirePerson() {
			JDialog dialog = new JDialog();
			dialog.getContentPane().add(new EditPersonEmployment(connection, person));
			dialog.setBounds(100, 100, 550, 400);
			dialog.setTitle("Hire!");
			dialog.setVisible(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.addWindowListener(new EditWindowListener());
			dialog.setModal(true);
		}	
	}
	
	private class CheckBoxController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
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
	
	public class EditWindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			setJobsTextArea();
		}
	}

}
