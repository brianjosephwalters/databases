package models;

public class PhoneNumber {
	private String phoneCode;
	private String phoneType;
	private String phoneNum;
	
	public PhoneNumber(String phoneCode, String phoneType, String phoneNum) {
		this.phoneCode = phoneCode;
		this.phoneType = phoneType;
		this.phoneNum = phoneNum;
	}
	/**
	 * @return the phoneCode
	 */
	public String getPhoneCode() {
		return phoneCode;
	}
	/**
	 * @return the phoneType
	 */
	public String getPhoneType() {
		return phoneType;
	}
	/**
	 * @return the phoneNum
	 */
	public String getPhoneNum() {
		return phoneNum;
	}
	/**
	 * @param phoneCode the phoneCode to set
	 */
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	/**
	 * @param phoneType the phoneType to set
	 */
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	/**
	 * @param phoneNum the phoneNum to set
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String toString() {
		return this.phoneType;
	}
}
