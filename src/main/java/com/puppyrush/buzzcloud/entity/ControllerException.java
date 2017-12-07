package com.puppyrush.buzzcloud.entity;

import java.util.Map;

import com.puppyrush.buzzcloud.bzexception.BZException;
import com.puppyrush.buzzcloud.bzexception.enumBZExceptionInterface;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public class ControllerException extends BZException {
	

	private ControllerException(String errString, Map<enumBZExceptionInterface,Boolean> errors, enumPage toPage){
		super(errString,errors,toPage);
	}
	public static class Builder extends BZException.Builder<ControllerException>{
		
		public Builder(enumPage toPage) {
			super(toPage);
			// TODO Auto-generated constructor stub
		}


		@Override		
		public ControllerException build(){
			return new ControllerException(errString,errCodeMap,toPage);
		}
	}

}
