package page.member;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.EntityException;
import entity.member.Member;
import entity.member.MemberController;
import entity.member.MemberDB;
import entity.member.MemberManager;
import entity.member.enums.enumMemberState;
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
		String email = "";
		String sessionId = request.getRequestedSessionId();
		try{
			
			//필요조건
			if( request.getParameter("email")==null || request.getParameter("password")==null)
				throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404);
	
			email = (String)request.getParameter("email");
			
			if(MemberController.getInstance().containsEntity(sessionId)){
				member = MemberController.getInstance().getMember(sessionId);
				MemberController.getInstance().addMember(member,sessionId);
			}
			else
				member = MemberDB.getInstance().getMember(email);
			

			if(!member.doLoginManager())
				throw new EntityException(enumMemberState.NOT_EQUAL_PASSWORD, enumPage.LOGIN_MANAGER);
			
			//returns.put("view", enumPage.M.toString());
			
		}catch(EntityException e){
			
			returns.put("view", enumPage.LOGIN_MANAGER.toString());
			returns.put("message", e.getErrCode().toString());
			returns.put("messageKind", enumCautionKind.ERROR);
			
		}
		
		return returns;
		
	
	}
}
