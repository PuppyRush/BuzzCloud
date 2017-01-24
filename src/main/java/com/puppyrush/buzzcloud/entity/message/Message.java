package com.puppyrush.buzzcloud.entity.message;

import java.sql.Timestamp;

import com.puppyrush.buzzcloud.entity.interfaces.Entity;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.sun.mail.handlers.message_rfc822;

public class Message implements Entity {

	private int messageId;	
	private int writer;
	private String contents;
	private Timestamp registredDate;
	
	protected Message(Builder bld){
		
		messageId = bld.messageId;
		this.writer = bld.writer;
		this.contents= bld.contents ;
		registredDate = bld.registredDate;
		
	}

	public static class Builder{
		

		private int messageId;	
		private int writer;
		private String contents;
		private Timestamp registredDate;
		
		public Builder(int id, int writer, String contetns){
			
			messageId = id;
			this.writer = writer;
			this.contents = contetns;
			registredDate = new Timestamp(System.currentTimeMillis());
			
		}
		
		public Builder registredDate(Timestamp reg){
			
			this.registredDate = reg;
			return this;
		}
		
		public Message build(){
			return new Message(this);
		}
		
	}
	
	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getWriter() {
		return writer;
	}

	public void setWriter(int writer) {
		this.writer = writer;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Timestamp getRegistredDate() {
		return registredDate;
	}

	public void setRegistredDate(Timestamp registredDate) {
		this.registredDate = registredDate;
	}
	
}
