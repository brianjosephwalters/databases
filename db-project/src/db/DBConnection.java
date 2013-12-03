package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public static final Connection getConnection() 
			throws SQLException{
		String dbLocation = "@dbsvcs.cs.uno.edu:1521:" + "orcl";
		String url = "jdbc:oracle:thin" + ':' + dbLocation;
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection conn = DriverManager.getConnection(url, "bwalters", "HtoaYUen");
		return conn;
	}
	
	public static final Connection getConnection(String username, String password) 
			throws SQLException {
		String dbLocation = "@dbsvcs.cs.uno.edu:1521:" + "orcl";
		String url = "jdbc:oracle:thin" + ':' + dbLocation;
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}
	
	public static final Connection getConnection(String server, String username, String password) 
			throws SQLException {
		String dbLocation = "@" + server +":1521:" + "orcl";
		String url = "jdbc:oracle:thin" + ':' + dbLocation;
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}
	
	public static final Connection getConnection2() 
			throws SQLException{
		String dbLocation = "@localhost:1521:" + "orcl";
		String url = "jdbc:oracle:thin" + ':' + dbLocation;
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection conn = DriverManager.getConnection(url, "bwalters", "HtoaYUen");
		return conn;
	}
	
}
