package com.puppyrush.buzzcloud.entity.band.enums;

import com.puppyrush.buzzcloud.entity.enumEntityState;
import com.puppyrush.buzzcloud.entity.member.enumMember;

public enum enumBandState implements enumMember, enumEntityState
{
	NOT_EXIST_BAND("그룹을 찾지 못하였습니다.");

	private int value;
	private String enumStr;
	 
	enumBandState(String str){
		enumStr = str;
	}
	
	enumBandState(int i){
		value = i;
	}
	
	public int toInt(){
		return value;
	}
	
	public String toString(){
		return enumStr;
	}
}