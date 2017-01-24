package com.puppyrush.buzzcloud.entity.message.issue.enums;

public enum IssueType {
	
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
