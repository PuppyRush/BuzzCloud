package com.puppyrush.buzzcloud.entity.member;

import org.springframework.stereotype.Repository;

@Repository("anonymousMember")
public final class AnonymousMember implements AbsMember {

	private String sessionId;
	
	private AnonymousMember(){
		
	}
	
	public AnonymousMember(String sessionId){
		this.sessionId = sessionId;
	}
	
	public String Get(){
		return sessionId;
	}
}
