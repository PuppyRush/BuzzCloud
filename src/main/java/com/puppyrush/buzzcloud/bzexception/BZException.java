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
	
	private Throwable e;
	private String errorMessage;
	private enumPage toPage;
	private enumInstanceMessage instanceMessageType;
	private String instanceMessage;
	private Map<enumBZExceptionInterface,Boolean> errCodeMap;
	private Map<String, Object> stringMap;

	public static abstract class Builder <T extends BZException>{
		
		protected Throwable e;
		protected String errorMessage;
		protected enumPage toPage;
		protected Map<enumBZExceptionInterface,Boolean> errCodeMap;
		protected enumInstanceMessage instanceMessageType;
		protected String instanceMessage;
		protected Map<String, Object> stringMap;
		
		public Builder(enumPage toPage){
			e = new Exception();
			this.toPage = toPage;
			this.errorMessage = "시스템 에러입니다.";
			instanceMessage = "시스템 에러입니다. 관리자에게 문의하세요.";
			errCodeMap =  new HashMap<enumBZExceptionInterface,Boolean>();
			stringMap = new HashMap<String, Object>();
		}
		
		public Builder<T> throwable(Throwable e){
			this.e = e;
			return this;
		}
		
		public Builder<T> errorMessage(String msg){
			this.errorMessage = msg;
			return this;
		}
		
		public Builder<T> errorCode(enumBZExceptionInterface error){
			errCodeMap.put(error, true);
			return this;
		}
		
		public Builder<T> errorCode(enumBZExceptionInterface... vars){
			for(enumBZExceptionInterface enums : vars){
				errCodeMap.put(enums, true);
			}
			return this;
		}
		
		public Builder<T> instanceMessage(String errStr){
			this.instanceMessage = errStr;
			return this;
		}
		
		public Builder<T> instanceMessageType(enumInstanceMessage msgType){
			this.instanceMessageType = msgType;
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

	protected BZException(Builder bld){
		this.e = bld.e;
		this.instanceMessage = bld.instanceMessage;
		this.errorMessage = bld.errorMessage;
		this.errCodeMap = bld.errCodeMap;
		this.toPage = bld.toPage;
		this.instanceMessageType = bld.instanceMessageType;
		this.instanceMessage = bld.instanceMessage;
		this.stringMap = bld.stringMap;		
	}

	final public String getErrorString(){
		return instanceMessage;
	}
	
	final public enumPage getToPage(){
		return toPage;
	}
	
	final public Map<enumBZExceptionInterface,Boolean> getErrCode(){
		return errCodeMap;
	}
	
	final public Map<String, Object> getInstanceMessage(){
		
		return new InstanceMessage( instanceMessage, instanceMessageType).getMessage();
	}
	
	final public Map<String, Object> getReturns(){
		Map<String, Object> returns = new HashMap<String, Object>();
		returns.putAll(stringMap);
		returns.putAll(new InstanceMessage( instanceMessage, instanceMessageType).getMessage());
		returns.put("view", toPage.toString());
		return returns;
	}
	
	final public Map<String, Object> getReturnsForAjax(){
		Map<String, Object> returns = new HashMap<String, Object>();
		returns.putAll(stringMap);
		returns.putAll(new InstanceMessage( instanceMessage, instanceMessageType).getMessage());
		return returns;
	}
}
