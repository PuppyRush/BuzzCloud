package message.issue;

import message.Message;
import message.issue.enums.IssueType;

public class Issue extends Message{

	private IssueType issueType;
	
	public Issue(Message.Builder messageBld, IssueType type){
		
		super(messageBld);
		issueType = type;
		
	}
	
}
