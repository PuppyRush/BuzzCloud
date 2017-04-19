package com.puppyrush.buzzcloud.page.enums;

public enum enumPageError {

	NO_PARAMATER("파라메터가 모두 전달 되지 않았습니다. "),
	UNKNOWN_PARAMATER("파라메터의 이름이 잘못되었습니"),
	UNKNOWN_PARA_VALUE("파라메터로 넘어온 값의 형식이 존재하지않습니다."),
	WRONG_PARAMATER("");
	private String errStr;
	
	enumPageError(String str){
		errStr = str;
		
	}

	public String getString(){
		return errStr;
	}
}
