package com.puppyrush.buzzcloud.entity.message.enums;

public enum messageType {

	ERROR("red"),
	NORMAL("green"),
	WARNING("yello"),
	NOTIFICATION("blue");
	
	private String str;
	
	private messageType(String str){
		this.str = str;
	}
	
	@Override
	public String toString(){
		return str;
	}
}
