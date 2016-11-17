package page.enums;

import property.enums.enumSystem;

public enum enumPage {

	ROOT("http://114.129.212.30:"+enumSystem.PORT.toString()),
	
	ERROR404("/error/404error.jsp"),
	ERROR403("/error/403error.jsp"),
	
	ENTRY("/page/entryPage/EntryPage.jsp"),
	ENTRYTOMAIN("/page/entryPage/EntryToMain.jsp"),
	MAIN("/page/mainPage/MainPage.jsp"),
	
	CERTIFICATE("verifyRegistration.do"),
	LOGIN("/page/member/Login.jsp"),	//  /Member 폴더에서 시작.
	JOIN("/page/member/Join.jsp"),
	LOST_PASSWORD("/page/member/Lostpassword.jsp"),
	RESET_PASSWORD("/page/member/ResetPassword.jsp"),
	CERTIFICATE_FAILED_PASSWORD("/page/member/CertificatePassword.jsp"),
	INPUT_CERTIFICATION_NUMBER("/page/member/CheckAuthNum.jsp"),
	INPUT_MAIL("/page/member/InputMail.jsp"),
	REGSTRY_DEVELOPER("/page/member/RegistryDeveloper.jsp"),
	CHANGE_OLD_PWD("/page/member/ChangeOldPasswodjsp"),
	
	BROWSER("/page/fileBrowser/FileBrowser.jsp"),
	ISSUE("/page/issue/IssuePage.jsp"),
	
	MY_ACCOUNT("/page/manager/MyAccount.jsp"),
	GROUP_DASHBOARD("/page/manager/GroupDashboard.jsp"),
	GROUP_MANAGER("/page/manager/Group.jsp"),
	MEMBER_MANAGER("/page/manager/Member.jsp"),
	
	SETTINGS("/Settings.jsp"),
	
	MAIL("/buzzCloudMail.do"),
	
	LOGIN_MANAGER("/AdminPage/AdminLogin.jsp");
		
	
	private String pageDirection;
	
	enumPage(String str){
		pageDirection = str;
	}

	
	@Override
	public String toString(){
		return pageDirection;
	}
}
