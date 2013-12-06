package models;

import java.util.Date;

public class Project {
	private String projectCode;
	private String companyCode;
	private String projectTitle;
	private String companyName;
	private String budgetCode;
	private Date startDate;
	private Date endDate;
	
	public Project(String projectCode, String projectTitle, 
				   String companyCode, String companyName,
				   String budgetCode,
				   Date startDate, Date endDate) {
		this.projectCode = projectCode;
		this.companyCode = companyCode;
		this.projectTitle = projectTitle;
		this.budgetCode = budgetCode;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 * @return the projectCode
	 */
	public String getProjectCode() {
		return projectCode;
	}
	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}
	/**
	 * @return the projectTitle
	 */
	public String getProjectTitle() {
		return projectTitle;
	}
	/**
	 * @return the budgetCode
	 */
	public String getBudgetCode() {
		return budgetCode;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param projectCode the projectCode to set
	 */
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	/**
	 * @param projectTitle the projectTitle to set
	 */
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}
	/**
	 * @param budgetCode the budgetCode to set
	 */
	public void setBudgetCode(String budgetCode) {
		this.budgetCode = budgetCode;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String toString() {
		return this.projectCode + ": " + this.projectTitle;
	}
}
