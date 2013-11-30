package models;

import java.util.Date;

public class Job {
	private String jobCode;
	private String jobProfileCode;
	private String companyCode;
	private String jobType;
	private double payRate;
	private Date openingDate;
	private Date closingDate;
	
	public Job (String jobCode, String jobProfileCode, 
				String companyCode, String jobType, 
				double payRate, Date openingDate, Date closingDate) {
		this.jobCode = jobCode;
		this.jobProfileCode = jobProfileCode;
		this.companyCode = companyCode;
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
	 * @return the jobProfileCode
	 */
	public String getJobProfileCode() {
		return jobProfileCode;
	}
	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
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
	 * @param jobProfileCode the jobProfileCode to set
	 */
	public void setJobProfileCode(String jobProfileCode) {
		this.jobProfileCode = jobProfileCode;
	}
	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
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
}
