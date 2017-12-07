package com.puppyrush.buzzcloud.entity.message.instanceMessage;

import com.puppyrush.buzzcloud.entity.message.MessageEnum;

public enum enumInstanceMessage implements MessageEnum{

	ERROR("red"),
	SUCCESS("green"),
	WARNING("yello"),
	NOTIFICATION("blue"),
	NONAUTIFICATION("brown");
	
	private String str;
	
	private enumInstanceMessage(String str){
		this.str = str;
	}
	
	@Override
	public String toString(){
		return str;
	}
	
}
