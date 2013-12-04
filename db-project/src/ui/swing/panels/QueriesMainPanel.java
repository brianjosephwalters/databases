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
import models.Job;
import models.Person;
import models.Project;
import models.queries.CompanyQueries;
import models.queries.JobProfileQueries;
import models.queries.JobQueries;
import models.queries.PersonQueries;
import models.queries.ProjectQueries;
import models.queries.RequiredQueries;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;

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
	private List<Job> jobList;
	
	private DefaultComboBoxModel<Company> cbCompanyModel;
	private DefaultComboBoxModel<Project> cbProjectModel;
	private DefaultComboBoxModel<Person> cbPersonModel;
	private DefaultComboBoxModel<JobProfile> cbJobProfileModel;
	private DefaultComboBoxModel<Job> cbJobModel;
	
	private JTextField tfMissingK;
	private JComboBox<Company> cbCompany;
	private JComboBox<Project> cbProject;
	private JComboBox<Person> cbPerson;
	private JComboBox<JobProfile> cbJobProfile;
	private JComboBox<Job> cbJob;
	
	private JButton btnStaffBySalary;
	private JButton btnWorkersByName;
	private JButton btnLaborCostsForAll;
	private JButton btnCurrentEmployees;
	private JButton btnJobsCurrentlyHeld;
	private JButton btnSkills;
	private JButton btnSkillGapForCurrentJobs;
	private JButton btnJobProfilesQualified;
	private JButton btnHighestPayingJob;
	private JButton btnMissingSkills;
	private JButton btnCoursesForMissingSkills;
	private JButton btnMinimumCoursesForSkillSet;
	private JButton btnCheapestCoursesForMissingSkills;
	private JButton btnQuickestCoursesForMissingSkill;
	private JButton btnPeopleMissingK;
	private JButton btnSkillsMissedOne;
	private JButton btnSkillsMissedForK;
	private JButton btnRequiredSkills;
	private JButton btnQualifiedPeople;
	private JButton btnClosestQualified;
	private JButton btnMissingOne;
	
	private JPanel tablePanel;
	private JPanel panel_6;
	private JPanel panel_7;
	private JPanel panel_8;
	private JPanel panel_9;
	private JPanel panel_10;
	private JButton btnJobProfilesWithMostOpeningsPerQualifiedPerson;
	private JButton btnCoursesTrainingUnqualified;
	private JTabbedPane tabbedPane;
	private JPanel companyTab;
	private JPanel projectTab;
	private JPanel personJobTab;
	private JPanel jobprofileTab;
	private JPanel hardonesTab;
	
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
		jobList = new ArrayList<Job>();
		
		buttonController = new ButtonController();
		initializeGUIComponents();
	}
	
	private void initializeGUIComponents() {
		setLayout(new BorderLayout(0, 0));
		JPanel queryPanel = new JPanel();
		add(queryPanel, BorderLayout.WEST);
		queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.Y_AXIS));
		cbCompanyModel = new DefaultComboBoxModel<Company>();
		cbProjectModel = new DefaultComboBoxModel<Project>();
		cbPersonModel = new DefaultComboBoxModel<Person>();
		cbJobModel = new DefaultComboBoxModel<Job>();
		cbJobProfileModel = new DefaultComboBoxModel<JobProfile>();
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		queryPanel.add(tabbedPane);
		
		companyTab = new JPanel();
		tabbedPane.addTab("Company Queries", null, companyTab, null);
		
		JPanel companyPanel = new JPanel();
		companyTab.add(companyPanel);
		companyPanel.setBorder(new TitledBorder(null, "Company", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		cbCompany = new JComboBox<Company>();
		companyPanel.setLayout(new BoxLayout(companyPanel, BoxLayout.Y_AXIS));
		companyPanel.add(cbCompany);
		
		JPanel panel = new JPanel();
		companyPanel.add(panel);
		
		btnStaffBySalary = new JButton("2. Staff By Salary");
		btnStaffBySalary.addActionListener(buttonController);
		panel.setLayout(new GridLayout(2, 2, 0, 0));
		
		btnWorkersByName = new JButton("1. Workers By Name");
		btnWorkersByName.addActionListener(buttonController);
		panel.add(btnWorkersByName);
		panel.add(btnStaffBySalary);
		
		btnLaborCostsForAll = new JButton("3. Labor Costs For All");
		btnLaborCostsForAll.addActionListener(buttonController);
		panel.add(btnLaborCostsForAll);
		
		projectTab = new JPanel();
		tabbedPane.addTab("Project Queries", null, projectTab, null);
		
		JPanel projectPanel = new JPanel();
		projectTab.add(projectPanel);
		projectPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Project", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		cbProject = new JComboBox<Project>();
		projectPanel.setLayout(new BoxLayout(projectPanel, BoxLayout.Y_AXIS));
		projectPanel.add(cbProject);
		
		JPanel panel_5 = new JPanel();
		projectPanel.add(panel_5);
		
		btnCurrentEmployees = new JButton("5. Current Employees");
		btnCurrentEmployees.setHorizontalAlignment(SwingConstants.LEFT);
		btnCurrentEmployees.addActionListener(buttonController);
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.Y_AXIS));
		panel_5.add(btnCurrentEmployees);
		
		personJobTab = new JPanel();
		tabbedPane.addTab("Person and Job Queries", null, personJobTab, null);
		
		JPanel personPanel = new JPanel();
		personJobTab.add(personPanel);
		personPanel.setBorder(new TitledBorder(null, "Person", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		cbPerson = new JComboBox<Person>();
		personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.Y_AXIS));
		personPanel.add(cbPerson);
		
		JPanel panel_1 = new JPanel();
		personPanel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		panel_6 = new JPanel();
		panel_1.add(panel_6);
		panel_6.setLayout(new GridLayout(3, 2, 0, 0));
		
		btnJobsCurrentlyHeld = new JButton("4. Jobs Currently Held");
		panel_6.add(btnJobsCurrentlyHeld);
		
		btnSkills = new JButton("6. Skills");
		panel_6.add(btnSkills);
		
		btnSkillGapForCurrentJobs = new JButton("7. Skill Gap For Current Jobs");
		panel_6.add(btnSkillGapForCurrentJobs);
		
		btnJobProfilesQualified = new JButton("14. Job Profiles Qualified");
		panel_6.add(btnJobProfilesQualified);
		
		btnHighestPayingJob = new JButton("15. Highest Paying Job Qualified");
		panel_6.add(btnHighestPayingJob);
		btnHighestPayingJob.addActionListener(buttonController);
		btnJobProfilesQualified.addActionListener(buttonController);
		btnSkillGapForCurrentJobs.addActionListener(buttonController);
		btnSkills.addActionListener(buttonController);
		btnJobsCurrentlyHeld.addActionListener(buttonController);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Job", TitledBorder.TRAILING, TitledBorder.TOP, null, null));
		panel_1.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));
		
		cbJob = new JComboBox<Job>();
		panel_4.add(cbJob);
		
		panel_7 = new JPanel();
		panel_4.add(panel_7);
		panel_7.setLayout(new GridLayout(3, 2, 0, 0));
		
		btnMissingSkills = new JButton("9. Person's Missing Skills");
		panel_7.add(btnMissingSkills);
		btnMissingSkills.addActionListener(buttonController);
		
		btnMinimumCoursesForSkillSet = new JButton("10. Minimum Courses For Skill Set");
		panel_7.add(btnMinimumCoursesForSkillSet);
		btnMinimumCoursesForSkillSet.addActionListener(buttonController);
		btnMinimumCoursesForSkillSet.setEnabled(false);
		
		btnCoursesForMissingSkills = new JButton("11. Courses For Person's Missing Skills");
		panel_7.add(btnCoursesForMissingSkills);
		btnCoursesForMissingSkills.addActionListener(buttonController);
		btnCoursesForMissingSkills.setEnabled(false);
		
		btnCheapestCoursesForMissingSkills = new JButton("12. Cheapest Courses For Missing Skill");
		panel_7.add(btnCheapestCoursesForMissingSkills);
		btnCheapestCoursesForMissingSkills.addActionListener(buttonController);
		btnCheapestCoursesForMissingSkills.setEnabled(false);
		
		btnQuickestCoursesForMissingSkill = new JButton("13. Quickest Courses For Missing Skills");
		panel_7.add(btnQuickestCoursesForMissingSkill);
		btnQuickestCoursesForMissingSkill.addActionListener(buttonController);
		btnQuickestCoursesForMissingSkill.setEnabled(false);
		
		jobprofileTab = new JPanel();
		tabbedPane.addTab("Job Profile Queries", null, jobprofileTab, null);
		
		JPanel jobProfilePanel = new JPanel();
		jobprofileTab.add(jobProfilePanel);
		jobProfilePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Job Profile", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		cbJobProfile = new JComboBox<JobProfile>();
		jobProfilePanel.setLayout(new BoxLayout(jobProfilePanel, BoxLayout.Y_AXIS));
		jobProfilePanel.add(cbJobProfile);
		
		JPanel panel_2 = new JPanel();
		jobProfilePanel.add(panel_2);
		
		btnRequiredSkills = new JButton("8. Required Skills");
		btnRequiredSkills.addActionListener(buttonController);
		panel_2.setLayout(new GridLayout(3, 2, 0, 0));
		panel_2.add(btnRequiredSkills);
		
		btnQualifiedPeople = new JButton("16. Qualified People");
		btnQualifiedPeople.addActionListener(buttonController);
		panel_2.add(btnQualifiedPeople);
		
		btnClosestQualified = new JButton("17. Closest Qualified");
		btnClosestQualified.addActionListener(buttonController);
		panel_2.add(btnClosestQualified);
		
		btnMissingOne = new JButton("18. Missing One");
		btnMissingOne.addActionListener(buttonController);
		panel_2.add(btnMissingOne);
		
		btnSkillsMissedOne = new JButton("19. Skills Missed One");
		btnSkillsMissedOne.addActionListener(buttonController);
		panel_2.add(btnSkillsMissedOne);
		
		JPanel panel_3 = new JPanel();
		jobProfilePanel.add(panel_3);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Missing-K", TitledBorder.TRAILING, TitledBorder.TOP, null, null));
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		panel_8 = new JPanel();
		panel_3.add(panel_8);
		
		JLabel lblK = new JLabel("K");
		panel_8.add(lblK);
		
		tfMissingK = new JTextField();
		panel_8.add(tfMissingK);
		tfMissingK.setColumns(10);
		
		panel_9 = new JPanel();
		panel_3.add(panel_9);
		
		btnPeopleMissingK = new JButton("20. People Missing K");
		panel_9.add(btnPeopleMissingK);
		
		btnSkillsMissedForK = new JButton("21. Skills Missed For K");
		panel_9.add(btnSkillsMissedForK);
		
		hardonesTab = new JPanel();
		tabbedPane.addTab("The Hard Queries", null, hardonesTab, null);
		
		panel_10 = new JPanel();
		hardonesTab.add(panel_10);
		panel_10.setLayout(new GridLayout(2, 1, 0, 0));
		
		btnJobProfilesWithMostOpeningsPerQualifiedPerson = new JButton("22. Job Profiles With Most Openings Per Qualified People");
		btnJobProfilesWithMostOpeningsPerQualifiedPerson.setEnabled(false);
		panel_10.add(btnJobProfilesWithMostOpeningsPerQualifiedPerson);
		
		btnCoursesTrainingUnqualified = new JButton("23. Courses Training Unqualified People For Job Profiles \r\nWith The Most Openings");
		btnCoursesTrainingUnqualified.setEnabled(false);
		panel_10.add(btnCoursesTrainingUnqualified);
		btnSkillsMissedForK.addActionListener(buttonController);
		btnPeopleMissingK.addActionListener(buttonController);
		
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
			jobList = jobQueries.getAllJobs();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateJobComboBox() {
		generateJobList();
		cbJobModel = new DefaultComboBoxModel<Job>();
		for (Job job : jobList) {
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
			} else if (event.getSource() == btnSkillGapForCurrentJobs) {
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
				Job job = (Job)cbJob.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getSkillGapOfPersonForJob(
								person.getPersonCode(), job.getJobCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Skills Missing From Person For Job");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnRequiredSkills) {
				JobProfile jobProfile = (JobProfile)cbJobProfile.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getRequiredSkills(jobProfile.getJobProfileCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Skills Required for a Job Profile");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnQualifiedPeople) {
				JobProfile jobProfile = (JobProfile)cbJobProfile.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getAllQualifiedForJobProfile(jobProfile.getJobProfileCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("People Qualified for a Job Profile");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnClosestQualified) {
				JobProfile jobProfile = (JobProfile)cbJobProfile.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getNearestQualifiedForJobProfile(jobProfile.getJobProfileCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Nearest Qualified for a Job Profile");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnMissingOne) {
				JobProfile jobProfile = (JobProfile)cbJobProfile.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getPeopleMissingOneSkillForJobProfile(jobProfile.getJobProfileCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("People Missing One Skill for a Job Profile");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnSkillsMissedOne) {
				JobProfile jobProfile = (JobProfile)cbJobProfile.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getSkillsMissedByPeople(jobProfile.getJobProfileCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Skills People Missed by One for a Job Profile");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnCoursesForMissingSkills) {
				// 10. Not Yet implemented
			} else if (event.getSource() == btnMinimumCoursesForSkillSet) {
				Job job = (Job)cbJob.getSelectedItem();
				Person person = (Person)cbPerson.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getLeastCoursesForPersonToGetJob(job.getJobCode(), person.getPersonCode());
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("Least Courses for Person to get Job");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnCheapestCoursesForMissingSkills) {
				//12. Not Yet Implemented
			} else if (event.getSource() == btnQuickestCoursesForMissingSkill) {
				//13. Not Yet Implemented
			} else if (event.getSource() == btnPeopleMissingK) {
				Integer K = Integer.parseInt(tfMissingK.getText());
				Job job = (Job)cbJob.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getPeopleMissingUpToKSkills(job.getJobCode(), K);
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("People Missing up to K Skills For Job");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnSkillsMissedForK) {
				Integer K = Integer.parseInt(tfMissingK.getText());
				Job job = (Job)cbJob.getSelectedItem();
				try {
					ResultSet results = 
						requiredQueries.getUpToKSkillsMissedByPeople(job.getJobCode(), K);
					queryResultsPanel.setResultsSet(results);
					queryResultsPanel.setLabel("K Skills Missed By People For Job");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == btnJobProfilesWithMostOpeningsPerQualifiedPerson) {
				// 22.  Not Yet Implemented
			} else if (event.getSource() == btnCoursesTrainingUnqualified) {
				// 23.  Not Yet Implemented.
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
		QueriesMainPanel panel = new QueriesMainPanel(connection);
		frame.getContentPane().add(panel);
		
		frame.setTitle("Queries Panel");
		frame.setSize(900, 450);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
