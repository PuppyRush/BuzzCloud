package com.puppyrush.buzzcloud.service.member;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumCautionKind;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import java.util.HashMap;
import java.util.Map;


/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
 *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.
 *  
 *       해당 클래스의 기능순서도는  114.129.211.123/boards/2/topics/64 참고
*/

@Service("logout")
final public class Logout {


	@Autowired(required=false)
	private MemberController mCtl;
		
	public Map<String,Object> execute(String sId){
		
		Map<String , Object> returns = new HashMap<String , Object>();
		Member member = null;
		
		try{
					
			if(!mCtl.containsEntity(sId))
				throw new ControllerException(enumController.NOT_EXIST_MEMBER_FROM_MAP);
			
			member = mCtl.getMember(sId);
			
			if(member.isLogin()==false)
				throw new EntityException("로그인 한 유저가 아닙니다.", enumMemberState.NOT_LOGIN, enumPage.ENTRY);
			
			member.doLogout();
			
			mCtl.removeMember(sId);
			
			returns.put("view", enumPage.ENTRY.toString());	
			returns.put("doLogout", true);
			returns.put("message", "로그아웃에 성공하셨습니다.");
			returns.put("messageKind", enumCautionKind.NORMAL.toString());
			
		}catch( PageException e){
			returns.put("doLogout", false);
			returns.put("view", enumPage.ENTRY.toString());		
			returns.put("message", "로그아웃에 실패하셨습니다. 관리자에게 문의하세요.");
			returns.put("messageKind", enumCautionKind.ERROR.toString());
			e.printStackTrace();
		}
		catch( EntityException e){
			if(e.getErrCode() instanceof enumMemberState)
				switch((enumMemberState)e.getErrCode()){
					case NOT_LOGIN:
					case NOT_JOIN:
						returns.put("initSession", true);
						returns.put("view", e.getToPage().toString());		
						returns.put("message", "로그아웃에 실패하셨습니다. 관리자에게 문의하세요.");
						returns.put("messageKind", enumCautionKind.ERROR.toString());
	
						break;
					default:
						
						returns.put("doLogout", false);
						returns.put("view", e.getToPage().toString());		
						returns.put("message", "로그아웃에 실패하셨습니다. 관리자에게 문의하세요.");
						returns.put("messageKind", enumCautionKind.ERROR.toString());
						
						break;
				}
				
			e.printStackTrace();
			
		}catch(ControllerException e){
			returns.put("view", e.getToPage().toString());		
			
		}
		
		catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return returns;
	}
	
}