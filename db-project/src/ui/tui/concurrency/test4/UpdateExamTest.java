package ui.tui.concurrency.test4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DBConnection;

public class UpdateExamTest {
Connection connection;
	
	public UpdateExamTest() {
		try {
			connection = DBConnection.getConnection2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void test() {
		System.out.println("### Update the exam taken.");
		int count = 0;
		try {
			connection.setAutoCommit(true);
			PreparedStatement stmt = connection.prepareStatement(
				" UPDATE exam_taken " +
				"    SET score = '50' " +
				"    WHERE person_code = '10182' AND " +
				"          exam_code = '1001' AND " +
				"          exam_type_code = '11111' "
			);			
			count = stmt.executeUpdate();
			System.out.println("###Updating the exam's score: " + count);
		} catch (SQLException e) {
			System.out.println("###Unabled to update the exam: " + e);
		}
		
		if (count == 1) {
			System.out.println("###Updated Correctly!");
		} else {
			System.out.println("###Failed to update.  Try again.");
		}
	}
	
	/**
	 * Runs the AddExamTest program.
	 * @param args
	 */
	public static void main(String[] args) {
		UpdateExamTest test = new UpdateExamTest();
		test.test();
	}
}
