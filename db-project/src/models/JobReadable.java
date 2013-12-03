package models;

import java.util.Date;

public class JobReadable {
	private String jobCode;
	private String jobProfileTitle;
	private String companyName;
	private String jobType;
	private double payRate;
	private Date openingDate;
	private Date closingDate;
	
	public JobReadable (String jobCode, String jobProfileTitle, 
						String companyName, String jobType, 
						double payRate, Date openingDate, Date closingDate) {
		this.jobCode = jobCode;
		this.jobProfileTitle = jobProfileTitle;
		this.companyName = companyName;
		this.jobType = jobType;
		this.payRate = payRate;
		this.openingDate = openingDate;
		this.closingDate = closingDate;		
	}

	/**
	 * @return the jobCode
	 */
	public String getJobCode() {
		return jobCode;
	}

	/**
	 * @return the jobProfileTitle
	 */
	public String getJobProfileTitle() {
		return jobProfileTitle;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @return the jobType
	 */
	public String getJobType() {
		return jobType;
	}

	/**
	 * @return the payRate
	 */
	public double getPayRate() {
		return payRate;
	}

	/**
	 * @return the openingDate
	 */
	public Date getOpeningDate() {
		return openingDate;
	}

	/**
	 * @return the closingDate
	 */
	public Date getClosingDate() {
		return closingDate;
	}

	/**
	 * @param jobCode the jobCode to set
	 */
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	/**
	 * @param jobProfileTitle the jobProfileTitle to set
	 */
	public void setJobProfileTitle(String jobProfileTitle) {
		this.jobProfileTitle = jobProfileTitle;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @param jobType the jobType to set
	 */
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	/**
	 * @param payRate the payRate to set
	 */
	public void setPayRate(double payRate) {
		this.payRate = payRate;
	}

	/**
	 * @param openingDate the openingDate to set
	 */
	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	/**
	 * @param closingDate the closingDate to set
	 */
	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}
	
	public String toString() {
		return getJobCode() + ": " + getJobProfileTitle();
	}
}
