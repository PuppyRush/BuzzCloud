package com.puppyrush.buzzcloud.dbAccess;

import com.puppyrush.buzzcloud.bzexception.enumBZExceptionInterface;
import com.puppyrush.buzzcloud.entity.enumEntityState;
import com.puppyrush.buzzcloud.entity.member.enumMember;

public enum enumDBError implements enumBZExceptionInterface
{
	NOT_CORRESPOND_WHERE("조건절에 부합하는 값이 없습니다");

	private int value;
	private String enumStr;
	 
	enumDBError(String str){
		enumStr = str;
	}
	
	enumDBError(int i){
		value = i;
	}
	
	public int toInt(){
		return value;
	}
	
	public String toString(){
		return enumStr;
	}
}