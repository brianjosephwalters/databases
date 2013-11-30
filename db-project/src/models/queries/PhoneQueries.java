package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.PhoneNumber;

public class PhoneQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public PhoneQueries (Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * All phone numbers, sorted by address code.
	 */
	public List<PhoneNumber> getAllPhoneNumbers() 
			throws SQLException {
		List<PhoneNumber> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM phone_number");
		list = getListOfPhoneNumbers(stmt);
		
		return list;
	}
	
	/**
	 * The phone numbers and types of a particular company.
	 */
	public List<PhoneNumber> getCompanyAddresses(String company_code) 
			throws SQLException {
		List<PhoneNumber> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM phone_number NATURAL JOIN company_phone " +
			"WHERE company_code = ?");
		stmt.setString(1, company_code);
		
		list = getListOfPhoneNumbers(stmt);
		return list;
	}
	
	/**
	 * The addresses and types of a particular person.
	 */
	public List<PhoneNumber> getPersonPhoneNumbers(String person_code) 
			throws SQLException {
		List<PhoneNumber> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM phone_number NATURAL JOIN person_phone " +
			"WHERE person_code = ?");
		stmt.setString(1, person_code);
		
		list = getListOfPhoneNumbers(stmt);
		return list;
	}
	
	// Inserts
	public int addPhoneNumber(PhoneNumber phoneNumber) 
			throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			"INSERT INTO phone_number " +
			"VALUES (?, ?, ?)"
			);
		stmt.setString(1, phoneNumber.getPhoneCode());
		stmt.setString(2, phoneNumber.getPhoneType());
		stmt.setString(3, phoneNumber.getPhoneNum());
		count = stmt.executeUpdate();
		return count;
	}
		
	// Updates
		
	// Helper Functions
	private List<PhoneNumber> getListOfPhoneNumbers(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty phone number list
		List<PhoneNumber> list = new ArrayList<PhoneNumber>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new PhoneNumber from the results
			// and add it to the list.
			list.add( new PhoneNumber(
				results.getString("phone_code"),
				results.getString("phone_type"),
				results.getString("phone_num") )
			);
		}
		return list;
	}
	
}
