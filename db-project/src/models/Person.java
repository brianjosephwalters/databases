package models;

public class Person {
	private String personCode;
	private String lastName;
	private String firstName;
	private String gender;
	private String email;

	public Person(String personCode, String lastName, String firstName,
			String gender, String email) {
		this.personCode = personCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.gender = gender;
		this.email = email;
	}
	
	public String getPersonCode() {
		return personCode;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getGender() {
		return gender;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String toString() {
		return personCode + ": " + lastName + ", " + firstName;
	}
}
