package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Person;
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
			"FROM skill NATURAL JOIN course_skill " + 
			"WHERE course_code = ?");
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
			"FROM skill NATURAL JOIN person_skill " + 
			"WHERE person_code = ?");
		stmt.setString(1, personCode);
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	/**
	 * All skills not possessed by a person.
	 */
	public List<Skill> getSkillsNotPossessedByPerson(String personCode) 
			throws SQLException {
		List<Skill> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"WITH  lacked_skills AS" +
			"  ( (SELECT skill_code" + 
		    "     FROM skill)" +
		    "    MINUS " +
		    "    (SELECT skill_code" +
		    "     FROM skill NATURAL JOIN person_skill" +
		    "     WHERE person_code = ?) )" +
			"SELECT * " +
			"FROM skill NATURAL JOIN lacked_skills");
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
			"FROM skill NATURAL JOIN job_profile_skill" + 
			"WHERE job_profile_code = ?");
		stmt.setString(1, jobProfileCode);
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	/**
	 * All skills missing from Person to fulfill a job / Job Profile
	 */
	public List<Skill> getSkillsMissingFromPersonForJob(String personCode, 
														String jobCode,
														String jobProfileCode) 
			throws SQLException {
		List<Skill> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH " +
		    "   skills_job as " + 
			"     (SELECT skill_code " +
			"      FROM job_skill " +
			"      WHERE job_code = ?), " +
			"   skills_job_profile as " +
			"     (SELECT skill_code " +
			"      FROM job_profile_skill NATURAL JOIN job " +
			"      WHERE job_code = ?), " +
			"   skills_person as " +
			"     (SELECT skill_code " +
			"      FROM person_skill " +
			"      WHERE person_code = ?) " +
			" SELECT * " +
			" FROM skills NATURAL JOIN (skills_person MINUS " +
			"					        skills_job MINUS " + 
			"                           skills_job_profile) "
		);
		stmt.setString(1, jobCode);
		stmt.setString(2, jobProfileCode);
		stmt.setString(3, personCode);
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	// Inserts
	public int addSkillToPerson(Person person, Skill skill)
			throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(
			"INSERT INTO person_skill" +
			"  value(?, ?)");
		stmt.setString(1, person.getPersonCode());
		stmt.setString(2, skill.getSkillCode());
		return stmt.executeUpdate();
	}
	
	public int addSkillsToPerson(Person person, List<Skill> list)
			throws SQLException {
		int count = 0;
		connection.setAutoCommit(false);
		try {
			for (Skill skill : list) {
				PreparedStatement stmt = connection.prepareStatement(
					"INSERT INTO person_skill (person_code, skill_code)" +
					"  VALUES(?, ?)");
				stmt.setString(1, person.getPersonCode());
				stmt.setString(2, skill.getSkillCode());
				count += stmt.executeUpdate();
			}
			if (count != list.size()) {
				connection.rollback();
			} else {
				connection.commit();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		
		return count;
	}
	
	// Deletes
	public int removeSkillFromPerson(Person person, Skill skill) 
			throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(
			"DELETE FROM person_skill" +
			"WHERE person_code = ? AND" +
			"      skill_code = ?");
		stmt.setString(1, person.getPersonCode());
		stmt.setString(2, skill.getSkillCode());
		return stmt.executeUpdate();
	}
	
	public int removeSkillsFromPerson(Person person, List<Skill> list)
			throws SQLException {
		int count = 0;
		connection.setAutoCommit(false);
		try {
			for (Skill skill : list) {
				PreparedStatement stmt = connection.prepareStatement(
					"DELETE FROM person_skill " +
					"WHERE person_code = ? AND " +
					"      skill_code = ?");
				stmt.setString(1, person.getPersonCode());
				stmt.setString(2, skill.getSkillCode());
				count += stmt.executeUpdate();
			}
			if (count != list.size()) {
				connection.rollback();
			} else {
				connection.commit();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		
		return count;
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
				results.getString("skill_level"))
			);
		}
		return list;
	}
}
