package ui.tui.concurrency.test1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DBConnection;

/**
 * A class to register a person for a course.
 * NOTE: This is a component class for concurrency test 1.
 *       Assumes RemoveTest's setup() class has been called.
 * @author Brian J. Walters
 */
public class RegisterTest {
	Connection connection;
	
	/**
	 * Setup the RegisterTest class.
	 */
	public RegisterTest() {
		try {
			connection = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Registers a person for a course.
	 */
	public void register() {
		System.out.println("###Register Course Process Started");

		int count = 0;
		try {
			connection.setAutoCommit(true);
			PreparedStatement stmt = connection.prepareStatement(
				"INSERT INTO attended VALUES " +
				"	('100', '001', '1990', '9000', null, null)"
					
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
	}
	
	/**
	 * Runs the RegisterTest class.
	 * @param args
	 */
	public static void main(String[] args) {
		RegisterTest test = new RegisterTest();
		test.register();
	}
}
