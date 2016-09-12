package property.enums.widget;

public enum enumWidgetKind {

	SPORTS("sports"),
	WEATHER("weather"),
	GAMES("games"),
	SECURITY("security");
	
	private String kind;
	
	enumWidgetKind(String str){
		kind = str;
	}
	
	public String getString(){
		return kind;
	}
	
	
}
