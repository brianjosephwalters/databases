package models;

public class Address {
	private String addressCode;
	private String addressType;
	private String street1;
	private String street2;
	private String city;
	private String zipcode;
	
	public Address(String addressCode, String addressType, String street1,
				   String street2, String city, String zipcode) {
		this.addressCode = addressCode;
		this.addressType = addressType;
		this.street1 = street1;
		this.street2 = street2;
		this.city = city;
		this.zipcode = zipcode;
	}
	
	/**
	 * @return the addressCode
	 */
	public String getAddressCode() {
		return addressCode;
	}
	/**
	 * @return the addressType
	 */
	public String getAddressType() {
		return addressType;
	}
	/**
	 * @return the street1
	 */
	public String getStreet1() {
		return street1;
	}
	/**
	 * @return the street2
	 */
	public String getStreet2() {
		return street2;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}
	/**
	 * @param addressCode the addressCode to set
	 */
	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}
	/**
	 * @param addressType the addressType to set
	 */
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	/**
	 * @param street1 the street1 to set
	 */
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	/**
	 * @param street2 the street2 to set
	 */
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String toString() {
		return this.getAddressType();
	}
}
