package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Tool;

public class ToolQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public ToolQueries (Connection connection) {
		this.connection = connection;
	}
	/**
	 * All tools.
	 */
	public List<Tool> getAllTools() 
			throws SQLException {
		List<Tool> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM tool");
		list = getListOfTools(stmt);
		
		return list;
	}
	
	/**
	 * All tools used by a particular company.
	 */
	public List<Tool> getToolsForCompany(String company_code) 
			throws SQLException {
		List<Tool> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM tool NATURAL JOIN uses" +
			"WHERE company_code = ?");
		stmt.setString(1, company_code);
		list = getListOfTools(stmt);
		
		return list;
	}
	
	// Helper Functions
	private List<Tool> getListOfTools(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty Tool list
		List<Tool> list = new ArrayList<Tool>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Tool from the results
			// and add it to the list.
			list.add( new Tool(
				results.getString("tool_code"),
				results.getString("tool_name"),
				results.getString("tool_description"))
			);
		}
		return list;
	}
}
