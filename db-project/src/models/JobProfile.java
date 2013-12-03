package models;

public class JobProfile {
	private String jobProfileCode;
	private String jobProfileTitle;
	private String jobProfileDescription;
	
	public JobProfile(String jobProfileCode, String jobProfileTitle,
					  String jobProfileDescription) {
		this.jobProfileCode = jobProfileCode;
		this.jobProfileTitle = jobProfileTitle;
		this.jobProfileDescription = jobProfileDescription;
	}
	/**
	 * @return the jobProfileCode
	 */
	public String getJobProfileCode() {
		return jobProfileCode;
	}
	/**
	 * @return the jobProfileTitle
	 */
	public String getJobProfileTitle() {
		return jobProfileTitle;
	}
	/**
	 * @return the jobProfileDescription
	 */
	public String getJobProfileDescription() {
		return jobProfileDescription;
	}
	/**
	 * @param jobProfileCode the jobProfileCode to set
	 */
	public void setJobProfileCode(String jobProfileCode) {
		this.jobProfileCode = jobProfileCode;
	}
	/**
	 * @param jobProfileTitle the jobProfileTitle to set
	 */
	public void setJobProfileTitle(String jobProfileTitle) {
		this.jobProfileTitle = jobProfileTitle;
	}
	/**
	 * @param jobProfileDescription the jobProfileDescription to set
	 */
	public void setJobProfileDescription(String jobProfileDescription) {
		this.jobProfileDescription = jobProfileDescription;
	}
	
	public String toString() {
		return this.getJobProfileCode() + ": " + this.getJobProfileTitle();
	}
}
