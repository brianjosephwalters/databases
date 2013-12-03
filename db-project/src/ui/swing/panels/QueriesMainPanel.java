package ui.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JTextField;

import db.DBConnection;

import models.Company;
import models.JobProfile;
import models.JobReadable;
import models.Person;
import models.Project;
import models.queries.CompanyQueries;
import models.queries.JobProfileQueries;
import models.queries.JobQueries;
import models.queries.PersonQueries;
import models.queries.ProjectQueries;
import models.queries.RequiredQueries;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class QueriesMainPanel extends JPanel {
	Connection connection;
	
	ButtonController buttonController;
	
	private RequiredQueries requiredQueries;
	private CompanyQueries companyQueries;
	private ProjectQueries projectQueries;
	private PersonQueries personQueries;
	private JobProfileQueries jobProfileQueries;
	private JobQueries jobQueries;
	
	private QueryResultsPanel queryResultsPanel;
	
	private List<Company> companyList;
	private List<Project> projectList;
	private List<Person> personList;
	private List<JobProfile> jobProfileList;
	private List<JobReadable> jobList;
	
	private DefaultComboBoxModel<Company> cbCompanyModel;
	private DefaultComboBoxModel<Project> cbProjectModel;
	private DefaultComboBoxModel<Person> cbPersonModel;
	private DefaultComboBoxModel<JobProfile> cbJobProfileModel;
	private DefaultComboBoxModel<JobReadable> cbJobModel;
	
	private JTextField tfMissingK;
	private JComboBox<Company> cbCompany;
	private JComboBox<Project> cbProject;
	private JComboBox<Person> cbPerson;
	private JComboBox<JobProfile> cbJobProfile;
	private JComboBox<JobReadable> cbJob;
	
	private JButton btnStaffBySalary;
	private JButton btnWorkersByName;
	private JButton btnLaborCostsForAll;
	private JButton btnCurrentEmployees;
	private JButton btnJobsCurrentlyHeld;
	private JButton btnSkills;
	private JButton btnSkillGapForJobs;
	private JButton btnJobProfilesQualified;
	private JButton btnHighestPayingJob;
	private JButton btnMissingSkills;
	private JButton btnCoursesForMissingSkills;
	private JButton btnMinimumCoursesForMissingSkills;
	private JButton btnCheapestCoursesForMissingSkills;
	private JButton btnQuickestCoursesForMissingSkill;
	private JButton btnMissingK;
	private JButton btnSkillsMissed;
	private JButton btnSkillsMissedForK;
	private JButton btnRequiredSkills;
	private JButton btnQualifiedPeople;
	private JButton btnClosestQualified;
	private JButton btnMissing;
	
	
	
	private JPanel tablePanel;
	
	public QueriesMainPanel(Connection connection) {
		this.connection = connection;
		
		this.requiredQueries = new RequiredQueries(connection);
		this.companyQueries = new CompanyQueries(connection);
		this.projectQueries = new ProjectQueries(connection);
		this.personQueries = new PersonQueries(connection);
		this.jobProfileQueries = new JobProfileQueries(connection);
		this.jobQueries = new JobQueries(connection);
		
		this.queryResultsPanel = new QueryResultsPanel("Nothing Yet");
		
		companyList = new ArrayList<Company>();
		projectList = new ArrayList<Project>();
		personList = new ArrayList<Person>();
		jobProfileList = new ArrayList<JobProfile>();
		jobList = new ArrayList<JobReadable>();
		
		buttonController = new ButtonController();
		initializeGUIComponents();
	}
	
	private void initializeGUIComponents() {
		setLayout(new BorderLayout(0, 0));
		JPanel queryPanel = new JPanel();
		add(queryPanel, BorderLayout.WEST);
		queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.Y_AXIS));
		
		JPanel companyPanel = new JPanel();
		companyPanel.setBorder(new TitledBorder(null, "Company", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		queryPanel.add(companyPanel);
		companyPanel.setLayout(new BorderLayout(0, 0));
		
		cbCompany = new JComboBox<Company>();
		cbCompanyModel = new DefaultComboBoxModel<Company>();
		companyPanel.add(cbCompany, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		companyPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		btnStaffBySalary = new JButton("Staff By Salary");
		btnStaffBySalary.addActionListener(buttonController);
		panel.add(btnStaffBySalary);
		
		btnWorkersByName = new JButton("Workers By Name");
		btnWorkersByName.addActionListener(buttonController);
		panel.add(btnWorkersByName);
		
		btnLaborCostsForAll = new JButton("Labor Costs For All");
		btnLaborCostsForAll.addActionListener(buttonController);
		panel.add(btnLaborCostsForAll);
		
		JPanel projectPanel = new JPanel();
		projectPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Project", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		queryPanel.add(projectPanel);
		projectPanel.setLayout(new BorderLayout(0, 0));
		
		cbProject = new JComboBox<Project>();
		cbProjectModel = new DefaultComboBoxModel<Project>();
		projectPanel.add(cbProject, BorderLayout.NORTH);
		
		JPanel panel_5 = new JPanel();
		projectPanel.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		btnCurrentEmployees = new JButton("Current Employees");
		btnCurrentEmployees.addActionListener(buttonController);
		panel_5.add(btnCurrentEmployees);
		
		JPanel personPanel = new JPanel();
		personPanel.setBorder(new TitledBorder(null, "Person", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		queryPanel.add(personPanel);
		personPanel.setLayout(new BorderLayout(0, 0));
		
		cbPerson = new JComboBox<Person>();
		cbPersonModel = new DefaultComboBoxModel<Person>();
		personPanel.add(cbPerson, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		personPanel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		btnJobsCurrentlyHeld = new JButton("Jobs Currently Held");
		btnJobsCurrentlyHeld.addActionListener(buttonController);
		panel_1.add(btnJobsCurrentlyHeld);
		
		btnSkills = new JButton("Skills");
		btnSkills.addActionListener(buttonController);
		panel_1.add(btnSkills);
		
		btnSkillGapForJobs = new JButton("Skill Gap For Jobs");
		btnSkillGapForJobs.addActionListener(buttonController);
		panel_1.add(btnSkillGapForJobs);
		
		btnJobProfilesQualified = new JButton("Job Profiles Qualified");
		btnJobProfilesQualified.addActionListener(buttonController);
		panel_1.add(btnJobProfilesQualified);
		
		btnHighestPayingJob = new JButton("Highest Paying Job");
		btnHighestPayingJob.addActionListener(buttonController);
		panel_1.add(btnHighestPayingJob);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Job", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));
		
		cbJob = new JComboBox<JobReadable>();
		cbJobModel = new DefaultComboBoxModel<JobReadable>();
		panel_4.add(cbJob);
		
		btnMissingSkills = new JButton("Missing Skills");
		btnMissingSkills.addActionListener(buttonController);
		panel_4.add(btnMissingSkills);
		
		btnCoursesForMissingSkills = new JButton("Courses For Missing Skills");
		btnCoursesForMissingSkills.addActionListener(buttonController);
		panel_4.add(btnCoursesForMissingSkills);
		
		btnMinimumCoursesForMissingSkills = new JButton("Minimum Courses For Missing Skill");
		btnMinimumCoursesForMissingSkills.addActionListener(buttonController);
		panel_4.add(btnMinimumCoursesForMissingSkills);
		
		btnCheapestCoursesForMissingSkills = new JButton("Cheapest Courses For Missing Skill");
		btnCheapestCoursesForMissingSkills.addActionListener(buttonController);
		panel_4.add(btnCheapestCoursesForMissingSkills);
		
		btnQuickestCoursesForMissingSkill = new JButton("Quickest Courses For Missing Skills");
		btnQuickestCoursesForMissingSkill.addActionListener(buttonController);
		panel_4.add(btnQuickestCoursesForMissingSkill);
		
		JPanel jobProfilePanel = new JPanel();
		jobProfilePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Job Profile", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		queryPanel.add(jobProfilePanel);
		jobProfilePanel.setLayout(new BorderLayout(0, 0));
		
		cbJobProfile = new JComboBox<JobProfile>();
		cbJobProfileModel = new DefaultComboBoxModel<JobProfile>();
		jobProfilePanel.add(cbJobProfile, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		jobProfilePanel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		btnRequiredSkills = new JButton("Required Skills");
		btnRequiredSkills.addActionListener(buttonController);
		panel_2.add(btnRequiredSkills);
		
		btnQualifiedPeople = new JButton("Qualified People");
		btnQualifiedPeople.addActionListener(buttonController);
		panel_2.add(btnQualifiedPeople);
		
		btnClosestQualified = new JButton("Closest Qualified");
		btnClosestQualified.addActionListener(buttonController);
		panel_2.add(btnClosestQualified);
		
		btnMissing = new JButton("Missing 1");
		btnMissing.addActionListener(buttonController);
		panel_2.add(btnMissing);
		
		btnSkillsMissed = new JButton("Skills Missed");
		btnSkillsMissed.addActionListener(buttonController);
		panel_2.add(btnSkillsMissed);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Missing-K", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(panel_3);
		
		JLabel lblK = new JLabel("K");
		panel_3.add(lblK);
		
		tfMissingK = new JTextField();
		panel_3.add(tfMissingK);
		tfMissingK.setColumns(10);
		
		btnMissingK = new JButton("Missing K");
		btnMissingK.addActionListener(buttonController);
		panel_3.add(btnMissingK);
		
		btnSkillsMissedForK = new JButton("Skills Missed For K");
		btnSkillsMissedForK.addActionListener(buttonController);
		panel_3.add(btnSkillsMissedForK);
		
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout(0, 0));
		tablePanel.add(queryResultsPanel);
		add(tablePanel);
		
		updateCompanyComboBox();
		updateProjectComboBox();
		updatePersonComboBox();
		updateJobComboBox();
		updateJobProfileComboBox();
	}
	
	private void generateCompanyList() {
		try {
			companyList = companyQueries.getAllCompanies();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateCompanyComboBox() {
		generateCompanyList();
		cbCompanyModel = new DefaultComboBoxModel<Company>();
		for (Company company : companyList) {
			cbCompanyModel.addElement(company);
		}
		cbCompany.setModel(cbCompanyModel);
	}
	
	private void generateProjectList() {
		try {
			projectList = projectQueries.getAllProjects();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateProjectComboBox() {
		generateProjectList();
		cbProjectModel = new DefaultComboBoxModel<Project>();
		for (Project project: projectList) {
			cbProjectModel.addElement(project);
		}
		cbProject.setModel(cbProjectModel);
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
			jobList = jobQueries.getAllJobsReadable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateJobComboBox() {
		generateJobList();
		cbJobModel = new DefaultComboBoxModel<JobReadable>();
		for (JobReadable job : jobList) {
			cbJobModel.addElement(job);
		}
		cbJob.setModel(cbJobModel);
	}
	
	private void generateJobProfileList() {
		try {
			jobProfileList = jobProfileQueries.getAllJobProfiles();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateJobProfileComboBox() {
		generateJobProfileList();
		cbJobProfileModel = new DefaultComboBoxModel<JobProfile>();
		for (JobProfile jobProfile: jobProfileList) {
			cbJobProfileModel.addElement(jobProfile);
		}
		cbJobProfile.setModel(cbJobProfileModel);
	}
	
	private class ButtonController implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == btnWorkersByName ) {
				Company company = (Company)cbCompany.getSelectedItem();
				try {
					ResultSet results = requiredQueries.getCompanyEmployees(company.getCompanyCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Company Employees");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnStaffBySalary) {
				Company company = (Company)cbCompany.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getCompanyFullTimeEmployeesBySalaryDesc(company.getCompanyCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Company Staff by Salary");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnLaborCostsForAll) {
				Company company = (Company)cbCompany.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getCompanyCurrentTotalLaborCost(company.getCompanyCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Company Total Labor Costs");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnCurrentEmployees) {
				Project project = (Project)cbProject.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getCurrentProjectEmployees(project.getProjectCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Project Emloyees");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnJobsCurrentlyHeld) {
				Person person = (Person)cbPerson.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getCurrentEmployment(person.getPersonCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Current Employment For Person");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnSkills) {
				Person person = (Person)cbPerson.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getPersonSkills(person.getPersonCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Skills Of Person");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnSkillGapForJobs) {
				Person person = (Person)cbPerson.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getCurrentSkillGap(person.getPersonCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Skill Gap of Person For Current Jobs");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnJobProfilesQualified) {
				Person person = (Person)cbPerson.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getJobProfileQualifiedFor(person.getPersonCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Jobs Qualified For");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnHighestPayingJob) {
				Person person = (Person)cbPerson.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getHighestPayingJobGivenSkills(person.getPersonCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Highest Paying Jobs Qualified For");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnMissingSkills) {
				Person person = (Person)cbPerson.getSelectedItem();
				JobReadable job = (JobReadable)cbJob.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getSkillGapOfPersonForJob(
								person.getPersonCode(), job.getJobCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Skills Missing From Person For Job");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnCoursesForMissingSkills) {
				
			} else if (event.getSource() == btnMinimumCoursesForMissingSkills) {
				
			} else if (event.getSource() == btnCheapestCoursesForMissingSkills) {
				
			} else if (event.getSource() == btnQuickestCoursesForMissingSkill) {
				
			} else if (event.getSource() == btnMissingK) {
				
			} else if (event.getSource() == btnSkillsMissedForK) {
				
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
		QueriesMainPanel panel = new QueriesMainPanel(connection);
		frame.getContentPane().add(panel);
		
		frame.setTitle("Queries Panel");
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
