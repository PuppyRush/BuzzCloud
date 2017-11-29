package com.puppyrush.buzzcloud.entity.message.instanceMessage;

import com.puppyrush.buzzcloud.entity.message.MessageEnum;

public enum InstanceMessageType implements MessageEnum{

	ERROR("red"),
	SUCCESS("green"),
	WARNING("yello"),
	NOTIFICATION("blue"),
	NONAUTIFICATION("brown");
	
	private String str;
	
	private InstanceMessageType(String str){
		this.str = str;
	}
	
	@Override
	public String toString(){
		return str;
	}
	
}
