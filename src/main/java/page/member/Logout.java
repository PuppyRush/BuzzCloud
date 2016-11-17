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
public class Logout implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			{
		
		HashMap<String , Object> returns = new HashMap<String , Object>();
		Member member = null;
		
		try{
						
		
			String sessionId = request.getRequestedSessionId();
			
			
			if(!MemberController.getInstance().containsEntity(sessionId))
				throw new ControllerException(enumController.NOT_EXIST_MEMBER_FROM_MAP);
			
			member = MemberController.getInstance().getMember(sessionId);
			
			if(!member.isLogin())
				throw new EntityException("로그인 한 유저가 아닙니다.", enumMemberState.NOT_LOGIN, enumPage.ENTRY);
			
			member.doLogout();
			
			returns.put("view", enumPage.ENTRY.toString());	
			returns.put("doLogout", true);
			returns.put("message", "로그아웃에 성공하셨습니다.");
			returns.put("messageKind", enumCautionKind.NORMAL);
			
		}catch( PageException e){
			returns.put("doLogout", false);
			returns.put("view", enumPage.ENTRY.toString());		
			returns.put("message", "로그아웃에 실패하셨습니다. 관리자에게 문의하세요.");
			returns.put("messageKind", enumCautionKind.ERROR);
			e.printStackTrace();
		}
		catch( EntityException e){
			if(e.getErrCode() instanceof enumMemberState)
				switch((enumMemberState)e.getErrCode()){
					case NOT_LOGIN:
					case NOT_JOIN:
						returns.put("initSession", true);
						returns.put("view", e.getToPage().toString());		
						returns.put("message", "로그아웃에 실패하셨습니다. 관리자에게 문의하세요.");
						returns.put("messageKind", enumCautionKind.ERROR);
	
						break;
					default:
						
						returns.put("doLogout", false);
						returns.put("view", e.getToPage().toString());		
						returns.put("message", "로그아웃에 실패하셨습니다. 관리자에게 문의하세요.");
						returns.put("messageKind", enumCautionKind.ERROR);
						
						break;
				}
				
			e.printStackTrace();
			
		}catch(ControllerException e){
			returns.put("view", e.getToPage().toString());		
			
		}
		
		catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return returns;
	}
	
}