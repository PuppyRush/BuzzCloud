package page.enums;

import property.enums.enumSystem;

public enum enumPage {

	ROOT("http://114.129.211.123:"+enumSystem.PORT.toString()+"/"),
	
	ERROR404("/error/404error.jsp"),
	ERROR403("/error/403error.jsp"),
	
	ENTRY("/page/entryPage/entryPage.jsp"),
	CERTIFICATE("verifyRegistration.do"),
		LOGIN("/Member/Login.jsp"),	//  /Member 폴더에서 시작.
		JOIN("/Member/Join.jsp"),
		RESET_PASSWORD("/Member/ResetPassword.jsp"),
		CERTIFICATE_FAILED_PASSWORD("/Member/CertificatePassword.jsp"),
		INPUT_CERTIFICATION_NUMBER("/Member/CheckAuthNum.jsp"),
		INPUT_MAIL("/Member/InputMail.jsp"),
		REGSTRY_DEVELOPER("/Member/RegistryDeveloper.jsp"),
		CHANGE_OLD_PWD("/Member/ChangeOldPasswodjsp"),
		
		
	SETTINGS("/Settings.jsp"),
	
	MAIL("/buzzCloudMail.do"),
	
	LOGIN_MANAGER("/AdminPage/AdminLogin.jsp"),
		MANAGE_MEMBER("/AdminPage/User.jsp"),
		MANAGE_SERVER("AdminPage/Server.jsp");
	
	private String pageDirection;
	
	enumPage(String str){
		pageDirection = str;
	}

	
	@Override
	public String toString(){
		return pageDirection;
	}
}
