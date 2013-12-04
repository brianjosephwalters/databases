package ui.swing.panels.job;

import java.awt.BorderLayout;
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
	private JobEmployedPanel jobEmployedPanel;
	private JobEligiblePeoplePanel jobEligiblePeoplePanel;
	private JPanel jobHolderPanel;
	
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
		jobSkillPanel = new JobSkillPanel(connection);
		jobCertificatePanel = new JobCertificatePanel(connection);
		this.addPropertyChangeListener(jobFormPanel.new JobListener());
		this.addPropertyChangeListener(jobSkillPanel.new JobListener());
		this.addPropertyChangeListener(jobCertificatePanel.new JobListener());
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(jobFormPanel);
		infoPanel.add(jobSkillPanel);
		infoPanel.add(jobCertificatePanel);
		
		// Setup Job Holder Panel
		jobEmployedPanel = new JobEmployedPanel(connection);
		this.addPropertyChangeListener(jobEmployedPanel.new JobListener());
		jobEligiblePeoplePanel = new JobEligiblePeoplePanel(connection);
		this.addPropertyChangeListener(jobEligiblePeoplePanel.new JobListener());
		
		jobHolderPanel = new JPanel();
		jobHolderPanel.setLayout(new BoxLayout(jobHolderPanel, BoxLayout.Y_AXIS));
		jobHolderPanel.add(jobEmployedPanel);
		jobHolderPanel.add(jobEligiblePeoplePanel);
		
		
		// Setup Main Panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(infoPanel);
		mainPanel.add(jobHolderPanel);		

		setLayout(new BorderLayout(0, 0));
		
		
		
		add(navPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
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
			connection = DBConnection.getConnection2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame();
		frame.getContentPane().add(new JobMainPanel(connection));
		frame.setBounds(10, 10, 650, 500);
		frame.setTitle("Job Program");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
