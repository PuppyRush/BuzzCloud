package com.puppyrush.buzzcloud.page;

import java.util.Map;

import com.puppyrush.buzzcloud.bzexception.BZException;
import com.puppyrush.buzzcloud.bzexception.enumBZExceptionInterface;
import com.puppyrush.buzzcloud.mail.MailException;
import com.puppyrush.buzzcloud.mail.MailException.Builder;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;

public class PageException extends BZException {

	private PageException(Builder bld){
		super(bld);
	}
	public static class Builder extends BZException.Builder<PageException>{
		
		public Builder(enumPage toPage) {
			super(toPage);
			// TODO Auto-generated constructor stub
		}


		@Override		
		public PageException build(){
			return new PageException(this);
		}
	}
}
