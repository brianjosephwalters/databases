package ui.swing.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import models.Job;
import models.queries.JobQueries;
import db.DBConnection;

@SuppressWarnings("serial")
public class JobMainPanel extends JPanel {
	private Connection connection;
	
	private JobQueries jobQueries;
	
	private List<Job> jobList;
	private Job currentJob;
	
	private PropertyChangeSupport pcS;
	
	private JobNavigationPanel navPanel;
	private JobFormPanel jobFormPanel;
	private JobSkillPanel jobSkillPanel;
	private JobCertificatePanel jobCertificatePanel;
	
	public JobMainPanel(Connection connection) {
		this.connection = connection;
		this.jobQueries = new JobQueries(connection);
		this.jobList = new ArrayList<Job>();
		
		pcS = new PropertyChangeSupport(this);
		
		try {
			jobList = jobQueries.getAllJobs();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		initializeGUIComponents();
		setCurrentJob(getJobList().get(0));
	}
	
	private void initializeGUIComponents() {
		// Setup Navigation Panel
		navPanel = new JobNavigationPanel(jobList);
		navPanel.addPropertyChangeListener(new NavigationListener());
		this.addPropertyChangeListener(navPanel.new ListListener());
		
		// Setup Info Panel
		jobFormPanel = new JobFormPanel(connection);
		this.addPropertyChangeListener(jobFormPanel.new JobListener());
		
		jobSkillPanel = new JobSkillPanel(connection);
		this.addPropertyChangeListener(jobSkillPanel.new JobListener());
		jobCertificatePanel = new JobCertificatePanel(connection);
		this.addPropertyChangeListener(jobCertificatePanel.new JobListener());
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(jobFormPanel);
		infoPanel.add(jobSkillPanel);
		infoPanel.add(jobCertificatePanel);
		
		// Setup Main Panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(10,10));
		mainPanel.add(navPanel, BorderLayout.NORTH);
		mainPanel.add(infoPanel, BorderLayout.CENTER);

		setLayout(new FlowLayout(10,10,10));
		add(mainPanel);
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
	
	public void setJobList(List<Job> newJobList) {
		List<Job> oldJobList = this.jobList;
		this.jobList = newJobList;
		pcS.firePropertyChange("jobList", oldJobList, newJobList);
	}
	
	public Job getCurrentJob() {
		return this.currentJob;
	}
	
	public void setCurrentJob(Job newJob) {
		Job oldJob = this.currentJob;
		this.currentJob = newJob;
		pcS.firePropertyChange("currentPerson", oldJob, newJob);
	}
	
	public class NavigationListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("currentIndex")) {
				int index = (Integer)evt.getNewValue();
				setCurrentJob(getJobList().get(index));
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
		frame.add(new JobMainPanel(connection));
		frame.setBounds(10, 10, 650, 500);
		frame.setTitle("Job Program");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
