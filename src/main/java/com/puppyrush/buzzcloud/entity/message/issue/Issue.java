package com.puppyrush.buzzcloud.entity.message.issue;

import com.puppyrush.buzzcloud.entity.message.Message;
import com.puppyrush.buzzcloud.entity.message.issue.enums.IssueType;

public final class Issue extends Message{

	private IssueType issueType;
	
	public Issue(Message.Builder messageBld, IssueType type){
		
		super(messageBld);
		issueType = type;
		
	}
	
}
