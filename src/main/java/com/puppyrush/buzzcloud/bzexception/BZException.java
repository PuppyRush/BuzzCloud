package com.puppyrush.buzzcloud.bzexception;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.puppyrush.buzzcloud.entity.authority.member.enumMemberAuthority;
import com.puppyrush.buzzcloud.entity.interfaces.Entity;
import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.InstanceMessage;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public abstract class BZException extends Exception{
	
	private String errString;
	private enumPage toPage;
	private enumInstanceMessage instanceMessage;
	private Map<enumBZExceptionInterface,Boolean> errCodeMap;
	private Map<String, Object> stringMap;

	public static abstract class Builder <T extends BZException>{
		
		protected String errString;
		protected enumPage toPage;
		protected Map<enumBZExceptionInterface,Boolean> errCodeMap;
		protected enumInstanceMessage instanceMessage;
		protected Map<String, Object> stringMap;
		
		public Builder(enumPage toPage){
			this.toPage = toPage;
			errString = "System Error";
			errCodeMap =  new HashMap<enumBZExceptionInterface,Boolean>();
			stringMap = new HashMap<String, Object>();
		}
		
		public Builder<T> errorString(String errStr){
			this.errString = errStr;
			return this;
		}
		
		public Builder<T> errorCode(enumBZExceptionInterface... vars){
			for(enumBZExceptionInterface enums : vars){
				errCodeMap.put(enums, true);
			}
			return this;
		}
		
		public Builder<T> instanceMessage(enumInstanceMessage msg){
			this.instanceMessage = msg;
			return this;
		}
		
		public Builder<T> putString(String key, Object value){
			this.stringMap.put(key, value);
			return this;
		}
		
		public Builder<T> putString(Map<String, Object> stringMap){
			this.stringMap.putAll(stringMap);
			return this;
		}
		
		
		public abstract T build();
		
	}

	protected BZException(String errString, Map<enumBZExceptionInterface,Boolean> errors, enumPage toPage){
		this.errString = errString;
		this.errCodeMap = errors;
		this.toPage = toPage;
		instanceMessage = enumInstanceMessage.WARNING;
		stringMap = new HashMap<String, Object>();
	}

	final public String getErrorString(){
		return errString;
	}
	
	final public enumPage getToPage(){
		return toPage;
	}
	
	final public Map<enumBZExceptionInterface,Boolean> getErrCode(){
		return errCodeMap;
	}
	
	final public Map<String, Object> getInstanceMessage(){
		
		return new InstanceMessage( errString, instanceMessage).getMessage();
	}
	
	final public Map<String, Object> getReturns(){
		Map<String, Object> returns = new HashMap<String, Object>();
		returns.putAll(stringMap);
		returns.putAll(new InstanceMessage( errString, instanceMessage).getMessage());
		returns.put("view", toPage.toString());
		return returns;
	}
	
	final public Map<String, Object> getReturnsForAjax(){
		Map<String, Object> returns = new HashMap<String, Object>();
		returns.putAll(stringMap);
		returns.putAll(new InstanceMessage( errString, instanceMessage).getMessage());
		return returns;
	}
}
