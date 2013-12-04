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
	
	// Inserts
	public int addJobProfile(JobProfile jobProfile) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" INSERT INTO job_profile " +
			" VALUES (?, ?, ?)"
			);
		stmt.setString(1, jobProfile.getJobProfileCode());
		stmt.setString(2, jobProfile.getJobProfileTitle());
		stmt.setString(3, jobProfile.getJobProfileDescription());
		
		count = stmt.executeUpdate();
		return count; 
	}
	
	// Updates
	public int updateJobProfile(JobProfile jobProfile) throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			" UPDATE job_profile " +
			" SET job_profile_title = ? " +
			"     job_profile_description = ? " +
			" WHERE job_profile_code = ? "
		);
		stmt.setString(1, jobProfile.getJobProfileTitle());
		stmt.setString(2, jobProfile.getJobProfileDescription());
		stmt.setString(3, jobProfile.getJobProfileCode());
		count = stmt.executeUpdate();
		return count;
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
