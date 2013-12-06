package ui.tui.concurrency.test3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DBConnection;

public class UpdateCourseSkillsTest {
	Connection connection;
	
	public UpdateCourseSkillsTest() {
		try {
			connection = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateSkills() {
		System.out.println("### Updating the skills provided by a course.");
		int count = 0;
		try {
			connection.setAutoCommit(true);
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO course_skill VALUES ('98000', '411')"
			);
			count = stmt.executeUpdate();
			System.out.println("###Adding skills to course: " + count);
		} catch (SQLException e) {
			System.out.println("###Unabled to insert skill into course: " + e);
		}
		
		if (count == 1) {
			System.out.println("###Inserted Correctly!");
		} else {
			System.out.println("###Failed to insert.  Try again.");
		}
	}
	
	public void cleanUp() {
		System.out.println("### Cleaning up.");
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" DELETE FROM course_skill WHERE course_code = '98000' "
			);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("###Unabled to remove skill into course: " + e);
		}
	}
	
	public static void main(String[] args) {
		UpdateCourseSkillsTest test = new UpdateCourseSkillsTest();
		test.updateSkills();
		//test.cleanUp();
	}
}
