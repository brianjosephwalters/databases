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
	}
	
	public static void main(String[] args) {
		EmployTest test = new EmployTest();
		test.employ();
	}
}
