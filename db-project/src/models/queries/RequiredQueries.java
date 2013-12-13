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
	public ResultSet getCompanyEmployees(String companyCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				" SELECT last_name, first_name " + 
				" FROM person NATURAL JOIN employment NATURAL JOIN job " + 
				" WHERE company_code = ? AND " + 
				" start_date < CURRENT_DATE AND " + 
				" (end_date > CURRENT_DATE OR end_date is NULL) " + 
				" ORDER BY last_name  ",
				ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, companyCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	//1.a
	public ResultSet getCompanyEmployeesNoDuplicates(String companyCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				" SELECT DISTINCT last_name, first_name " + 
				" FROM person NATURAL JOIN employment NATURAL JOIN job " + 
				" WHERE company_code = ? AND " + 
				" start_date < CURRENT_DATE AND " + 
				" (end_date > CURRENT_DATE OR end_date is NULL) " + 
				" ORDER BY last_name",
				ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, companyCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

	//2. List a company’s staff by salary in descending order. 
	public ResultSet getCompanySalaryEmployeesBySalaryDesc(String companyCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" SELECT person_code, last_name, first_name, pay_rate " + 
			" FROM person NATURAL JOIN employment NATURAL JOIN " + 
			"      job NATURAL JOIN company " + 
			" WHERE company_code = ? AND  " + 
			"       pay_type = 'salary' AND " + 
			" start_date < CURRENT_DATE AND " + 
			"       (end_date > CURRENT_DATE OR end_date IS NULL) " + 
			" ORDER BY pay_rate DESC",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, companyCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	// 2a
	public ResultSet getCompanyFullTimeEmployeesBySalaryDesc(String companyCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" SELECT person_code, last_name, first_name, " +
			"     CASE  " + 
			"         WHEN pay_type = 'salary' THEN pay_rate  " + 
			"         WHEN pay_type = 'wage' THEN pay_rate * 1920  " + 
			"         ELSE NULL  " + 
			"     END as calc_wage  " + 
			" FROM person NATURAL JOIN employment NATURAL JOIN " + 
			"      job NATURAL JOIN company " + 
			" WHERE company_code = ? AND  " + 
			"       job_type = 'full-time' AND " + 
			" start_date < CURRENT_DATE AND " + 
			"       (end_date > CURRENT_DATE OR end_date IS NULL) " + 
			" ORDER BY calc_wage DESC " + 
			"",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, companyCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

	//3. List companies’ labor cost (total salaries and wage rates by 1920 hours) 
	//   in descending order.
	public ResultSet getAllCompanyCurrentTotalLaborCost() 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH   " + 
			" company_employees as (  " + 
			"   SELECT *  " + 
			"   FROM person NATURAL JOIN employment NATURAL JOIN job " + 
			"   WHERE start_date < current_date AND " + 
			"         (end_date > current_date OR end_date is NULL) ), " + 
			" person_wages as ( " + 
			"   SELECT company_code, person_code, " + 
			"        CASE  " + 
			"           WHEN pay_type = 'salary' THEN pay_rate  " + 
			"           WHEN pay_type = 'wage' THEN pay_rate * 1920  " + 
			"           ELSE NULL  " + 
			"       END as calc_wage  " + 
			"   FROM company_employees " + 
			"   ORDER BY calc_wage DESC ) " + 
			" SELECT company_code, company_name, sum(calc_wage) as total_wages " + 
			" FROM person_wages NATURAL JOIN company " + 
			" GROUP BY company_code, company_name " + 
			" ORDER BY sum(calc_wage) DESC",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	// 3.a
	public ResultSet getCurrentCompanyEmployeesWithWages(String companyCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH   " + 
			" company_employees as (  " + 
			"   SELECT *  " + 
			"   FROM person NATURAL JOIN employment NATURAL JOIN job " + 
			"   WHERE company_code = ? AND  " + 
			"         start_date < current_date AND " + 
			"         (end_date > current_date OR end_date is NULL) ) " + 
			" SELECT person_code, last_name, first_name, " + 
			"      CASE  " + 
			"          WHEN pay_type = 'salary' THEN pay_rate  " + 
			"          WHEN pay_type = 'wage' THEN pay_rate * 1920  " + 
			"          ELSE NULL  " + 
			"      END as calc_wage  " + 
			" FROM company_employees " + 
			" ORDER BY calc_wage DESC",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, companyCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	//3.b
	public ResultSet getCurrentCompanyEmployeesWithTotalWages(String companyCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH   " + 
			" company_employees as (  " + 
			"   SELECT *  " + 
			"   FROM person NATURAL JOIN employment NATURAL JOIN job " + 
			"   WHERE company_code = ? AND  " + 
			"         start_date < current_date AND " + 
			"         (end_date > current_date OR end_date is NULL) ), " + 
			" person_wages as ( " + 
			"   SELECT person_code, last_name, first_name, CASE  " + 
			"            WHEN pay_type = 'salary' THEN pay_rate  " + 
			"            WHEN pay_type = 'wage' THEN pay_rate * 1920  " + 
			"            ELSE NULL  " + 
			"        END as calc_wage  " + 
			"   FROM company_employees " + 
			"   ORDER BY calc_wage DESC ) " + 
			" SELECT person_code, last_name, first_name, " + 
			" sum(calc_wage) " + 
			" FROM person_wages " + 
			" GROUP BY person_code, last_name, first_name " + 
			" ORDER BY sum(calc_wage) DESC",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, companyCode);
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
	// 5.b
	public ResultSet getAllProjectEmployees(String projectCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" SELECT person_code, last_name, first_name " + 
			" FROM person NATURAL JOIN employment NATURAL JOIN " + 
			"      job NATURAL JOIN job_project " + 
			" WHERE project_code = ?",
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
	
	//10. Find the course set(s) with the minimum number of courses 
	//    that can cover a given skill set. 
	public ResultSet getLeastCoursesToCoverJobSkills( String jobProfileCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH  " + 
			" required_skills as ( " + 
			"   SELECT skill_code from job_profile_skill WHERE job_profile_code = ? ),  " + 
			" course_sets as ( " + 
			"   (SELECT course_code as A, null as B, null as C " + 
			"   FROM course_skill) " + 
			"   UNION " + 
			"   (SELECT A.course_code as A, B.course_code as B, null as C " + 
			"    FROM course_skill A, course_skill B " + 
			"    WHERE a.course_code < b.course_code) " + 
			"    UNION " + 
			"   (SELECT A.course_code as A, B.course_code as B, C.course_code as C " + 
			"    FROM course_skill A, course_skill B, course_skill C " + 
			"    WHERE a.course_code < b.course_code AND " + 
			"          b.course_code < c.course_code ) ), " + 
			" course_set_skills as ( " + 
			"   SELECT A, B, C, skill_code " + 
			"   FROM course_sets, course_skill " + 
			"   WHERE course_sets.A = course_skill.course_code OR " + 
			"         course_sets.B = course_skill.course_code OR " + 
			"         course_sets.C = course_skill.course_code), " + 
			" satisfying_sets as ( " + 
			"   SELECT *  " + 
			"   FROM ( " + 
			"       (SELECT A, B, C from course_set_skills) " + 
			"       MINUS " + 
			"       (SELECT A, B, C " + 
			"        FROM (  SELECT A, B, C, skill_code " + 
			"                FROM (SELECT A, B, C " + 
			"                      FROM course_set_skills), (SELECT * FROM required_skills) " + 
			"                MINUS " + 
			"                SELECT A, B, C, skill_code " + 
			"                FROM course_set_skills) ) ) ), " + 
			" sets_count as ( " + 
			" 					SELECT A, B, C, CASE " + 
			"                   WHEN B is null THEN 1 " + 
			"					WHEN C is null THEN 2 " + 
			"                   ELSE 3 " + 
			"                 END as num_courses " + 
			" FROM satisfying_sets ) " + 
			" SELECT * " + 
			" FROM sets_count WHERE num_courses = (SELECT min(num_courses) FROM sets_count) ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
			);
		stmt.setString(1, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//11. List the courses that can make up the missing knowledge/skills 
	//    for a person to pursue a specific job. 
	public ResultSet getCoursesForPersonToCoverMissingSkillsForJob(String personCode, String jobCode) 
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH  " + 
			" required_skills as ( " + 
			"    (SELECT skill_code FROM job_skill WHERE job_code = ? " + 
			"     UNION " + 
			"     SELECT skill_code FROM job_profile_skill NATURAL JOIN job " + 
			"     WHERE job_code = ? ) " + 
			"     MINUS " + 
			"     SELECT skill_code " + 
			"     FROM person_skill " + 
			"     WHERE person_code = ? " + 
			"    ),  " + 
			" course_sets as ( " + 
			"   (SELECT course_code as A, null as B, null as C " + 
			"    FROM course_skill) " + 
			"   UNION " + 
			"   (SELECT A.course_code as A, B.course_code as B, null as C " + 
			"    FROM course_skill A, course_skill B " + 
			"    WHERE a.course_code < b.course_code) " + 
			"   UNION " + 
			"   (SELECT A.course_code as A, B.course_code as B, C.course_code as C " + 
			"    FROM course_skill A, course_skill B, course_skill C " + 
			"    WHERE a.course_code < b.course_code AND " + 
			"          b.course_code < c.course_code ) ), " + 
			" course_set_skills as ( " + 
			"   SELECT A, B, C, skill_code " + 
			"   FROM course_sets, course_skill " + 
			"   WHERE course_sets.A = course_skill.course_code OR " + 
			"         course_sets.B = course_skill.course_code OR " + 
			"         course_sets.C = course_skill.course_code), " + 
			" satisfying_sets as ( " + 
			"   SELECT *  " + 
			"   FROM ( " + 
			"       (SELECT A, B, C from course_set_skills) " + 
			"       MINUS " + 
			"       (SELECT A, B, C " + 
			"        FROM (  SELECT A, B, C, skill_code " + 
			"                FROM (SELECT A, B, C " + 
			"                      FROM course_set_skills), (SELECT * FROM required_skills) " + 
			"                MINUS " + 
			"                SELECT A, B, C, skill_code " + 
			"                FROM course_set_skills) ) ) ), " + 
			" sets_count as ( " + 
			" 				  SELECT A, B, C, CASE " + 
			"                   WHEN B is null THEN 1 " + 
			"                   WHEN C is null THEN 2 " + 
			"                   ELSE 3 " + 
			"                 END as num_courses " + 
			" FROM satisfying_sets ) " + 
			" SELECT A, B, C, num_courses " + 
			" FROM sets_count WHERE num_courses = (SELECT min(num_courses) FROM sets_count)",
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
	//12. Find the cheapest way to make up one’s skill gap by showing the courses to take. 
	public ResultSet getCheapestCourseSetForGap(String jobProfileCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"WITH  " + 
				"required_skills as ( " + 
				"    SELECT skill_code FROM job_profile_skill WHERE job_profile_code = ? ),  " + 
				"available_courses as (  " + 
				"    SELECT UNIQUE course_code " + 
				"    FROM course_skill NATURAL JOIN course NATURAL JOIN section " + 
				"    WHERE status = 'active' AND " + 
				"          to_date(year, 'yyyy') >= current_date ), " + 
				"course_sets as ( " + 
				"    (SELECT course_code as A, null as B, null as C " + 
				"     FROM available_courses) " + 
				"    UNION " + 
				"    (SELECT A.course_code as A, B.course_code as B, null as C " + 
				"     FROM available_courses A, available_courses B " + 
				"     WHERE a.course_code < b.course_code ) " + 
				"    UNION " + 
				"    (SELECT A.course_code as A, B.course_code as B, C.course_code as C " + 
				"     FROM available_courses A, available_courses B, available_courses C " + 
				"     WHERE A.course_code < B.course_code AND " + 
				"           B.course_code < C.course_code ) ), " + 
				"course_set_skills as ( " + 
				"    SELECT A, B, C, skill_code " + 
				"    FROM course_sets, course_skill " + 
				"    WHERE course_sets.A = course_skill.course_code OR " + 
				"        course_sets.B = course_skill.course_code OR " + 
				"        course_sets.C = course_skill.course_code), " + 
				"satisfying_sets as ( " + 
				"    SELECT *  " + 
				"    FROM ( " + 
				"        (SELECT A, B, C from course_set_skills) " + 
				"        MINUS " + 
				"        (SELECT A, B, C " + 
				"         FROM (SELECT A, B, C, skill_code " + 
				"               FROM (SELECT A, B, C " + 
				"                     FROM course_set_skills), (SELECT * FROM required_skills) " + 
				"               MINUS " + 
				"               SELECT A, B, C, skill_code " + 
				"               FROM course_set_skills) ) ) " + 
				"    WHERE EXISTS (SELECT * FROM required_skills) ), " + 
				"satisfying_sets_course as ( " + 
				"    SELECT A, B, C, A as course_code " + 
				"    FROM satisfying_sets  " + 
				"   UNION " + 
				"    SELECT A, B, C, B as course_code " + 
				"    FROM satisfying_sets  " + 
				"   UNION " + 
				"    SELECT A, B, C, C as course_code " + 
				"    FROM satisfying_sets ), " + 
				"satisfying_sets_cost as ( " + 
				"    SELECT A, B, C, SUM(retail_price) as cost " + 
				"    FROM satisfying_sets_course NATURAL JOIN course " + 
				"    GROUP BY A, B, C ) " + 
				"SELECT A, B, C, cost " + 
				"FROM satisfying_sets_cost " + 
				"WHERE cost = (SELECT min(cost) " + 
				"              FROM satisfying_sets_cost) ",
				ResultSet.TYPE_SCROLL_SENSITIVE,
	            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//13. Find the “shortest” way to make up one’s skill gap by 
	//    taking minimum number of courses. (Show the courses to take.) 
	public ResultSet getShortestCourseSetForGap(String jobProfileCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"WITH  " + 
			"required_skills as ( " + 
			"    SELECT skill_code from job_profile_skill WHERE job_profile_code = ?),  " + 
			"available_courses as (  " + 
			"    SELECT UNIQUE course_code " + 
			"    FROM course_skill NATURAL JOIN course NATURAL JOIN section " + 
			"    WHERE status = 'active' AND " + 
			"        to_date(year, 'yyyy') >= current_date ), " + 
			"course_sets as ( " + 
			"    (SELECT course_code as A, null as B, null as C " + 
			"     FROM available_courses) " + 
			"    UNION " + 
			"    (SELECT A.course_code as A, B.course_code as B, null as C " + 
			"     FROM available_courses A, available_courses B " + 
			"     WHERE a.course_code < b.course_code ) " + 
			"    UNION " + 
			"    (SELECT A.course_code as A, B.course_code as B, C.course_code as C " + 
			"     FROM available_courses A, available_courses B, available_courses C " + 
			"     WHERE A.course_code < B.course_code AND " + 
			"           B.course_code < C.course_code ) ), " + 
			"course_set_skills as ( " + 
			"    SELECT A, B, C, skill_code " + 
			"    FROM course_sets, course_skill " + 
			"    WHERE course_sets.A = course_skill.course_code OR " + 
			"        course_sets.B = course_skill.course_code OR " + 
			"        course_sets.C = course_skill.course_code), " + 
			"satisfying_sets as ( " + 
			"    SELECT *  " + 
			"    FROM ( " + 
			"        (SELECT A, B, C from course_set_skills) " + 
			"        MINUS " + 
			"        (SELECT A, B, C " + 
			"         FROM (SELECT A, B, C, skill_code " + 
			"               FROM (SELECT A, B, C " + 
			"                     FROM course_set_skills), (SELECT * FROM required_skills) " + 
			"              MINUS " + 
			"               SELECT A, B, C, skill_code " + 
			"               FROM course_set_skills) ) ) " + 
			"    WHERE EXISTS (SELECT * FROM required_skills) ), " + 
			"sets_count as ( " + 
			"    SELECT A, B, C, CASE " + 
			"      WHEN B is null THEN 1 " + 
			"      WHEN C is null THEN 2 " + 
			"      ELSE 3 " + 
			"    END as num_courses " + 
			"FROM satisfying_sets ), " + 
			"min_sets as ( " + 
			"    SELECT A, B, C, num_courses " + 
			"    FROM sets_count  " + 
			"    WHERE num_courses = (SELECT min(num_courses) FROM sets_count)), " + 
			"satisfying_sets_course as ( " + 
			"    SELECT A, B, C, A as course_code " + 
			"    FROM min_sets  " + 
			"   UNION " + 
			"    SELECT A, B, C, B as course_code " + 
			"    FROM min_sets  " + 
			"   UNION " + 
			"    SELECT A, B, C, C as course_code " + 
			"    FROM min_sets ), " + 
			"satisfying_sets_sections as ( " + 
			"    SELECT A, B, C, course_code, section_code, year " + 
			"    FROM (satisfying_sets_course NATURAL JOIN course) NATURAL JOIN section " + 
			"    WHERE status = 'active' AND " + 
			"          to_date(year, 'yyyy') >= current_date ), " + 
			"satisfying_sets_soonest as (  " + 
			"    SELECT A, B, C, course_code, min(year) as soonest " + 
			"    FROM satisfying_sets_sections " + 
			"    GROUP BY A, B, C, course_code ), " + 
			"satisfying_set_last_complete as ( " + 
			"    SELECT A, B, C, max(soonest) as last_complete " + 
			"    FROM satisfying_sets_soonest " + 
			"    GROUP BY A, B, C ) " + 
			"SELECT A, B, C, last_complete " + 
			"FROM satisfying_set_last_complete " + 
			"WHERE last_complete = (  " + 
			"        SELECT min(last_complete) " + 
			"        FROM satisfying_set_last_complete) ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	//***************
	
	//14. List all the possible job profiles that a person is qualified. 
	public ResultSet getJobProfilesQualifiedFor(String personCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH person_skills as ( " + 
			" SELECT skill_code " + 
			"      	FROM person NATURAL JOIN person_skill " + 
			"      	WHERE person_code = ?), " + 
			" person_certificates as ( " + 
			"   SELECT certificate_code " + 
			"   FROM earns " + 
			"   WHERE person_code = ?) " + 
			"  " + 
			" SELECT job_profile_code, job_profile_title " + 
			" FROM job_profile JP " + 
			" WHERE NOT EXISTS ( " + 
			"     	SELECT skill_code " + 
			"      	FROM job_profile_skill " + 
			"      	WHERE JP.job_profile_code = job_profile_skill.job_profile_code " + 
			"     	MINUS " + 
			"    	SELECT * " + 
			"      	FROM person_skills) " + 
			"   	AND " + 
			"   	NOT EXISTS ( " + 
			"   	SELECT certificate_code " + 
			"      	FROM job_profile_certificate " + 
			"      	WHERE JP.job_profile_code = job_profile_code " + 
			"     	MINUS " + 
			"     	SELECT * " + 
			"      	FROM person_certificates)",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	// 14 a
	public ResultSet getJobsQualifiedFor(String personCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"WITH " + 
			"person_skills as ( " + 
			"    SELECT skill_code " + 
			"    FROM person NATURAL JOIN person_skill " + 
			"    WHERE person_code = ?), " + 
			"person_certificates as ( " + 
			"    SELECT certificate_code " + 
			"    FROM earns " + 
			"    WHERE person_code = ?), " + 
			"job_profiles_qualified as ( " + 
			"    SELECT job_profile_code, job_profile_title " + 
			"    FROM job_profile JP " + 
			"    WHERE NOT EXISTS ( " + 
			"            SELECT skill_code " + 
			"            FROM job_profile_skill " + 
			"            WHERE JP.job_profile_code = job_profile_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_skills ) " + 
			"    AND " + 
			"    NOT EXISTS ( " + 
			"            SELECT certificate_code " + 
			"            FROM job_profile_certificate " + 
			"            WHERE JP.job_profile_code = job_profile_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_certificates ) ) " + 
			"SELECT job_code , job_profile_title " + 
			"FROM job_profiles_qualified NATURAL JOIN job J " + 
			"WHERE NOT EXISTS (  " + 
			"        SELECT skill_code " + 
			"        FROM job_skill " + 
			"        WHERE J.job_code = job_code " + 
			"       MINUS " + 
			"        SELECT * " + 
			"        FROM person_skills ) " + 
			"    AND " + 
			"    NOT EXISTS (  " + 
			"        SELECT certificate_code " + 
			"        FROM job_certificate " + 
			"        WHERE J.job_code = job_code " + 
			"       MINUS " + 
			"        SELECT * " + 
			"        FROM person_certificates )",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//15b. Find the job with the highest pay rate for a person according to his/her skills. 
	// Assumes this is "all time" high.  
	public ResultSet getHighestPayingJobEverGivenSkills(String personCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"WITH " + 
			"person_skills as ( " + 
			"    SELECT skill_code " + 
			"    FROM person NATURAL JOIN person_skill " + 
			"    WHERE person_code  =?), " + 
			"person_certificates as ( " + 
			"    SELECT certificate_code " + 
			"    FROM earns " + 
			"    WHERE person_code = ?), " + 
			"job_profiles_qualified as ( " + 
			"    SELECT job_profile_code " + 
			"    FROM job_profile JP " + 
			"    WHERE NOT EXISTS (  " + 
			"            SELECT skill_code " + 
			"            FROM job_profile_skill " + 
			"            WHERE JP.job_profile_code = job_profile_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_skills ) " + 
			"    AND " + 
			"    NOT EXISTS (  " + 
			"            SELECT certificate_code " + 
			"            FROM job_profile_certificate " + 
			"            WHERE JP.job_profile_code = job_profile_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_certificates ) ), " + 
			"jobs_qualified as ( " + 
			"    SELECT job_code " + 
			"    FROM job_profiles_qualified NATURAL JOIN job J " + 
			"    WHERE NOT EXISTS (  " + 
			"            SELECT skill_code " + 
			"            FROM job_skill " + 
			"            WHERE J.job_code = job_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_skills ) " + 
			"    AND " + 
			"    NOT EXISTS ( " + 
			"            SELECT certificate_code " + 
			"            FROM job_certificate " + 
			"            WHERE J.job_code = job_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_certificates ) ), " + 
			"best_paying_job as ( " + 
			"    SELECT job_code, MAX(CASE " + 
			"                           WHEN pay_type = 'salary' THEN pay_rate " + 
			"                           WHEN pay_type = 'wage' THEN pay_rate * 1920 " + 
			"                           ELSE NULL " + 
			"                         END) as max_pay " + 
			"    FROM job NATURAL JOIN jobs_qualified " + 
			"    GROUP BY job_code ) " + 
			" " + 
			"SELECT job_code, max_pay " + 
			"FROM best_paying_job " + 
			"WHERE max_pay = (SELECT max(max_pay)  " + 
			"                 FROM best_paying_job) ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	//15.b
	public ResultSet getHighestPayingJobAvailableGivenSkills(String personCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement( 
			" WITH " + 
			"person_skills as ( " + 
			"    SELECT skill_code " + 
			"    FROM person NATURAL JOIN person_skill " + 
			"    WHERE person_code = ?), " + 
			"person_certificates as ( " + 
			"    SELECT certificate_code " + 
			"    FROM earns " + 
			"    WHERE person_code = ?), " + 
			"job_profiles_qualified as ( " + 
			"    SELECT job_profile_code " + 
			"    FROM job_profile JP " + 
			"    WHERE NOT EXISTS ( " + 
			"            SELECT skill_code " + 
			"            FROM job_profile_skill " + 
			"            WHERE JP.job_profile_code = job_profile_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_skills ) " + 
			"      AND " + 
			"      NOT EXISTS (  " + 
			"            SELECT certificate_code " + 
			"            FROM job_profile_certificate " + 
			"            WHERE JP.job_profile_code = job_profile_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_certificates ) ), " + 
			"jobs_qualified as ( " + 
			"    SELECT job_code " + 
			"    FROM job_profiles_qualified NATURAL JOIN job J " + 
			"    WHERE NOT EXISTS ( " + 
			"            SELECT skill_code " + 
			"            FROM job_skill " + 
			"            WHERE J.job_code = job_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_skills ) " + 
			"        AND " + 
			"        NOT EXISTS (  " + 
			"            SELECT certificate_code " + 
			"            FROM job_certificate " + 
			"            WHERE J.job_code = job_code " + 
			"           MINUS " + 
			"            SELECT * " + 
			"            FROM person_certificates ) ), " + 
			"available_jobs as ( " + 
			"    SELECT job_code, job_profile_code, company_code, " + 
			"           job_type, pay_rate, pay_type, " + 
			"           opening_date, closing_date " + 
			"    FROM job " + 
			"    WHERE opening_date < current_date AND " + 
			"          (closing_date > current_date OR closing_date is null) " + 
			"   MINUS " + 
			"    SELECT job_code, job_profile_code, company_code, " + 
			"           job_type, pay_rate, pay_type, " + 
			"           opening_date, closing_date " + 
			"    FROM employment NATURAL JOIN job " + 
			"    WHERE end_date is null OR end_date > current_date), " + 
			"best_paying_job as ( " + 
			"    SELECT job_code, MAX(CASE " + 
			"                           WHEN pay_type = 'salary' THEN pay_rate " + 
			"                           WHEN pay_type = 'wage' THEN pay_rate * 1920 " + 
			"                           ELSE NULL " + 
			"                         END) as max_pay " + 
			"    FROM available_jobs NATURAL JOIN jobs_qualified " + 
			"    GROUP BY job_code ) " + 
			"     " + 
			"SELECT job_code, max_pay " + 
			"FROM best_paying_job " + 
			"WHERE max_pay = (SELECT max(max_pay)  " + 
			"                 FROM best_paying_job)",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, personCode);
		stmt.setString(2, personCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//16. List all the names along with the emails of the persons who are 
	//    qualified for a job profile. 
	public ResultSet getAllQualifiedForJobProfile(String jobProfileCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH  " + 
			"required_skills as ( " + 
			"    SELECT skill_code " + 
			"    FROM job_profile_skill " + 
			"    WHERE job_profile_code = ?), " + 
			"required_certificates as ( " + 
			"    SELECT certificate_code " + 
			"    FROM job_profile_certificate " + 
			"    WHERE job_profile_code = ?) " + 
			"SELECT last_name, first_name, email " + 
			"FROM person P " + 
			"WHERE NOT EXISTS (  " + 
			"         SELECT * FROM required_skills " + 
			"        MINUS " + 
			"         SELECT skill_code " + 
			"         FROM person_skill " + 
			"         WHERE P.person_code = person_code) " + 
			"      AND " + 
			"      NOT EXISTS (  " + 
			"         SELECT * FROM required_certificates " + 
			"        MINUS " + 
			"         SELECT certificate_code " + 
			"         FROM earns " + 
			"         WHERE P.person_code = person_code )",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		stmt.setString(2, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	//17. Suppose there is a new job profile that has nobody qualified. 
	//    List the persons who miss the least number of 
	//    skills and report the “least number”.
	public ResultSet getNearestQualifiedForJobProfile(String jobProfileCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH " + 
			"required_skills as ( " + 
			"    SELECT skill_code " + 
			"    FROM job_profile_skill " + 
			"    WHERE job_profile_code = ?), " + 
			"missing_skills (person_code, num_missing) as ( " + 
			"    SELECT person_code, COUNT(skill_code) " + 
			"    FROM person P, required_skills J " + 
			"    WHERE J.skill_code IN (  " + 
			"            SELECT * " + 
			"            FROM required_skills " + 
			"           MINUS " + 
			"            SELECT skill_code " + 
			"            FROM person_skill " + 
			"            WHERE person_code = P.person_code ) " + 
			"    GROUP BY person_code ), " + 
			"min_num_missing (min_missing) as ( " + 
			"    SELECT min(num_missing) " + 
			"    FROM missing_skills ) " + 
			"SELECT person_code, num_missing " + 
			"FROM missing_skills, min_num_missing " + 
			"WHERE num_missing = min_num_missing.min_missing",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
			);
		stmt.setString(1, jobProfileCode);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	// 18.  Make a 'missing-one' list that lists people who miss only one 
	//      skill for a specified job profile
	public ResultSet getPeopleMissingOneSkillForJobProfile(String jobProfileCode)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH " + 
			" skill_codes as ( " + 
			"   SELECT skill_code " + 
			"   FROM job_profile_skill " + 
			"   WHERE job_profile_code = ?), " + 
			" missing_skills (person_code, num_missing) as ( " + 
			" 	SELECT person_code, COUNT(skill_code) " + 
			" 	FROM person P, (SELECT * " + 
			"                 	FROM skill_codes) J " + 
			" 	WHERE J.skill_code IN ( " + 
			"   	SELECT * " + 
			"    	FROM skill_codes " + 
			"   	MINUS " + 
			"    	SELECT skill_code " + 
			"    	FROM person_skill " + 
			"    	WHERE person_code = P.person_code ) " + 
			" 	GROUP BY person_code ) " + 
			" SELECT person_code, num_missing " + 
			" FROM missing_skills " + 
			" WHERE num_missing = 1",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
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
			" WITH " + 
			" skill_codes as ( " + 
			"   SELECT skill_code " + 
			"   FROM job_profile_skill " + 
			"   WHERE job_profile_code = ?), " + 
			" missing_skills (person_code, num_missing) as ( " + 
			" 	SELECT person_code, COUNT(skill_code) " + 
			" 	FROM person P, (SELECT * " + 
			"                 	FROM skill_codes) J " + 
			"	WHERE J.skill_code IN ( " + 
			"   	SELECT * " + 
			"    	FROM skill_codes " + 
			"   	MINUS " + 
			"    	SELECT skill_code " + 
			"   	FROM person_skill " + 
			"    	WHERE person_code = P.person_code ) " + 
			" 	GROUP BY person_code ), " + 
			" people_lacking_one (person_code) as ( " + 
			" 	SELECT person_code " + 
			" 	FROM missing_skills " + 
			" 	WHERE num_missing = 1 ) " + 
			"      " + 
			" SELECT skill_code, COUNT(person_code) as num_people_lacking " + 
			" FROM people_lacking_one P , (SELECT * " + 
			"                           	FROM skill_codes) J " + 
			" WHERE J.skill_code IN ( " + 
			"  	SELECT * " + 
			"  	FROM skill_codes " + 
			" 	MINUS " + 
			" 	SELECT skill_code " + 
			" 	FROM person_skill " + 
			" 	WHERE person_code = P.person_code ) " + 
			" GROUP BY skill_code",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
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
			" WITH " + 
			" skill_codes as ( " + 
			"   SELECT skill_code " + 
			"   FROM job_profile_skill " + 
			"   WHERE job_profile_code = ?), " + 
			" missing_skills (person_code, num_missing) as ( " + 
			"	SELECT person_code, COUNT(skill_code) " + 
			"	FROM person P, (SELECT * " + 
			"                 	FROM skill_codes) J " + 
			" 	WHERE J.skill_code IN ( " + 
			"   	SELECT * " + 
			"   	FROM skill_codes " + 
			"  	MINUS " + 
			"   	SELECT skill_code " + 
			"   	FROM person_skill " + 
			"   	WHERE person_code = P.person_code ) " + 
			"	GROUP BY person_code ) " + 
			" SELECT person_code, num_missing " + 
			" FROM missing_skills " + 
			" WHERE num_missing <= ? " + 
			" ORDER BY num_missing DESC",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		stmt.setInt(2, K);
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
			" WITH " + 
			" skill_codes as ( " + 
			"   SELECT skill_code " + 
			"   FROM job_profile_skill " + 
			"   WHERE job_profile_code = ?), " + 
			" missing_skills (person_code, num_missing) as ( " + 
			"	SELECT person_code, COUNT(skill_code) " + 
			"	FROM person P, (SELECT * " + 
			"                	FROM skill_codes) J " + 
			"	WHERE J.skill_code IN ( " + 
			"   	SELECT * " + 
			"   	FROM skill_codes " + 
			"  	MINUS " + 
			"   	SELECT skill_code " + 
			"   	FROM person_skill " + 
			"   	WHERE person_code = P.person_code ) " + 
			"	GROUP BY person_code ), " + 
			" people_lacking_at_most_x (person_code, num_missing) as ( " + 
			"	SELECT person_code, num_missing " + 
			" 	FROM missing_skills " + 
			"	WHERE num_missing <= ? ) " + 
			" SELECT skill_code, COUNT(person_code) as num_people_lacking " + 
			" FROM people_lacking_at_most_x P , (SELECT * " + 
			"                                	FROM skill_codes) J " + 
			" WHERE J.skill_code IN ( " + 
			" 	SELECT * " + 
			" 	FROM skill_codes " + 
			"	MINUS " + 
			" 	SELECT skill_code " + 
			" 	FROM person_skill " + 
			" 	WHERE person_code = P.person_code ) " + 
			" GROUP BY skill_code",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		stmt.setString(1, jobProfileCode);
		stmt.setInt(2, K);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
//	--  22.  Find the job profiles that have the most openings due to lack of
//	--       qualified workers.  If there are many open jobs of a job profile but 
//	--       at the same time there are many jobless people, then this is not a job 
//	--       profile that training can help fill up. What we want to find is such a 
//	--       job profile that has the largest difference between the unfilled jobs 
//	--       of this profile and the number of jobless people who are qualified for 
//	--       this job profile.
	public ResultSet getMaxDifferenceJobOffersPeopleQualified()
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			" WITH  " + 
			" unemployed_people as ( " + 
			"    SELECT person_code FROM person " + 
			"   MINUS " + 
			"    SELECT DISTINCT person_code " + 
			"    FROM employment " + 
			"    WHERE start_date < CURRENT_DATE AND " + 
			"         (end_date > CURRENT_DATE OR end_date is NULL) ), " + 
			" lacking_qualified as ( " + 
			"    SELECT job_profile_code, num_unfilled, num_qualified,  " + 
			"           (num_unfilled - num_qualified) as lack_qualified " + 
			"    FROM  " + 
			"        (SELECT job_profile_code, count(*) as num_unfilled " + 
			"         FROM job NATURAL LEFT OUTER JOIN employment " + 
			"         WHERE (closing_date > current_date or closing_date is null) AND " + 
			"               (start_date > current_date or start_date is null) AND " + 
			"               (end_date < current_date OR end_date is null) " + 
			"         GROUP BY job_profile_code ) " + 
			"    NATURAL JOIN " + 
			"        (SELECT job_profile_code, count(*) as num_qualified " + 
			"         FROM job_profile JP, person P " + 
			"         WHERE NOT EXISTS ( " + 
			"               SELECT skill_code " + 
			"               FROM job_profile_skill " + 
			"               WHERE job_profile_code = JP.job_profile_code " + 
			"              MINUS " + 
			"               SELECT skill_code " + 
			"               FROM unemployed_people NATURAL JOIN person_skill " + 
			"               WHERE person_code = P.person_code ) " + 
			"        GROUP BY job_profile_code ) ), " + 
			" max_lacking_qualified as ( " + 
			"    SELECT max(lack_qualified) as max_lack_qualified " + 
			"    FROM lacking_qualified ) " + 
			"SELECT * " + 
			"FROM lacking_qualified, max_lacking_qualified " + 
			"WHERE lack_qualified = max_lacking_qualified.max_lack_qualified ",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

//	-- 23.  Find the courses that can help people find a job by training people 
//	--      toward the job profiles that have the most openings due to lack of 
//	--      qualified workers.  
	public ResultSet CoursesForJobsLackingQualifiedWorkers()
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
			"WITH  " + 
			"umemployed_people as ( " + 
			"    SELECT person_code FROM person " + 
			"   MINUS " + 
			"    SELECT DISTINCT person_code " + 
			"    FROM employment " + 
			"    WHERE start_date < CURRENT_DATE AND " + 
			"         (end_date > CURRENT_DATE OR end_date is NULL) ), " + 
			"lacking_qualified as ( " + 
			"    SELECT job_profile_code, num_unfilled, num_qualified,  " + 
			"           (num_unfilled - num_qualified) as lack_qualified " + 
			"    FROM  " + 
			"        (SELECT job_profile_code, count(*) as num_unfilled " + 
			"         FROM job NATURAL LEFT OUTER JOIN employment " + 
			"         WHERE (closing_date > current_date or closing_date is null) AND " + 
			"               (start_date > current_date or start_date is null) AND " + 
			"               (end_date < current_date OR end_date is null) " + 
			"         GROUP BY job_profile_code ) " + 
			"    NATURAL JOIN " + 
			"        (SELECT job_profile_code, count(*) as num_qualified " + 
			"         FROM job_profile JP, person P " + 
			"         WHERE NOT EXISTS ( " + 
			"               SELECT skill_code " + 
			"               FROM job_profile_skill " + 
			"               WHERE job_profile_code = JP.job_profile_code " + 
			"              MINUS " + 
			"               SELECT skill_code " + 
			"               FROM umemployed_people NATURAL JOIN person_skill " + 
			"               WHERE person_code = P.person_code ) " + 
			"        GROUP BY job_profile_code ) ), " + 
			"max_lacking_qualified as ( " + 
			"    SELECT max(lack_qualified) as max_lack_qualified " + 
			"    FROM lacking_qualified ), " + 
			"jobs_lacking_qualified as ( " + 
			"    SELECT job_profile_code " + 
			"    FROM lacking_qualified, max_lacking_qualified " + 
			"    WHERE lack_qualified = max_lacking_qualified.max_lack_qualified ), " + 
			"course_sets as ( " + 
			"    (SELECT course_code as A, null as B, null as C " + 
			"     FROM course_skill) " + 
			"   UNION " + 
			"    (SELECT A.course_code as A, B.course_code as B, null as C " + 
			"     FROM course_skill A, course_skill B " + 
			"     WHERE a.course_code < b.course_code) " + 
			"   UNION " + 
			"    (SELECT A.course_code as A, B.course_code as B, C.course_code as C " + 
			"     FROM course_skill A, course_skill B, course_skill C " + 
			"     WHERE a.course_code < b.course_code AND " + 
			"           b.course_code < c.course_code ) ), " + 
			"course_set_skills as ( " + 
			"    SELECT A, B, C, skill_code " + 
			"    FROM course_sets, course_skill " + 
			"    WHERE course_sets.A = course_skill.course_code OR " + 
			"          course_sets.B = course_skill.course_code OR " + 
			"          course_sets.C = course_skill.course_code), " + 
			"satisfying_sets as ( " + 
			"  SELECT *  " + 
			"  FROM ( " + 
			"      (SELECT A, B, C, job_profile_code " + 
			"       FROM course_set_skills, jobs_lacking_qualified) " + 
			"      MINUS " + 
			"      (SELECT A, B, C, job_profile_code " + 
			"       FROM (   " + 
			"               SELECT A, B, C, job_profile_code, skill_code " + 
			"               FROM (SELECT A, B, C " + 
			"                     FROM course_set_skills), " + 
			"                    (SELECT job_profile_code, skill_code  " + 
			"                     FROM jobs_lacking_qualified NATURAL JOIN job_profile_skill) " + 
			"               MINUS " + 
			"               SELECT A, B, C, job_profile_code, skill_code " + 
			"               FROM course_set_skills, jobs_lacking_qualified) ) ) ) " + 
			"SELECT *  " + 
			"FROM satisfying_sets",
			ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
		);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
}
