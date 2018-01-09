package com.puppyrush.buzzcloud.entity.band.enums;

import com.puppyrush.buzzcloud.entity.enumEntityState;
import com.puppyrush.buzzcloud.entity.member.enumMember;

public enum enumBandStandard implements enumMember
{
	MAX_CAPACITY(1024), //MB
	NAME_MIN_LENGTH(5),
	NAME_MAX_LENGTH(15),
	MAX_MEMBER_SIZE(100);

	private int value;
	private String enumStr;
	 
	enumBandStandard(String str){
		enumStr = str;
	}
	
	enumBandStandard(int i){
		value = i;
	}
	
	public int toInt(){
		return value;
	}
	
	public String toString(){
		return enumStr;
	}
}