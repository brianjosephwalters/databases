package ui.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import ui.swing.panels.PersonFormPanel.PersonListener;
import models.Job;
import models.JobReadable;
import models.Person;
import models.queries.JobQueries;
import models.queries.PersonQueries;
import javax.swing.JScrollPane;


public class AddPersonJobPanel extends JPanel {
	private Connection connection;
	private Person person;
	private PropertyChangeSupport pcS;
	
	private List<Person> personList;
	private List<JobReadable> jobList;
	
	private PersonQueries personQueries;
	private JobQueries jobQueries;
	
	private ComboBoxController comboboxController;
	private ListController listController;
	private ButtonController buttonController;
	
	private JComboBox<Person> cbPerson;
	private DefaultComboBoxModel<Person> cbPersonModel;
	private PersonFormPanel personFormPanel;
	private JList<JobReadable> lJobs;
	private DefaultListModel<JobReadable> lJobsModel;
	private JButton btnOkay;
	private JButton btnCancel;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JPanel panel_1;
	
	
	public AddPersonJobPanel (Connection connection, Person person) {
		this.connection = connection;
		
		this.personList = new ArrayList<Person>();
		this.jobList = new ArrayList<JobReadable>();
		this.personQueries = new PersonQueries(connection);
		this.jobQueries = new JobQueries(connection);
		
		this.comboboxController = new ComboBoxController();
		this.listController = new ListController();
		this.buttonController = new ButtonController();
		
		initializeGUIComponents();
		setPerson(person);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public Person getPerson() {
		return this.person;
	}
	
	public void setPerson(Person newPerson) {
		Person oldPerson = this.person;
		this.person = newPerson;
		pcS.firePropertyChange("person", oldPerson, newPerson);
	}	
	
	private void initializeGUIComponents() {
		cbPersonModel = new DefaultComboBoxModel<Person>();
		this.addPropertyChangeListener(personFormPanel.new PersonListener());
		
		lJobsModel = new DefaultListModel<JobReadable>();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		panel = new JPanel();
		add(panel);
		cbPerson = new JComboBox<Person>();
		cbPerson.setEnabled(false);
		
		personFormPanel = new PersonFormPanel(connection);
		
		JPanel personPanel = new JPanel();
		panel.add(personPanel);
		personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.Y_AXIS));
		personPanel.add(cbPerson);
		personPanel.add(personFormPanel);
		cbPerson.setSelectedItem(person);
		
		panel_1 = new JPanel();
		add(panel_1);
		
		btnOkay = new JButton("Okay");
		btnCancel = new JButton("Cancel");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(btnOkay);
		buttonPanel.add(btnCancel);
		
		JPanel jobPanel = new JPanel();
		panel_1.add(jobPanel);
		jobPanel.setLayout(new BoxLayout(jobPanel, BoxLayout.Y_AXIS));
		
		scrollPane = new JScrollPane();
		jobPanel.add(scrollPane);
		lJobs = new JList<JobReadable>();
		scrollPane.setViewportView(lJobs);
		lJobs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jobPanel.add(buttonPanel);
		
		updatePersonComboBox();
		updateJobList();
	}
	
	private void generatePersonList() {
		try {
			personList = personQueries.getAllPeople();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updatePersonComboBox() {
		generatePersonList();
		cbPersonModel = new DefaultComboBoxModel<Person>();
		for (Person person : personList) {
			cbPersonModel.addElement(person);
		}
		cbPerson.setModel(cbPersonModel);
	}
	
	private void generateJobList() {
		try {
			jobList = jobQueries.getAvailableJobsReadable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateJobList() {
		generateJobList();
		lJobsModel.clear();
		for (JobReadable job : jobList) {
			lJobsModel.addElement(job);
		}
	}
	
	private class ComboBoxController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cbPerson) {
				Person person = (Person)cbPerson.getSelectedItem(); 
				setPerson(person);
			}
		}
	}
	
	private class ListController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnOkay) {
				
			}
			else if (e.getSource() == btnCancel) {
				
			}
		}
	}
	
}
