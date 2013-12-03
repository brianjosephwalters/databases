package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Job;
import models.JobReadable;

public class JobQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public JobQueries (Connection connection) {
		this.connection = connection;
	}

	// Queries
	public List<Job> getJob(String jobCode) 
			throws SQLException {
		List<Job> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM job " +
			" WHERE job_code = ?");
		stmt.setString(1, jobCode);
		list = getListOfJobs(stmt);
		return list;
	}
	
	public List<JobReadable> getJobReadable(String jobCode) 
			throws SQLException {
		List<JobReadable> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM job NATURAL JOIN job_profile NATURAL JOIN company" +
			" WHERE job_code = ?");
		stmt.setString(1, jobCode);
		list = getListOfJobsReadable(stmt);
		return list;
	}
	
	public List<Job> getAllJobs() 
			throws SQLException {
		List<Job> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM job ");
		list = getListOfJobs(stmt);
		
		return list;
	}
	
	public List<JobReadable> getAllJobsReadable() 
			throws SQLException {
		List<JobReadable> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM job NATURAL JOIN job_profile NATURAL JOIN company ");
		list = getListOfJobsReadable(stmt);
		
		return list;
	}
	
	public List<Job> getAvailableJobs() 
			throws SQLException {
		List<Job> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT job_code, job_profile_code, company_code " +
			" FROM job " +
		    " WHERE opening_date < current_date AND" +
			"       (closing_date > current_date OR " +
		    "        closing_date is null) " +
			" MINUS " +
			" SELECT job_code, job_profile_code, company_code " +
			" FROM employment NATURAL JOIN job" +
			" WHERE end_date is null OR " +
            "       end_date > current_date) "
			);
		list = getListOfJobs(stmt);
		
		return list;
	}
	
	public List<JobReadable> getAvailableJobsReadable() 
			throws SQLException {
		List<JobReadable> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH available_jobs as " +
		    "   (SELECT job_code, job_profile_code, company_code, job_type, pay_rate, opening_date, closing_date " +
			"    FROM job " +
		    "    WHERE opening_date < current_date AND" +
			"          (closing_date > current_date OR " +
		    "           closing_date is null) " +
			"   MINUS " +
			"    SELECT job_code, job_profile_code, company_code, job_type, pay_rate, opening_date, closing_date " +
			"	 FROM employment NATURAL JOIN job" +
			"    WHERE end_date is null OR " +
            "          end_date > current_date) " +
			" SELECT * " +
            " FROM available_jobs NATURAL JOIN job_profile NATURAL JOIN company "
			);
		list = getListOfJobsReadable(stmt);
		
		return list;
	}
	
	
	public List<Job> getJobsOfPerson(String personCode)
			throws SQLException {
		List<Job> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM job NATURAL JOIN employment " + 
			" WHERE person_code = ? ");
		stmt.setString(1, personCode);
		list = getListOfJobs(stmt);
		
		return list;
	}
	
	public List<JobReadable> getJobsOfPersonReadable(String personCode)
			throws SQLException {
		List<JobReadable> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM job NATURAL JOIN employment NATURAL JOIN job_profile NATURAL JOIN company" + 
			" WHERE person_code = ? ");
		stmt.setString(1, personCode);
		list = getListOfJobsReadable(stmt);
		
		return list;
	}
	
	public List<Job> getCurrentJobsOfPerson(String personCode)
			throws SQLException {
		List<Job> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM job NATURAL JOIN employment " + 
			" WHERE person_code = ? AND " + 
			"       start_date < CURRENT_DATE AND" +
			"      (end_date > CURRENT_DATE OR end_date IS NULL)");
		stmt.setString(1, personCode);
		list = getListOfJobs(stmt);
		
		return list;
	}
	
	
	public List<JobReadable> getCurrentJobsOfPersonReadable(String personCode) 
			throws SQLException {
		List<JobReadable> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM job NATURAL JOIN employment NATURAL JOIN job_profile NATURAL JOIN company " + 
			" WHERE person_code = ? AND " + 
			"       start_date < CURRENT_DATE AND" +
			"      (end_date > CURRENT_DATE OR end_date IS NULL)");
		stmt.setString(1, personCode);
		list = getListOfJobsReadable(stmt);
		
		return list;
	}

	public List<JobReadable> getJobsQualifiedForByPerson(String personCode) 
			throws SQLException {
		List<JobReadable> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH job_profiles_qualified as ( " +
			"   SELECT job_profile_code " +
			"   FROM job_profile JP " +
			"   WHERE NOT EXISTS ( " +
			"         (SELECT skill_code " +
			"          FROM job_profile_skill " +
			"          WHERE JP.job_profile_code = job_profile_code) " +
			"        MINUS " +
			"         (SELECT skill_code " +
			"          FROM person NATURAL JOIN person_skill " +
			"          WHERE person_code = ?) ) " +
			"       AND  " +
			"       NOT EXISTS ( " +
			"         (SELECT certificate_code " +
			"          FROM job_profile_certificate " +
			"          WHERE JP.job_profile_code = job_profile_code) " +
			"         MINUS " +
			"         (SELECT skill_code " +
			"          FROM person NATURAL JOIN person_skill " +
			"          WHERE person_code = ?) ) ) " +
			" SELECT * " +
			" FROM job_profiles_qualified NATURAL JOIN job J " + 
			"      NATURAL JOIN job_profile NATURAL JOIN company" +
			" WHERE NOT EXISTS ( " +
			"       (SELECT skill_code " +
			"        FROM job_skill " +
			"        WHERE J.job_code = job_code) " +
			"       MINUS " +
			"       (SELECT skill_code " +
			"        FROM person NATURAL JOIN person_skill " +
			"        WHERE person_code = ?) ) " +
			"      AND  " +
			"       NOT EXISTS ( " +
			"       (SELECT certificate_code " +
			"        FROM job_certificate " +
			"        WHERE J.job_code = job_code) " +
			"       MINUS " +
			"       (SELECT skill_code " +
			"        FROM person NATURAL JOIN person_skill " +
			"        WHERE person_code = ?) )	"
		);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		stmt.setString(3, personCode);
		stmt.setString(4, personCode);
		list = getListOfJobsReadable(stmt);
		return list;
	}
	
	public List<JobReadable> getAvailableJobsQualifiedForByPerson(String personCode) 
			throws SQLException {
		List<JobReadable> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH available_jobs as " +
		    "   (SELECT job_code, job_profile_code, company_code, job_type, pay_rate, opening_date, closing_date  " +
			"    FROM job " +
		    "    WHERE opening_date < current_date AND" +
			"          (closing_date > current_date OR " +
		    "           closing_date is null) " +
			"   MINUS " +
			"    SELECT job_code, job_profile_code, company_code, job_type, pay_rate, opening_date, closing_date  " +
			"	 FROM employment NATURAL JOIN job" +
			"    WHERE end_date is null OR " +
            "          end_date > current_date), " +
            " job_profiles_qualified as ( " +
			"   SELECT job_profile_code " +
			"   FROM job_profile JP " +
			"   WHERE NOT EXISTS ( " +
			"         (SELECT skill_code " +
			"          FROM job_profile_skill " +
			"          WHERE JP.job_profile_code = job_profile_code) " +
			"        MINUS " +
			"         (SELECT skill_code " +
			"          FROM person NATURAL JOIN person_skill " +
			"          WHERE person_code = ?) ) " +
			"       AND  " +
			"       NOT EXISTS ( " +
			"         (SELECT certificate_code " +
			"          FROM job_profile_certificate " +
			"          WHERE JP.job_profile_code = job_profile_code) " +
			"         MINUS " +
			"         (SELECT skill_code " +
			"          FROM person NATURAL JOIN person_skill " +
			"          WHERE person_code = ?) ) ) " +
			" SELECT * " +
			" FROM job_profiles_qualified NATURAL JOIN available_jobs J " + 
			"      NATURAL JOIN job_profile NATURAL JOIN company " +
			" WHERE NOT EXISTS ( " +
			"       (SELECT skill_code " +
			"        FROM job_skill " +
			"        WHERE J.job_code = job_code) " +
			"       MINUS " +
			"       (SELECT skill_code " +
			"        FROM person NATURAL JOIN person_skill " +
			"        WHERE person_code = ?) ) " +
			"      AND  " +
			"       NOT EXISTS ( " +
			"       (SELECT certificate_code " +
			"        FROM job_certificate " +
			"        WHERE J.job_code = job_code) " +
			"       MINUS " +
			"       (SELECT skill_code " +
			"        FROM person NATURAL JOIN person_skill " +
			"        WHERE person_code = ?) )	"
		);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		stmt.setString(3, personCode);
		stmt.setString(4, personCode);
		list = getListOfJobsReadable(stmt);
		return list;
	}
	
	public List<JobReadable> getJobsNotQualifiedForByPersonReadable(String personCode) 
			throws SQLException {
		List<JobReadable> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH available_jobs as " +
		    "   (SELECT job_code, job_profile_code, company_code, " +
		    "           job_type, pay_rate, opening_date, closing_date " +
			"    FROM job " +
		    "    WHERE opening_date < current_date AND" +
			"          (closing_date > current_date OR " +
		    "           closing_date is null) " +
			"   MINUS " +
			"    SELECT job_code, job_profile_code, company_code, " +
			"           job_type, pay_rate, opening_date, closing_date " +
			"	 FROM employment NATURAL JOIN job" +
			"    WHERE end_date is null OR " +
            "          end_date > current_date) " +
            " SELECT * " +
			" FROM available_jobs J NATURAL JOIN " + 
			"      job_profile NATURAL JOIN company " +
			" WHERE  " + 
			"   EXISTS " +
			"     ( SELECT skill_code  " +
			"        FROM job_profile_skill NATURAL JOIN job " +
			"        WHERE J.job_code = job_code  " +
			"       MINUS " +
			"        SELECT skill_code FROM person_skill WHERE person_code = ?  " +
			"     )  " +
			"   OR  " +
			"   EXISTS  " +
			"     ( SELECT skill_code  " +
			"       FROM job_skill  " +
			"       WHERE J.job_code = job_code  " +
			"      MINUS  " +
			"       SELECT skill_code FROM person_skill WHERE person_code = ? " +
			"     )  " +
			"  OR  " +
			"  EXISTS  " +
			"     (  SELECT certificate_code " +
			"        FROM job NATURAL JOIN job_profile_certificate " +
			"        WHERE J.job_code = job_code " +
			"       MINUS " +
			"        SELECT certificate_code FROM earns WHERE person_code = ?  " +
			"     ) " +
			"   OR  " +
			"   EXISTS  " +
			"     ( SELECT certificate_code  " +
			"       FROM job_certificate  " +
			"       WHERE J.job_code = job_code  " +
			"      MINUS  " +
			"       SELECT certificate_code FROM earns WHERE person_code = ? " +
			"     )  "
			);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		stmt.setString(3, personCode);
		stmt.setString(4, personCode);
		list = getListOfJobsReadable(stmt);
		return list;
	}
	
	
	// Still need to fix the below...
	public List<Job> getAllJobsQualifiedByPersonSkill(String personCode) 
			throws SQLException {
		List<Job> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT *  " +
			" FROM job J  " +
			" WHERE  " +
			"   NOT EXISTS  " +
			"     ( SELECT skill_code  " +
			"       FROM job_profile_skill NATURAL JOIN job  " +
			"       WHERE J.job_code = job_code  " +
			"      MINUS  " +
			"       SELECT skill_code FROM person_skill WHERE person_code = ? " +
			"     )  " +
			"   AND  " +
			"   NOT EXISTS  " +
			"     ( SELECT skill_code  " +
			"       FROM job_skill  " +
			"       WHERE J.job_code = job_code  " +
			"      MINUS  " +
			"       SELECT skill_code FROM person_skill WHERE person_code = ? " +
			"     )  ");
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		list = getListOfJobs(stmt);
		return list;
	}
	
	public List<Job> getAllJobsQualifiedByPersonCertificate(String personCode) 
			throws SQLException{
		List<Job> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT *  " +
			" FROM job J  " +
			" WHERE  " +
			"   NOT EXISTS  " +
			"     ( SELECT certificate_code  " +
			"       FROM job NATURAL JOIN job_profile_certificate  " +
			"       WHERE J.job_code = job_code  " +
			"      MINUS  " +
			"       SELECT certificate_code FROM earns WHERE person_code = ?  " +
			"   AND  " +
			"   NOT EXISTS  " +
			"     ( SELECT certificate_code  " +
			"       FROM job_certificate  " +
			"       WHERE J.job_code = job_code  " +
			"      MINUS  " +
			"       SELECT certificate_code FROM earns WHERE person_code = ? " +
			"     )  ");
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		list = getListOfJobs(stmt);
		return list;
	}
	
	public void hirePerson(String jobCode, String personCode)
		throws SQLException {
		
		PreparedStatement stmt1 = connection.prepareStatement(
				" UPDATE job " +
				" SET closing_date = CURRENT_DATE " +
				" WHERE job_code = ? "
		);
		PreparedStatement stmt2 = connection.prepareStatement(
				"INSERT INTO employment VALUES " +
				"	(?, ?, CURRENT_DATE, null)"
		);
		try {
			connection.setAutoCommit(false);
			stmt1.setString(1, jobCode);
			stmt2.setString(1, personCode);
			stmt2.setString(2, jobCode);
			
			stmt1.executeUpdate();
			stmt2.executeUpdate();
			connection.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.setAutoCommit(false);
		}
	}
	
	// Helper Functions
	private List<Job> getListOfJobs(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty person list
		List<Job> list = new ArrayList<Job>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new PhoneNumber from the results
			// and add it to the list.
			
			list.add( new Job(
				results.getString("job_code"),
				results.getString("job_profile_code"),
				results.getString("company_code"),
				results.getString("job_type"),
				results.getDouble("pay_rate"),
				results.getDate("opening_date"),
				results.getDate("closing_date"))
			);
		}
		return list;
	}
	
	private List<JobReadable> getListOfJobsReadable(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty person list
		List<JobReadable> list = new ArrayList<JobReadable>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new PhoneNumber from the results
			// and add it to the list.
			
			list.add( new JobReadable(
				results.getString("job_code"),
				results.getString("job_profile_title"),
				results.getString("company_name"),
				results.getString("job_type"),
				results.getDouble("pay_rate"),
				results.getDate("opening_date"),
				results.getDate("closing_date"))
			);
		}
		return list;
	}
}
