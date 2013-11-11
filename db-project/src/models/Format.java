package models;

public class Format {
	private String formatCode;
	private String formatName;
	private String formatDescription;
	private double fees;
	/**
	 * @return the formatCode
	 */
	public String getFormatCode() {
		return formatCode;
	}
	/**
	 * @return the formatName
	 */
	public String getFormatName() {
		return formatName;
	}
	/**
	 * @return the formatDescription
	 */
	public String getFormatDescription() {
		return formatDescription;
	}
	/**
	 * @return the fees
	 */
	public double getFees() {
		return fees;
	}
	/**
	 * @param formatCode the formatCode to set
	 */
	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}
	/**
	 * @param formatName the formatName to set
	 */
	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}
	/**
	 * @param formatDescription the formatDescription to set
	 */
	public void setFormatDescription(String formatDescription) {
		this.formatDescription = formatDescription;
	}
	/**
	 * @param fees the fees to set
	 */
	public void setFees(double fees) {
		this.fees = fees;
	}
}
