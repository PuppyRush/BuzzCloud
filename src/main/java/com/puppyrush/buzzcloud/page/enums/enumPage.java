package com.puppyrush.buzzcloud.page.enums;

import com.puppyrush.buzzcloud.property.enums.enumSystem;

public enum enumPage {

	ROOT("http://114.129.212.30:"+enumSystem.PORT.toString()),
	
	ERROR404("/error/404error"),
	ERROR403("/error/403error"),
	
	ENTRY("entryPage/EntryPage"),
	ENTRYTOMAIN("entryPage/EntryToMain"),
	MAIN("mainPage/MainPage"),
	
	CERTIFICATE("verifyRegistration.do"),
	LOGIN("member/Login"),
	JOIN("member/Join"),
	LOST_PASSWORD("member/Lostpassword"),
	RESET_PASSWORD("member/ResetPassword"),
	CERTIFICATE_FAILED_PASSWORD("member/CertificatePassword"),
	INPUT_CERTIFICATION_NUMBER("member/CheckAuthNum"),
	INPUT_MAIL("member/InputMail"),
	REGSTRY_DEVELOPER("member/RegistryDeveloper"),
	CHANGE_OLD_PWD("member/ChangeOldPasswodjsp"),
	
	BROWSER("fileBrowser/FileBrowser"),
	ISSUE("issue/IssuePage"),
	
	MY_ACCOUNT("manager/MyAccount"),
	GROUP_DASHBOARD("manager/GroupDashBoard"),
	GROUP_MANAGER("manager/Group"),
	GROUP_MEMBER("manager/Member"),
	
	SETTINGS("/Settings"),
	
	MAIL("/buzzCloudMail.do"),
	
	LOGIN_MANAGER("/AdminPage/AdminLogin");
		
	
	private String pageDirection;
	
	enumPage(String str){
		pageDirection = str;
	}

	
	@Override
	public String toString(){
		return pageDirection;
	}
}
