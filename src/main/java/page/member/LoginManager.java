package page.member;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.MemberManager;
import member.Member;
import member.MemberException;
import member.enums.enumMemberState;
import page.PageException;
import page.enums.enumCautionKind;
import page.enums.enumPage;
import page.enums.enumPageError;
import property.commandAction;


public class LoginManager implements commandAction{
	
	
	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		
		HashMap<String , Object> returns = new HashMap<String , Object>();
		Member member = null;
		String nick_or_mail = null;
		String userType="";
		String _sId;
		try{
			
			//필요조건
			if( request.getParameter("email")==null || request.getParameter("password")==null)
				throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404);
	
			_sId = (String)request.getParameter("sessionId");
			member = MemberManager.getMember(_sId);
			String id = (String)request.getParameter("login_username");

			if(!member.doLoginManager())
				throw new MemberException(enumMemberState.NOT_EQUAL_PASSWORD, enumPage.LOGIN_MANAGER);
			
			returns.put("view", enumPage.MANAGE_MEMBER.toString());
			
		}catch(MemberException e){
			
			returns.put("view", enumPage.LOGIN_MANAGER.toString());
			returns.put("message", e.getErrCode().getString());
			returns.put("messageKind", enumCautionKind.ERROR);
			
		}
		
		return returns;
		
	
	}
}
