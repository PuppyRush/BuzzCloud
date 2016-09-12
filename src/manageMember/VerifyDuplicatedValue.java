package manageMember;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import javaBean.MemberProcess;
import javaBean.Member;
import property.commandAction;

/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
   *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.
*/
public class VerifyDuplicatedValue implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {

		HashMap<String , Object> returns = new HashMap<String , Object>();
		
		if(request.getParameter("nickname") == null || request.getParameter("email") == null)
			throw new Exception( request.getRequestURI() + "검사할 항목이 없습니다.");

		if(request.getParameter("nickname") != null){
			
			String nickname = (String)request.getParameter("nickname");
	
	
			if(!MemberProcess.isExist("user","nickname",nickname)){
				returns.put("message", "닉네임이 중복됩니다.");
				returns.put("isDuplicatedNickname", "true");
			}
			else{
				returns.put("message", "닉네임이 중복되지 않습니다");
				returns.put("isDuplicatedNickname", "false");	
			}

		}
		else if(request.getParameter("email") != null){
			
			String email = (String)request.getParameter("email");
			
			if(!MemberProcess.isExist("user","email" ,email)){
				returns.put("message", "닉네임이 중복됩니다.");
				returns.put("isDuplicatedEmail", "true");
			}
			else{
				returns.put("message", "닉네임이 중복되지 않습니다");
				returns.put("isDuplicatedEmail", "false");	
			}
			
			
		}
		
		
		returns.put("view", "/");
		
		return returns;
		
	}
	
}
