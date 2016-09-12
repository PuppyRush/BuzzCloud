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
public class Join implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		
		
		Member member = new Member();
		HashMap<String , Object> returns = new HashMap<String , Object>();
		String idType = (String) request.getParameter("idType");

		if(request.getParameter("sessionId")==null){
			returns.put("is_successLogin", "false");
			returns.put("view", "/");		
			throw new NullPointerException("sessionId값이 서버로 전송되지 않았습니다.");
		}else
			member = Member.getMember( (String)request.getParameter("sessionId"));
		
		member.setId(0);
		
		member.setEmail((String) request.getParameter("register_email"));
		member.setIdType((String) request.getParameter("idType"));
		member.setNickname((String) request.getParameter("register_username"));
		member.setReg_date(new Timestamp(System.currentTimeMillis()));
		
		
		switch (idType) {
				
			case "google":
			case "naver":
		

				member.setPassword(" ");
				
				break;
	
			case "inner":
				
				member.setPassword((String) request.getParameter("register_password"));
				
				break;
	
			default:
				returns.put("message", "kind의 종류가 명확하지 않습니다");
				return returns;
				
		}

		
		if (MemberProcess.joinMember(member)){
			returns.put("isSuccessJoin", "true");
			returns.put("message", "가입에 성공하였습니다. 로그인 하세요");
		}
		else{
			returns.put("isSuccessJoin", "false");
			returns.put("message","가입에 실패하였습니다.");
		}
		returns.put("view", "/");
		returns.put("innerJoin", "true");
				
	return returns;
    }
}
