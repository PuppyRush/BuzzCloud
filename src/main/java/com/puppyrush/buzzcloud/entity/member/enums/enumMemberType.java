package com.puppyrush.buzzcloud.entity.member.enums;

import com.puppyrush.buzzcloud.entity.member.MemberEnum;

public enum enumMemberType implements MemberEnum{
	
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

