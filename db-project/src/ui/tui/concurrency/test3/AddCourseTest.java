package ui.tui.concurrency.test3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import db.DBConnection;

public class AddCourseTest {
	Connection connection;
	Scanner scanner;
	
	public AddCourseTest() {
		try {
			this.connection = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.scanner = new Scanner(System.in);
	}
	
	public void addSamplePerson() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO person VALUES ('9000', 'Walters', 'Brian', 'male', 'bwalters@uno.edu')"
			);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Unable to insert person: " + e);
		}
	}
	
	public void addSampleCourse() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO course VALUES ('98000', 'A sample Course', "
				+ " 'Lots of information goes here', 'beginner', 'active', '300') "
			);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Unable to insert course: " + e);
		}
	}
	
	public void addSampleCourseSkills() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO course_skill VALUES ('98000', '131')"
			);
			stmt.executeUpdate();
			PreparedStatement stmt2 = connection.prepareStatement(
				" INSERT INTO course_skill VALUES ('98000', '132')"
			);
			stmt2.executeUpdate();
			PreparedStatement stmt3 = connection.prepareStatement(
				" INSERT INTO course_skill VALUES ('98000', '500')"
			);
			stmt3.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Unable to insert skills for course: " + e);
		}
	}
	
	public void addSampleSection() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO section VALUES ('98000', '001', '2013', '100', 350)"
			);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Unable to insert section: " + e);
		}
	}
	
	public void setUp() {
		System.out.println("Adding a sample person...");
		addSamplePerson();
		System.out.println("Adding a course to test...");
		addSampleCourse();
		System.out.println("Giving the course some skills...");
		addSampleCourseSkills();
		System.out.println("Adding a sample course and section...");
		addSampleSection();
	}
	
	public void addCourseToPerson() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO attended VALUES ('98000', '001', '2013', '9000', CURRENT_DATE, '90')"
			);
			System.out.println("Person added to course: " + stmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("Unable to add section to person: " + e);
		}
	}
	
	public void readSkillsOfPerson() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" SELECT *" + 
			    " FROM person_skill"
			);
			stmt.executeQuery();
		} catch (SQLException e) {
			System.out.println("Unable to read person_skill: " + e);
		}
	}
	
	public void addSkillsToPerson() {
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" INSERT INTO person_skill SELECT '9000', skill_code FROM course_skill WHERE course_code = '98000'"
			);
			System.out.println("Skills added to person: " + stmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("Unable to add section to person: " + e);
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
		try {
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			System.out.println("Reading skills for person");
			readSkillsOfPerson();
						
			pause("Press any key to continue.");
			
			System.out.println("Adding a course to a person");
			addCourseToPerson();
			
			System.out.println("Adding the relevant skills to the person.");
			addSkillsToPerson();
			
			
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
		try {
			PreparedStatement stmt1 = connection.prepareStatement(
				" DELETE FROM course_skill WHERE course_code = '98000' "
			);
			PreparedStatement stmt2 = connection.prepareStatement(
				" DELETE FROM person_skill WHERE person_code = '9000' "
			);
			PreparedStatement stmt3 = connection.prepareStatement(
				" DELETE FROM attended WHERE course_code = '98000' "
			);
			PreparedStatement stmt4 = connection.prepareStatement(
				" DELETE FROM section WHERE course_code = '98000' "
			);
			PreparedStatement stmt5 = connection.prepareStatement(
				" DELETE FROM course WHERE course_code = '98000' "
			);
			PreparedStatement stmt6 = connection.prepareStatement(
				" DELETE FROM person WHERE person_code = '9000' "
			);
			
				
			stmt1.executeUpdate();
			stmt2.executeUpdate();
			stmt3.executeUpdate();
			stmt4.executeUpdate();
			stmt5.executeUpdate();
			stmt6.executeUpdate();
			System.out.println("Finished cleaning up");
		} catch (SQLException e) {
			System.out.println("Unable to clean up.");
		}
	}
	
	/**
	 * Runs the RemoveTest program.
	 * @param args
	 */
	public static void main(String[] args) {
		AddCourseTest test = new AddCourseTest();
		test.setUp();
		test.test();
		test.cleanUp();
	}
	
}
