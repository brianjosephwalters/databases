package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Address;

public class AddressQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public AddressQueries (Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * All addresses.
	 */
	public List<Address> getAllAddresses() 
			throws SQLException {
		List<Address> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM address");
		list = getListOfAddresses(stmt);
		
		return list;
	}
	
	/**
	 * The addresses and types of a particular company.
	 */
	public List<Address> getCompanyAddresses(String companyCode) 
			throws SQLException {
		List<Address> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM address NATURAL JOIN company_address " +
			"WHERE company_code = ?");
		stmt.setString(1, companyCode);
		
		list = getListOfAddresses(stmt);
		return list;
	}
		
	/**
	 * The addresses and types of a particular person.
	 */
	public List<Address> getPersonAddresses(String personCode) 
			throws SQLException {
		List<Address> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM address NATURAL JOIN person_address " +
			"WHERE person_code = ?");
		stmt.setString(1, personCode);
		
		list = getListOfAddresses(stmt);
		return list;
	}
	
	// Inserts
	public int addAddress(Address address) 
			throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			"INSERT INTO address " +
			"VALUES (?, ?, ?, ?, ?, ?)"
			);
		stmt.setString(1, address.getAddressCode());
		stmt.setString(2, address.getAddressType());
		stmt.setString(3, address.getStreet1());
		stmt.setString(4, address.getStreet2());
		stmt.setString(5, address.getCity());
		stmt.setString(6, address.getZipcode());
		count = stmt.executeUpdate();
		return count;
	}
	
	// Updates
	
	// Helper Functions
	private List<Address> getListOfAddresses(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty address list
		List<Address> list = new ArrayList<Address>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Address from the results
			// and add it to the list.
			list.add( new Address(
				results.getString("address_code"),
				results.getString("address_type"),
				results.getString("street_1"),
				results.getString("street_2"),
				results.getString("city"),
				results.getString("zipcode"))
			);
		}
		return list;
	}
}
