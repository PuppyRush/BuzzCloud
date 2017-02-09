package com.puppyrush.buzzcloud.entity.message.issue;

import java.sql.Timestamp;

import com.puppyrush.buzzcloud.entity.message.Message;

import com.puppyrush.buzzcloud.entity.message.enums.IssueType;

public final class Issue extends Message{

	private IssueType issueType;
	private int writer;
	private Timestamp registredDate;
	
	private Issue(Builder bld){
		
		super(bld.messageId,bld.contents);
		
		this.writer = bld.writer;	
		registredDate = bld.registredDate;
		
	}

	public static class Builder{
		
		private IssueType issueType;
		private int messageId;	
		private int writer;
		private String contents;
		private Timestamp registredDate;
		
		public Builder(){
			
			messageId = -1;
			this.writer = -1;
			this.contents = "";
			registredDate = new Timestamp(System.currentTimeMillis());
			
		}
		
		public Builder messageId(int id){
			this.messageId = id;
			return this;
		}
		
		public Builder writer(int writer){
			this.writer = writer;
			return this;
		}
		
		public Builder contents(String contents){
			this.contents = contents;
			return this;
		}
		
		public Builder issueType(IssueType type){
			this.issueType = type;
			return this;
		}
		
		public Builder registredDate(Timestamp reg){
			
			this.registredDate = reg;
			return this;
		}
		
		public Issue build(){
			if(messageId==-1 || writer==-1)
				throw new IllegalArgumentException("corrupted builder of issue");
			return new Issue(this);			
		}
		
	}


	public int getWriter() {
		return writer;
	}

	public void setWriter(int writer) {
		this.writer = writer;
	}



	public Timestamp getRegistredDate() {
		return registredDate;
	}

	public void setRegistredDate(Timestamp registredDate) {
		this.registredDate = registredDate;
	}
	
	
}
