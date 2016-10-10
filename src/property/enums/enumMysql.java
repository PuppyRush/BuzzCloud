package property.enums;


public enum enumMysql {

	dbId("root"),
	dbPasswd("oss()90"),
	dbUrl("jdbc:mysql://127.0.0.1:3306/buzzcloud"),
	
	dbDriver("com.mysql.jdbc.Driver");
			
	
	private String enumStr;
	
	enumMysql(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
	
}

