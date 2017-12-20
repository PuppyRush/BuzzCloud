package com.puppyrush.buzzcloud.service.entity.member;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberAbnormalState;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.mail.PostManImple;
import com.puppyrush.buzzcloud.mail.MailManager;
import com.puppyrush.buzzcloud.mail.enumMailType;
import com.puppyrush.buzzcloud.page.enums.enumPage;



@Service("findPassword")
final public class SendMail{
	
	@Autowired(required=false)
	private MemberDB mDB;
	
	@Autowired(required=false)
	private MemberManager mMng;
	
	@Autowired(required=false)
	private MailManager mailMng;
	
	
	public Map<String,Object> execute(String email) throws SQLException, AddressException, MessagingException, EntityException, ControllerException{
				
		Map<String,Object> returns = new HashMap<String,Object>();
		
		if(mDB.isJoin(email)==false){
			returns.putAll(new InstanceMessage("가입하지 않은 메일 입니다.", enumInstanceMessage.ERROR).getMessage());
		}
		else{
			Member member = mDB.getMember(email);
			Map<enumMemberAbnormalState, Boolean> state = member.getAbnormalState();
			if(state.containsKey(enumMemberAbnormalState.JOIN_CERTIFICATION) && state.get(enumMemberAbnormalState.JOIN_CERTIFICATION)){
				mMng.requestCertificateJoin(member);
				returns.putAll(new InstanceMessage("가입을 위한 인증번호를 전송하였습니다. 메일을 확인하세요.", enumInstanceMessage.SUCCESS).getMessage());
			}
			else if(state.containsKey(enumMemberAbnormalState.SLEEP) && state.get(enumMemberAbnormalState.SLEEP)){
				mailMng.SendCertificationMail(member, enumMailType.SLEEP);
				returns.putAll(new InstanceMessage("휴면계정 해제를 위한 인증번호를 전송하였습니다. 메일을 확인하세요.", enumInstanceMessage.SUCCESS).getMessage());
			}
			else if(state.containsKey(enumMemberAbnormalState.OLD_PASSWORD) && state.get(enumMemberAbnormalState.OLD_PASSWORD)){
				if(!mDB.isOnSiteAccount(email))
					returns.putAll(new InstanceMessage("분실 비밀번호는 사이트 가입자만 찾을 수 있습니다.", enumInstanceMessage.ERROR).getMessage());
				else{
					mMng.setLostPassword(email);	
					returns.putAll(new InstanceMessage("임시비밀번호를 메일로 보냈습니다. 메일을 확인하세요.", enumInstanceMessage.SUCCESS).getMessage());
				}
				
			}
			else if(state.containsKey(enumMemberAbnormalState.LOST_PASSWORD) && state.get(enumMemberAbnormalState.LOST_PASSWORD)){
				mailMng.SendCertificationMail(member, enumMailType.LOST_PASSWORD);
				returns.putAll(new InstanceMessage("임시비밀번호를 전송하였습니다. 메일을 확인하세요.", enumInstanceMessage.SUCCESS).getMessage());
			}		
			else if(state.containsKey(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT) && state.get(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT)){
				mailMng.SendCertificationMail(member, enumMailType.EXCEEDED_LOGIN_COUNT);
				returns.putAll(new InstanceMessage("임시비밀번호를 전송하였습니다. 메일을 확인하세요.", enumInstanceMessage.SUCCESS).getMessage());
			}		
			
		}
		returns.put("view",enumPage.ENTRY.toString());
		return returns;		
	}
	
	
}
