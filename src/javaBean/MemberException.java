package javaBean;

import property.enums.enumMemberState;

public class MemberException extends Exception {
	
	
	
	private final enumMemberState ERR_CODE;// 생성자를 통해 초기화 한다.
	
	public MemberException(String msg, enumMemberState errcode){ //생성자
		super(msg);
		ERR_CODE=errcode;
	}
	
	public MemberException(enumMemberState errcode){// 생성자
		this(errcode.getString(), errcode);// ERR_CODE를 100(기본값)으로 초기화한다.
	}
	
	public String getErrCode(){// 에러 코드를 얻을 수 있는 메서드도 추가한다.
		return ERR_CODE.getString();// 이 메서드는 주로 getMessage()와 함께 사용될 것이다.
	}
}
