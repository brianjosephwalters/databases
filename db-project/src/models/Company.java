package models;

public class Company {
	private String companyCode;
	private String companyName;
	private String website;
	
	public Company (String companyCode, String companyName, String website) {
		this.companyCode = companyCode;
		this.companyName = companyName;
		this.website = website;
	}
	
	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}
	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	public String toString() {
		return this.companyCode + ": " + this.companyName;
	}
}
