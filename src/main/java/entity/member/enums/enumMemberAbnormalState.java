package entity.member.enums;

import page.enums.enumPage;

public enum enumMemberAbnormalState {


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
