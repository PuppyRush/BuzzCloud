package com.puppyrush.buzzcloud.service.entity.member;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.mail.enumMailState;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
 *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.
 *  
 *       해당 클래스의 기능순서도는  114.129.211.123/boards/2/topics/64 참고
*/

@Service("changePassword")
final public class ChangePassword {


	@Autowired(required=false)
	private MemberController mCtl;
	
	@Autowired(required=false)
	private MemberManager mMng;
	
	public Map<String,Object> execute(String sId, String oldPassword, String newPassword) throws ControllerException, EntityException, SQLException, PageException{
		
		Map<String , Object> returns = new HashMap<String , Object>();
		
		if(!mCtl.containsEntity(sId))
			throw (new ControllerException.Builder(enumPage.ENTRY))
			.errorString("로그인 한 유저가 아닙니다.")
			.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build(); 
		
		Member member = mCtl.getMember(sId);
		
		if(member.isLogin()==false)
			throw (new EntityException.Builder(enumPage.ENTRY))
			.errorString("로그인 한 유저가 아닙니다.")
			.errorCode(enumMemberState.NOT_LOGIN).build(); 
		
		if(!member.getPlanePassword().equals(oldPassword))
			throw (new PageException.Builder(enumPage.MY_ACCOUNT))
			.errorString("입력하신 현재 비밀번호가 일치하지 않습니다.")
			.errorCode(enumPageError.WRONG_PARAMATER).build(); 
			
		mMng.updatePassword(member.getId(), newPassword);
		
		returns.putAll(new InstanceMessage( "비밀번호 변경에 성공하였습니다.", enumInstanceMessage.SUCCESS).getMessage());			
		
		
		return returns;
	}
	
}