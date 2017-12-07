package com.puppyrush.buzzcloud.service.entity.member;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.mail.PostManImple;
import com.puppyrush.buzzcloud.mail.PostMan;
import com.puppyrush.buzzcloud.mail.enumMail;
import com.puppyrush.buzzcloud.page.enums.enumPage;



@Service("findPassword")
final public class FindPassword{
	
	@Autowired(required=false)
	private MemberDB mDB;
	
	@Autowired(required=false)
	private MemberManager mMng;
	
	@Autowired(required=false)
	private DBManager dbMng;
	
	@Autowired(required=false)
	private PostManImple postman;
	
	public Map<String,Object> execute(String email) throws SQLException, AddressException, MessagingException{
				
		Map<String,Object> returns = new HashMap<String,Object>();
		
	
		if(mDB.isJoin(email)==false){
			returns.putAll(new InstanceMessage("가입하지 않은 메일 입니다.", enumInstanceMessage.ERROR).getMessage());
			
		}
		else{
			if(!mDB.isOnSiteAccount(email)){
				returns.putAll(new InstanceMessage("분실 비밀번호는 사이트 가입자만 찾을 수 있습니다.", enumInstanceMessage.ERROR).getMessage());
				
			}else{						
				
				mMng.setLostPassword(email);
				returns.putAll(new InstanceMessage("임시비밀번호를 메일로 보냈습니다. 메일을 확인하세요.", enumInstanceMessage.SUCCESS).getMessage());
			}
		}
		returns.put("view",enumPage.ENTRY.toString());
		return returns;		
	}
	
	
}
