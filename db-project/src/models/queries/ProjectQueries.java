package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Project;

public class ProjectQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public ProjectQueries (Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * All projects.
	 */
	public List<Project> getAllProjects() 
			throws SQLException {
		List<Project> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM project ");
		list = getListOfProjects(stmt);
		
		return list;
	}
	
	// Helper Functions
	private List<Project> getListOfProjects(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty project list
		List<Project> list = new ArrayList<Project>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Project from the results
			// and add it to the list.
			list.add( new Project(
				results.getString("project_code"),
				results.getString("company_code"),
				results.getString("project_title"),
				results.getString("budget_code"),
				results.getDate("start_date"),
				results.getDate("end_date"))
			);
		}
		return list;
	}
}
