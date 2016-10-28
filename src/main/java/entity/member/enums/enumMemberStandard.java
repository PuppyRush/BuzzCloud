package entity.member.enums;


public enum enumMemberStandard {
	
	RESEND_STANDRATE_DATE("24"),				//hour
	PASSWD_CHANGE_DATE_OF_MONTH("3"),		//day
	POSSIBILLTY_FAILD_LOGIN_NUM("5");
	
	
	private String enumStr;
	
	enumMemberStandard(String str){
		enumStr = str;
	}
	
	@Override
	public String toString(){
		return enumStr;
	}
	
}
