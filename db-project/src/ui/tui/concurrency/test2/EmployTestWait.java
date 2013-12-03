package ui.tui.concurrency.test2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import db.DBConnection;

public class EmployTestWait {
	Connection connection;
	Scanner scanner;
	
	public EmployTestWait() {
		try {
			connection = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		scanner = new Scanner(System.in);
	}
	
	public void setUp() {
		System.out.println("Employment Test with WAITING");
		System.out.println("Adding a dummy job to the database");
		int count = 0;
		try {
			connection.setAutoCommit(true);
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO job VALUES " +
				" ('8000', '300', '100', 'full-time', '35000', 'salary', " +
				"   TO_DATE('2013-11-13', 'yyyy-mm-dd'), " +
				"   TO_DATE('2014-11-23', 'yyyy-mm-dd')) "
			);
			count = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Job already in database: " + e);
		}
		if (count == 0) {
			System.out.println("Insert Failed");
		} 
		System.out.println("Now on to hiring!");
	}
	
	/**
	 * Pause the running of a transaction.
	 * @param string A prompt to be displayed
	 */
	private void pause(String string) {
		System.out.println(string);
		String s = scanner.nextLine();
	}
	
	private void setJobClosed() throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" UPDATE job " +
				" SET closing_date = CURRENT_DATE " +
				" WHERE job_code = '8000' "
			);
	
			System.out.println("Updating Job's status...");
			System.out.println(stmt.executeUpdate() + " lines changed");
			
		} catch (SQLException e) {
			System.out.println("Updating Job Error: " + e);
			throw e;
		}
	}
	
	private void setHired() throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				"INSERT INTO employment VALUES " +
				"	('10182', '8000', CURRENT_DATE, null)"
			);
	
			System.out.println("Adding Employment...");
			System.out.println(stmt.executeUpdate() + " lines added");
			
		} catch (SQLException e) {
			System.out.println("Adding Employment Error: " + e);
			throw e;
		}
	}
	
	public void hire() {
		System.out.println("Hire Phase");
		
		try {
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			setJobClosed();
			pause("Press enter to continue.");
			setHired();
			System.out.println("Committing...");
			connection.commit();
		
		} catch (SQLException e) {
			try {
				System.out.println("Error: Rollback!");
				connection.rollback();
			} catch (SQLException event) {
				System.out.println("Unable to rollback: " + e);
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				System.out.println("Reseting AutoCommit Error: " + e);
			}
		}
	}
	
	public void cleanUp() {
		System.out.println("Clean up after experiment:");
		try {
			PreparedStatement stmt2 = connection.prepareStatement(
					" DELETE FROM employment " +
					"	 WHERE job_code = '8000' AND " +
					"          person_code = '10182' "
				);
		
			System.out.println("Deleting Employment...");
			System.out.println(stmt2.executeUpdate() + " lines removed" +
					"");
			
		} catch (SQLException e) {
			System.out.println("Unable to remove employment: " + e);
		}
		
		try {
			PreparedStatement stmt = connection.prepareStatement(
					" DELETE FROM employment " +
					"	 WHERE job_code = '8000' AND " +
					"          person_code = '10467' "
				);
		
			System.out.println("Deleting...");
			System.out.println(stmt.executeUpdate() + " lines removed");
			
		} catch (SQLException e) {
			System.out.println("Unable to remove employment: " + e);
		}
		
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" DELETE FROM job WHERE job_code = '8000' "
			);
		
			System.out.println("Removing job...");
			System.out.println(stmt.executeUpdate() + " lines changed");
		} catch (SQLException e) {
			System.out.println("Unable to remove job: " + e);
		}
	}
	
	public static void main(String[] args) {
		EmployTestWait test = new EmployTestWait();
		test.setUp();
		test.hire();
		test.cleanUp();
	}
}
