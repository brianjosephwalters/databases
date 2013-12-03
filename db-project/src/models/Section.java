package models;

public class Section {
	private String courseCode;
	private String sectionCode;
	private int year;
	private String formatCode;
	private double cost;
	
	public Section(String courseCode, String sectionCode, int year,
				   String formatCode, double cost) {
		this.courseCode = courseCode;
		this.sectionCode = sectionCode;
		this.year = year;
		this.formatCode = formatCode;
		this.cost = cost;
	}
	
	/**
	 * @return the courseCode
	 */
	public String getCourseCode() {
		return courseCode;
	}
	/**
	 * @return the sectionCode
	 */
	public String getSectionCode() {
		return sectionCode;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @return the formatCode
	 */
	public String getFormatCode() {
		return formatCode;
	}
	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * @param courseCode the courseCode to set
	 */
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	/**
	 * @param sectionCode the sectionCode to set
	 */
	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @param formatCode the formatCode to set
	 */
	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
}
