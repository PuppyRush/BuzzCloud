package com.puppyrush.buzzcloud.page.member;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.property.commandAction;
import com.puppyrush.buzzcloud.property.enums.*;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * 
 * 	비밀번호 변경을 위한 클래스 
 * 
 * @author cmk
 *
 */
public class ChangePassword implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		

		HashMap<String , Object> returns = new HashMap<String , Object>();

	
		if(request.getParameter("email") == null || request.getParameter("password") == null)
			throw new NullPointerException("이메일 혹은 새로운 비밀번호가  클라이언트로부터 전송되지 않았습니다");


		
		
		return returns;
		
	}

	
}
