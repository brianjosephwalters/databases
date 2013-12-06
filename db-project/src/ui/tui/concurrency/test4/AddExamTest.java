package ui.tui.concurrency.test4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import db.DBConnection;

public class AddExamTest {
Connection connection;
Scanner scanner;

	public AddExamTest() {
		try {
			this.connection = DBConnection.getConnection2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.scanner = new Scanner(System.in);
	}
	
	public void addExamTaken() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO exam_taken VALUES ('10182', '1001', '11111', '90') "
			);
			System.out.println("Exam Taken: " + stmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("Unable to add exam take: " + e);
		}
	}
	
	public void updateEarns() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO earns " +
				"     SELECT person_code, certificate_code " +
				"     FROM exam_taken NATURAL JOIN exam NATURAL JOIN exam_type" +
				"     WHERE person_code = '10182'"
			);
			System.out.println("Certificates Gained: " + stmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("Unable to add certificate take: " + e);
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
	
	public void test() {
		System.out.println("Now running the test.");
		System.out.println("Starting the transaction...");
		try {
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			System.out.println("Adding Exam Taken.");
			addExamTaken();
						
			pause("Press any key to continue.");
			
			System.out.println("Adding the certificate.");
			updateEarns();
			
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
				System.out.println("Error Reseting AutoCommit: " + e);
			}
		}
	}
	
	public void cleanUp() {
		System.out.println("Cleaning up!");
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" DELETE FROM earns WHERE person_code = '10182' AND certificate_code = '100'"
			);
			PreparedStatement stmt2 = connection.prepareStatement(
				" DELETE FROM exam_taken WHERE person_code = '10182' "
			);
			stmt.executeUpdate();
			stmt2.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Unable to clean up: " + e);
		}
	}
	
	/**
	 * Runs the AddExamTest program.
	 * @param args
	 */
	public static void main(String[] args) {
		AddExamTest test = new AddExamTest();
		test.test();
		test.cleanUp();
	}
	
}
