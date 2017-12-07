package com.puppyrush.buzzcloud.entity;

import java.util.Map;

import com.puppyrush.buzzcloud.bzexception.BZException;
import com.puppyrush.buzzcloud.bzexception.enumBZExceptionInterface;
import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;
import com.puppyrush.buzzcloud.entity.member.enumMember;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public final class EntityException extends BZException {
	

	private EntityException(String errString, Map<enumBZExceptionInterface,Boolean> errors, enumPage toPage){
		super(errString,errors,toPage);
	}
	public static class Builder extends BZException.Builder<EntityException>{
		
		public Builder(enumPage toPage) {
			super(toPage);
			// TODO Auto-generated constructor stub
		}


		@Override		
		public EntityException build(){
			return new EntityException(errString,errCodeMap,toPage);
		}
	}

}
