package com.puppyrush.buzzcloud.service.member;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.page.enums.enumPage;

import java.util.HashMap;
import java.util.Map;


/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
 *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.
 *  
 *       해당 클래스의 기능순서도는  114.129.211.123/boards/2/topics/64 참고
*/

@Service("alreadyLogin")
public class AlreadyLogin{

	@Autowired(required=false)
	private MemberController mCtl;	
	
	public Map<String, Object> execute(String sessionId){
		
		Map<String, Object> result = new HashMap<String, Object>(); 
		
		try{
			if(mCtl.containsEntity(sessionId)==false)
				result.put("alreadyLogin", false);
			else{
				Member member = mCtl.getMember(sessionId);
				if(member.isLogin()){
					result.put("alreadyLogin", true);
					
				}
				else
					result.put("alreadyLogin", false);
	
			}
		}catch(ControllerException e){
			
		}
			
		result.put("view", enumPage.MAIN.toString());		
		
		return result;
	}



}