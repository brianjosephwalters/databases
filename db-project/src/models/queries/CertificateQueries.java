package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Certificate;
import models.Person;

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
			" SELECT * " +
			" FROM certificate");
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
			" SELECT * " +
			" FROM certificate " +
			" WHERE tool_code = ?");
		stmt.setString(1, toolCode);
		
		list = getListOfCertificates(stmt);
		return list;
	}
	
	/**
	 * All certificates issued by a company.
	 */
	public List<Certificate> getCertificatesFromCompany(String companyCode) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM certificate " +
			" WHERE company_code = ?");
		stmt.setString(1, companyCode);
		list = getListOfCertificates(stmt);
		return list;
	}
	
	
	/**
	 * All certificates required by a job.
	 */
	public List<Certificate> getCertificatesForJob(String jobCode) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM certificate NATURAL job_certificate" +
			" WHERE job_code = ?");
		stmt.setString(1, jobCode);
		list = getListOfCertificates(stmt);
		return list;
	}
	
	/**
	 * All certificates required by a job profile.
	 */
	public List<Certificate> getCertificatesForJobProfile(String jobProfileCode) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM certificate NATURAL JOIN job_profile_certificate " +
			" WHERE job_code = ? ");
		stmt.setString(1, jobProfileCode);
		list = getListOfCertificates(stmt);
		return list;
	}
	
	public List<Certificate> getCertificatesForJobAndJobProfile(String jobCode,
																String jobProfileCode)
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH certificate_codes AS" +
			" 	(SELECT certificate_code " +
			" 	FROM certificate NATURAL JOIN job_certificate "  + 
			" 	WHERE job_code = ? " +
			" 	UNION " +
			" 	SELECT certificate_code " +
			" 	FROM certificate NATURAL JOIN job_profile_certificate " +
			" 	WHERE job_profile_code = ?)" +
			" SELECT *" +
			" FROM certificate NATURAL JOIN certificate_codes");
		stmt.setString(1, jobProfileCode);
		stmt.setString(2, jobProfileCode);
		list = getListOfCertificates(stmt);
		return list;
	}
	
	public List<Certificate> getCertificatesMissingFromPersonForJob(String personCode,
																	String jobCode,
																	String jobProfileCode)
			throws SQLException {
		List<Certificate> list = null;
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
			"                                SELECT * FROM job_profile_certificates)"
			);
		stmt.setString(1, jobCode);
		stmt.setString(2, jobProfileCode);
		stmt.setString(3, personCode);
		list = getListOfCertificates(stmt);
		return list;
	}
	

	
	/**
	 * All certificates prepared for by a course.
	 */
	public List<Certificate> getCertificatesPreparedByCourse(String courseCode) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM certificate NATURAL JOIN prepares_for " +
			" WHERE course_code = ? ");
		stmt.setString(1, courseCode);
		list = getListOfCertificates(stmt);
		return list;
	}
	
	/**
	 * All certificates possessed by a person.
	 */
	public List<Certificate> getCertificatesForPerson(String personCode) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM certificate NATURAL JOIN earns"  +
			" WHERE person_code = ?");
		stmt.setString(1, personCode);
		list = getListOfCertificates(stmt);
		return list;
	}
	
	/**
	 * All certificates not possessed by a person.
	 */
	public List<Certificate> getCertificatesNotPossessedByPerson(String personCode) 
			throws SQLException {
		List<Certificate> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" WITH  lacked_certificates AS" +
			"   ( (SELECT certificate_code" + 
		    "      FROM certificate)" +
		    "     MINUS " +
		    "     (SELECT certificate_code" +
		    "      FROM certificate NATURAL JOIN earns" +
		    "      WHERE person_code = ?) )" +
			" SELECT * " +
			" FROM certificate NATURAL JOIN lacked_certificates");
		stmt.setString(1, personCode);
		list = getListOfCertificates(stmt);
		
		return list;
	}	
	
	// Insertions
	public int addCertificate(Certificate certificate) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" INSERT INTO certificate " +
			"   VALUES (?, ?, ?, ?, ?, ?) "
		);
		stmt.setString(1, certificate.getCertificateCode());
		stmt.setString(2, certificate.getCertificateTitle());
		stmt.setString(3, certificate.getCertificateDescription());
		stmt.setString(4, certificate.getToolCode());
		stmt.setString(5, certificate.getCompanyCode());
		stmt.setInt(6, certificate.getDaysValid());
		count = stmt.executeUpdate();
		return count;
	}
	
	public int addCertificatesToPerson(Person person, List<Certificate> list)
			throws SQLException {
		int count = 0;
		connection.setAutoCommit(false);
		try {
			for (Certificate certificate : list) {
				PreparedStatement stmt = connection.prepareStatement(
					" INSERT INTO earns " +
					"   VALUES (?, ?) "
				);
				stmt.setString(1, person.getPersonCode());
				stmt.setString(2, certificate.getCertificateCode());
				count += stmt.executeUpdate();
			}
			if (count != list.size()) {
				connection.rollback();
			} else {
				connection.commit();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		
		return count;
	}
	
	// Updates
	public int updateCertificate(Certificate certificate) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" UPDATE certificate " +
			" SET certificate_title = ? " +
			"     certificate_description = ? " +
			"     tool_code = ? " +
			"     company_code = ? " +
			"     days_valid = ? " +
			" WHERE certificate_code = ? "
		);
		stmt.setString(1, certificate.getCertificateTitle());
		stmt.setString(2, certificate.getCertificateDescription());
		stmt.setString(3, certificate.getToolCode());
		stmt.setString(4, certificate.getCompanyCode());
		stmt.setInt(5, certificate.getDaysValid());
		stmt.setString(6, certificate.getCertificateCode());
		count = stmt.executeUpdate();
		return count;
	}
	
	// Deletes
	
	public int removeCertificateFromPerson(Person person, Certificate certificate) 
			throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(
			" DELETE FROM earns " +
			" WHERE person_code = ? AND " +
			"       certificate_code = ? ");
		stmt.setString(1, person.getPersonCode());
		stmt.setString(2, certificate.getCertificateCode());
		return stmt.executeUpdate();
	}
	
	public int removeCertificatesFromPerson(Person person, List<Certificate> list)
			throws SQLException {
		int count = 0;
		connection.setAutoCommit(false);
		try {
			for (Certificate certificate : list) {
				PreparedStatement stmt = connection.prepareStatement(
					" DELETE FROM earns " +
					" WHERE person_code = ? AND " +
					"       certificate_code = ? ");
				stmt.setString(1, person.getPersonCode());
				stmt.setString(2, certificate.getCertificateCode());
				count += stmt.executeUpdate();
			}
			if (count != list.size()) {
				connection.rollback();
			} else {
				connection.commit();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		return count;
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