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
	
	public Map<String,Object> execute(String email) throws SQLException, AddressException, MessagingException{
				
		Map<String,Object> returns = new HashMap<String,Object>();
		
	
		if(mDB.isJoin(email)==false){
			returns.putAll(new InstanceMessage("가입하지 않은 메일 입니다.", InstanceMessageType.ERROR).getMessage());
			
		}
		else{
			if(!isOnSiteAccount(email)){
				returns.putAll(new InstanceMessage("분실 비밀번호는 사이트 가입자만 찾을 수 있습니다.", InstanceMessageType.ERROR).getMessage());
				
			}else{						
				String temp = getTemporaryPassword();
				sendMail(temp, email);
				mMng.setLostPassword(email,0, temp);
				returns.putAll(new InstanceMessage("임시비밀번호를 메일로 보냈습니다. 메일을 확인하세요.", InstanceMessageType.SUCCESS).getMessage());
			}
		}
		returns.put("view",enumPage.ENTRY.toString());
		return returns;		
	}
	
	private boolean isOnSiteAccount(String email){
		
		Map<String, Object> where = new HashMap<String, Object>();
		List<String> sel = new ArrayList<String>();
		int id = mDB.getIdOfEmail(email);
		where.put("memberId", id);
		sel.add("registrationKind");
		List<Map<String,Object>> res = dbMng.getColumnsOfPart("member", sel, where);
		
		if(res.isEmpty())
			return false;
		else if(enumMemberType.valueOf((String)res.get(0).get("registrationKind")).equals(enumMemberType.NOTHING)){
			return true;
		}
		return false;
		
	}
	
	private void sendMail(String temporaryPw, String to) throws AddressException, MessagingException{
		
		String subject = "[BuzzCloud]요청하신 임시비밀 번호 입니다.";
		String content = "비밀번호 분실로 임시 비밀번호를 보냅니다. 유효기간은 하루동안이니 이 안에 로그인 하시기 바랍니다.\n임시비밀번호 : " + temporaryPw; 
		
		PostMan man = new PostManImple();
		man.send(new PostManImple.Builder(enumMail.gmailID.toString(), to).subject(subject).content(content).build());
	
		
	}
	
	private String getTemporaryPassword(){
		final int numbers = '9'-'0'+1;
		final int letters = 'Z'-'A'+1;
		StringBuilder tempPW = new StringBuilder("");
		for(int i=0 ; i < 6; i++){
			if(new Random().nextBoolean()){
				tempPW.append((char)(new Random().nextInt(letters)+'A'));
			}
			else
				tempPW.append((char)(new Random().nextInt(numbers)+'0'));
		}
		return tempPW.toString();
	}
}
