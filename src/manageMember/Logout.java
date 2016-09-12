package manageMember;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.HashMap;
import javaBean.MemberProcess;
import javaBean.Member;
import property.commandAction;
import property.enums.enumUserState;

/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
 *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.
 *  
 *       해당 클래스의 기능순서도는  114.129.211.123/boards/2/topics/64 참고
*/
public class Logout implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		
		HashMap<String , Object> returns = new HashMap<String , Object>();
		Member member = null;
		String nick_or_mail = null;
		
		System.out.println((String)request.getParameter("sessionId"));
		
		if(request.getParameter("sessionId")==null){
			returns.put("is_successLogin", "false");
			returns.put("view", "/");		
			throw new NullPointerException("sessionId값이 서버로 전송되지 않았습니");
		}else
			member = Member.getMember( (String)request.getParameter("sessionId"));
		
		
		
		MemberProcess.logoutMember(member);
		returns.put("view", "/");	
		returns.put("doLogout", "true");
		
		return returns;
	}
	
}