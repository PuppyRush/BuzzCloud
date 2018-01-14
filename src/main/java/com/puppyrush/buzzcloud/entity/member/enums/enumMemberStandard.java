package com.puppyrush.buzzcloud.entity.member.enums;

import com.puppyrush.buzzcloud.entity.member.enumMember;

public enum enumMemberStandard implements enumMember{
	
	
	PASSWD_CHANGE_DATE_OF_MONTH("3"),		//day
	POSSIBILLTY_FAILD_LOGIN_NUM("5"),
	NAME_LENGTH(4),
	NOT_JOIN_MEMBER(-9999);
	
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
