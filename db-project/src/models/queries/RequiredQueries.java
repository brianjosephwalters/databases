package models.queries;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

public class RequiredQueries {
	private Connection conn;
	
	public RequiredQueries(Connection connection) {
		this.conn = connection;
	}
	
	//1. List a company’s workers by names. 
	public ResultSet getCompanyEmployees(String company_id) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				" SELECT person_code, last_name, first_name " +
				" FROM person NATURAL JOIN employment NATURAL JOIN job " +
				" WHERE company_code = ? AND " +
				" start_date < CURRENT_DATE AND " +
				" (end_date > CURRENT_DATE OR end_date is NULL)",
				ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, company_id);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

	//2. List a company’s staff by salary in descending order. 
	public ResultSet getCompanyFullTimeEmployeesBySalaryDesc(String companyCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" SELECT person_code, last_name, first_name, pay_rate " +
			" FROM person NATURAL JOIN employment NATURAL JOIN " +
			"      job NATURAL JOIN company " +
			" WHERE company_code = ? AND  " +
			"       pay_type = 'salary' AND " +
			" start_date < CURRENT_DATE AND " +
			"       (end_date > CURRENT_DATE OR end_date IS NULL) ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, companyCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

	//3. List companies’ labor cost (total salaries and wage rates by 1920 hours) 
	//   in descending order.
	public ResultSet getCompanyCurrentTotalLaborCost(String company_code) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH " +  
			" company_employees as ( " + 
			"   SELECT *  " +
			"   FROM person NATURAL JOIN employment NATURAL JOIN job " +
			"   WHERE company_code = ? AND  " +
			"         start_date < current_date AND " +
			"         (end_date > current_date OR end_date is NULL) ), " +
			" person_wages as ( " +
			"   SELECT person_code, " + 
			"        CASE  " +
			"            WHEN pay_type = 'salary' THEN pay_rate " + 
			"            WHEN pay_type = 'wage' THEN pay_rate * 1920 " + 
			"            ELSE NULL  " +
			"        END as calc_wage  " +
			"   FROM company_employees " +
			"   ORDER BY calc_wage DESC ) " +
			" SELECT person_code, sum(calc_wage) " +
			 "FROM person_wages " +
			" GROUP BY person_code " +
			" ORDER BY sum(calc_wage) DESC ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, company_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//4. Find all the jobs a person is currently holding. 
	public ResultSet getCurrentEmployment(String personCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" SELECT job_code, job_profile_title " +
			" FROM person NATURAL JOIN employment NATURAL JOIN " +
			"      job NATURAL JOIN job_profile " +
			" WHERE person_code = ? AND " +
			"       start_date < CURRENT_DATE AND " +
			"       (end_date > CURRENT_DATE OR end_date IS NULL) ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//5. List all the workers who are working for a specific project. 
	public ResultSet getCurrentProjectEmployees(String projectCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT person_code, last_name, first_name " +
			"FROM person NATURAL JOIN employment NATURAL JOIN " +
			"     job NATURAL JOIN job_project " +
			"WHERE project_code = ? AND  " +
			"      start_date < CURRENT_DATE AND " +
			"      (end_date > CURRENT_DATE OR end_date IS NULL) ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, projectCode);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//6. List a person’s knowledge/skills in a readable format. 
	public ResultSet getPersonSkills(String personCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" SELECT skill_name " +
			" FROM skill NATURAL JOIN person_skill " +
			" WHERE person_code = ? ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
			);
		stmt.setString(1, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//7. List the skill gap of a worker between his/her job(s) and his/her skills. 
	public ResultSet getCurrentSkillGap(String personCode)
			throws SQLException {
		// Is this the right direction?
		PreparedStatement stmt = conn.prepareStatement(
			" WITH " +  
			" person_current_jobs as ( " + 
			"   SELECT job_code, job_profile_code " + 
			"   FROM employment NATURAL JOIN job " + 
			"   WHERE person_code = ? and " + 
			"         start_date < current_date and " + 
			"         (end_date > current_date or end_date is null) ) " + 
			" (SELECT skill_code " + 
			" FROM person_current_jobs NATURAL JOIN job_skill " + 
			" UNION " + 
			" SELECT skill_code " + 
			" FROM person_current_jobs NATURAL JOIN job_profile_skill) " + 
			" MINUS " + 
			" SELECT skill_code " + 
			" FROM person_skill " + 
			" WHERE person_code = ? ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//8. List the required knowledge/skills of a job profile in a readable format. 
	public ResultSet getRequiredSkills(String job_profile_code)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT skill_name " +
			"FROM job_profile_skill NATURAL JOIN skill " +
			"WHERE job_profile_code = ? ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
			);
		stmt.setString(1, job_profile_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}	
	
	//9. List a person’s missing knowledge/skills for a specific job in a readable format
	//Maybe surround with a select which gives only the skill_name
	public ResultSet getSkillGapOfPersonForJob(String personCode, String jobCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" SELECT skill_name " + 
			" FROM skill NATURAL JOIN " + 
			" (  (SELECT skill_code " + 
			"     FROM job_skill " + 
			"     WHERE job_code = ? " + 
			"    UNION " + 
			"     SELECT skill_code " + 
			"     FROM job_profile_skill NATURAL JOIN job " + 
			"     WHERE job_code = ? ) " + 
			"   MINUS " + 
			"   SELECT skill_code " + 
			"   FROM person_skill " + 
			"   WHERE person_code = ? ) ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobCode);
		stmt.setString(2, jobCode);
		stmt.setString(3, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;	
	}
	
	//***************
	//10. Find the course set(s) with the minimum number of courses 
	//    that can cover a given skill set. 
	
	// First part of #10
	public ResultSet getAllCourseCombinationsSatisfyingJobRequirements(String jobCode) 
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
		stmt.setString(1, jobCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

	// Actual #10.
	public ResultSet getLeastCoursesForJob(String jobCode)
			throws SQLException {
		// See above, just count and min on the course_code
		PreparedStatement stmt = conn.prepareStatement(""
			);
		stmt.setString(1, jobCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//11. List the courses that can make up the missing knowledge/skills 
	//    for a person to pursue a specific job. 
	public ResultSet getCoursesForPersonToGetJob(String person_code, String jobCode) 
			throws SQLException {
		// See above
		// and subtract Person's skills from the original skill set.
		// List all of the courses.
		PreparedStatement stmt = conn.prepareStatement(""
			);
		stmt.setString(1, jobCode);
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
	public ResultSet getJobProfileQualifiedFor(String personCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" SELECT job_profile_code, job_profile_title " +
			" FROM job_profile JP " +
			" WHERE NOT EXISTS ( " +
			"        (SELECT skill_code " +
			"         FROM job_profile_skill " +
			"         WHERE JP.job_profile_code = job_profile_skill.job_profile_code) " +
			"        MINUS " +
			"        (SELECT skill_code " +
			"         FROM person NATURAL JOIN person_skill " +
			"         WHERE person_code = ?) ) " +
			"      AND  " +
			"      NOT EXISTS ( " +
			"        (SELECT certificate_code " +
			"         FROM job_profile_certificate " +
			"         WHERE JP.job_profile_code = job_profile_code) " +
			"        MINUS " +
			"        (SELECT skill_code " +
			"         FROM person NATURAL JOIN person_skill " +
			"         WHERE person_code = ?) ) ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//15. Find the job with the highest pay rate for a person according to his/her skills. 
	// Assumes this is "all time" high.  
	public ResultSet getHighestPayingJobGivenSkills(String personCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"  WITH " + 
			"  job_profiles_qualified as ( " +
			"   SELECT job_profile_code " +
			"   FROM job_profile JP " +
			"   WHERE NOT EXISTS ( " +
			"         (SELECT skill_code " +
			"          FROM job_profile_skill " +
			"          WHERE JP.job_profile_code = job_profile_code) " +
			"         MINUS " +
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
			"          WHERE person_code = ?) ) ), " +
			" jobs_qualified as ( " +
			"   SELECT job_code " +
			"   FROM job_profiles_qualified NATURAL JOIN job J " +
			"   WHERE NOT EXISTS ( " +
			"       (SELECT skill_code " +
			"        FROM job_skill " +
			"        WHERE J.job_code = job_code) " +
			"       MINUS " +
			"       (SELECT skill_code " +
			"        FROM person NATURAL JOIN person_skill " +
			"        WHERE person_code = ?) ) " +
			"       AND  " +
			"       NOT EXISTS ( " +
			"        (SELECT certificate_code " +
			"        FROM job_certificate " +
			"        WHERE J.job_code = job_code) " +
			"       MINUS " +
			"        (SELECT skill_code " +
			"        FROM person NATURAL JOIN person_skill " +
			"        WHERE person_code = ? ) ) ) " +
			" SELECT job_code, MAX(CASE  " +
			"             WHEN pay_type = 'salary' THEN pay_rate " + 
			"             WHEN pay_type = 'wage' THEN pay_rate * 1920 " + 
			"             ELSE NULL  " +
			"            END) as max_pay " +
			" FROM job NATURAL JOIN jobs_qualified " +
			" GROUP BY job_code ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		stmt.setString(3, personCode);
		stmt.setString(4, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//16. List all the names along with the emails of the persons who are 
	//    qualified for a job profile. 
	public ResultSet getAllQualifiedForJobProfile(String jobProfileCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH required_skills as ( " +
			" 	    SELECT skill_code " +
			" 	    FROM job_profile_skill " +
			"	    WHERE job_profile_code = ?) " +
			"SELECT last_name, first_name, email " +
			"FROM person P " +
			"WHERE NOT EXISTS ( " +
			"       SELECT * FROM required_skills " +
			"		MINUS " +
			"       SELECT skill_code " +
			"       FROM person_skill " +
			"       WHERE P.person_code = person_code) " +
			"      AND " +
			"      NOT EXISTS ( " +
			"        SELECT * FROM required_skills " +
			"        MINUS " +
			"        (SELECT certificate_code " +
			"        FROM earns " +
			"        WHERE P.person_code = person_code) ) ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//17. Suppose there is a new job profile that has nobody qualified. 
	//    List the persons who miss the least number of 
	//    skills and report the “least number”.
	public ResultSet getNearestQualifiedForJobProfile(String jobProfileCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"WITH missing_skills (person_code, num_missing) as ( " +
			"	SELECT person_code, COUNT(skill_code) " +
			"	FROM person P, (SELECT skill_code  " +
			"	                FROM job_profile_skill " + 
			"	                WHERE job_profile_code = ?) J " + 
			"	WHERE J.skill_code IN ( " +
			"	   SELECT skill_code " +
			"	   FROM job_profile_skill " +
			"	   WHERE job_profile_code = ? " +
			"	  MINUS " +
			"	   SELECT skill_code " +
			"	   FROM person_skill " +
			"	   WHERE person_code = P.person_code ) " +
			"	GROUP BY person_code ), " +
			"	min_num_missing (min_missing) as ( " +
			"	  SELECT min(num_missing) " +
			"	  FROM missing_skills ) " +
			"	SELECT person_code, num_missing " +
			"	FROM missing_skills, min_num_missing " +
			"	WHERE num_missing = min_num_missing.min_missing ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
			);
		stmt.setString(1, jobProfileCode);
		stmt.setString(2, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	// 18.  Make a 'missing-one' list that lists people who miss only one 
	//      skill for a specified job profile
	public ResultSet getPeopleMissingOneSkillForJobProfile(String jobProfileCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH missing_skills (person_code, num_missing) as ( " +
			"    SELECT person_code, COUNT(skill_code) " +
			"     FROM person P, (SELECT skill_code  " +
			"                     FROM job_profile_skill " + 
			"                     WHERE job_profile_code = ?) J " + 
			"     WHERE J.skill_code IN ( " +
			"        SELECT skill_code " +
			"        FROM job_profile_skill " +
			"        WHERE job_profile_code = ? " +
			"       MINUS " +
			"        SELECT skill_code " +
			"        FROM person_skill " +
			"        WHERE person_code = P.person_code ) " +
			"     GROUP BY person_code ) " +
			" SELECT person_code, num_missing " +
			" FROM missing_skills " +
			" WHERE num_missing = 1 ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		stmt.setString(2, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	// 19.  List the skill_code and the number of people in the missing-one list 
	//      for a given job_profile in the descending order of the people counts.  
	//      IE, sort by skill_code, where we count the number of people missing 
	//      that skill.
	public ResultSet getSkillsMissedByPeople(String jobProfileCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"WITH " + 
			" missing_skills (person_code, num_missing) as ( " +
			"     SELECT person_code, COUNT(skill_code) " +
			"     FROM person P, (SELECT skill_code  " +
			"                     FROM job_profile_skill  " +
			"                     WHERE job_profile_code = ?) J " + 
			"     WHERE J.skill_code IN ( " +
			"        SELECT skill_code " +
			"        FROM job_profile_skill " +
			"        WHERE job_profile_code = ? " +
			"       MINUS " +
			"        SELECT skill_code " +
			"        FROM person_skill " +
			"        WHERE person_code = P.person_code ) " +
			"     GROUP BY person_code ), " +
			" people_lacking_one (person_code) as ( " +
			"     SELECT person_code " +
			"     FROM missing_skills " +
			"     WHERE num_missing = 1 )     " +
			" SELECT skill_code, COUNT(person_code) as num_people_lacking " +
			" FROM people_lacking_one P , (SELECT skill_code  " +
			"                              FROM job_profile_skill " + 
			"                              WHERE job_profile_code = ?) J " + 
			" WHERE J.skill_code IN ( " +
			"      SELECT skill_code " +
			"      FROM job_profile_skill " +
			"      WHERE job_profile_code = ? " +
			"     MINUS " +
			"      SELECT skill_code " +
			"      FROM person_skill " +
			"      WHERE person_code = P.person_code ) " +
			" GROUP BY skill_code  ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		stmt.setString(2, jobProfileCode);
		stmt.setString(3, jobProfileCode);
		stmt.setString(4, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	// 20.  For a specified job profile and a given small number k, 
	//      make a 'missing-k' list that lists the people's IDS and the 
	//      number of missing skills for the people who miss only up to k skills 
	//      in the descending order of missing skills.
	public ResultSet getPeopleMissingUpToKSkills(String jobProfileCode, Integer K)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH missing_skills (person_code, num_missing) as ( " +
			"     SELECT person_code, COUNT(skill_code) " +
			"     FROM person P, (SELECT skill_code  " +
			"                     FROM job_profile_skill " + 
			"                     WHERE job_profile_code = ?) J " + 
			"     WHERE J.skill_code IN ( " +
			"        SELECT skill_code " +
			"        FROM job_profile_skill " +
			"        WHERE job_profile_code = ? " +
			"       MINUS " +
			"       SELECT skill_code " +
			"        FROM person_skill " +
			"        WHERE person_code = P.person_code ) " +
			"     GROUP BY person_code ) " +
			" SELECT person_code, num_missing " +
			" FROM missing_skills " +
			" WHERE num_missing <= ? " +
			" ORDER BY num_missing DESC ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		stmt.setString(2, jobProfileCode);
		stmt.setInt(3, K);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	// 21.  Given a job profile and its corresponding missing-k list specified in 20, 
	//		Find every skill that is needed by at least one person in the given 
	//		missing-k list.  List each skill_code and the number of people who need 
	//		it in the descending order of the people counts.
	public ResultSet getUpToKSkillsMissedByPeople(String jobProfileCode, Integer K)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH missing_skills (person_code, num_missing) as ( " +
			"     SELECT person_code, COUNT(skill_code) " +
			"     FROM person P, (SELECT skill_code  " +
			"                     FROM job_profile_skill  " +
			"                     WHERE job_profile_code = ?) J " + 
			"     WHERE J.skill_code IN ( " +
			"        SELECT skill_code " +
			"        FROM job_profile_skill " +
			"        WHERE job_profile_code = ? " +
			"       MINUS " +
			"        SELECT skill_code " +
			"        FROM person_skill " +
			"        WHERE person_code = P.person_code ) " +
			"     GROUP BY person_code ), " +
			" people_lacking_at_most_x (person_code, num_missing) as ( " +
			"     SELECT person_code, num_missing " +
			"     FROM missing_skills " +
			"     WHERE num_missing <= ? )" +
			" SELECT skill_code, COUNT(person_code) as num_people_lacking " +
			" FROM people_lacking_at_most_x P , (SELECT skill_code  " +
			"                                    FROM job_profile_skill  " +
			"                                    WHERE job_profile_code = ?) J " + 
			" WHERE J.skill_code IN ( " +
			"      SELECT skill_code " +
			"      FROM job_profile_skill " +
			"      WHERE job_profile_code = ? " +
			"     MINUS " +
			"      SELECT skill_code " +
			"      FROM person_skill " +
			"      WHERE person_code = P.person_code ) " +
			" GROUP BY skill_code  ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		stmt.setString(2, jobProfileCode);
		stmt.setInt(3, K);
		stmt.setString(4, jobProfileCode);
		stmt.setString(5, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
}
