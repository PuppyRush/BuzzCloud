package page.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;

import property.commandAction;
import member.*;
import member.enums.enumMemberType;
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
							
			enumMemberType _idType = enumMemberType.valueOf(request.getParameter("idType"));		
			
		
			String _sId = request.getRequestedSessionId();
			String _email =request.getParameter("email"); 
			String _pw="";			
			String _nickname= request.getParameter("nickname");
			
			switch (_idType) {
					
				case GOOGLE:
				case NAVER:			
					_pw="";
					
					break;
		
				case NOTHING:
					_pw = request.getParameter("password");
					
					break;
		
				default:
					throw new PageException(enumPageError.UNKNOWN_PARA_VALUE, enumPage.ERROR404);
										
			}	
	
			Member tempMember = new Member.Builder(_email, _sId).idType(_idType).planePassword(_pw).build();
			
			if ( tempMember.doJoin())
				if(MemberManager.requestCertificateJoin(MemberManager.getMember(_sId))){
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
