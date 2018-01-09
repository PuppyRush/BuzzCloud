package com.puppyrush.buzzcloud.entity.authority.file;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;

public enum enumFileAuthority implements EnumEntity {
	
	CREATES("파일/폴더 생성 가능"),
	REMOVE("파일/폴더 삭제 가능"),
	DOWNLOAD("다운로드 가능"),
	UPLOAD("업로드 가능");
	
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

	public static Map<enumFileAuthority,Boolean> toEnumMap(List<String> ary){
		
		Map<enumFileAuthority ,Boolean> bandAuthMap = new EnumMap<enumFileAuthority ,Boolean>(enumFileAuthority.class);
		
		for(String str : ary){
			for(enumFileAuthority bandauth : enumFileAuthority.values()){
				if(bandauth.toString().equals(str)){
					bandAuthMap.put(bandauth, true);
					break;
				}
			}	
		}
		return bandAuthMap;
	}
}
