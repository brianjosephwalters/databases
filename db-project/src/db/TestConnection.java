package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
	
	public static void main(String args[]) throws SQLException{
		String dbLocation = "@dbsvcs.cs.uno.edu:1521:" + "orcl";
		String url = "jdbc:oracle:thin" + ':' + dbLocation;
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection conn = DriverManager.getConnection(url, "bwalters", "HtoaYUen");
	}
}
