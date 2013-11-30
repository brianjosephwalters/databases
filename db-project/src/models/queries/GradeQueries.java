package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Grade;

public class GradeQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public GradeQueries (Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * All grades.
	 */
	public List<Grade> getAllGrades() 
			throws SQLException {
		List<Grade> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM attended");
		list = getListOfGrades(stmt);
		
		return list;
	}
	
	/**
	 * All grades of person.
	 */
	public List<Grade> getPersonGrades(String personCode) 
			throws SQLException {
		List<Grade> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM attended" +
			"WHERE person_code = ?");
		stmt.setString(1, personCode);
		list = getListOfGrades(stmt);
		
		return list;
	}
	
	/**
	 * All grades for section of course.
	 */
	public List<Grade> getPersonGrades(String courseCode, String sectionCode, int year) 
			throws SQLException {
		List<Grade> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM attended" +
			"WHERE course_code = ? AND" +
			"      section-code = ? AND" +
			"      year = ?");
		stmt.setString(1, courseCode);
		stmt.setString(2, sectionCode);
		stmt.setInt(3, year);
		list = getListOfGrades(stmt);
		
		return list;
	}
	
	
	// Helper Functions
	private List<Grade> getListOfGrades(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty Grade list
		List<Grade> list = new ArrayList<Grade>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Grade from the results
			// and add it to the list.
			list.add( new Grade(
				results.getString("person_code"),
				results.getString("course_code"),
				results.getString("section_code"),
				results.getInt("year"),
				results.getString("score"))
			);
		}
		return list;
	}
}
