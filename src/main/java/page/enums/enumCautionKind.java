package page.enums;

public enum enumCautionKind {

	NORMAL("green"),
	CATUION("yellow"),
	ERROR("red"),
	INTERNAL_ERROR("red");
	
	private String kind;
	
	enumCautionKind(String str){
		kind = str;
	}
	
	public String getString(){
		return kind;
	}
	
}
