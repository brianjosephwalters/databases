package ui.swing.panels;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import models.JobReadable;
import models.Person;
import models.queries.JobQueries;
import models.queries.PersonQueries;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class EditPersonEmployment extends JPanel {
	private Connection connection;
	
	private JobQueries jobQueries;
	private PersonQueries personQueries;
	
	private ButtonController buttonController;
	private CheckBoxController checkBoxController;
	private ComboBoxController comboBoxController;
	
	private List<Person> personList;
	private List<JobReadable> jobList;
	
	private DefaultComboBoxModel<Person> cbPersonModel;
	private DefaultListModel<JobReadable> lJobModel;
	
	private JComboBox<Person> cbPerson;
	private JList<JobReadable> lJobList;
	
	
	private JCheckBox chkbxShowAll;
	private JButton btnHire;
	private JCheckBox chckbxEnablePerson;
	private JButton btnCancel;
	
	/**
	 * Create the panel.
	 */
	public EditPersonEmployment(Connection connection, Person person) {
		this.connection = connection;
		this.jobQueries = new JobQueries(connection);
		this.personQueries = new PersonQueries(connection);
		
		this.buttonController = new ButtonController();
		this.checkBoxController = new CheckBoxController();
		this.comboBoxController = new ComboBoxController();
		
		personList = new ArrayList<Person>();
		jobList = new ArrayList<JobReadable>();
		
		cbPersonModel = new DefaultComboBoxModel<Person>();
		lJobModel = new DefaultListModel<JobReadable>();
		
		initializeGUIComponents();
		displayPersonComboBox();
		setPerson(person);
		displayJobList();
	}
	
	public void initializeGUIComponents() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		cbPerson = new JComboBox<Person>();
		cbPerson.setEnabled(false);
		cbPerson.addActionListener(comboBoxController);
		panel.add(cbPerson, BorderLayout.NORTH);
		
		chckbxEnablePerson = new JCheckBox("Enable Person");
		chckbxEnablePerson.addActionListener(checkBoxController);
		panel.add(chckbxEnablePerson);
		
		JPanel panel_1 = new JPanel();
		add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		lJobList = new JList<JobReadable>();
		lJobList.setModel(lJobModel);
		panel_1.add(lJobList);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.SOUTH);
		
		chkbxShowAll = new JCheckBox("Show All");
		chkbxShowAll.addActionListener(checkBoxController);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(buttonController);
		panel_2.add(btnCancel);
		panel_2.add(chkbxShowAll);
		
		btnHire = new JButton("Hire!");
		btnHire.addActionListener(buttonController);
		panel_2.add(btnHire);
	}
	
	public void setPerson(Person person) {
		for (int i = 0; i < cbPersonModel.getSize(); i++) {
			if (cbPersonModel.getElementAt(i).getPersonCode().equals(person.getPersonCode())) {
				cbPerson.setSelectedIndex(i);
			}
		}
	}
	
	private void generatePersonList() {
		try {
			personList = personQueries.getAllPeople();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void displayPersonComboBox() {
		generatePersonList();
		cbPersonModel = new DefaultComboBoxModel<Person>();
		for (Person person : personList) {
			cbPersonModel.addElement(person);
		}
		cbPerson.setModel(cbPersonModel);
	}
	
	private void generateJobList() {
		String personCode = ((Person)cbPerson.getSelectedItem()).getPersonCode();
		if (!chkbxShowAll.isSelected()) {
			try {
				jobList = jobQueries.getAvailableJobsQualifiedForByPerson(personCode);
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		} else {
			try {
				jobList = jobQueries.getAvailableJobsReadable();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
	}
	
	private void displayJobList() {
		generateJobList();
		lJobModel.clear();
		
		for (JobReadable job : jobList) {
			lJobModel.addElement(job);
		}
		lJobList.setModel(lJobModel);
		
	}
	
	private void close() {
		Window window = SwingUtilities.getWindowAncestor(this);
		window.dispose();
	}
	
	private class ComboBoxController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			displayJobList();
		}
		
	}
	
	private class ButtonController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnHire) {
				hireButton();
			} else if (e.getSource() == btnCancel) {
				close();
			}
		}
		public void hireButton() {
			String personCode = ((Person)cbPerson.getSelectedItem()).getPersonCode();
			String jobCode = lJobList.getSelectedValue().getJobCode();
			try {
				jobQueries.hirePerson(jobCode, personCode);
			} catch (SQLException e) {
				
			}
		}
	}
	
	private class CheckBoxController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == chkbxShowAll) {
				displayJobList();
			} else if (e.getSource() == chckbxEnablePerson) {
				if (chckbxEnablePerson.isSelected()) {
					cbPerson.setEnabled(true);
				}
				if (!chckbxEnablePerson.isSelected()) {
					cbPerson.setEnabled(false);
				}
				displayJobList();
			}
		}
	}
}
