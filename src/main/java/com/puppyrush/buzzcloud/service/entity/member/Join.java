package com.puppyrush.buzzcloud.service.entity.member;


import com.puppyrush.buzzcloud.controller.form.JoinForm;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumEntityState;
import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("join")
public class Join{

	@Autowired(required=false)
	private MemberDB mDB;
	
	@Autowired(required=false)
	private MemberManager mMng;
			
	public Map<String,Object> execute(JoinForm form) throws SQLException, EntityException, PageException, NumberFormatException, AddressException, MessagingException{
			
		Map<String, Object> returns = new HashMap<String,Object>();
		Member member=null;
	

		enumMemberType type = enumMemberType.valueOf(form.getIdType());
		
		switch (type) {
				
			case GOOGLE:
			case NAVER:			
				form.setPassword("");
				member = new Member.Builder(form.getEmail()).registrationKind(type).planePassword(form.getPassword()).build();
				break;
	
			case NOTHING:
				member = new Member.Builder(form.getEmail()).registrationKind(type).planePassword(form.getPassword()).nickname(form.getNickname()).build();
				if(form.getPassword()==null || form.getPassword().length()<7)
					throw (new PageException.Builder(enumPage.JOIN))
					.instanceMessage("비 정상적인 접근입니다.")
					.errorCode(enumPageError.NO_PARAMATER).build(); 
			
				
				break;
	
			default:
				throw (new PageException.Builder(enumPage.JOIN))
				.instanceMessage("비 정상적인 접근입니다.")
				.errorCode(enumPageError.UNKNOWN_PARA_VALUE).build(); 									
		}	
		
		join(member);
		returns.putAll(new InstanceMessage("가입에 성공하였습니다. 메일인증을 하신 후 로그인하세요.", enumInstanceMessage.SUCCESS).getMessage());
		returns.put("isSuccess", true);
		return returns;
	}
	
	
	
	private void join(Member member) throws SQLException, EntityException, NumberFormatException, AddressException, PageException, MessagingException{
		
		if(checkJoinAndDuplicated(member)){
			member.doJoin();
			mMng.requestCertificateJoin(member);					
		}
	}
	
	private boolean checkJoinAndDuplicated(Member member) throws SQLException, PageException{
			
		if(mDB.isJoin(member.getEmail()) )
			throw (new PageException.Builder(enumPage.LOGIN))
			.instanceMessage("이미 가입한 유저입니다.")
			.errorCode(enumPageError.WRONG_PARAMATER).build(); 
		else if(mDB.isExistNickname(member.getNickname(),member.getId()))
			throw (new PageException.Builder(enumPage.JOIN))
			.instanceMessage("닉네임이 중복됩니다.")
			.errorCode(enumPageError.WRONG_PARAMATER).build(); 
		
		return true;
	}
}
	
