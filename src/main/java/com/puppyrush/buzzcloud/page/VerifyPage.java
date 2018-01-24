package com.puppyrush.buzzcloud.page;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.enumSystem;

@Service("verifyPage")
public class VerifyPage {

	@Autowired(required = false)
	private MemberController mCtl;
	
	/**
	 * 페이지 마다 요청되는 유저의 권한(가입, 로그인, 개발자등록 여부등의 상태)을 점검한다.
	 * 
	 * @param sId  브라우져의 sessionId
	 * @param from 어느 페이지에서 왔는지를 명시하는 변수
	 * @return	   인증 성공여부 실패 할 경우 가야할 페이지, 오류메시지를 반환한다.
	 * 	(HashMap 형태로 isSuccessVerify, to, messageKind, message를 반환한다)
	 * @throws EntityException 
	 * @throws ControllerException 
	 * @throws PageException 
	 */
	public Map<String,Object> execute(String sId, String fromPage) throws EntityException, ControllerException, PageException{
			
		Map<String , Object> returns = new HashMap<String , Object>();
		enumPage enumpage;
		
		if(enumPage.valueOf(fromPage) == null)
			throw (new PageException.Builder(enumPage.ENTRY))
			.instanceMessage("페이지 인증 오류입니다. 관리자에게 문의하세요.")
			.instanceMessageType(enumInstanceMessage.ERROR)
			.errorCode(enumPageError.UNKNOWN_PARA_VALUE).build(); 	
		else
			enumpage = enumPage.valueOf(fromPage);
		
		if(!mCtl.containsEntity(sId))
			throw (new ControllerException.Builder(enumPage.LOGIN))
			.instanceMessage("로그인 후 사용해주세요.")
			.instanceMessageType(enumInstanceMessage.ERROR)
			.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build(); 		
		
		Member member = mCtl.getMember(sId);
		
		switch(enumpage){
				
			case LOGIN_MANAGER:
				if(member.getEmail().equals(enumSystem.ADMIN.toString()))
					throw (new EntityException.Builder(enumPage.LOGIN_MANAGER))
					.instanceMessage("로그인 후 사용하세요")
					.instanceMessageType(enumInstanceMessage.ERROR)
					.errorCode(enumMemberState.NOT_ADMIN).build(); 	
				break;
				
			case BROWSER:
			case MAIN:
			case MY_ACCOUNT:
			case GROUP_DASHBOARD:
			case GROUP_MEMBER:
			case GROUP_MANAGER:
				if(!member.isJoin())
					if(member.getEmail().equals(enumSystem.ADMIN.toString()))
						throw (new EntityException.Builder(enumPage.JOIN))
						.instanceMessage("가입 후 사용하세요")
						.instanceMessageType(enumInstanceMessage.ERROR)
						.errorCode(enumMemberState.NOT_JOIN).build(); 	
				if(!member.isLogin())
					if(member.getEmail().equals(enumSystem.ADMIN.toString()))
						throw (new EntityException.Builder(enumPage.LOGIN))
						.instanceMessage("로그인 후 사용하세요")
						.instanceMessageType(enumInstanceMessage.ERROR)
						.errorCode(enumMemberState.NOT_LOGIN).build(); 	
				if(member.isLogout())
					if(member.getEmail().equals(enumSystem.ADMIN.toString()))
						throw (new EntityException.Builder(enumPage.LOGIN))
						.instanceMessage("로그인 후 사용하세요")
						.instanceMessageType(enumInstanceMessage.ERROR)
						.errorCode(enumMemberState.LOGOUT).build(); 	
				break;
				
			default:
				throw (new PageException.Builder(enumPage.ERROR404))
				.instanceMessage("페이지 인증 에러입니다.  관리자에게 문의하세요.")
				.instanceMessageType(enumInstanceMessage.ERROR)
				.errorCode(enumPageError.WRONG_PARAMATER).build();
		}//switch
	
		returns.put("isSuccessVerify", true);
		return returns;
	}
}

