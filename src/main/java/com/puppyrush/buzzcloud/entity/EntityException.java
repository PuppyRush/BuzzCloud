package com.puppyrush.buzzcloud.entity;

import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public class EntityException extends Exception {
	
	
	private enumPage toPage;
	private final EnumEntity ERR_CODE;// 생성자를 통해 초기화 한다.
	
	public EntityException(EnumEntity errcode){
				
		this("내부오류", errcode , enumPage.ERROR404);
	}
	
	public EntityException(String msg, EnumEntity errcode, enumPage page){ //생성자
		super(msg);
		ERR_CODE=errcode;
		toPage = page;
	}
	
	public EntityException(EnumEntity errcode, enumPage page){// 생성자
		this(errcode.toString(), errcode,page);// ERR_CODE를 100(기본값)으로 초기화한다.

	}
	
	public enumPage getToPage(){
		return toPage;
	}
	
	public EnumEntity getErrCode(){
		return ERR_CODE;
	}
}
