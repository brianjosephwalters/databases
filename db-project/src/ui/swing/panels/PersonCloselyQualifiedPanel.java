package ui.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import models.Certificate;
import models.Course;
import models.Exam;
import models.Job;
import models.JobReadable;
import models.Person;
import models.Skill;
import models.queries.CertificateQueries;
import models.queries.CourseQueries;
import models.queries.ExamTypeQueries;
import models.queries.JobQueries;
import models.queries.SkillQueries;

@SuppressWarnings("serial")
public class PersonCloselyQualifiedPanel extends JPanel {
	private Connection connection;
	private JobQueries jobQueries;
	private SkillQueries skillQueries;
	private CourseQueries courseQueries;
	private CertificateQueries certificateQueries;
	private ExamTypeQueries examTypeQueries;
	private Person person;
	private Job job;
	private List<JobReadable> listJobs;
	private List<Skill> listMissingSkills;
	private List<Course> listCoursesForSkills;
	private List<Certificate> listMissingCertificates;
	private List<Course> listCoursesForCertificates;
	private List<Exam> listExamsForCertificates;
	
	private ComboBoxController cbController;
	
	private JComboBox<JobReadable> cbJobs;
	private DefaultComboBoxModel<JobReadable> cbJobsModel;
	
	private JTextArea taMissingSkills;
	private JTextArea taCoursesForSkills;
	private JTextArea taMissingCertificates;
	private JTextArea taCoursesForCertificates;
	private JTextArea taExamsForCertificates;
	
	PersonCloselyQualifiedPanel(Connection connection) {
		this.connection = connection;
		this.jobQueries = new JobQueries(connection);
		this.skillQueries = new SkillQueries(connection);
		this.courseQueries = new CourseQueries(connection);
		this.certificateQueries = new CertificateQueries(connection);
		this.examTypeQueries = new ExamTypeQueries(connection);
		this.listJobs = new ArrayList<JobReadable>();
		this.person = null;
		
		this.listMissingSkills = new ArrayList<Skill>();
		this.listCoursesForSkills = new ArrayList<Course>();
		this.listMissingCertificates = new ArrayList<Certificate>();
		this.listCoursesForCertificates = new ArrayList<Course>();
		this.listExamsForCertificates = new ArrayList<Exam>();
		
		this.cbController = new ComboBoxController();
	
		intializeGUIController();
	}
	
	private void intializeGUIController() {
		cbJobs = new JComboBox<JobReadable>();
		cbJobs.addActionListener(cbController);
		cbJobsModel = new DefaultComboBoxModel<JobReadable>();
		
		taMissingSkills = new JTextArea();
		taCoursesForSkills = new JTextArea();
		taMissingCertificates = new JTextArea();
		taCoursesForCertificates = new JTextArea();
		taExamsForCertificates = new JTextArea();
		
		taMissingSkills.setEditable(false);
		taCoursesForSkills.setEditable(false);
		taMissingCertificates.setEditable(false);
		taCoursesForCertificates.setEditable(false);
		taExamsForCertificates.setEditable(false);
		
	}
	
	private void clearJobs() {
		
	}
	
	private void displayPersonJob(JobReadable jobReadable) {
		try {
			this.job = jobQueries.getJob(jobReadable.getJobCode()).get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		displayMissingSkills();
		displayCoursesForSkills();
		displayMissingCertificates();
		generateMissingCoursesForCertificatesList();
		generateMissingExamsForCertificatesList();
	}
	
	private void generateMissingSkillsList() {
		String personCode = person.getPersonCode();
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
		
		try {
			this.listMissingSkills = skillQueries.getSkillsMissingFromPersonForJob(personCode, jobCode, jobProfileCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void displayMissingSkills() {
		generateMissingSkillsList();
		StringBuilder sb = new StringBuilder();
		for (Skill skill : listMissingSkills) {
			sb.append(skill.getSkillName() + ", ");
		}
		taMissingSkills.setText(sb.toString());
	}
	
	private void generateCoursesForSkillsList() {
		String personCode = person.getPersonCode();
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
		
	}
	
	private void displayCoursesForSkills() {
		generateCoursesForSkillsList();
	}
	
	private void generateMissingCertificatesList() {
		String personCode = person.getPersonCode();
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
	}
	
	private void displayMissingCertificates() {
		generateMissingCertificatesList();
	}
	
	private void generateMissingCoursesForCertificatesList() {
		String personCode = person.getPersonCode();
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
	}

	private void displayMissingCoursesForCertificates() {
		generateMissingCoursesForCertificatesList();
	}
	
	private void generateMissingExamsForCertificatesList() {
		String personCode = person.getPersonCode();
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
	}
	
	private void displayMissingExamsForCertificates() {
		generateMissingExamsForCertificatesList();
	}
	
	private void generateJobList() {
		String personCode = person.getPersonCode();
		try {
			listJobs = jobQueries.getJobsNotQualifiedForByPersonReadable(personCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void displayJobs() {
		generateJobList();
		cbJobsModel = new DefaultComboBoxModel<JobReadable>();
		cbJobsModel.removeAllElements();
		for (JobReadable job: listJobs) {
			cbJobsModel.addElement(job);
		}
		cbJobs.setModel(cbJobsModel);
	}
	
	private void displayPerson(Person person) {
		this.person = person;
		clearJobs();
		displayJobs();
		JobReadable job = (JobReadable)cbJobs.getSelectedItem();
		displayPersonJob(job);
	}
	
	
	private class ComboBoxController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cbJobs) {
				JobReadable job = (JobReadable)cbJobs.getSelectedItem(); 
				displayPersonJob(job);
			}
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
