package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Job;
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
			" SELECT * " +
			" FROM skill ");
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
			" SELECT * " +
			" FROM skill NATURAL JOIN course_skill " + 
			" WHERE course_code = ? ");
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
			" SELECT * " +
			" FROM skill NATURAL JOIN person_skill " + 
			" WHERE person_code = ?");
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
			" WITH  lacked_skills AS" +
			"   ( (SELECT skill_code" + 
		    "      FROM skill)" +
		    "     MINUS " +
		    "     (SELECT skill_code" +
		    "      FROM skill NATURAL JOIN person_skill" +
		    "      WHERE person_code = ?) )" +
			" SELECT * " +
			" FROM skill NATURAL JOIN lacked_skills");
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
			" SELECT * " +
			" FROM skill NATURAL JOIN job_profile_skill" + 
			" WHERE job_profile_code = ?");
		stmt.setString(1, jobProfileCode);
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	public List<Skill> getSkillsForJob(String jobCode) 
			throws SQLException {
		List<Skill> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM skill NATURAL JOIN job_skill "  + 
			" WHERE job_code = ? ");
		stmt.setString(1, jobCode);
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	public List<Skill> getSkillsForJobAndJobProfile(String jobCode,
													String jobProfileCode) 
			throws SQLException {
		List<Skill> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH skill_codes AS" +
			" 	(SELECT skill_code " +
			" 	FROM skill NATURAL JOIN job_skill "  + 
			" 	WHERE job_code = ? " +
			" 	UNION " +
			" 	SELECT skill_code " +
			" 	FROM skill NATURAL JOIN job_profile_skill " +
			" 	WHERE job_profile_code = ?)" +
			" SELECT *" +
			" FROM skill NATURAL JOIN skill_codes");
		stmt.setString(1, jobCode);
		stmt.setString(2, jobProfileCode);
		list = getListOfSkills(stmt);
		
		return list;
	}
	
	/**
	 * All skills not required by a job.
	 */
	public List<Skill> getSkillsNotRequiredByJob(String jobCode) 
			throws SQLException {
		List<Skill> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH  lacked_skills AS" +
			"   ( (SELECT skill_code" + 
		    "      FROM skill)" +
		    "     MINUS " +
		    "     (SELECT skill_code" +
		    "      FROM skill NATURAL JOIN job_skill" +
		    "      WHERE job_code = ?) )" +
			" SELECT * " +
			" FROM skill NATURAL JOIN lacked_skills");
		stmt.setString(1, jobCode);
		list = getListOfSkills(stmt);
		
		return list;
	}	
	
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
			"      WHERE person_code = ?), " +
			"	missing_skills as ( (SELECT * FROM skills_job " +
			"                        INTERSECT" +
			"                         SELECT * FROM skills_job_profile ) " +
            "       			     MINUS " +
            "        			     SELECT * FROM skills_person ) " +
			" SELECT * " +
			" FROM skill NATURAL JOIN missing_skills "
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
	
	public int addSkillToJob(Job job, Skill skill)
			throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(
			"INSERT INTO job_skill" +
			"  value(?, ?)");
		stmt.setString(1, job.getJobCode());
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
					" INSERT INTO person_skill (person_code, skill_code) " +
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
	
	public int addSkillsToJob(Job job, List<Skill> list)
			throws SQLException {
		int count = 0;
		connection.setAutoCommit(false);
		try {
			for (Skill skill : list) {
				PreparedStatement stmt = connection.prepareStatement(
					" INSERT INTO job_skill (job_code, skill_code) " +
					"  VALUES(?, ?)");
				stmt.setString(1, job.getJobCode());
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
	
	// Updates
	public int updateSkill(Skill skill) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" UPDATE skill " +
			" SET skill_name " +
			"     skill_description " +
			"     skill_level " +
			" WHERE skill_code = ?"
		);
		stmt.setString(1, skill.getSkillName());
		stmt.setString(2, skill.getSkillDescription());
		stmt.setString(3, skill.getSkillLevel());
		stmt.setString(4, skill.getSkillCode());
		count = stmt.executeUpdate();
		return count;
	}	
	
	// Deletes
	public int removeSkillFromPerson(Person person, Skill skill) 
			throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(
			" DELETE FROM person_skill " +
			" WHERE person_code = ? AND " +
			"       skill_code = ? ");
		stmt.setString(1, person.getPersonCode());
		stmt.setString(2, skill.getSkillCode());
		return stmt.executeUpdate();
	}
	
	public int removeSkillFromJob(Job job, Skill skill) 
			throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(
			" DELETE FROM job_skill " +
			" WHERE job_code = ? AND " +
			"       skill_code = ? ");
		stmt.setString(1, job.getJobCode());
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
					" DELETE FROM person_skill " +
					" WHERE person_code = ? AND " +
					"       skill_code = ? ");
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
	
	public int removeSkillsFromJob(Job job, List<Skill> list)
			throws SQLException {
		int count = 0;
		connection.setAutoCommit(false);
		try {
			for (Skill skill : list) {
				PreparedStatement stmt = connection.prepareStatement(
					" DELETE FROM job_skill " +
					" WHERE job_code = ? AND " +
					"       skill_code = ? ");
				stmt.setString(1, job.getJobCode());
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
				results.getClob("skill_description").toString(),
				results.getString("skill_level"))
			);
		}
		return list;
	}
}
