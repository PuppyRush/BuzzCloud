package page.member;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mail.enumMailType;
import member.MemberManager;
import member.Member;
import member.MemberController;
import member.MemberException;
import member.enums.enumMemberAbnormalState;
import member.enums.enumMemberState;
import member.enums.enumMemberType;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;

import page.PageException;
import page.enums.enumCautionKind;
import page.enums.enumPage;
import page.enums.enumPageError;
import property.commandAction;



/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
 *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.    
 *       해당 클래스의 기능순서도는  http://114.129.211.123/boards/2/topics/64 참고
 *  Login class에서 호출하는 함수들은 모두 예외를 밖으로 던져 마지막으로 Login class의 catch에서 처리한다.
*/
public class Login implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable
			{
		request.setCharacterEncoding("UTF-8");
		
		HashMap<String , Object> returns = new HashMap<String , Object>();
		enumMemberType idType = enumMemberType.NOTHING;
		
		try{
			//필요조건
			if(request.getParameter("idType")==null || request.getParameter("email")==null
					||	request.getParameter("nickname")==null ||	request.getParameter("password")==null)
				throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404);
			
			
			
			String email = request.getParameter("email");
			String sessionId = request.getRequestedSessionId();
			String planePassword = request.getParameter("password");
			String nickname = request.getParameter("nickname");
			
			//가입 방식에 따라 달리 처리한다.
			idType = enumMemberType.valueOf(request.getParameter("idType"));
			
			Member tempMember = new Member.Builder(email, sessionId).idType(idType).nickname(nickname).planePassword(planePassword).build();
			
			switch(idType){
				case NOTHING:
					
					//가입여부 확인.
					if(!MemberManager.isMember(email))
						throw new MemberException("이메일이 존재하지 않습니다. 가입후 사용하세요. ", enumMemberState.NOT_JOIN, enumPage.JOIN);
					
					//로그인 여부 확인.				
					if(MemberController.containsMember(sessionId)){
						tempMember = MemberController.getMember(sessionId);
						if(tempMember.isLogin())
							throw new MemberException( enumMemberState.ALREADY_LOGIN, enumPage.LOGIN);
					}

					EnumMap<enumMemberAbnormalState, Boolean> state = MemberManager.getMemberAbnormalStates(tempMember);
					//잠김상태인가?
					if( state.containsValue(true)){
						
						//아직 가입 인증을 안한경우.
						if(state.containsKey(enumMemberAbnormalState.JOIN_CERTIFICATION) && state.get( enumMemberAbnormalState.JOIN_CERTIFICATION))
							throw new MemberException(enumMemberState.NOT_JOIN_CERTIFICATION, enumPage.ENTRY);
							
						//비밀번호 분실상태인가?
						//아래의 두 상태는 비밀번호 일치여부를 검사할 필요 없음.
						if( state.get(enumMemberAbnormalState.LOST_PASSWORD) ){					
							
							if(MemberManager.isSendedmail(tempMember.getEmail(), enumMailType.LOST_PASSWORD))
								throw new MemberException(enumMemberState.LOST_PASSWORD, enumPage.INPUT_CERTIFICATION_NUMBER);
							else
								throw new MemberException(enumMemberState.LOST_PASSWORD, enumPage.INPUT_MAIL);
							
		
						}
						//비밀번호 초과상태인가
						else if(state.get(enumMemberAbnormalState.FAILD_LOGIN) ){
						
							if(MemberManager.isSendedmail(tempMember.getEmail(), enumMailType.FAILED_LOGIN))
								throw new MemberException(enumMemberState.EXCEED_FAILD_LOGIN, enumPage.INPUT_CERTIFICATION_NUMBER);
							else
								throw new MemberException(enumMemberState.EXCEED_FAILD_LOGIN, enumPage.INPUT_MAIL);
		
						
						}
								
						//잠김 상태중에서도 아래 두가지는 확인이 필요함.
						if(state.get(enumMemberAbnormalState.LOST_PASSWORD )){
							
							tempMember.doLogin(sessionId);
							
							
						}
						
						else if(state.get(enumMemberAbnormalState.SLEEP) ){
						
							
							
						}
						
						
					}//ABNORMAL=0. 잠김상태가 아니면 로그인을 시도한다.
					else{
					
						if(tempMember.doLogin(sessionId)==false)
							throw new MemberException(enumMemberState.NOT_EQUAL_PASSWORD, enumPage.LOGIN);	
						else{//로그인성공
							
							returns.put("message", "로그인에 성공하셨습니다.");
							returns.put("messageKind", enumCautionKind.NORMAL);
							returns.put("isSuccessLogin", "true");
							returns.put("id", email);
						}
						
						returns.put("view", enumPage.ENTRY.toString());				
						
					}	
					break;
					
				case NAVER:
				case GOOGLE:
					
					if(!MemberManager.isMember(email)){
						tempMember.doJoin(sessionId);
						tempMember = MemberController.getMember(sessionId);
	
					}
					else{
						tempMember.doLogin(sessionId);
					}
					returns.put("view", enumPage.MAIN.toString());		
					
					break;
					
				default:
					throw new PageException(enumPageError.UNKNOWN_PARA_VALUE,enumPage.ERROR404);
				
			}
		}catch(PageException e){
			e.printStackTrace();
			
			returns.put("is_successLogin", "false");
			returns.put("view",e.getPage());		
			
		
		}catch(MemberException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			returns.put("view", e.getToPage().toString());	
			returns.put("message", e.getMessage());
			returns.put("messageKind", enumCautionKind.ERROR);
			
		}catch(SQLException e){
			e.printStackTrace();
			returns.put("view", enumPage.ERROR403.toString());
			returns.put("message", "알수없는 오류 발생. 관리자에게 문의하세요");
			returns.put("messageKind", enumCautionKind.ERROR);
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			returns.put("view", enumPage.ERROR403.toString());
			returns.put("message", "알수없는 오류 발생. 관리자에게 문의하세요");
			returns.put("messageKind", enumCautionKind.ERROR);
		}

		
		return returns;
	}
	
}
