package com.puppyrush.buzzcloud.service.entity.member;


import com.puppyrush.buzzcloud.controller.form.JoinForm;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
 *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.    
 *       해당 클래스의 기능순서도는  http://114.129.211.123/boards/2/topics/64 참고
 *  Login class에서 호출하는 함수들은 모두 예외를 밖으로 던져 마지막으로 Login class의 catch에서 처리한다.
*/

@Service("join")
public class Join{

	@Autowired(required=false)
	private MemberDB mDB;
	
	@Autowired(required=false)
	private MemberManager mMng;
			
	public Map<String,Object> execute(JoinForm joinForm){
			
		Map<String, Object> returns = new HashMap<String,Object>();
		
		try{

			enumMemberType type = enumMemberType.valueOf(joinForm.getIdType());
			
			switch (type) {
					
				case GOOGLE:
				case NAVER:			
					joinForm.setPassword("");
					
					break;
		
				case NOTHING:
					
					if(joinForm.getPassword()==null || joinForm.getPassword().length()<7)
						throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404); 
					
					break;
		
				default:
					throw new PageException(enumPageError.UNKNOWN_PARA_VALUE, enumPage.ERROR404);
										
			}	
			
			returns = join(joinForm);
	
		}
		catch(PageException e){
			returns.put("isSuccess", false);
			returns.put("message", e.getMessage());
			
		}catch(Exception e){
			returns.put("isSuccess", false);
			returns.put("message", e.getMessage());
		}
		catch(Throwable e){
			returns.put("isSuccess", false);
			returns.put("message", e.getMessage());
		}
					
		return returns;
	}
	
	private Map<String,Object> join(JoinForm form) throws SQLException, EntityException, Throwable{
		
		Map<String, Object> returns = new HashMap<String, Object>();
		Member tempMember = new Member.Builder(form.getEmail()).registrationKind(enumMemberType.valueOf(form.getIdType())).planePassword(form.getPassword()).build();
		
		if(mDB.isJoin(tempMember.getEmail()) ){
			
			returns.put("isSuccess", false);
			returns.put("message", "이미 가입한 유저입니다.");
			
		}
		else{
			
			tempMember.doJoin();
			mMng.requestCertificateJoin(tempMember);					
			
			returns.put("isSuccess", true);
			returns.put("message", "가입에 성공하였습니다. 메일인증을 하신 후 로그인하세요.");
				
		}
		
		return returns;
		
	}
}
	
