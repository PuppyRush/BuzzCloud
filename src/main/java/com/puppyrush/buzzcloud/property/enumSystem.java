package com.puppyrush.buzzcloud.property;

public enum enumSystem {

	PORT("8100"),
	IP("210.179.101.193"),
	URL_ROOT("http://"+IP.toString()+":"+PORT.toString()),
	RESOURCE_FOLDER_ABS_PATH("/home/cmk/workspace/BuzzCloud/src/main/webapp/resources/"),
	RESOURCE_FOLDER_RAT_PATH("/resources/"),
	DEFAULT_DIRVER_ABS_PATH("/tmp/buzzcloud/"),
	DEFAULT_FOLDER_NAME("upload"),
	MEMBERS_FOLDER_NAME("member"),
	ADMIN("gooddaumi@naver.com"),
	ADMIN_ID(0),
	MAX_CAPACITY(1024), //MB
	
	
	
	MAX_MEMBER_IMAGE_WIDTH(120),
	MAX_MEMBER_IMAGE_HEIGHT(120),
	
	INTERNAL_ERROR("내부오류. 관리자에게 문의하세요");
	
	private String str;
	private int number;
	
	enumSystem(int i){
		number = i;
	}
	
	enumSystem(String str){
		this.str = str;
	}
	
	@Override
	public String toString(){
		return str;
	}
	
	public int toInt(){
		return number;
	}
	
	
}
