package ui.swing.panels.job;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.swing.panels.DeletePersonDialog;
import ui.swing.panels.EditPersonDialog;
import ui.swing.panels.NewPersonDialog;

import models.Job;
import models.Person;
import models.queries.JobQueries;
import models.queries.PersonQueries;

import java.awt.Component;

@SuppressWarnings("serial")
public class JobNavigationPanel2 extends JPanel {
	Connection connection;
	NavigationController controller;
	
	JobQueries jobQueries;
	
	List<Job> jobList;
	Job currentJob;
	int currentIndex;
	
	PropertyChangeSupport pcS;

	JButton btnDelete;
	JButton btnPrevious;
	JButton btnNext;
	JButton btnNew;
	JTextField tfIndex;
	JTextField tfMax;
	private JButton btnEdit;
	private Component horizontalStrut;

	/**
	 * Create the panel.
	 */
	public JobNavigationPanel2(Connection connection) {
		this.connection = connection;
		this.jobQueries = new JobQueries(connection);
		
		pcS = new PropertyChangeSupport(this);
		
		this.jobList = new ArrayList<Job>();
		this.currentJob = null;
		this.currentIndex = 0;
		
		
		this.controller = new NavigationController();
		
		initializeGUIComponents();
		resetJobList();
	}
	
	private void initializeGUIComponents() {
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(controller);
		
		btnPrevious = new JButton("Previous");
		btnPrevious.addActionListener(controller);
		
		tfIndex = new JTextField();
		tfIndex.setHorizontalAlignment(JTextField.CENTER);
		tfIndex.addActionListener(controller);
		
		tfMax = new JTextField();
		tfMax.setHorizontalAlignment(JTextField.CENTER);
		tfMax.setEditable(false);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(controller);
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(controller);
		
		btnNew = new JButton("New");
		btnNew.addActionListener(controller);
		
		setLayout(new BoxLayout( this, BoxLayout.X_AXIS));
		add(Box.createHorizontalGlue());
		add(btnDelete);
		add(Box.createHorizontalStrut(10));
		add(btnPrevious);
		add(Box.createHorizontalStrut(10));
		add(tfIndex);
		add(Box.createHorizontalStrut(10));
		add(new JLabel("of"));
		add(tfMax);
		add(Box.createHorizontalStrut(10));
		add(btnNext);
		add(Box.createHorizontalStrut(10));
		add(btnNew);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		add(horizontalStrut);
		
		
		add(btnEdit);
		add(Box.createHorizontalGlue());
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public List<Job> getJobList() {
		return this.jobList;
	}
	
	public void setJobList(List<Job> newList) {
		List<Job> oldList = this.jobList;
		this.jobList = newList;
		pcS.firePropertyChange("currentList", oldList, newList);
	}
	
	public Job getCurrentJob() {
		return this.currentJob;
	}
	
	public void setCurrentJob(Job newJob) {
		Job oldJob = this.currentJob;
		this.currentJob = newJob;
		pcS.firePropertyChange("currentJob", oldJob, newJob);
	}
	
	private List<Job> generateJobList() {
		List<Job> list = new ArrayList<Job>();
		try {
			list = jobQueries.getAllJobs();
		} catch (SQLException e) {
			System.out.println("Unable to get people!");
		}
		return list;
	}
	
	private void updateJobList(int index) {
		setJobList(generateJobList());
		setCurrentIndex(index);
		setCurrentJob(jobList.get(currentIndex));
	}
	
	public void resetJobList() {
		setJobList(generateJobList());
		setCurrentIndex(0);
		setCurrentJob(jobList.get(currentIndex));
	}
	
	private void setCurrentIndex(int index) {
		this.currentIndex = index;
		tfIndex.setText("" + (index + 1));
		tfMax.setText("" + jobList.size());
	}
	
	private int getCurrentIndex() {
		return this.currentIndex;
	}
	
	private class NavigationController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnDelete) {
				deleteButton();
			} else if (e.getSource() == btnPrevious) {
				prevButton();
			} else if (e.getSource() == btnNext) {
				nextButton();
			} else if (e.getSource() == btnNew) {
				newButton();
			} else if (e.getSource() == btnEdit) {
				editButton();
			}
			else if (e.getSource() == tfIndex) {
				indexUpdate();
			} 
		}
		
		private void deleteButton() {
//			Job job = getCurrentJob();
//			JDialog dialog = new DeleteJobDialog(connection, job);
//			dialog.addWindowListener(new DeleteWindowListener());
		}
		
		private void prevButton() {
			if (getCurrentIndex() - 1 < 0) {
				setCurrentIndex(jobList.size() - 1);
			} else {
				setCurrentIndex(getCurrentIndex() - 1);
			}
			setCurrentJob(jobList.get(getCurrentIndex()));
		}
		
		private void nextButton() {
			if (getCurrentIndex() + 1 == jobList.size()) {
				setCurrentIndex(0);
			} else {
				setCurrentIndex(getCurrentIndex() + 1);
			}
			setCurrentJob(jobList.get(getCurrentIndex()));
		}
		
		private void newButton () {
			JDialog dialog = new NewPersonDialog(connection);
			dialog.addWindowListener(new NewDialogListener());
		}
		
		private void editButton() {
//			Job job = getCurrentJob();
//			JDialog dialog = new EditJobDialog(connection, job);
//			dialog.addWindowListener(new EditWindowListener());
		}
		
		private void indexUpdate() {
			int index = Integer.parseInt(tfIndex.getText()) - 1;
			if (index >= 0 && index < jobList.size()) {
				setCurrentIndex(index);
			}
			setCurrentJob(jobList.get(getCurrentIndex()));
		}
	}
	
	public class EditWindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			updateJobList(currentIndex);
		}
	}
	
	public class NewDialogListener extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			updateJobList(currentIndex);
		}
	}
	
	public class DeleteWindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			updateJobList(currentIndex);
		}
	}

}
