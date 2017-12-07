package com.puppyrush.buzzcloud.entity.message.instanceMessage;

import java.util.HashMap;
import java.util.Map;

import com.puppyrush.buzzcloud.entity.message.Message;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;

public final class InstanceMessage extends Message{

	private enumInstanceMessage type;

	public InstanceMessage(String contents, enumInstanceMessage type){
		super(-1,contents);
		this.type = type;
	}
	
	
	public enumInstanceMessage getType() {
		return type;
	}

	public void setType(enumInstanceMessage type) {
		this.type = type;
	}
	
	public Map<String,Object> getMessage(){
		
		Map<String, Object> returns = new HashMap<String, Object>();
		returns.put("message", this.getContents());
		returns.put("messageKind", type.toString());
		
		return returns;
		
	}

	
}
