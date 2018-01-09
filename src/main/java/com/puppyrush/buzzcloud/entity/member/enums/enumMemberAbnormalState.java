package com.puppyrush.buzzcloud.entity.member.enums;

import com.puppyrush.buzzcloud.entity.member.enumMember;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public enum enumMemberAbnormalState implements enumMember  {


	LOST_PASSWORD("lostPassword"),
	EXCEEDED_LOGIN_COUNT("exceededLoginCount"),
	SLEEP("sleep"),
	OLD_PASSWORD("oldPassword"),
	JOIN_CERTIFICATION("joinCertification"),
	IS_ABNORMAL("isAbnormal");
		
	private enumPage toPage;
	private String enumStr;
	
	enumMemberAbnormalState(String str){
		enumStr = str;
	}
	
	public String toString(){
		return enumStr;
	}
	
}
