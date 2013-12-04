package models;

public class ExamType {
	private String examTypeCode;
	private String certificateCode;
	private String examTitle;
	
	public ExamType(String examTypeCode, String certificateCode, String examTitle) {
		this.examTypeCode = examTypeCode;
		this.certificateCode = certificateCode;
		this.examTitle = examTitle;
	}
	/**
	 * @return the examTypeCode
	 */
	public String getExamTypeCode() {
		return examTypeCode;
	}
	/**
	 * @return the certificateCode
	 */
	public String getCertificateCode() {
		return certificateCode;
	}
	/**
	 * @return the examTitle
	 */
	public String getExamTitle() {
		return examTitle;
	}
	/**
	 * @param examTypeCode the examTypeCode to set
	 */
	public void setExamTypeCode(String examTypeCode) {
		this.examTypeCode = examTypeCode;
	}
	/**
	 * @param certificateCode the certificateCode to set
	 */
	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}
	/**
	 * @param examTitle the examTitle to set
	 */
	public void setExamTitle(String examTitle) {
		this.examTitle = examTitle;
	}
	
	public String toString() {
		return this.getExamTypeCode() + ": " + this.getExamTitle();
	}
}
