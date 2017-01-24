package com.puppyrush.buzzcloud.entity.member.enums;


public enum enumMemberStandard {
	
	RESEND_STANDRATE_DATE("24"),				//hour
	PASSWD_CHANGE_DATE_OF_MONTH("3"),		//day
	POSSIBILLTY_FAILD_LOGIN_NUM("5"),
	NAME_LENGTH(4);
	
	private String enumStr;
	private int enumInt;
	
	enumMemberStandard(int enumInt){
		this.enumInt = enumInt;
	}
	
	enumMemberStandard(String str){
		enumStr = str;
	}
	
	@Override
	public String toString(){
		return enumStr;
	}
	
	public int toInt(){
		return enumInt;
	}
	
}
