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
public class VerifyAuthNum implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		

		Member mdb = new Member();
		HashMap<String , Object> returns = new HashMap<String , Object>();
		String authNum;	

		if(request.getParameter("chkNum") == null){
			
			throw new NullPointerException("chkNum값이 전송되지 않았습니다");
		}
		else
			authNum = (String)request.getParameter("chkNum");
		

		if(request.getParameter("email") != null){
				
			String email = (String)request.getParameter("email");
			mdb.setEmail(email);
		}
		else
		{
			throw new NullPointerException("닉네임이나 이메일 값이 클라이언트 페이지로부터 전달되지 않았습니다");
			
		}
		
			
		if(MemberProcess.isConcordTempValue(mdb, authNum)){
			
			returns.put("view", "password_Reset");
			returns.put("corret_AuthNum", "true");
			
		}
		else{
			returns.put("view", "CheckAuthNum");
			returns.put("corret_AuthNum", "false");
		
		}
			
				
			
		
		
		
		return returns;
		
	}
	
}
