package property.enums;


public enum enumMysql {
	
	dbId("widget"),
	dbPasswd("widget"),
	dbUrl("jdbc:mysql://221.166.236.59:3306/widgetstore"),
	
/*	dbId("root"),
	dbPasswd("oss()90"),
	dbUrl("jdbc:mysql://127.0.0.1:3306/widgetstore"),
	*/
	dbDriver("com.mysql.jdbc.Driver");
			
	
	private String enumStr;
	
	enumMysql(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
	
}

