package ui.swing.panels.person;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import models.Certificate;
import models.Course;
import models.Exam;
import models.ExamType;
import models.Job;
import models.Person;
import models.Skill;
import models.queries.CertificateQueries;
import models.queries.CourseQueries;
import models.queries.ExamTypeQueries;
import models.queries.JobQueries;
import models.queries.SkillQueries;

import javax.swing.border.TitledBorder;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;

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
	private List<Job> listJobs;
	private List<Skill> listMissingSkills;
	private List<Course> listCoursesForSkills;
	private List<Certificate> listMissingCertificates;
	private List<Course> listCoursesForCertificates;
	private List<ExamType> listExamsForCertificates;
	
	private ComboBoxController cbController;
	
	private JComboBox<Job> cbJobs;
	private DefaultComboBoxModel<Job> cbJobsModel;
	
	private JTextArea taMissingSkills;
	private JTextArea taCoursesForSkills;
	private JTextArea taMissingCertificates;
	private JTextArea taCoursesForCertificates;
	private JTextArea taExamsForCertificates;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JPanel panel_7;
	
	public PersonCloselyQualifiedPanel(Connection connection) {
		this.connection = connection;
		this.jobQueries = new JobQueries(connection);
		this.skillQueries = new SkillQueries(connection);
		this.courseQueries = new CourseQueries(connection);
		this.certificateQueries = new CertificateQueries(connection);
		this.examTypeQueries = new ExamTypeQueries(connection);
		this.listJobs = new ArrayList<Job>();
		this.person = null;
		
		this.listMissingSkills = new ArrayList<Skill>();
		this.listCoursesForSkills = new ArrayList<Course>();
		this.listMissingCertificates = new ArrayList<Certificate>();
		this.listCoursesForCertificates = new ArrayList<Course>();
		this.listExamsForCertificates = new ArrayList<ExamType>();
		
		this.cbController = new ComboBoxController();
	
		intializeGUIController();
	}
	
	private void intializeGUIController() {
		cbJobs = new JComboBox<Job>();
		cbJobs.addActionListener(cbController);
		cbJobsModel = new DefaultComboBoxModel<Job>();
		setLayout(new BorderLayout(0, 0));
		add(cbJobs, BorderLayout.NORTH);
		
		panel_5 = new JPanel();
		add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));
		
		panel_6 = new JPanel();
		panel_5.add(panel_6);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.Y_AXIS));
		
		panel_4 = new JPanel();
		panel_6.add(panel_4);
		panel_4.setBorder(new TitledBorder(null, "Missing Certificates", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		
		taMissingCertificates = new JTextArea();
		panel_4.add(taMissingCertificates);
		taMissingCertificates.setEditable(false);
		
		panel_3 = new JPanel();
		panel_6.add(panel_3);
		panel_3.setBorder(new TitledBorder(null, "Exams for Certificates", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		taExamsForCertificates = new JTextArea();
		panel_3.add(taExamsForCertificates);
		taExamsForCertificates.setEditable(false);
		
		panel_2 = new JPanel();
		panel_6.add(panel_2);
		panel_2.setBorder(new TitledBorder(null, "Courses for Certificates", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		taCoursesForCertificates = new JTextArea();
		panel_2.add(taCoursesForCertificates);
		taCoursesForCertificates.setEditable(false);
		
		panel_7 = new JPanel();
		panel_5.add(panel_7);
		panel_7.setLayout(new BoxLayout(panel_7, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		panel_7.add(panel);
		panel.setBorder(new TitledBorder(null, "Missing Skills", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		taMissingSkills = new JTextArea();
		panel.add(taMissingSkills);
		
		taMissingSkills.setEditable(false);
		
		panel_1 = new JPanel();
		panel_7.add(panel_1);
		panel_1.setBorder(new TitledBorder(null, "Courses To Gain Skills", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		taCoursesForSkills = new JTextArea();
		taCoursesForSkills.setEnabled(false);
		panel_1.add(taCoursesForSkills);
		taCoursesForSkills.setEditable(false);
	}
	
	private void clearJobs() {
		
	}
	
	private void displayPersonJob(Job job) {
		if (job != null) {
			try {
				this.job = jobQueries.getJob(job.getJobCode()).get(0);	
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		displayMissingSkills();
		displayCoursesForSkills();
		displayMissingCertificates();
		displayMissingCoursesForCertificates();
		displayMissingExamsForCertificates();
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
		if (this.job != null && this.person != null) {
			generateMissingSkillsList();
		}
		StringBuilder sb = new StringBuilder();
		for (Skill skill : listMissingSkills) {
			sb.append(skill.getSkillName() + "\n");
		}
		taMissingSkills.setText(sb.toString());
	}
	
	private void generateCoursesForSkillsList() {
		String personCode = person.getPersonCode();
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
		
	}
	
	private void displayCoursesForSkills() {
		if (this.job != null && this.person != null) {
			generateCoursesForSkillsList();
		}
	}
	
	private void generateMissingCertificatesList() {
		String personCode = person.getPersonCode();
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
		try {
			this.listMissingCertificates = certificateQueries.getCertificatesMissingFromPersonForJob(
					personCode, jobCode, jobProfileCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void displayMissingCertificates() {
		if (this.job != null && this.person != null) {
			generateMissingCertificatesList();
			System.out.println(listMissingCertificates);
		}
		StringBuilder sb = new StringBuilder();
		for (Certificate certificate : listMissingCertificates) {
			sb.append(certificate.getCertificateTitle() + "\n");
		}
		taMissingCertificates.setText(sb.toString());
	}
	
	private void generateMissingCoursesForCertificatesList() {
		String personCode = person.getPersonCode();
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
		try {
			this.listCoursesForCertificates = courseQueries.getCoursesRequiredForCertificateForJob(
					personCode, jobCode, jobProfileCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void displayMissingCoursesForCertificates() {
		if (this.job != null && this.person != null) {
			generateMissingCoursesForCertificatesList();
		}
		StringBuilder sb = new StringBuilder();
		for (Course course : listCoursesForCertificates) {
			sb.append(course.getCourseTitle() + "\n");
		}
		taCoursesForCertificates.setText(sb.toString());
	}
	
	private void generateMissingExamsForCertificatesList() {
		String personCode = person.getPersonCode();
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
		try {
			this.listExamsForCertificates = examTypeQueries.getExamTypesForCertificatesPeopleAreMissingForJob(
					personCode, jobCode, jobProfileCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void displayMissingExamsForCertificates() {
		if (this.job != null && this.person != null) {
			generateMissingExamsForCertificatesList();
		}
		StringBuilder sb = new StringBuilder();
		for (ExamType exam : listExamsForCertificates) {
			sb.append(exam.getExamTitle() + "\n");
		}
		taMissingCertificates.setText(sb.toString());
	}
	
	private void generateJobList() {
		String personCode = person.getPersonCode();
		try {
			listJobs = jobQueries.getAvailableJobsNotQualifiedForByPerson(personCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void displayJobs() {
		if (this.person != null) {
			generateJobList();
		}
		cbJobsModel = new DefaultComboBoxModel<Job>();
		cbJobsModel.removeAllElements();
		for (Job job: listJobs) {
			cbJobsModel.addElement(job);
		}
		cbJobs.setModel(cbJobsModel);
	}
	
	private void displayPerson(Person person) {
		this.person = person;
		clearJobs();
		displayJobs();
		Job job = (Job)cbJobs.getSelectedItem();
		displayPersonJob(job);
	}
	
	
	private class ComboBoxController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cbJobs) {
				Job job = (Job)cbJobs.getSelectedItem(); 
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
