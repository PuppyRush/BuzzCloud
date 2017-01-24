package com.puppyrush.buzzcloud.page.member;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumCautionKind;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.commandAction;


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
