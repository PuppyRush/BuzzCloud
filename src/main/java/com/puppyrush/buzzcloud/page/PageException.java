package com.puppyrush.buzzcloud.page;

import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;

public class PageException extends Exception {

	enumPageError err;
	enumPage page;

	public PageException(enumPageError err){
		super(err.getString());
		this.err = err;
		page = enumPage.ERROR404;
	}
	
	public PageException(enumPageError err, enumPage page){
		super(err.getString());
		this.err = err;
		this.page = page;
	}
	
	public PageException(String errMsg, enumPageError err, enumPage page){
		super(errMsg);
		this.err = err;
		this.page = page;
	}
	
	public enumPage getPage(){
		return page;
	}
	
	public String toString(){
		return err.getString();
	}
}
