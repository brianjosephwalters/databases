package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Exam;

public class ExamQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public ExamQueries (Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * All exams.
	 */
	public List<Exam> getAllExams() throws SQLException {
		List<Exam> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM exam");
		list = getListOfExams(stmt);
		
		return list;
	}
	
	// Inserts
	public int addExam(Exam exam) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" INSERT INTO exam " +
			" VALUES (?, ?, ?)"
			);
		stmt.setString(1, exam.getExamCode());
		stmt.setString(2, exam.getExamTypeCode());
		stmt.setDate(3, new java.sql.Date(exam.getExamDate().getTime()));
		count = stmt.executeUpdate();
		return count; 
	}
	
	// Updates
	public int updateExam(Exam exam) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" UPDATE exam " +
			" SET exam_type_code = ? " +
			"     exam_date = ? " +
			" WHERE exam_code = ? "
		);
		stmt.setString(1, exam.getExamTypeCode());
		stmt.setDate(2, new java.sql.Date(exam.getExamDate().getTime()));
		stmt.setString(3, exam.getExamCode());
		count = stmt.executeUpdate();
		return count;
	}
		
	// Helper Functions
	private List<Exam> getListOfExams(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty exam list
		List<Exam> list = new ArrayList<Exam>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Exam from the results
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
