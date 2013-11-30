package db.test;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

public class TestQueries {
	private Connection conn;
	
	//1. List a company’s workers by names. 
	public ResultSet getCompanyEmployees(String company_id) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT person_code, last_name, first_name " +
			"FROM person NATURAL JOIN employment NATURAL JOIN " +
			"	  job NATURAL JOIN company " +
			"WHERE company_id = ? AND " +
			"      start_date < CURRENT_DATE AND " +
			"      (end_date > CURRENT_DATE OR end_date IS NULL) ");
		stmt.setString(1, company_id);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

	//2. List a company’s staff by salary in descending order. 
	public ResultSet getCompanyFullTimeEmployeesBySalaryDesc(String company_code) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT person_code, last_name, first_name, pay_rate " +
			"FROM person NATURAL JOIN employment NATURAL JOIN " +
			"	  job NATURAL JOIN company " +
			"WHERE company_code = ? AND pay_type = 'salary' AND " +
			"      start_date < CURRENT_DATE AND " +
			"      (end_date > CURRENT_DATE OR end_date IS NULL) " +
			"ORDER BY DESC ");
		stmt.setString(1, company_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

	//3. List companies’ labor cost (total salaries and wage rates by 1920 hours) 
	//   in descending order.
	public ResultSet getCompanyCurrentTotalLaborCost(String company_code) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT sum(calculated_wage) " +
			"FROM person NATURAL JOIN employment NATURAL JOIN " +
			"	  job NATURAL JOIN company " +
			"WHERE company_code = ? AND " +
			"      start_date < CURRENT_DATE AND " +
			"      (end_date > CURRENT_DATE OR end_date IS NULL) " +
			"GROUP BY person_code " +
			"HAVING calculated_wage as " +
			"     CASE " +
			"         WHEN pay_rate = 'salary' THEN pay_rate " +
			"         WHEN pay_rate = 'wage' THEN pay_rate * 1920 " +
			"         ELSE NULL" +
			"     END ");
		stmt.setString(1, company_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//4. Find all the jobs a person is currently holding. 
	public ResultSet getCurrentEmployment(String person_code) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT job_code, job_profile_title " +
			"FROM person NATURAL JOIN employment NATURAL JOIN " +
			"	  job NATURAL JOIN job_profile " +
			"WHERE person_code = ? AND " +
			"      start_date < CURRENT_DATE AND " +
			"      (end_date > CURRENT_DATE OR end_date IS NULL) "
			);
		stmt.setString(1, person_code);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//5. List all the workers who are working for a specific project. 
	public ResultSet getCurrentProjectEmployees(String project_code)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT person_code, last_name, first_name " +
			"FROM person NATURAL JOIN employment NATURAL JOIN " +
			"	  job NATURAL JOIN job_project " +
			"WHERE project_code = ? AND " +
			"      start_date < CURRENT_DATE AND " +
			"      (end_date > CURRENT_DATE OR end_date IS NULL) "
			);
		stmt.setString(1, project_code);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//6. List a person’s knowledge/skills in a readable format. 
	public ResultSet getPersonSkills(String person_code)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT skill_name " +
			"FROM skill NATURAL JOIN person_skill " +
			"WHERE person_code = ? "
			);
		stmt.setString(1, person_code);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//7. List the skill gap of a worker between his/her job(s) and his/her skills. 
	public ResultSet getCurrentSkillGap(String person_code)
			throws SQLException {
		// Is this the right direction?
		PreparedStatement stmt = conn.prepareStatement(
			"(SELECT DISTINCT skill_code " +
			" FROM person NATURAL JOIN employment NATURAL JOIN " +
			"      job NATURAL JOIN job_profile NATURAL JOIN " +
			"      job_profile_skill " +
			" WHERE person_code = ? AND " +
			"      start_date < CURRENT_DATE AND " +
			"      (end_date > CURRENT_DATE OR end_date IS NULL) ) " +
			"EXCEPT " +
			"(SELECT skill_code " +
			" FROM person NATURAL JOIN person_skill " +
			" WHERE person_code = ?) "
			);
		stmt.setString(1, person_code);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//8. List the required knowledge/skills of a job profile in a readable format. 
	public ResultSet getRequiredSkills(String job_profile_code)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT skill_name " +
			"FROM job_profile_skill NATURAL JOIN skill " +
			"WHERE job_profile_code = ? "
			);
		stmt.setString(1, job_profile_code);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	
	//9. List a person’s missing knowledge/skills for a specific job in a readable format
	//Maybe surround with a select which gives only the skill_name
	public ResultSet getSkillGapOfPersonForJob(String person_code, String job_code)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"(SELECT skill_code, skill_name " +
			" FROM job NATURAL JOIN job_profile NATURAL JOIN " +
			"      job_profile_skill NATURAL JOIN skill " +
			" WHERE job_code = ?) " +
			"EXCEPT " +
			"(SELECT skill_code, skill_name " +
			" FROM person NATURAL JOIN " +
			"      person_skill NATURAL JOIN skill " +
			" WHERE person_code = ?) "
			);
		stmt.setString(1, person_code);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//***************
	//10. Find the course set(s) with the minimum number of courses 
	//    that can cover a given skill set. 
	
	// First part of #10
	public ResultSet getAllCourseCombinationsSatisfyingJobRequirements(String job_code) 
			throws SQLException {
		// Does anything need to be made DISTINCT?
		// Doesn't quite work.  Basically needs a base case.
		PreparedStatement stmt = conn.prepareStatement(
			"WITH recursive course_set(set_id, course_code) as " +
			"	(SELECT course_code as set_id, course_code" +	// Add the set_id and course_code from 
			"    FROM course_set as CS1 JOIN course as C" +		// the join-produced rows of the accumulating set and course
			"    WHERE ( " +									// WHERE
			"            (SELECT DISTINCT skill_code" +			// 	Get the skill_codes provided by the course (outer)
			"    	      FROM course_skill" +
			"			  WHERE skill_code.course_code = C.course_code)" +
			"            INTERSECT" +							
			"            ( (SELECT skill_code" +				//  Get the skill_codes required by the job_profile
			"               FROM job NATURAL JOIN job_profile_code NATURAL JOIN" +
			"                    job_profile_skill" +
			"               WHERE job_code = ?)" +
			"              EXCEPT" +							// Remove from the skill codes provided by job_profile
			"              (SELECT DISTINCT skill_code" +		// the skill codes contained in the current course set (outer)
			"               FROM course_set as CS2 NATURAL JOIN course_skill" + 
			"               WHERE CS1.set_id = CS2.set_id) ) )" +  //
			"          IS NOT NULL)" +
			"SELECT set_id, course_code" +
			"FROM course_set" +
			"GROUP BY set_id"
			);
		stmt.setString(1, job_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

	// Actual #10.
	public ResultSet getLeastCoursesForJob(String job_code)
			throws SQLException {
		// See above, just count and min on the course_code
		PreparedStatement stmt = conn.prepareStatement(""
			);
		stmt.setString(1, job_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//11. List the courses that can make up the missing knowledge/skills 
	//    for a person to pursue a specific job. 
	public ResultSet getCoursesForPersonToGetJob(String person_code, String job_code) 
			throws SQLException {
		// See above
		// and subtract Person's skills from the original skill set.
		// List all of the courses.
		PreparedStatement stmt = conn.prepareStatement(""
			);
		stmt.setString(1, job_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//12. Find the cheapest way to make up one’s skill gap by showing the courses to take. 
	public ResultSet getCheapestCourseSetForGap(String person_code, String job_code)
			throws SQLException {
		// See above
		// and subtract Person's skills from the original skill set.
		// This time, instead of listing all the courses, find the set with the
		// cheapest total.
		PreparedStatement stmt = conn.prepareStatement(""
			);
		stmt.setString(1, job_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//13. Find the “shortest” way to make up one’s skill gap by 
	//    taking minimum number of courses. (Show the courses to take.) 
	public ResultSet getShortestCourseSetForGap(String person_code, String job_code)
			throws SQLException {
		// See above, just count and min on the course_code
		// and subtract Person's skills from the original skill set.
		// This time, instead of listing all the courses, find the set with the
		// cheapest total.
		PreparedStatement stmt = conn.prepareStatement(""
			);
		stmt.setString(1, job_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//***************
	//14. List all the possible job profiles that a person is qualified. 
	public ResultSet getJobProfileQualifiedFor(String person_code)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT job_profile_code, job_profile_title" +
			"FROM job_profile as JP" +
			"WHERE ( (SELECT skill_code" +
			"         FROM job_profile NATURAL JOIN job_profile_skill" +
			"         WHERE JP.job_profile_code = job_profile.job_profile_code)" +
			"        EXCEPT" +
			"        (SELECT skill_code" +
			"         FROM person NATURAL JOIN person_skill" +
			"         WHERE person_code=?) ) IS NOT NULL" +
			"      AND" +
			"      ( (SELECT certificate_code" +
			"         FROM job_profile NATURAL JOIN job_profile_certificate" +
			"         WHERE JP.job_profile_code = job_profile.job_profile_code)" +
			"        EXCEPT" +
			"        (SELECT skill_code" +
			"         FROM person NATURAL JOIN person_skill" +
			"         WHERE person_code=?) ) IS NOT NULL "
			);
		stmt.setString(1, person_code);
		stmt.setString(2, person_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//15. Find the job with the highest pay rate for a person according to his/her skills. 
	// Assumes this is "all time" high.  
	public ResultSet getHighestPayingJobGivenSkills(String person_code, String job_code)
			throws SQLException {
		// Make above a "WITH" statement for job_profiles.
		// Then remove based on the additional "job" requirements
		// Then calculate using CASE statement, and GROUPBY with max.
		PreparedStatement stmt = conn.prepareStatement(
			""
			);
		stmt.setString(1, job_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//16. List all the names along with the emails of the persons who are 
	//    qualified for a job profile. 
	public ResultSet getAllQualifiedForJobProfile(String job_profile_code)
			throws SQLException {
		// Similar to above, just reverse the EXCEPTS.
		PreparedStatement stmt = conn.prepareStatement(
			""
			);
		stmt.setString(1, job_profile_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//17. Suppose there is a new job profile that has nobody qualified. 
	//    List the persons who miss the least number of 
	//    skills and report the “least number”.
	//Not Done
	public ResultSet getNearestQualifiedForJobProfile(String job_profile_code)
			throws SQLException {
		// Just keep track of certificates and skills as a count 
		// of "requirements".
		PreparedStatement stmt = conn.prepareStatement(
			"(SELECT skill_code" +
			" FROM job_profile_skill" +
			" WHERE job_profile_code = ?)" +
			"EXCEPT" +
			"(SELECT skill_code" +
			" FROM person_skill" +
			" WHERE person_code = P.person_code)" +
			"" +
			"(SELECT certificate_code" +
			" FROM job_profile_certificate" +
			" WHERE job_profile_code = ?)" +
			"EXCEPT" +
			"(SELECT skill_code" +
			" FROM person_certificate" +
			" WHERE person_code = P.person_code)" +
			""
			);
		stmt.setString(1, job_profile_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
}
