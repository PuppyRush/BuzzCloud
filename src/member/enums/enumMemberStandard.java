package member.enums;


public enum enumMemberStandard {
	
	RESEND_STANDRATE_DATE("24"),				//hour
	PASSWD_CHANGE_STADNDATE_DATE("90"),		//day
	POSSIBILLTY_FAILD_LOGIN_NUM("5");
	
	private String enumStr;
	
	enumMemberStandard(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
	
}
