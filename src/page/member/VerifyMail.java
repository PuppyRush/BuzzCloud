package page.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.ManageMember;
import member.Member;
import member.MemberException;
import member.enums.enumMemberState;
import page.enums.enumCautionKind;
import page.enums.enumPage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.HashMap;

import property.ConnectMysql;
import property.commandAction;

/**
 * 	inputMailforAuth.do 요청 처리
 *  	메일이 일치 하는지 여부결과를 반환한다.  
*/
public class VerifyMail implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		
		
		
		HashMap<String , Object> returns = new HashMap<String , Object>();
		
		String sId = request.getRequestedSessionId();
		String mail = (String) request.getParameter("mail");
		Member member = Member.getMember(sId);
		
		try{
			if(!ManageMember.isMember(member))
				throw new MemberException("일치하는 메일이 존재하지 않습니다.", enumMemberState.NOT_JOIN, enumPage.JOIN);
			
	
					
			if(member.getEmail().equals(mail)){
				returns.put("view", enumPage.ENTRY.toString());
				returns.put("message", "메일로 인증번호가 전송되었습니다. 메일을 확인하세요.");
				returns.put("messageKind", enumCautionKind.NORMAL);
				returns.put("notExistMail","false");
			}
			else
				throw new MemberException("일치하는 메일이 존재하지 않습니다.", enumMemberState.NOT_EXIST_IN_DB, enumPage.INPUT_MAIL);
				
					
			
		}catch(MemberException e){
			returns.put("view", e.getToPage().toString());
			returns.put("message", e.toString());
			returns.put("messageKind", enumCautionKind.ERROR);
		}
		
		
		
				
	return returns;
    }
}
