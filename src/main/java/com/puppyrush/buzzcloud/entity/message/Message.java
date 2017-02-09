package com.puppyrush.buzzcloud.entity.message;

import java.sql.Timestamp;

import com.puppyrush.buzzcloud.entity.interfaces.Entity;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.sun.mail.handlers.message_rfc822;

public abstract class Message implements Entity {

	private int messageId;	
	private String contents;
		
	protected Message(int id, String contents){
		this.messageId = id;
		this.contents = contents;
	}
	
	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}



	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}


	
}
