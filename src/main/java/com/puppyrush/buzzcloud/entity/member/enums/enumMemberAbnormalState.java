package com.puppyrush.buzzcloud.entity.member.enums;

import com.puppyrush.buzzcloud.entity.member.MemberEnum;
import com.puppyrush.buzzcloud.page.enums.enumPage;

public enum enumMemberAbnormalState implements MemberEnum  {


	LOST_PASSWORD("lostPassword"),
	FAILD_LOGIN("failedLogin"),
	SLEEP("sleep"),
	OLD_PASSWORD("oldPassword"),
	JOIN_CERTIFICATION("joinCertification");
		
	private enumPage toPage;
	private String enumStr;
	
	enumMemberAbnormalState(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
	
}
