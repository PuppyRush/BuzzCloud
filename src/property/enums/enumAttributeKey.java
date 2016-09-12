package property.enums;

public enum enumAttributeKey {

	VIEW("VIEW"),
	STRING("STRING"),
	CUSTOM("CUSTOM"),
	STORE("STORE"),
	MEMBER("MEMBER");
	
	private String enumStr;
	
	enumAttributeKey(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
}
