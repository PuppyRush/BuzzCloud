package com.puppyrush.buzzcloud.entity;

import java.util.Map;

import com.puppyrush.buzzcloud.bzexception.BZException;
import com.puppyrush.buzzcloud.bzexception.enumBZExceptionInterface;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.page.enums.enumPage;

import cn.bluejoe.elfinder.controller.FsException.Builder;

public class ControllerException extends BZException {
	

	private ControllerException(Builder bld){
		super(bld);
	}
	public static class Builder extends BZException.Builder<ControllerException>{
		
		public Builder(enumPage toPage) {
			super(toPage);
			// TODO Auto-generated constructor stub
		}


		@Override		
		public ControllerException build(){
			return new ControllerException(this);
		}
	}

}
