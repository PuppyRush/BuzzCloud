package property.enums;

public enum enumMemberState
{
	NOT_EXIST_MEMBER_FROM_MAP("not exist member from MemberMap"),
	NOT_LOGIN("this member isnt login now"),
	NOT_JOIN("this member wasn't joined"),
	NOT_LOGOUT("this member inst join now"),
	ERROR("unknow error");
	
	private String enumStr;
	
	enumMemberState(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
}