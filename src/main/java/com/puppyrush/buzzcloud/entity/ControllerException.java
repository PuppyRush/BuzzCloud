package com.puppyrush.buzzcloud.entity;

import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public class ControllerException extends Exception {
	
	public final static enumPage toPage = enumPage.ERROR404;
	
	private final enumController ERR_CODE;// 생성자를 통해 초기화 한다.
	
	public ControllerException(String msg, enumController errcode){ //생성자
		super(msg);
		ERR_CODE=errcode;
		
	}
	
	public ControllerException(enumController errcode){// 생성자
		this(errcode.toString(), errcode);// ERR_CODE를 100(기본값)으로 초기화한다.

	}
	
	public enumPage getToPage(){
		return toPage;
	}
	
	public enumController getErrCode(){
		return ERR_CODE;
	}
}
