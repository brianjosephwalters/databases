package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Certificate;

public class CertificateQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public CertificateQueries (Connection connection) {
		this.connection = connection;
	}

	/**
	 * All certificates.
	 */
	
	public List<Certificate> getAllCertificates() 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM certificate");
		list = getListOfCertificates(stmt);
		
		return list;
	}
	
	/**
	 * All certificates available for a tool.
	 */
	public List<Certificate> getCertificatesForTool(String toolCode) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM certificate" +
			"WHERE tool_code = ?");
		list = getListOfCertificates(stmt);
		stmt.setString(1, toolCode);
		return list;
	}
	
	/**
	 * All certificates issued by a company.
	 */
	public List<Certificate> getCertificatesFromCompany(String companyCode) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM certificate" +
			"WHERE company_code = ?");
		list = getListOfCertificates(stmt);
		stmt.setString(1, companyCode);
		return list;
	}
	
	
	/**
	 * All certificates required by a job.
	 */
	public List<Certificate> getCertificatesForJob(String job_code) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM certificate NATURAL JOIN job_profile_certificate" +
			"     NATURAL JOIN job" +
			"WHERE job_code = ?");
		list = getListOfCertificates(stmt);
		stmt.setString(1, job_code);
		return list;
	}
	
	/**
	 * All certificates required by a job profile.
	 */
	public List<Certificate> getCertificatesForJobProfile(String job_profile_code) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM certificate NATURAL JOIN job_profile_certificate" +
			"WHERE job_code = ?");
		list = getListOfCertificates(stmt);
		stmt.setString(1, job_profile_code);
		return list;
	}
	
	/**
	 * All certificates prepared for by a course.
	 */
	public List<Certificate> getCertificatesPreparedByCourse(String course_code) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM certificate NATURAL JOIN prepares_for" +
			"WHERE course_code = ?");
		list = getListOfCertificates(stmt);
		stmt.setString(1, course_code);
		return list;
	}
	
	/**
	 * All certificates possessed by a person.
	 */
	public List<Certificate> getCertificatesForPerson(String person_code) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM certificate NATURAL JOIN exam_type NATURAL JOIN" +
			"     exam NATURAL JOIN takes" +
			"WHERE person_code = ?");
		list = getListOfCertificates(stmt);
		stmt.setString(1, person_code);
		return list;
	}
	
	// Helper Functions
	private List<Certificate> getListOfCertificates(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty Certificate list
		List<Certificate> list = new ArrayList<Certificate>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Certificate from the results
			// and add it to the list.
			list.add( new Certificate(
				results.getString("certificate_code"),
				results.getString("certificate_title"),
				results.getString("certificate_description"),
				results.getString("tool_code"),
				results.getString("company_code"),
				results.getInt("days_valid"))
				);
		}
		return list;
	}
}