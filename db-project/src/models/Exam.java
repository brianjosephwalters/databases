package models;

import java.util.Date;

public class Exam {
	private String examCode;
	private String examTypeCode;
	private Date examDate;
	
	public Exam (String examCode, String examTypeCode, Date examDate) {
		this.examCode = examCode;
		this.examTypeCode = examTypeCode;
		this.examDate = examDate;
	}
	
	/**
	 * @return the examCode
	 */
	public String getExamCode() {
		return examCode;
	}
	
	/**
	 * @return the examTypeCode
	 */
	public String getExamTypeCode() {
		return examTypeCode;
	}
	
	/**
	 * @return the examDate
	 */
	public Date getExamDate() {
		return examDate;
	}
	/**
	 * @param examCode the examCode to set
	 */
	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}
	
	/**
	 * @param examTypeCode the examTypeCode to set
	 */
	public void setExamTypeCode(String examTypeCode) {
		this.examTypeCode = examTypeCode;
	}
	
	/**
	 * @param examDate the examDate to set
	 */
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
}
