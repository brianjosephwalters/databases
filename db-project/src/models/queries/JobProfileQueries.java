package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.JobProfile;

public class JobProfileQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public JobProfileQueries (Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * All Job Profiles.
	 */
	public List<JobProfile> getAllJobProfiles() 
			throws SQLException {
		List<JobProfile> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			" SELECT * " +
			" FROM job_profile ");
		list = getListOfJobProfiles(stmt);
		
		return list;
	}
	
	// Helper Functions
	private List<JobProfile> getListOfJobProfiles(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty job profiles list
		List<JobProfile> list = new ArrayList<JobProfile>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new JobProfile from the results
			// and add it to the list.
			list.add( new JobProfile(
				results.getString("job_profile_code"),
				results.getString("job_profile_title"),
				results.getString("job_profile_description") )
			);
		}
		return list;
	}
}
