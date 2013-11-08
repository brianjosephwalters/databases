package db.queries;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class TestQueries {
	
	public ResultSet getCompanyEmployees(String company_id) {
		PreparedStatement stmt = conn.preparedStatement(
			"SELECT last_name, first_name " +
			"FROM person NATURAL JOIN employment NATURAL JOIN " +
			"	  job NATURAL JOIN company " +
			"WHERE company_id = ?");
		stmt.setString(1, company_id);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}

	public ResultSet getCompanyFullTimeEmployeesBySalaryDesc(String company_code) {
		PreparedStatement stmt = conn.preparedStatement(
			"SELECT last_name, first_name, pay_rate" +
			"FROM person NATURAL JOIN employment NATURAL JOIN " +
			"	  job NATURAL JOIN company " +
			"WHERE company_code = ? AND pay_type = 'salary'" +
			"ORDER BY DESC");
		stmt.setString(1, company_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
	
	public ResultSet getCompanyTotalLaborCost(String company_code) {
		PreparedStatement stmt = conn.preparedStatement(
			"SELECT sum(calculated_wage)" +
			"FROM person NATURAL JOIN employment NATURAL JOIN " +
			"	  job NATURAL JOIN company " +
			"WHERE company_code = ?" +
			"GROUP BY person_code" +
			"HAVING calculated_wage as" +
			"     CASE" +
			"         WHEN pay_rate = 'salary' THEN pay_rate" +
			"         WHEN pay_rate = 'wage' THEN pay_rate * 1920" +
			"         ELSE NULL" +
			"     END");
		stmt.setString(1, company_code);
		ResultSet rset = stmt.executeQuery();
		return rset;
	}
}
