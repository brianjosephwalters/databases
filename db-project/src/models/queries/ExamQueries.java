package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Exam;
import models.Job;

public class ExamQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public ExamQueries (Connection connection) {
		this.connection = connection;
	}
		
		
	// Helper Functions
	private List<Exam> getListOfExams(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty person list
		List<Exam> list = new ArrayList<Exam>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new PhoneNumber from the results
			// and add it to the list.
			
			list.add( new Exam(
				results.getString("exam_code"),
				results.getString("exam_type_code"),
				results.getDate("exam_date"))
			);
		}
		return list;
	}
		
		
		
}
