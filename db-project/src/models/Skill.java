package models;

public class Skill {
	private String skillCode;
	private String skillName;
	private String skillDescription;
	private String skillLevel;
	/**
	 * @return the skillCode
	 */
	public String getSkillCode() {
		return skillCode;
	}
	/**
	 * @return the skillName
	 */
	public String getSkillName() {
		return skillName;
	}
	/**
	 * @return the skillDescription
	 */
	public String getSkillDescription() {
		return skillDescription;
	}
	/**
	 * @return the skillLevel
	 */
	public String getSkillLevel() {
		return skillLevel;
	}
	/**
	 * @param skillCode the skillCode to set
	 */
	public void setSkillCode(String skillCode) {
		this.skillCode = skillCode;
	}
	/**
	 * @param skillName the skillName to set
	 */
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	/**
	 * @param skillDescription the skillDescription to set
	 */
	public void setSkillDescription(String skillDescription) {
		this.skillDescription = skillDescription;
	}
	/**
	 * @param skillLevel the skillLevel to set
	 */
	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}
	
}
