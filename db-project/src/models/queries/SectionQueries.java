package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Section;

public class SectionQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public SectionQueries (Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * All sections
	 */
	public List<Section> getAllSections() 
			throws SQLException {
		List<Section> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM section");
		list = getListOfSections(stmt);
		
		return list;
	}
	
	/**
	 * All sections of a course every offered.
	 */
	public List<Section> getAllSectionsOfCourse(String courseCode) 
			throws SQLException {
		List<Section> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM section" +
			"WHERE course_code = ?");
		stmt.setString(1, courseCode);
		list = getListOfSections(stmt);
		
		return list;
	}
	
	
	/**
	 * All sections of a course currently offered.
	 */
	public List<Section> getAllCurrentSectionsOfCourse(String courseCode, int year) 
			throws SQLException {
		List<Section> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM section" +
			"WHERE course_code = ? AND" +
			"      year = ?");
		stmt.setString(1, courseCode);
		stmt.setInt(2, year);
		list = getListOfSections(stmt);
		
		return list;
	}
	
	/**
	 * All courses of a particular format.
	 */
	public List<Section> getAllSectionsWithFormat(String formatCode) 
			throws SQLException {
		List<Section> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM section" +
			"WHERE format_code = ?");
		stmt.setString(1, formatCode);
		list = getListOfSections(stmt);
		
		return list;
	}

	/**
	 * All current courses of a particular format.
	 */
	public List<Section> getAllCurrentSectionsWithFormat(String formatCode, int year) 
			throws SQLException {
		List<Section> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM section" +
			"WHERE format_code = ? AND" +
			"      year = ?");
		stmt.setString(1, formatCode);
		stmt.setInt(2, year);
		list = getListOfSections(stmt);
		
		return list;
	}

	
	/**
	 * All sections provided by a company.
	 */
	public List<Section> getAllSectionsFromCompany(String companyCode) 
			throws SQLException {
		List<Section> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM section NATURAL JOIN provides" +
			"WHERE company_code = ?");
		stmt.setString(1, companyCode);
		list = getListOfSections(stmt);
		
		return list;
	}
	
	/**
	 * All sections currently provided by a company.
	 */
	public List<Section> getAllSectionsFromCompany(String companyCode, int year) 
			throws SQLException {
		List<Section> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM section NATURAL JOIN provides" +
			"WHERE company_code = ?" +
			"      AND year = ?");
		stmt.setString(1, companyCode);
		stmt.setInt(2, year);
		list = getListOfSections(stmt);
		
		return list;
	}
	
	
	// Helper Functions
	private List<Section> getListOfSections(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty Section list
		List<Section> list = new ArrayList<Section>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Section from the results
			// and add it to the list.
			list.add( new Section(
				results.getString("course_code"),
				results.getString("section_code"),
				results.getInt("year"),
				results.getString("format_code"),
				results.getDouble("cost"))
			);
		}
		return list;
	}
}
