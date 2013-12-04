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
			" FROM project NATURAL JOIN company ");
		list = getListOfProjects(stmt);
		
		return list;
	}
	
	// Inserts
	public int addProject(Project project) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" INSERT INTO project " +
			" VALUES (?, ?, ?, ?, ?, ?)"
			);
		stmt.setString(1, project.getProjectCode());
		stmt.setString(2, project.getCompanyCode());
		stmt.setString(3, project.getProjectTitle());
		stmt.setString(4, project.getBudgetCode());
		stmt.setDate(5, new java.sql.Date(project.getStartDate().getTime()));
		stmt.setDate(6, new java.sql.Date(project.getEndDate().getTime()));
		count = stmt.executeUpdate();
		return count;
	}

	// Updates
	public int updateProject(Project project) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" UPDATE project " +
			" SET company_code = ? " +
			"     project_title = ? " +
			"     budget_code = ? " +
			"     start_date = ? " +
			"     end_date = ? " +
			" WHERE project_code = ? "
		);
		stmt.setString(1, project.getCompanyCode());
		stmt.setString(2, project.getProjectTitle());
		stmt.setString(3, project.getBudgetCode());
		stmt.setDate(4, new java.sql.Date(project.getStartDate().getTime()));
		stmt.setDate(5, new java.sql.Date(project.getEndDate().getTime()));
		stmt.setString(6, project.getProjectCode());
		count = stmt.executeUpdate();
		return count;
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
				results.getString("project_title"),
				results.getString("company_code"),
				results.getString("company_title"),
				results.getString("budget_code"),
				results.getDate("start_date"),
				results.getDate("end_date"))
			);
		}
		return list;
	}
}
