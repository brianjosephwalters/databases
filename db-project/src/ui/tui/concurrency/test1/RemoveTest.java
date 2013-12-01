package ui.tui.concurrency.test1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import db.DBConnection;
/**
 * A class to insert and remove a person from the database.
 * NOTE: This is a component class for concurrency test 1.
 * @author Brian J. Walters
 */
public class RemoveTest {
	Connection connection;
	Scanner scanner;
	
	/**
	 * Setup the RemoveTest class.
	 */
	public RemoveTest() {
		try {
			this.connection = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		scanner = new Scanner(System.in);
	}
	
	/**
	 * Setup the Database data we will need to remove.
	 */
	public void setUp() {
		
		System.out.println("Remove Test");
		System.out.println("Adding a person to the database");
		int count = 0;
		try {
			connection.setAutoCommit(true);
			PreparedStatement stmt = connection.prepareStatement(
				"INSERT INTO person VALUES ('9000', 'Walters', 'Brian', 'male', 'bwalters@uno.edu')"
			);
			count = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Person already in database: " + e);
		}
		if (count == 0) {
			System.out.println("Insert Failed");
		} 
		System.out.println("Now on to removal!");
	}
	
	
	/**
	 * Pause the running of a transaction.
	 * @param string A prompt to be displayed
	 */
	private void pause(String string) {
		System.out.println(string);
		String s = scanner.nextLine();
	}
	
	/**
	 * Removes a course from the database.
	 * @throws SQLException
	 */
	private void removeCourses() throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement("" +
					"DELETE FROM attended " +
					"	WHERE person_code = '9000'"
			);
			System.out.println("Removing Courses Attended...");
			System.out.println(stmt.executeUpdate() + " lines removed");
		} catch (SQLException e) {
			System.out.println("Remove Courses Error: " + e);
			throw e;
		}
	}
	
	/**
	 * Removes a person from the database.
	 * @throws SQLException
	 */
	private void removePerson() throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement("" +
					"DELETE FROM person " +
					"	WHERE person_code = '9000'"
			);
	
			System.out.println("Removing Person...");
			System.out.println(stmt.executeUpdate() + " lines removed");
			
		} catch (SQLException e) {
			System.out.println("Remove Person Error: " + e);
			throw e;
		}
	}
	
	/**
	 * Our main test class.  Introduces a transaction
	 * that removes the courses a person is associated with
	 * and then the person.  There is a pause in the middle
	 * of the transaction to allow for concurrency testing.
	 */
	public void test() {
		System.out.println("Removal phase");
		try {
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			removeCourses();
			pause("Pausing RemoveTest.");
			removePerson();
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
		System.out.println("Finished Commital Phase");
	}
	
	/**
	 * Runs the RemoveTest program.
	 * @param args
	 */
	public static void main(String[] args) {
		RemoveTest remove = new RemoveTest();
		remove.setUp();
		remove.test();
	}
	
}
