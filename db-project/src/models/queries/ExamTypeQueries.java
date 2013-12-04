package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.ExamType;

public class ExamTypeQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public ExamTypeQueries (Connection connection) {
		this.connection = connection;
	}
	
	public List<ExamType> getAllExamTypes() 
			throws SQLException {
		List<ExamType> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM exam_type ");
		list = getListOfExamTypes(stmt);
		return list;
	}
	
	public List<ExamType> getExamTypeForCertificate(String certificateCode) 
			throws SQLException {
		List<ExamType> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM exam_type " +
			" WHERE certificate_code = ?");
		stmt.setString(1, certificateCode);
		list = getListOfExamTypes(stmt);
		return list;
	}
	
	public List<ExamType> getExamTypesForCertificatesPeopleAreMissingForJob(String personCode,
																			String jobCode,
																			String jobProfileCode)
			throws SQLException {
		List<ExamType> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH " + 
			"   job_certificates as " +
			"     (SELECT certificate_code " +
			"      FROM job_certificate " +
			"      WHERE job_code = ?), " +
			"   job_profile_certificates as " +
			"     (SELECT certificate_code " +
			"      FROM job_profile_certificate " +
			"      WHERE job_profile_code = ?)," +
			"   person_certificates as " +
			"     (SELECT certificate_code " +
			"      FROM earns " +
			"      WHERE person_code = ?) " +
			" SELECT * " + 
			" FROM certificate NATURAL JOIN (SELECT * FROM person_certificates MINUS " +
			"                                SELECT * FROM job_certificates MINUS " +
			"                                SELECT * FROM job_profile_certificates)" +
			"      NATURAL JOIN exam_type "
		);
		stmt.setString(1, jobCode);
		stmt.setString(2, jobProfileCode);
		stmt.setString(3, personCode);
		list = getListOfExamTypes(stmt);
		return list;
	}
	
	// Inserts
	public int addExamType(ExamType examType) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" INSERT INTO exam_type " +
			" VALUES (?, ?, ?)"
			);
		stmt.setString(1, examType.getExamTypeCode());
		stmt.setString(2, examType.getCertificateCode());
		stmt.setString(3, examType.getExamTitle());
		count = stmt.executeUpdate();
		return count; 
	}
	
	// Updates
	public int updateExamType(ExamType examType) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" UPDATE exam_type " +
			" SET certificate_code = ? " +
			"     exam_title = ? " +
			" WHERE exam_type_code = ? "
		);
		stmt.setString(1, examType.getCertificateCode());
		stmt.setString(2, examType.getExamTitle());
		stmt.setString(3, examType.getExamTypeCode());
		count = stmt.executeUpdate();
		return count;
	}
	
	// Helper Functions
	private List<ExamType> getListOfExamTypes(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty person list
		List<ExamType> list = new ArrayList<ExamType>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new PhoneNumber from the results
			// and add it to the list.
			
			list.add( new ExamType(
				results.getString("exam_type_code"),
				results.getString("certificate_code"),
				results.getString("exam_title"))
			);
		}
		return list;
	}
}
