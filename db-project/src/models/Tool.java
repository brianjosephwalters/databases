package models;

public class Tool {
	private String toolCode;
	private String toolName;
	private String toolDescription;
	
	public Tool(String code, String name, String desc) {
		this.toolCode = code;
		this.toolName = name;
		this.toolDescription = desc;
	}
	
	/**
	 * @return the toolCode
	 */
	public String getToolCode() {
		return toolCode;
	}
	/**
	 * @return the toolName
	 */
	public String getToolName() {
		return toolName;
	}
	/**
	 * @return the toolDescription
	 */
	public String getToolDescription() {
		return toolDescription;
	}
	/**
	 * @param toolCode the toolCode to set
	 */
	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}
	/**
	 * @param toolName the toolName to set
	 */
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}
	/**
	 * @param toolDescription the toolDescription to set
	 */
	public void setToolDescription(String toolDescription) {
		this.toolDescription = toolDescription;
	}
}
