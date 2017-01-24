package com.puppyrush.buzzcloud.entity.authority.member;

public enum enumMemberAuthority {

	OWNER(1),
	ADMIN(2),
	MEMBER(3),
	VIEWER(4);
	
	private int number;

	private enumMemberAuthority(int num){
		this.number = num;
	}
	
	@Override
	public String toString(){
		return this.name();
	}
	
	public int toInteger(){
		return number;
	}
}
