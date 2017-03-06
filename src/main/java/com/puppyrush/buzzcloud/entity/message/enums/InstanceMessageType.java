package com.puppyrush.buzzcloud.entity.message.enums;

public enum InstanceMessageType {

	ERROR("red"),
	SUCCESS("green"),
	WARNING("yello"),
	NOTIFICATION("blue");
	
	private String str;
	
	private InstanceMessageType(String str){
		this.str = str;
	}
	
	@Override
	public String toString(){
		return str;
	}
	
}
