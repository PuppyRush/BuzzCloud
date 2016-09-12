package property.enums.widget;

public enum enumWidgetPosition {

	OUR("our"),
	GIT("git");
	
	private String kind;
	
	enumWidgetPosition(String str){
		kind = str;
	}
	
	public String getString(){
		return kind;
	}
	
}
