package com.puppyrush.buzzcloud.service.entity.member;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.mail.enumMailState;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;

import java.sql.SQLException;
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
	
	@Autowired(required=false)
	private BandController bCtl;
	
	
	public Map<String,Object> execute(String sId) throws ControllerException, EntityException, SQLException{
		
		Map<String , Object> returns = new HashMap<String , Object>();
		Member member = null;
		
		if(!mCtl.containsEntity(sId))
			throw (new ControllerException.Builder(enumPage.ENTRY))
			.instanceMessage("로그인 한 유저가 아닙니다.")
			.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build(); 
		
		member = mCtl.getMember(sId);
		
		if(member.isLogin()==false)
			throw (new EntityException.Builder(enumPage.ENTRY))
			.instanceMessage("로그인 한 유저가 아닙니다.")
			.errorCode(enumMemberState.NOT_LOGIN).build(); 
		
		member.doLogout();
		
		mCtl.removeMember(sId);

		returns.put("view", enumPage.ENTRY.toString());	
		returns.putAll(new InstanceMessage( "로그아웃에 성공하셨습니다.", enumInstanceMessage.SUCCESS).getMessage());			
		
		
		return returns;
	}
	
}