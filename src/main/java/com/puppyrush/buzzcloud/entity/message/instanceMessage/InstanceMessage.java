package com.puppyrush.buzzcloud.entity.message.instanceMessage;

import java.util.HashMap;
import java.util.Map;

import com.puppyrush.buzzcloud.entity.message.Message;
import com.puppyrush.buzzcloud.entity.message.enums.IssueType;
import com.puppyrush.buzzcloud.entity.message.enums.InstanceMessageType;

public final class InstanceMessage extends Message{

	private InstanceMessageType type;

	public InstanceMessage(String contents, InstanceMessageType type){
		super(-1,contents);
		this.type = type;
	}
	
	
	public InstanceMessageType getType() {
		return type;
	}

	public void setType(InstanceMessageType type) {
		this.type = type;
	}
	
	public Map<String,Object> getMessage(){
		
		Map<String, Object> returns = new HashMap<String, Object>();
		returns.put("contents", this.getContents());
		returns.put("messageType", type.toString());
		
		return returns;
		
	}

	
}
