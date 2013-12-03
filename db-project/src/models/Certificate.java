package models;

public class Certificate {
	private String certificateCode;
	private String certificateTitle;
	private String certificateDescription;
	private String toolCode;
	private String companyCode;
	private int daysValid;
	
	public Certificate(String certCode,String certTitle,
					   String certDescription, String toolCode,
					   String companyCode, int daysValid) {
		this.certificateCode = certCode;
		this.certificateTitle = certTitle;
		this.certificateDescription = certDescription;
		this.toolCode = toolCode;
		this.companyCode = companyCode;
		this.daysValid = daysValid;
	}
	
	/**
	 * @return the certificateCode
	 */
	public String getCertificateCode() {
		return certificateCode;
	}
	/**
	 * @return the certificateTitle
	 */
	public String getCertificateTitle() {
		return certificateTitle;
	}
	/**
	 * @return the certificateDescription
	 */
	public String getCertificateDescription() {
		return certificateDescription;
	}
	/**
	 * @return the toolCode
	 */
	public String getToolCode() {
		return toolCode;
	}
	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}
	/**
	 * @return the daysValid
	 */
	public int getDaysValid() {
		return daysValid;
	}
	/**
	 * @param certificateCode the certificateCode to set
	 */
	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}
	/**
	 * @param certificateTitle the certificateTitle to set
	 */
	public void setCertificateTitle(String certificateTitle) {
		this.certificateTitle = certificateTitle;
	}
	/**
	 * @param certificateDescription the certificateDescription to set
	 */
	public void setCertificateDescription(String certificateDescription) {
		this.certificateDescription = certificateDescription;
	}
	/**
	 * @param toolCode the toolCode to set
	 */
	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}
	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	/**
	 * @param daysValid the daysValid to set
	 */
	public void setDaysValid(int daysValid) {
		this.daysValid = daysValid;
	}
}
