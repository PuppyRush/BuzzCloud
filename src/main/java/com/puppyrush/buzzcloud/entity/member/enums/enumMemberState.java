package com.puppyrush.buzzcloud.entity.member.enums;

import com.puppyrush.buzzcloud.bzexception.enumBZExceptionInterface;
import com.puppyrush.buzzcloud.entity.enumEntityState;
import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;
import com.puppyrush.buzzcloud.entity.member.enumMember;

public enum enumMemberState implements enumMember, enumEntityState
{
	LOGOUT("로그아웃했습니다."),
	NOT_LOGIN("로그인후 사용 가능합니다."),
	ALREADY_LOGIN("이미 로그인중 입니다."),
	NOT_JOIN("가입후 사용 가능합니다."),
	ALREADY_JOIN("이미 가입한 유저입니다"),
	NOT_LOGOUT("로그아웃되어있지 않습니다."),
	NOT_DEVELOPER("개발자로 등록이 되어 있지 않습니다.  등록 후 사용하세요"),
	NOT_ADMIN("관리자가 아닙니다."),
	ALREADY_CERTIFICATION("이미 인증이 되어있습니다"),
	NOT_JOIN_CERTIFICATION("가입후 메일인증을 하지 않았습니다. 메일 인증 후 로그인 하세요."),
	NOT_EQUAL_PASSWORD("비밀번호가 일치하지 않습니다."),
	LOST_PASSWORD("비밀번호를 분실하신 상태입니다."),
	EXCEEDED_LOGIN_COUNT("비밀번호를 "+enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString()+"회 이상 초과 하셨습니다."),
	PASSING_CHANGE_PWD("비밀번호를 변경하지 않은지 "+enumMemberStandard.PASSWD_CHANGE_DATE_OF_MONTH+"되셨습니다."),
	SLEEP("계정이 휴면상태입니다."),
	NOT_EXIST_MEMBER("존재하지 않는 사용자 이거나  중복됩니다."),
	ERROR("unknow error"),
	
	RESOLVE_JOIN(0);

	private int value;
	private String enumStr;
	 
	enumMemberState(String str){
		enumStr = str;
	}
	
	enumMemberState(int i){
		value = i;
	}
	
	public int toInt(){
		return value;
	}
	
	public String toString(){
		return enumStr;
	}
}