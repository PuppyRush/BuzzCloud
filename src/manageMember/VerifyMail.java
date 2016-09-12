package manageMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import javaBean.MemberProcess;
import javaBean.Member;
import property.commandAction;

/**
 * 	inputMailforAuth.do 요청 처리
 *  	메일이 일치 하는지 여부결과를 반환한다.  
*/
public class VerifyMail implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		
		
		Member member = new Member();
		HashMap<String , Object> returns = new HashMap<String , Object>();
		
		String mail = (String) request.getParameter("mail");
		
		int u_num = MemberProcess.sthToId(member);
		String email_of_unum = (String)MemberProcess.getSthJustOne("user", "user_num", u_num, "email");
				
		if(email_of_unum.equals(mail)){
			
			
			
			returns.put("notExistMail","false");
		}
		else
			returns.put("notExistMail","true");
				


		
		returns.put("view", "/");
		
				
	return returns;
    }
}
