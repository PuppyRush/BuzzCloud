package com.puppyrush.buzzcloud.dbAccess;

import com.puppyrush.buzzcloud.bzexception.BZException;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public final class DBException extends BZException {
	
	private DBException(Builder bld){
		super(bld);
	}
	public static class Builder extends BZException.Builder<DBException>{
		
		public Builder(enumPage toPage) {
			super(toPage);
			// TODO Auto-generated constructor stub
		}


		@Override		
		public DBException build(){
			return new DBException(this);
		}
	}

}
