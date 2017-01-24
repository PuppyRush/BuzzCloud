package com.puppyrush.buzzcloud.page.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.page.enums.enumCautionKind;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.ConnectMysql;
import com.puppyrush.buzzcloud.property.commandAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * 	inputMailforAuth.do 요청 처리
 *  	메일이 일치 하는지 여부결과를 반환한다.  
*/
public class VerifyMail implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		
		
		
		HashMap<String , Object> returns = new HashMap<String , Object>();
		
		String sessionId = request.getRequestedSessionId();
		String email = (String) request.getParameter("mail");

		try{
			if(!MemberDB.getInstance().isJoin(email))
				throw new EntityException("일치하는 메일이 존재하지 않습니다.", enumMemberState.NOT_JOIN, enumPage.JOIN);
			
			returns.put("view", enumPage.ENTRY.toString());
			returns.put("message", "메일로 인증번호가 전송되었습니다. 메일을 확인하세요.");
			returns.put("messageKind", enumCautionKind.NORMAL);
			returns.put("notExistMail","false");
		
		}catch(EntityException e){
			returns.put("view", e.getToPage().toString());
			returns.put("message", e.toString());
			returns.put("messageKind", enumCautionKind.ERROR);
		}
					
		return returns;
    }
}
