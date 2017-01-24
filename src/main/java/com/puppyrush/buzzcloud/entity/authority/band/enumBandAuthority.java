package com.puppyrush.buzzcloud.entity.authority.band;

import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;

public enum enumBandAuthority implements EnumEntity{
	
	FINAL("isFinal"),
	ROOT("isRoot"),
	CAN_JOIN_MEMBER("canJoinMember"),
	CAN_VIEW_UPPER_BAND("canViewUpperBand"),
	CAN_VIEW_SUB_BAND("canViewSubBand"),
	CAN_VIEW_SIBILING_BAND("canViewSiblingBand"),
	CAN_MAKE_SUB_BAND("canMakeSubBand"),
	CAN_MAKE_SIBLING_BAND("canMakeSiblingBand");
	
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
	
}
