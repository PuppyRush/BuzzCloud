package com.puppyrush.buzzcloud.entity.band.enums;

public enum enumBand {

	DEFAULT_CAPACITY(1024*1024*500);

	
	private String str;
	private int enumInt;
	
	enumBand(String str){
		this.str = str;
	}
	
	enumBand(int integer){
		this.enumInt = integer;
	}
	
	@Override
	public String toString(){
		return str;
	}
	
	public int toInteger(){
		return enumInt;
	}
	
}
