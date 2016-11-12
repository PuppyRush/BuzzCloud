package page.member;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.EntityException;
import entity.member.Member;
import entity.member.MemberController;
import entity.member.MemberDB;
import entity.member.MemberManager;
import entity.member.enums.enumMemberState;
import mail.enumMailType;

import java.sql.Timestamp;
import java.util.HashMap;

import page.PageException;
import page.enums.enumCautionKind;
import page.enums.enumPage;
import page.enums.enumPageError;
import property.commandAction;


/**
 *     비밀번호를 분실페이지로부터 요청을 처리 하기 위한 클래스
   *  	
*/
public class LostPassword implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		

		
		HashMap<String , Object> returns = new HashMap<String , Object>();
		try{
			if(request.getParameter("email")==null)
				throw new PageException(enumPageError.NO_PARAMATER);
							
			String sId = request.getRequestedSessionId();
			String email = (String)request.getParameter("email");
			
			if(!MemberDB.getInstance().isMember( email ))
				throw new EntityException("입려하신 메일과 일치하는 메일이 없습니다.",enumMemberState.NOT_JOIN, enumPage.LOST_PASSWORD);
									
			if(MemberManager.getInstance().isSendedmail(email, enumMailType.LOST_PASSWORD)){
				returns.put("view", enumPage.INPUT_CERTIFICATION_NUMBER.toString());
				returns.put("message", "메일이 이미 전송되었습니다.");
				returns.put("messageKind", enumCautionKind.ERROR);
			}
			else
				returns.put("view", enumPage.INPUT_MAIL.toString());				
		
						
		}catch(EntityException e){
			returns.put("view", e.getToPage());
			returns.put("message", e.getMessage());
			returns.put("messageKind", enumCautionKind.ERROR);

		}catch(PageException e){
			returns.put("view", enumPage.ENTRY.toString());
			returns.put("message", "내부오류. 관리자에게 문의하세요.");
			returns.put("messageKind", enumCautionKind.ERROR);
		
		}
		return returns;
		
	}
	
}
