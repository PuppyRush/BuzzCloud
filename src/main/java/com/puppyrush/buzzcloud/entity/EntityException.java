package com.puppyrush.buzzcloud.entity;

import com.puppyrush.buzzcloud.bzexception.BZException;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public final class EntityException extends BZException {
	
	private EntityException(Builder bld){
		super(bld);
	}
	public static class Builder extends BZException.Builder<EntityException>{
		
		public Builder(enumPage toPage) {
			super(toPage);
			// TODO Auto-generated constructor stub
		}


		@Override		
		public EntityException build(){
			return new EntityException(this);
		}
	}

}
