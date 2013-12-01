package ui.tui.concurrency.test2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import db.DBConnection;

public class EmployTest {

	Connection connection;
	Scanner scanner;
	
	public EmployTest() {
		try {
			connection = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		scanner = new Scanner(System.in);
	}
	
	public void employ() {
		System.out.println("###Forced Hire Process Started");

		int count = 0;
		try {
			connection.setAutoCommit(true);
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO employment VALUES " +
				"	 ('10467', '8000', CURRENT_DATE, null)"
			);
			count = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Insertion failed: " + e);
		}
		
		if (count == 1) {
			System.out.println("###Inserted Correctly!");
		} else {
			System.out.println("###Failed to insert.  Try again.");
		}
		
		pause("Hit enter when other transaction has rolled back.");
	}
	
	public void cleanUp() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
					" DELETE FROM employment " +
					"	 WHERE job_code = '8000' AND " +
					"          person_code = '10467' "
				);
		
			System.out.println("Adding Employment...");
			System.out.println(stmt.executeUpdate() + " lines removed");
			
		} catch (SQLException e) {
			System.out.println("Unable to remove employment: " + e);
		}
	}
	
	/**
	 * Pause the running of a transaction.
	 * @param string A prompt to be displayed
	 */
	private void pause(String string) {
		System.out.println(string);
		String s = scanner.nextLine();
	}
	
	public static void main(String[] args) {
		EmployTest test = new EmployTest();
		test.employ();
		test.cleanUp();
	}
}
