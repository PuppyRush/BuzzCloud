package mail;

public enum enumMail {
	
	gmailID("gooddaumi@gmail.com"),
	gmailPW("rksk!@12");

	private String enumStr;
	
	enumMail(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
}
