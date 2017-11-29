package com.puppyrush.buzzcloud.entity.message.issue;

import com.puppyrush.buzzcloud.entity.message.MessageEnum;

public enum IssueType implements MessageEnum {
	
	NOTIFICATION("NOTIFICATION"),
	GROUP_ISSUE("GROUP_ISSUE");
	
	private String str;
	
	private IssueType(String str){
		this.str = str;
	}
	
	@Override
	public String toString(){
		return str;
	}
}
