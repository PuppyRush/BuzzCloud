package com.puppyrush.buzzcloud.entity.authority.band;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;

public enum enumBandAuthority implements EnumEntity{
	
	FINAL("최하단 그룹"),
	ROOT("최상단 그룹"),
	CAN_JOIN_MEMBER("가입 허용"),
	CAN_VIEW_UPPER_BAND("상위 그룹 열람 가능"),
	CAN_VIEW_SUB_BAND("하위 그룹 열람 가능"),
	CAN_VIEW_SIBILING_BAND("동등 그룹 열람 가능"),
	CAN_MAKE_SUB_BAND("하위 그룹 생성 가능"),
	CAN_MAKE_SIBLING_BAND("동등 그룹 생성 가능");
	
	private String str;
	
	private enumBandAuthority(String str){
		this.str = str;
	}
	
	@Override
	public String toString(){
		return str;
	}
	
	public String getString(){
		return this.name();
	}

	
	public static Map<enumBandAuthority,Boolean> toEnumMap(List<String> ary){
	
		Map<enumBandAuthority ,Boolean> bandAuthMap = new EnumMap<enumBandAuthority ,Boolean>(enumBandAuthority.class);
		
		for(String str : ary){
			for(enumBandAuthority bandauth : enumBandAuthority.values()){
				if(bandauth.toString().equals(str)){
					bandAuthMap.put(bandauth, true);
					break;
				}
			}	
		}
		return bandAuthMap;
	}
}
