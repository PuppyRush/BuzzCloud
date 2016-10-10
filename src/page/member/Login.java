package page.member;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.ManageMember;
import member.Member;
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
		
		
	
		HashMap<String , Object> returns = new HashMap<String , Object>();
		Member member = null;
		String email = null;
		String userType="";
		String sId="";
		try{
			//필요조건
			if(request.getParameter("idType")==null || request.getParameter("email")==null
					||	request.getParameter("nickname")==null ||	request.getParameter("password")==null)
				throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404);
			
			email = (String)request.getParameter("email");
			
			//가입여부 확인
			sId = request.getRequestedSessionId();
			member = Member.getMember(sId);
			member.setEmail(email);
			
			if(!ManageMember.isMember(member))
				throw new MemberException("가입되지 않은 유저입니다. 가입후 사용하세요. ", enumMemberState.NOT_JOIN, enumPage.ENTRY);
			
			//멤버객체 설정			
			Member.setMemberFromDB(member);			
			member.setPlanePassword((String)request.getParameter("password"));	
			
			
			//가입 방식에 따라 달리 처리한다.
			userType = (String)request.getParameter("idType");
			if( userType.equals( enumMemberType.NOTHING.getString() ) ){
	
				EnumMap<enumMemberAbnormalState, Boolean> state = ManageMember.getMemberStates(member);
				//잠김상태인가?
				if( state.containsValue(true)){
					
					//아직 가입 인증을 안한경우.
					if(state.containsKey(enumMemberAbnormalState.JOIN_CERTIFICATION) && state.get( enumMemberAbnormalState.JOIN_CERTIFICATION))
						throw new MemberException(enumMemberState.NOT_JOIN_CERTIFICATION, enumPage.ENTRY);
						
					//비밀번호 분실상태인가?
					//아래의 두 상태는 비밀번호 일치여부를 검사할 필요 없음.
					if( state.get(enumMemberAbnormalState.LOST_PASSWORD) ){
							
						
						if(ManageMember.isSendmail(member))
							throw new MemberException(enumMemberState.LOST_PASSWORD, enumPage.INPUT_CERTIFICATION_NUMBER);
						else
							throw new MemberException(enumMemberState.LOST_PASSWORD, enumPage.INPUT_MAIL);
						
	
					}
					//비밀번호 초과상태인가
					else if(state.get(enumMemberAbnormalState.FAILD_LOGIN) ){
					
						if(ManageMember.isSendmail(member))
							throw new MemberException(enumMemberState.EXCEED_FAILD_LOGIN, enumPage.INPUT_CERTIFICATION_NUMBER);
						else
							throw new MemberException(enumMemberState.EXCEED_FAILD_LOGIN, enumPage.INPUT_MAIL);
	
					
					}
							
					//잠김 상태중에서도 아래 두가지는 확인이 필요함.
					if(state.get(enumMemberAbnormalState.LOST_PASSWORD )){
						if(ManageMember.loginMember(member)==false)
							throw new MemberException(enumMemberState.NOT_EQUAL_PASSWORD, enumPage.LOGIN);				
						else
							//로그인 실패하면 3개월 이상 변경여부 검사 안함
							if(ManageMember.isPassingDate(member))
								throw new MemberException(enumMemberState.PASSING_CHANGE_PWD, enumPage.CHANGE_OLD_PWD);						
					}
					else if(state.get(enumMemberAbnormalState.SLEEP) ){
					
						
						
					}
					
					
				}//ABNORMAL=0. 잠김상태가 아니면 로그인을 시도한다.
				else{
				
					if(ManageMember.loginMember(member)==false)
						throw new MemberException(enumMemberState.NOT_EQUAL_PASSWORD, enumPage.LOGIN);	
					else{//로그인성공
						member.setSessionId((String)request.getParameter("sessionId"));
						returns.put("message", "로그인에 성공하셨습니다.");
						returns.put("messageKind", enumCautionKind.NORMAL);
						returns.put("isSuccessLogin", "true");
						returns.put("id", email);
					}
					
					returns.put("view", enumPage.ENTRY.toString());				
					
				}	
	
				
			}//userType = nothing
			else if(userType.equals( enumMemberType.GOOGLE.getString() ) ){
				
			}
			else if(userType.equals( enumMemberType.NAVER.getString() ) ){
				
			}
			else
				throw new PageException( "시스템에 존재하지 않는" + userType + "값이 전달되었습니다.",enumPageError.UNKNOWN_PARA_VALUE , enumPage.ERROR404); 
			
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
			returns.put("message", "데이버베이스 조작 오류입니다.");
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