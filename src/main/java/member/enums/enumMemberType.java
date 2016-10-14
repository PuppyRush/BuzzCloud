package member.enums;


public enum enumMemberType {
	
	NOTHING("NOTHING"),
	NAVER("NAVER"),
	GOOGLE("GOOGLE");
			
	private String enumStr;
	
	enumMemberType(String str){
		enumStr = str;
	}
	
	public String toString(){
		return enumStr;
	}
	
}

