package entity.message.issue;

import entity.message.Message;
import entity.message.issue.enums.IssueType;

public final class Issue extends Message{

	private IssueType issueType;
	
	public Issue(Message.Builder messageBld, IssueType type){
		
		super(messageBld);
		issueType = type;
		
	}
	
}
