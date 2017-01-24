package com.puppyrush.buzzcloud.entity.authority.file;

import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;

public enum enumFileAuthority implements EnumEntity {
	
	CREATE("canCreate"),
	REMOVE("canRemove"),
	DOWNLOAD("canDownload"),
	UPLOAD("canUpload");
	
	private String str;
	
	private enumFileAuthority(String str){
		this.str = str;
	}
	
	@Override
	public String toString(){
		return str;
	}
	
	public String getString(){
		return this.name();
	}
}
