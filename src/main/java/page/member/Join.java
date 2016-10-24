package page.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.member.*;
import entity.member.enums.enumMemberState;
import entity.member.enums.enumMemberType;

import java.util.HashMap;

import property.commandAction;
import page.PageException;
import page.enums.enumCautionKind;
import page.enums.enumPage;
import page.enums.enumPageError;

/**
 * JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다. 외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면
 * 가입절차를 밟지 않는다.
 */
public class Join implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		

		HashMap<String , Object> returns = new HashMap<String , Object>();
		try{
			if(request.getParameter("email")==null || request.getParameter("idType")==null || request.getParameter("password")==null|| request.getParameter("nickname")==null)
				throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404);
							
			enumMemberType idType = enumMemberType.valueOf(request.getParameter("idType"));		
			
		
			String sId = request.getRequestedSessionId();
			String email =request.getParameter("email"); 
			String pw="";			
			String nickname= request.getParameter("nickname");
			
			switch (idType) {
					
				case GOOGLE:
				case NAVER:			
					pw="";
					
					break;
		
				case NOTHING:
					pw = request.getParameter("password");
					break;
		
				default:
					throw new PageException(enumPageError.UNKNOWN_PARA_VALUE, enumPage.ERROR404);
										
			}	
	
			Member tempMember = new Member.Builder(email, sId).idType(idType).planePassword(pw).build();
			
			if(MemberManager.isMember(tempMember.getEmail()) ){
				
				MemberManager.requestCertificateJoin(tempMember);
				
			}
			else{
				
				tempMember.doJoin(sId);
				
				returns.put("view", enumPage.ENTRY.toString());
				returns.put("isSuccessJoin", "true");
				returns.put("message", "가입에 성공하였습니다. 메일인증을 하신 후 로그인하세요.");
				returns.put("messageKind", enumCautionKind.NORMAL);	
				
			}
		
	
		}catch(PageException e){
			returns.put("view", e.getPage().toString());
			returns.put("isSuccessJoin", "false");
			returns.put("message",e.getMessage());
			returns.put("messageKind", enumCautionKind.ERROR);
		}catch(MemberException e){
			returns.put("view", enumPage.JOIN.toString());
			returns.put("isSuccessJoin", "false");
			returns.put("message",e.getMessage());
			returns.put("messageKind", enumCautionKind.ERROR);
			
		}
	return returns;
    }
}
