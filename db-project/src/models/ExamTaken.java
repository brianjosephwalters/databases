package models;

public class ExamTaken {
	private String personCode;
	private String examCode;
	private String examTypeCode;
	private int score;
	/**
	 * @return the personCode
	 */
	public String getPersonCode() {
		return personCode;
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
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param personCode the personCode to set
	 */
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
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
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
}
