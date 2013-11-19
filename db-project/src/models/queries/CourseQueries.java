package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Course;
import models.Person;

public class CourseQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public CourseQueries (Connection connection) {
		this.connection = connection;
	}
	
	// Queries
	/**
	 * All courses.
	 */
	public List<Course> getAllCourses() 
			throws SQLException {
		List<Course> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM course");
		list = getListOfCourses(stmt);
		
		return list;
	}

	/**
	 * All courses currently being offered.
	 */
	public List<Course> getCoursesOfferedInYear(int year) 
			throws SQLException {
		List<Course> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM course NATURAL JOIN section" +
			"WHERE year = ?");
		stmt.setInt(1,  year);
		list = getListOfCourses(stmt);
		
		return list;
	}
	
	/**
	 * All courses that provide a certain skill.
	 */
	public List<Course> getCoursesProvidingSkill(String skillCode) 
			throws SQLException {
		List<Course> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM course NATURAL JOIN course_skill" +
			"WHERE skill_code = ?");
		stmt.setString(1,  skillCode);
		list = getListOfCourses(stmt);
		
		return list;
	}
	
	
	/**
	 * All courses that prepare for a certificate.
	 */
	public List<Course> getCoursesPreparingForCertificate(String certCode) 
			throws SQLException {
		List<Course> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM course NATURAL JOIN prepares_for" +
			"WHERE certificate_code = ?");
		stmt.setString(1,  certCode);
		list = getListOfCourses(stmt);
		
		return list;
	}
	
	
	/**
	 * All courses that prepare for using a particular tool.
	 */
	public List<Course> getCoursesForTool(String toolCode) 
			throws SQLException {
		List<Course> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM course NATURAL JOIN prepares_for NATURAL JOIN certificate" +
			"WHERE tool_code = ?");
		stmt.setString(1,  toolCode);
		list = getListOfCourses(stmt);
		
		return list;
	}
	
	/**
	 * All courses offered by a company.
	 */
	public List<Course> getCoursesOfferedByCompany(String companyCode) 
			throws SQLException {
		List<Course> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM course NATURAL JOIN section NATURAL JOIN" +
			"     provides" +
			"WHERE company_code = ?");
		stmt.setString(1,  companyCode);
		list = getListOfCourses(stmt);
		
		return list;
	}
	
	/**
	 * All courses taken by a person.
	 */
	public List<Course> getCoursesAttended(String personCode) 
			throws SQLException {
		List<Course> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM course NATURAL JOIN section NATURAL JOIN" +
			"     attended" +
			"WHERE person_code = ?");
		stmt.setString(1,  personCode);
		list = getListOfCourses(stmt);
		
		return list;
	}
	
	/**
	 * All courses useful for a job profile.
	 */
	public List<Course> getCoursesUsefulForJobProfile(String jobProfileCode) 
			throws SQLException {
		List<Course> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM course NATURAL JOIN course_skill NATURAL JOIN" +
			"     job_profile" +
			"WHERE job_profile_code = ?");
		stmt.setString(1,  jobProfileCode);
		list = getListOfCourses(stmt);
		
		return list;
	}
	
	// Helper Functions
	private List<Course> getListOfCourses(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty Course list
		List<Course> list = new ArrayList<Course>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Course from the results
			// and add it to the list.
			list.add( new Course(
				results.getString("course_code"),
				results.getString("course_title"),
				results.getString("course_description"),
				results.getString("level"),
				results.getString("status"),
				results.getDouble("retail_price"))
			);
		}
		return list;
	}
}
