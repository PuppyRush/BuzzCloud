package com.puppyrush.buzzcloud.entity.member.enums;

import com.puppyrush.buzzcloud.entity.member.enumMember;

public enum enumMemberType implements enumMember{
	
	NOTHING("NOTHING"),
	NAVER("NAVER"),
	GOOGLE("GOOGLE");
			
	private String enumStr;
	
	enumMemberType(String str){
		enumStr = str;
	}
	
	public String toString(){
		return enumStr;
	}
	
}

