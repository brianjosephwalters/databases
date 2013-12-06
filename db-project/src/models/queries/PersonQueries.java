package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Person;

public class PersonQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public PersonQueries (Connection connection) {
		this.connection = connection;
	}
	
	// Queries
	public List<Person> getAllPeople() 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM person");
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getPeopleByLastName(String lastName) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM person " +
			" WHERE last_name = ?");
		stmt.setString(1, lastName);
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getPeopleWithSkill(String skillCode) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM person NATURAL JOIN person_skill " +
			" WHERE skill_code = ?");
		stmt.setString(1, skillCode);
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getPeopleEmployed() 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM person NATURAL JOIN employment");
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getPeopleUnEmployed() 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"(SELECT * " +
			" FROM person)" +
			" EXCEPT" +
			" (SELECT * " +
			" FROM person NATURAL JOIN employment)");
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getPeopleEmployedAtJob(String jobCode) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM person NATURAL JOIN employment " +
			" WHERE job_code = ? ");
		stmt.setString(1, jobCode);
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getCurrentPersonEmployedAtJob(String jobCode) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM person NATURAL JOIN employment " +
			" WHERE job_code = ? AND " +
			"  	    start_date < CURRENT_DATE AND " +
			"       (end_date > CURRENT_DATE OR end_date IS NULL) ");
		stmt.setString(1, jobCode);
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	// People who took a course
	public List<Person> getPeopleAttendingCourse(String course_code) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT *" +
			" FROM attended NATURAL JOIN person" +
			" WHERE course_code = ?"
			);
		stmt.setString(1, course_code);

		list = getListOfPeople(stmt);
		
		return list;
	}
	
	// People who attended a section of a course
	public List<Person> getPeopleAttendingSectionOfCourse(String course_code, String section_code, Integer year)
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT *" +
			" FROM attended NATURAL JOIN person" +
			" WHERE course_code = ? AND section_code = ?" +
			"       year = ?"
			);
		stmt.setString(1, course_code);
		stmt.setString(2, course_code);
		stmt.setInt(3,  year);

		list = getListOfPeople(stmt);
		
		return list;
	}
	// People who earned a certificate
	// Use grade somehow?
	public List<Person> getPeopleWithCertificate(String certificate_code) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT *" +
			" FROM person NATURAL JOIN takes NATURAL JOIN " +
			"      exam NATURAL JOIN exam_type" +
			" WHERE certificate_code = ?"
			);
		stmt.setString(1, certificate_code);
		list = getListOfPeople(stmt);
		return list;
	}
	
	// People who took an exam
	public List<Person> getPeopleTakenExam(String exam_type_code) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT *" +
			" FROM person NATURAL JOIN takes NATURAL JOIN exam" +
			" WHERE exam_type_code = ?"
			);
		stmt.setString(1, exam_type_code);
		list = getListOfPeople(stmt);
		return list;
	}
	
	// People who can use a tool
	public List<Person> getPeopleCertifiedWithTool(String tool_code) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT *" +
			" FROM person NATURAL JOIN takes NATURAL JOIN exam NATURAL JOIN " +
			"     exam_type NATURAL_JOIN certificate" +
			" WHERE tool_code = ?"
			);
		stmt.setString(1, tool_code);
		list = getListOfPeople(stmt);
		return list;
	}
	
	// People who work for a company
	public List<Person> getPeopleCurrentlyAtCompany(String company_code) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT *" +
			" FROM person NATURAL JOIN employment NATURAL JOIN job" +
			" WHERE company_code = ? AND" +
			"      start_date < CURRENT_DATE AND" +
			"      (end_date > CURRENT_DATE OR end_date IS NULL)"
		);
		stmt.setString(1, company_code);
		list = getListOfPeople(stmt);
		return list;
	}
	// People who have ever worked for a company
	public List<Person> getPeopleWorkedOnCompany(String company_code) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT *" +
			" FROM person NATURAL JOIN employment NATURAL JOIN job" +
			" WHERE company_code = ?"
		);
		stmt.setString(1, company_code);
		list = getListOfPeople(stmt);
		return list;
	}
	// People who work on a project
	public List<Person> getPeopleCurrentlyOnProject(String project_code) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT *" +
			"FROM person NATURAL JOIN employment NATURAL JOIN" +
			"     job NATURAL JOIN job_project" +
			"WHERE project_code = ?" +
			"      start_date < CURRENT_DATE AND" +
			"      (end_date > CURRENT_DATE OR end_date IS NULL)"
		);
		stmt.setString(1, project_code);
		list = getListOfPeople(stmt);
		return list;
	}
	
	// People who have ever worked on a project
	public List<Person> getPeopleWorkedOnProject(String project_code) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT *" +
			"FROM person NATURAL JOIN employment NATURAL JOIN" +
			"     job NATURAL JOIN job_project" +
			"WHERE project_code = ?"
		);
		stmt.setString(1, project_code);
		list = getListOfPeople(stmt);
		return list;
	}
	
	// People qualified for a job_profile
	public List<Person> getPeopleQualifiedForJobProfile(String jobProfileCode) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM person P " +
			" WHERE NOT EXISTS " +
			"   (SELECT skill_code " +
			"    FROM job_profile_skill " +
			"    WHERE job_profile_code = ? " + 
			"   MINUS  " +
			"    SELECT skill_code " +
			"    FROM person_skill " +
			"    WHERE person_code = P.person_code )"
		);
		stmt.setString(1, jobProfileCode);
		list = getListOfPeople(stmt);
		return list;
	}
	
	
	// People qualified for a job.
	// Still need to implement job_skill relation for additional
	// skills required by job.
	public List<Person> getPeopleQualifiedForJob(String jobCode) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM person P " +
			" WHERE NOT EXISTS " +
			"   (SELECT skill_code " +
			"    FROM job_skill " +
			"    WHERE job_code = ? " + 
			"   MINUS  " +
			"    SELECT skill_code " +
			"    FROM person_skill " +
			"    WHERE person_code = P.person_code )"
		);
		stmt.setString(1, jobCode);
		list = getListOfPeople(stmt);
		return list;
	}
	
	public List<Person> getPeopleFullyQualifiedForJob(String jobCode,
													  String jobProfileCode) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH skills as " + 
		    "	(SELECT skill_code  " + 
			"    FROM job_skill  " + 
			"    WHERE job_code = ? " + 
			"   UNION " + 
			"    SELECT skill_code  " + 
		    "    FROM job_profile_skill  " + 
		    "    WHERE job_profile_code = ?) " + 
		    " SELECT *  " + 
			" FROM person P  " + 
			" WHERE NOT EXISTS  " + 
		    "  (SELECT skill_code " +
		    "   FROM skills " + 
		    "   MINUS   " + 
			"	SELECT skill_code  " + 
			"	FROM person_skill  " + 
			"	WHERE person_code = P.person_code )"
		);
		stmt.setString(1, jobCode);
		stmt.setString(2, jobProfileCode);
		list = getListOfPeople(stmt);
		return list;
	}
	
	// Insertions
	public int addPerson(Person person) 
			throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" INSERT INTO person " +
			" VALUES (?, ?, ?, ?, ?) "
			);
		stmt.setString(1, person.getPersonCode());
		stmt.setString(2, person.getLastName());
		stmt.setString(3, person.getFirstName());
		stmt.setString(4, person.getGender());
		stmt.setString(5, person.getEmail());
		count = stmt.executeUpdate();
		return count;
	}
	
	// Updates
	public int updatePerson(Person person)
			throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" UPDATE person " +
			"   SET last_name = ?, " +
			"       first_name = ?, " +
			"       gender = ?, " +
			"       email = ? " +
			"   WHERE person_code = ? "
			);
		stmt.setString(1, person.getLastName());
		stmt.setString(2, person.getFirstName());
		stmt.setString(3, person.getGender());
		stmt.setString(4, person.getEmail());
		stmt.setString(5, person.getPersonCode());
		count = stmt.executeUpdate();
		return count;
	}
	
	
	// Deletes
	public int deletePersonTransaction(Person person) throws SQLException {
		int count = 0;
		try {
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			PreparedStatement stmt1 = connection.prepareStatement(
					"DELETE FROM exam_taken WHERE person_code = ?"
			);
			stmt1.setString(1, person.getPersonCode());
			
			PreparedStatement stmt2 = connection.prepareStatement(
					"DELETE FROM employment WHERE person_code = ?"
			);
			stmt2.setString(1, person.getPersonCode());
			
			PreparedStatement stmt3 = connection.prepareStatement(
					"DELETE FROM person_address WHERE person_code = ?"
			);
			stmt3.setString(1, person.getPersonCode());
			
			PreparedStatement stmt4 = connection.prepareStatement(
					"DELETE FROM person_phone WHERE person_code = ?"
			);
			stmt4.setString(1, person.getPersonCode());
			
			PreparedStatement stmt5 = connection.prepareStatement(
					"DELETE FROM person_skill WHERE person_code = ?"
			);
			stmt5.setString(1, person.getPersonCode());
			
			PreparedStatement stmt6 = connection.prepareStatement(
					"DELETE FROM earns WHERE person_code = ?"
			);
			stmt6.setString(1, person.getPersonCode());	
			
			stmt1.executeUpdate();
			stmt2.executeUpdate();
			stmt3.executeUpdate();
			stmt4.executeUpdate();
			stmt5.executeUpdate();
			stmt6.executeUpdate();
			
			PreparedStatement stmt7 = connection.prepareStatement(
					"DELETE FROM person WHERE person_code = ?"
			);
			stmt7.setString(1, person.getPersonCode());
			
			count = stmt7.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				System.out.println("Rollback Error: " + e2);
			}
			throw e;
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				System.out.println("Reseting AutoCommit Error: " + e);
			}
		}
		return count;
	}
	
	// Helper Functions
	private List<Person> getListOfPeople(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty person list
		List<Person> list = new ArrayList<Person>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new PhoneNumber from the results
			// and add it to the list.
			list.add( new Person(
				results.getString("person_code"),
				results.getString("last_name"),
				results.getString("first_name"),
				results.getString("gender"),
				results.getString("email"))
			);
		}
		return list;
	}
}
