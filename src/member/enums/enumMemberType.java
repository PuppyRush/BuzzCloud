package member.enums;


public enum enumMemberType {
	
	NOTHING("nothing"),
	NAVER("naver"),
	GOOGLE("google");
			
	private String enumStr;
	
	enumMemberType(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
	
}

