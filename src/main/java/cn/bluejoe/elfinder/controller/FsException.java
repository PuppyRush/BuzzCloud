package cn.bluejoe.elfinder.controller;

import java.io.IOException;
import java.util.Map;

import com.puppyrush.buzzcloud.bzexception.BZException;
import com.puppyrush.buzzcloud.bzexception.enumBZExceptionInterface;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public class FsException extends BZException
{

	private FsException(Builder bld){
		super(bld);
	}
	public static class Builder extends BZException.Builder<FsException>{
	
		public Builder(enumPage toPage) {
			super(toPage);
			// TODO Auto-generated constructor stub
		}


		@Override		
		public FsException build(){
			return new FsException(this);
		}
	}

}
