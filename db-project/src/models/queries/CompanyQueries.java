package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Company;

public class CompanyQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public CompanyQueries (Connection connection) {
		this.connection = connection;
	}

	/**
	 * All companies, by name.
	 */
	public List<Company> getAllCompanies() 
			throws SQLException {
		List<Company> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM company");
		list = getListOfCompanies(stmt);

		return list;
	}
	
	/**
	 * All of the companies that have a certain specialty
	 */
	public List<Company> getCompaniesWithSpecialty(String specialtyCode) 
			throws SQLException {
		List<Company> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM company NATURAL JOIN company_specialty" +
			" WHERE specialty_code = ?");
		stmt.setString(1, specialtyCode);
		list = getListOfCompanies(stmt);
		
		return list;
	}
	
	/**
	 * All of the companies that use a particular tool
	 */
	public List<Company> getCompaniesUsingTool(String toolCode) 
			throws SQLException {
		List<Company> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM company NATURAL JOIN uses" +
			" WHERE tool_code = ?");
		stmt.setString(1, toolCode);
		list = getListOfCompanies(stmt);

		return list;
	}
	
	/**
	 * All of the companies that offer a position (job_profile)
	 */
	public List<Company> getCompaniesWithJobProfile(String jobProfileCode) 
			throws SQLException {
		List<Company> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM company NATURAL JOIN job_company NATURAL JOIN job" +
			" WHERE job_profile_code = ?");
		stmt.setString(1, jobProfileCode);
		list = getListOfCompanies(stmt);

		return list;
	}
	
	/**
	 * All of the companies that offer a particular course.
	 */
	public List<Company> getCompaniesOfferingCourse(String courseCode) 
		throws SQLException {
		List<Company> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM company NATURAL JOIN provides NATURAL JOIN section" +
			" WHERE course_code = ?");
		stmt.setString(1, courseCode);
		list = getListOfCompanies(stmt);

		return list;
	}
	
	// Insertions
	public int addCompany(Company company) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" INSERT INTO company " +
			"   VALUES (?, ?, ?) "
		);
		stmt.setString(1, company.getCompanyCode());
		stmt.setString(2, company.getCompanyName());
		stmt.setString(3, company.getWebsite());
		count = stmt.executeUpdate();
		return count;
	}
	
	// Updates
	public int updateCompany(Company company) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" UPDATE company " +
			" SET company_name = ? " +
			"     website = ? " +
			" WHERE company_code = ? "
		);
		
		stmt.setString(1, company.getCompanyName());
		stmt.setString(2, company.getWebsite());
		stmt.setString(3, company.getCompanyCode());
		return count;
	}
	
	// Helper Functions
	private List<Company> getListOfCompanies(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty Company list
		List<Company> list = new ArrayList<Company>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Company from the results
			// and add it to the list.
			list.add( new Company(
				results.getString("company_code"),
				results.getString("company_name"),
				results.getString("website") )
			);
		}
		return list;
	}
}
