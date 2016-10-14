package member.enums;

public enum enumMemberState
{
	NOT_SAME_MEMBER("equals함수 결과 두 멤버가 일치하지 않습니다."),
	NOT_EXIST_IN_DB("db에 해당하는 유저가 존재하지 않습니다."),
	NOT_EXIST_MEMBER_FROM_MAP("내부오류. MAP에 멤버변수가 존재하지 않음."),
	NOT_LOGIN("로그인후 사용 가능합니다."),
	LOGIN("이미 로그인중 입니다."),
	NOT_JOIN("가입후 사용 가능합니다."),
	JOIN("이미 가입한 유저입니다"),
	NOT_LOGOUT("로그아웃되어있지 않습니다."),
	NOT_DEVELOPER("개발자로 등록이 되어 있지 않습니다.  등록 후 사용하세요"),
	NOT_ADMIN("관리자가 아닙니다."),
	NOT_JOIN_CERTIFICATION("가입후 메일인증을 하지 않았습니다. 메일 인증 후 로그인 하세요."),
	NOT_EQUAL_PASSWORD("비밀번호가 일치하지 않습니다."),
	LOST_PASSWORD("비밀번호를 분실하신 상태입니다."),
	EXCEED_FAILD_LOGIN("비밀번호를 "+enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString()+"회 이상 초과 하셨습니다."),
	PASSING_CHANGE_PWD("비밀번호를 변경하지 않은지 "+enumMemberStandard.PASSWD_CHANGE_STADNDATE_DATE+"되셨습니다."),
	ALREADY_REG_DEVELOPER(""),
	ALREADY_LOGIN_FROM_OTHER_DEVICE(""),
	ERROR("unknow error");
	
	private String enumStr;
	 
	enumMemberState(String str){
		enumStr = str;
	}
	
	public String getString(){
		return enumStr;
	}
}