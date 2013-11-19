package models;

public class Course {
	private String courseCode;
	private String courseTitle;
	private String courseDescription;
	private String courseLevel;
	private String status;
	private double retailPrice;
	
	public Course(String code, String title, String desc, String level,
				  String status, double retailPrice) {
		this.courseCode = code;
		this.courseTitle = title;
		this.courseDescription = desc;
		this.courseLevel = level;
		this.status = status;
		this.retailPrice = retailPrice;
	}
	
	/**
	 * @return the courseCode
	 */
	public String getCourseCode() {
		return courseCode;
	}
	/**
	 * @return the courseTitle
	 */
	public String getCourseTitle() {
		return courseTitle;
	}
	/**
	 * @return the courseDescription
	 */
	public String getCourseDescription() {
		return courseDescription;
	}
	/**
	 * @return the courseLevel
	 */
	public String getCourseLevel() {
		return courseLevel;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @return the retailPrice
	 */
	public double getRetailPrice() {
		return retailPrice;
	}
	/**
	 * @param courseCode the courseCode to set
	 */
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	/**
	 * @param courseTitle the courseTitle to set
	 */
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	/**
	 * @param courseDescription the courseDescription to set
	 */
	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}
	/**
	 * @param courseLevel the courseLevel to set
	 */
	public void setCourseLevel(String courseLevel) {
		this.courseLevel = courseLevel;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @param retailPrice the retailPrice to set
	 */
	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}
}
