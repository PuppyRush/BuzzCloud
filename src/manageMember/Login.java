package manageMember;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.HashMap;
import javaBean.MemberProcess;
import javaBean.Member;
import property.commandAction;
import property.enums.enumAttributeKey;
import property.enums.enumUserState;

/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
 *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.
 *  
 *       해당 클래스의 기능순서도는  114.129.211.123/boards/2/topics/64 참고
*/
public class Login implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		
	
		HashMap<String , Object> returns = new HashMap<String , Object>();
		Member member = null;
		String nick_or_mail = null;
		
		if(request.getParameter("sessionId")==null){
			returns.put("is_successLogin", "false");
			returns.put("view", "/");		
			throw new NullPointerException("sessionId값이 서버로 전송되지 않았습니");
		}else
			member = Member.getMember( (String)request.getParameter("sessionId"));
		
		System.out.println((String)request.getParameter("sessionId"));
		
		if(request.getParameter("idType") == null)
			throw new Exception( request.getRequestURI() + "에 kind가 없습니다");
		
		if( ((String)request.getParameter("idType")).equals("inner") ){
			
			nick_or_mail = (String)request.getParameter("login_username");
					//주석	
			if( nick_or_mail.contains("@") ){			
				member.setEmail(nick_or_mail);	
				member.setId( MemberProcess.sthToId(member));
				member.setNickname( (String)MemberProcess.getSthJustOne("user", "userNum", member.getId(), "nickname"));
			}
			else{
				member.setNickname(nick_or_mail);
				member.setId( MemberProcess.sthToId(member));
				member.setNickname( (String)MemberProcess.getSthJustOne("user", "userNum", member.getId(), "email"));
			}
			
			member.setPassword((String)request.getParameter("login_password"));			
				
			int code;
			//잠김상태인가?
			if( (code = MemberProcess.isLockingMember(member)) !=0){
				
				//비밀번호 분실상태인가?
				//아래의 두 상태는 비밀번호 일치여부를 검사할 필요 없음.
				if((code & Integer.valueOf( enumUserState.LOSTPW.getString()) ) == Integer.valueOf( enumUserState.LOSTPW.getString())  ){
									
					returns.put("userState","lostpw");		
					
					if(MemberProcess.isSendmail(member, Integer.valueOf(enumUserState.LOSTPW.getString())))
						returns.put("view", "WHERE?");
					else
						returns.put("view", "WHEER?");
					
					
					return returns;

				}
				//비밀번호 초과상태인가
				else if((code & Integer.valueOf( enumUserState.FAILD_LOGIN.getString()) ) == Integer.valueOf( enumUserState.FAILD_LOGIN.getString()) ){
					
					returns.put("userState","faild_login");
					returns.put("excessFaildcount","true");
					
					if(MemberProcess.isSendmail(member, Integer.valueOf(enumUserState.LOSTPW.getString())))
						returns.put("view", "WHERE?");
					else
						returns.put("view", "WHEER?");
					
					
					return returns;
				}
						
				//잠김 상태중에서도 아래 두가지는 확인이 필요함.
				if((code & Integer.valueOf( enumUserState.OLD_PASSWD.getString()) ) == Integer.valueOf( enumUserState.OLD_PASSWD.getString())  ){
				
					//로그인 실패하면 3개월 이상 변경여부 검사 안함
					if(MemberProcess.loginMember(member)==false){
						returns.put("message", "패스워드가 일치하지 않거나 아이디혹은 메일이 존재하지 않습니다.");
					}
					else{
						
						if(MemberProcess.isPassingDate(member)){
							returns.put("excessDateOfChange", "ture");
							returns.put("view", "password_Reset.html");
						}
						else{
							returns.put("innerLogin", "false");
							returns.put("view", "/");
						}
					}
					
					
					
					
				}
				else if((code & Integer.valueOf( enumUserState.SLEEP.getString()) ) == Integer.valueOf( enumUserState.SLEEP.getString())  ){
						
					if(MemberProcess.isSendmail(member, Integer.valueOf(enumUserState.SLEEP.getString())))
						returns.put("view", "WHERE?");
					else
						returns.put("view", "WHEER?");
					
				}
				
				
			}//isn't Locking member and Succes login process.
			else{
			
				if(MemberProcess.loginMember(member)==false){
					returns.put("message", "패스워드가 일치하지 않거나 아이디혹은 메일이 존재하지 않습니다.");
					returns.put("isSuccessLogin", "false");
					
				}
				else{
					member.setSessionId((String)request.getParameter("sessionId"));
					returns.put("isSuccessLogin", "true");
					returns.put("id", nick_or_mail);
				}
				
				returns.put("view", "/");				
				
			}	

			
		}
	
		return returns;
	}
	
}