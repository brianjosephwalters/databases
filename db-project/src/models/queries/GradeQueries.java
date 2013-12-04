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
			"FROM attended NATURAL JOIN course NATURAL JOIN person ");
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
			"FROM attended NATURAL JOIN course NATURAL JOIN person " +
			"WHERE person_code = ?");
		stmt.setString(1, personCode);
		list = getListOfGrades(stmt);
		
		return list;
	}
	
	/**
	 * All grades for section of course.
	 */
	public List<Grade> getSectionGrades(String courseCode, String sectionCode, int year) 
			throws SQLException {
		List<Grade> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM attended NATURAL JOIN course NATURAL JOIN person " +
			"WHERE course_code = ? AND" +
			"      section-code = ? AND" +
			"      year = ?");
		stmt.setString(1, courseCode);
		stmt.setString(2, sectionCode);
		stmt.setInt(3, year);
		list = getListOfGrades(stmt);
		
		return list;
	}
	
	// Inserts
	public int addGrade(Grade grade) 
			throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" INSERT INTO  attended " +
			" VALUES (?, ?, ?, ?, ?, ?) "
			);
		stmt.setString(1, grade.getCourseCode());
		stmt.setString(2, grade.getSectionCode());
		stmt.setInt(3, grade.getYear());
		stmt.setString(4, grade.getPersonCode());
		stmt.setDate(5, new java.sql.Date(grade.getCompletedDate().getTime()));
		stmt.setString(6, grade.getScore());
		count = stmt.executeUpdate();
		return count; 
	}
	
	// Updates
	public int updateGrades(Grade grade) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" UPDATE attended " +
			" SET score = ? " +
			"     completed_date = ? " +
			" WHERE person_code = ? " +
			"       course_code = ? " +
			"       section_code = ? " +
			"       year = ? "
		);
		stmt.setString(1, grade.getScore());
		stmt.setDate(2, new java.sql.Date(grade.getCompletedDate().getTime()));
		stmt.setString(2, grade.getPersonCode());
		stmt.setString(3, grade.getCourseCode());
		stmt.setString(4, grade.getSectionCode());
		stmt.setInt(5, grade.getYear());
		count = stmt.executeUpdate();
		return count;
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
				results.getString("first_name"),
				results.getString("last_name"),
				results.getString("course_code"),
				results.getString("section_code"),
				results.getInt("year"),
				results.getString("course_title"),
				results.getString("score"),
				results.getDate("completed_date")) 
			);
		}
		return list;
	}
}
