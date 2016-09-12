package property.enums;

public enum enumUserState {

	NORMAL("0"),
	LOSTPW("1"),
	FAILD_LOGIN("2"),
	SLEEP("4"),
	OLD_PASSWD("8");
	
	
	private String enumStr;
	
	enumUserState(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
	
}
