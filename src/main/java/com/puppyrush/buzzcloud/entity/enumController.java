package com.puppyrush.buzzcloud.entity;

import com.puppyrush.buzzcloud.entity.interfaces.EntityController;

public enum enumController {

	NOT_EXIST_MEMBER_FROM_MAP("맵에 객체가 존재하지 않습니다."),
	ALREAY_EXIST_MEMBER_FROM_MAP("맵에 객체가 이미 존재합니다.");
	
	String msg;
	
	private enumController(String str){
		msg = str;
	}
	
	@Override
	public String toString(){
		return msg;
	}
	
	
	
}
