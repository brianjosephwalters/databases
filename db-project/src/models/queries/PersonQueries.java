package models.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Person;

public class PersonQueries {
	// Instance Variables
	private Connection connection;
	
	// Constructors
	public PersonQueries (Connection connection) {
		this.connection = connection;
	}
	
	// Queries
	public List<Person> getAllPeople() 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM person");
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getPeopleByLastName(String lastName) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM person " +
			"WHERE last_name = ?");
		stmt.setString(1, lastName);
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getPeopleWithSkill(String skillCode) 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM person NATURAL JOIN person_skill " +
			"WHERE skill_code = ?");
		stmt.setString(1, skillCode);
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getPeopleEmployed() 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * " +
			"FROM person NATURAL JOIN employment");
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	public List<Person> getPeopleUnEmployed() 
			throws SQLException {
		List<Person> list = null;
		PreparedStatement stmt = connection.prepareStatement(
			"(SELECT * " +
			" FROM person)" +
			"EXCEPT" +
			"(SELECT * " +
			"FROM person NATURAL JOIN employment)");
		list = getListOfPeople(stmt);
		
		return list;
	}
	
	// People who attended a course
	// People who attended a section
	// People who earned a certificate
	// People who took an exam
	// People who can use a tool
	// People who work for a company
	// People who have ever worked for a company
	// People who work on a project
	// People who have ever worked on a project
	// People qualified for a job_profile
	// People qualified for a job.
	
	// Insertions
	public int addPerson(Person person) 
			throws SQLException {
		int count = 0;
		PreparedStatement stmt = connection.prepareStatement(
			"INSERT INTO person " +
			"VALUES (?, ?, ?, ?, ?)"
			);
		stmt.setString(1, person.getPersonCode());
		stmt.setString(2, person.getLastName());
		stmt.setString(3, person.getFirstName());
		stmt.setString(4, person.getGender());
		stmt.setString(4, person.getEmail());
		count = stmt.executeUpdate();
		return count;
	}
	
	// Updates
	
	private List<Person> getListOfPeople(PreparedStatement stmt) 
			throws SQLException {
		// Create an empty person list
		List<Person> list = new ArrayList<Person>();
		// Execute the query
		ResultSet results = stmt.executeQuery();
		// Walk through the results...
		while (results.next()) {
			// Create a new Person from the results
			// and add it to the list.
			list.add( new Person(
				results.getString("personCode"),
				results.getString("lastName"),
				results.getString("firstName"),
				results.getString("gender"),
				results.getString("email"))
			);
		}
		return list;
	}
}
