package page.member;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ControllerException;
import entity.EntityException;
import entity.enumController;
import entity.enumEntityState;
import entity.member.Member;
import entity.member.MemberController;
import entity.member.MemberManager;
import entity.member.enums.enumMemberState;

import java.util.HashMap;

import page.PageException;
import page.enums.enumCautionKind;
import page.enums.enumPage;
import page.enums.enumPageError;
import property.commandAction;


/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
 *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.
 *  
 *       해당 클래스의 기능순서도는  114.129.211.123/boards/2/topics/64 참고
*/
public class AlreadyLogin implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			{
		
		HashMap<String , Object> returns = new HashMap<String , Object>();	
		returns.put("view", enumPage.ENTRYTOMAIN.toString());		
		
		return returns;
	}
	
}