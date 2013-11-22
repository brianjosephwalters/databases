package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Skill;

public class SkillQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public SkillQueries (Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * All skills.
	 */
	public List<Skill> getAllSkills() 
			throws SQLException {
		List<Skill> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM skill");
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	/**
	 * All skills provided by a particular course.
	 */
	public List<Skill> getSkillsFromCourse(String courseCode) 
			throws SQLException {
		List<Skill> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM skill NATURAL JOIN course_skill");
		stmt.setString(1, courseCode);
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	/**
	 * All skills possessed by a person.
	 */
	public List<Skill> getSkillsOfPerson(String personCode) 
			throws SQLException {
		List<Skill> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM skill NATURAL JOIN person_skill");
		stmt.setString(1, personCode);
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	/**
	 * All skills required by a job profile.
	 */
	public List<Skill> getSkillsForJobProfile(String jobProfileCode) 
			throws SQLException {
		List<Skill> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM skill NATURAL JOIN job_profile_skill");
		stmt.setString(1, jobProfileCode);
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	// Helper Functions
	private List<Skill> getListOfSkills(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty Skill list
		List<Skill> list = new ArrayList<Skill>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Skill from the results
			// and add it to the list.
			list.add( new Skill(
				results.getString("skill_code"),
				results.getString("skill_name"),
				results.getString("skill_description"),
				results.getString("level"))
			);
		}
		return list;
	}
}
