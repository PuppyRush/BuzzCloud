package com.puppyrush.buzzcloud.service.entity.member;


import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.controller.form.LoginForm;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.enumAuthorityState;
import com.puppyrush.buzzcloud.entity.authority.member.enumMemberAuthority;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberAbnormalState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberStandard;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.mail.MailManager;
import com.puppyrush.buzzcloud.mail.PostMan;
import com.puppyrush.buzzcloud.mail.enumMailStandard;
import com.puppyrush.buzzcloud.mail.enumMailState;
import com.puppyrush.buzzcloud.mail.enumMailType;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.CommFunc;
import com.puppyrush.buzzcloud.property.enumSystem;



/**
 *  JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다.
 *  	  외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면 가입절차를 밟지 않는다.    
 *       해당 클래스의 기능순서도는  http://114.129.211.123/boards/2/topics/64 참고
 *  Login class에서 호출하는 함수들은 모두 예외를 밖으로 던져 마지막으로 Login class의 catch에서 처리한다.
*/

@Service("login")
final public class Login{

	@Autowired(required=false)
	private MemberController mCtl;
	
	@Autowired(required=false)
	private MemberDB mDB;
	
	@Autowired(required=false)
	private MemberManager mMng;
		
	@Autowired(required=false)
	private MailManager mailMng;
		
	
	public Map<String,Object> execute(LoginForm form){
				
		Map<String,Object> returns = new HashMap<String,Object>();
		
		try{
				
			Member member = mCtl.getMember(form);
			member.setSessionId(form.getSessionId());
			
			invalidateMember_If_dosentLogin(member);
			
			returns.putAll(doLoginAsCase(member,form.getIdType()));
					
			returns.put("nickname", member.getNickname());
			returns.put("id", String.valueOf(member.getId()));
			returns.put("email", member.getEmail());
			returns.putAll(new InstanceMessage("로그인에 성공하셨습니다.", enumInstanceMessage.SUCCESS).getMessage());
		}catch(PageException e){
			returns.putAll(e.getReturns());
			returns.put("is_successLogin", false);
		}catch(EntityException e){
			returns.putAll(e.getReturns());
		}catch(SQLException e){
						
			returns.put("view", enumPage.ERROR403);
			
			InstanceMessage msg = new InstanceMessage("알수없는 오류 발생. 관리자에게 문의하세요", enumInstanceMessage.ERROR);
			returns.putAll(msg.getMessage());
		}catch(Exception e){
			
			returns.put("view", enumPage.ERROR403.toString());
			InstanceMessage msg = new InstanceMessage("알수없는 오류 발생. 관리자에게 문의하세요", enumInstanceMessage.ERROR);
			returns.putAll(msg.getMessage());
		}

		return returns;
	}
	
	public void invalidateMember_If_dosentLogin(Member member) throws ControllerException{
				
		if(!member.isLogin()){
			if(mCtl.containsEntity(member.getId()))
				mCtl.removeEntity(member.getId());
			if(mCtl.containsEntity(member.getSessionId()))
				mCtl.removeMember(member.getSessionId());
		}
		
	}
	
	public Map<String,Object> doLoginAsCase(Member member, String loginMethod) throws PageException, AddressException, EntityException, ControllerException, SQLException, MessagingException{
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		if(member.getUserType().equals(enumMemberType.NOTHING) && loginMethod.equals(enumMemberType.NOTHING.toString())){
			nothingCase(member);		
		}
		else if(member.getUserType().equals(enumMemberType.NAVER) && loginMethod.equals(enumMemberType.NAVER.toString())){
			oauthCase(member);		
		}
		else if(member.getUserType().equals(enumMemberType.GOOGLE) && loginMethod.equals(enumMemberType.GOOGLE.toString())){
			oauthCase(member);		
		}
		else
			throw new PageException.Builder(enumPage.LOGIN)
				.instanceMessage("비 정상적인 접근입니다.")
				.errorCode(enumPageError.UNKNOWN_PARA_VALUE).build(); 
					
		returns.put("view", enumPage.MAIN.toString());		
		return returns;
		
	}
	
	private void oauthCase(Member member) throws PageException, SQLException, ControllerException, EntityException{
		
	
		if(mDB.isJoin(member.getEmail())){
			
			if(!member.doLogin())
				throw (new EntityException.Builder(enumPage.LOGIN))
				.instanceMessage("비밀번호가 일치하지 않습니다.")
				.errorCode(enumMemberState.NOT_EQUAL_PASSWORD).build();
			else
				mCtl.addMember(member, member.getSessionId());

			if(member.verify())
				mCtl.addMember(member, member.getSessionId());
		}
		else{
			member.doJoin();
			
			if(!member.doLogin())
				throw (new EntityException.Builder(enumPage.LOGIN))
				.instanceMessage("비밀번호가 일치하지 않습니다.")
				.errorCode(enumMemberState.NOT_EQUAL_PASSWORD).build();
			else
				mCtl.addMember(member, member.getSessionId());
			
			if(member.verify())
				mCtl.addMember(member, member.getSessionId());
			
		}
			
	}

	private void nothingCase(Member member) throws EntityException, ControllerException, SQLException, PageException, AddressException, MessagingException{
				
		checkJoinAndPreLogin(member);

		EnumMap<enumMemberAbnormalState, Boolean> state = mDB.getMemberAbnormalStates(member.getId());
		//잠김상태인가?
		if( state.get(enumMemberAbnormalState.IS_ABNORMAL)){
		
			lockingMember(state, member);
			
		}//ABNORMAL=0. 잠김상태가 아니면 로그인을 시도한다.
		else{
			nonLockingMember( member);
		}	
		
	}
		
	private void checkJoinAndPreLogin(Member member) throws EntityException, SQLException, ControllerException{
		
		//가입여부 확인.
		if(!mDB.isJoin(member.getEmail()))
			throw (new EntityException.Builder(enumPage.JOIN))
			.errorCode(enumMemberState.NOT_JOIN).instanceMessage("이메일이 존재하지 않습니다. 가입후 사용하세요. ").build();

		//로그인 여부 확인.				
		if(mCtl.containsEntity(member.getSessionId())){
			member = mCtl.getMember(member.getSessionId());
			if(member.isLogin())
				throw (new EntityException.Builder(enumPage.MAIN))
				.errorCode(enumMemberState.ALREADY_LOGIN).instanceMessage("이메일이 존재하지 않습니다. 가입후 사용하세요. ").build();
		
		}
		
	}
	
	private void lockingMember(EnumMap<enumMemberAbnormalState, Boolean> state, Member member) throws SQLException, EntityException, PageException, AddressException, MessagingException, NumberFormatException, ControllerException{
		
		//아직 가입 인증을 안한경우.
		if(state.get(enumMemberAbnormalState.JOIN_CERTIFICATION))
			throw (new EntityException.Builder(enumPage.ENTRY))
			.errorCode(enumMemberState.NOT_JOIN_CERTIFICATION).build();
			
		//비밀번호 분실상태인가?
		//아래의 두 상태는 비밀번호 일치여부를 검사할 필요 없음.
		else if( state.get(enumMemberAbnormalState.LOST_PASSWORD) ){					
			
			List<MailManager.MailInformation> results = mailMng.getMailDateInformationOf(member.getEmail(), enumMailType.LOST_PASSWORD);
			if(results.isEmpty()){
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.instanceMessage("비밀번호 분실상태입니다. 임시 비밀번호 발급을 위해 메일을 작성 바랍니다.")
				.putString("status",enumMemberAbnormalState.LOST_PASSWORD.toString())
				.errorCode(enumMailState.NOT_SENDED_MAIL).build(); 
			}
			if(results.size()>1){
				mailMng.invalidateMailOf(member.getEmail(), enumMailType.LOST_PASSWORD);
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.instanceMessage("인증과정에 문제가 생겼습니다. 임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.putString("status",enumMemberAbnormalState.LOST_PASSWORD.toString())
				.errorCode(enumMailState.DUPLICATED_SENED_MAIL).build();
			}
			int hour = Integer.parseInt(enumMailStandard.NEEDED_HOUR_OF_MAIL_CERTIFICATION.toString());
			
			if(CommFunc.isPassingDateFromToday(results.get(0).sendedDate, hour, CommFunc.CalendarKind.HOUR)){
				mailMng.invalidateMailOf(member.getEmail(), enumMailType.LOST_PASSWORD);
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.instanceMessage("인증기한이 초과됐습니다.  임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.putString("status",enumMemberAbnormalState.LOST_PASSWORD.toString())
				.errorCode(enumMemberState.LOST_PASSWORD).build();
			}
			else{
				
				if(!member.doLogin())
					throw (new EntityException.Builder(enumPage.LOGIN))
					.errorCode(enumMemberState.NOT_EQUAL_PASSWORD).build();
				else{
					mCtl.addMember(member, member.getSessionId());
					mMng.updateMemberAbnormalState(member.getEmail(), enumMemberAbnormalState.LOST_PASSWORD, 0);
				}
					
			}

		}
		//비밀번호 초과상태인가
		else if(state.get(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT) ){
		
			final List<MailManager.MailInformation> results = mailMng.getMailDateInformationOf(member.getEmail(), enumMailType.EXCEEDED_LOGIN_COUNT);
			if(results.isEmpty()){
				final int count = Integer.parseInt( enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString()); 
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.instanceMessage("비밀번호 시도 횟수를 "+count+"회 초과 했습니다.  임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.putString("status",enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT.toString())
				.errorCode(enumMailState.NOT_SENDED_MAIL).build(); 
				
			}
			if(results.size()>1)
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.instanceMessage("인증과정에 문제가 생겼습니다. 임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.putString("status",enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT.toString())
				.errorCode(enumMailState.DUPLICATED_SENED_MAIL).build();
			
			int hour = Integer.parseInt(enumMailStandard.NEEDED_HOUR_OF_MAIL_CERTIFICATION.toString());
			
			if(CommFunc.isPassingDateFromToday(results.get(0).sendedDate, hour, CommFunc.CalendarKind.HOUR)){
				mailMng.invalidateMailOf(member.getEmail(), enumMailType.EXCEEDED_LOGIN_COUNT);
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.putString("status",enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT.toString())
				.instanceMessage("인증기한이 초과됐습니다.  임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMemberState.LOST_PASSWORD).build();
			}
			else{
				
				if(!member.doLogin())
					throw (new EntityException.Builder(enumPage.LOGIN))
					.instanceMessage("")
					.errorCode(enumMemberState.NOT_EQUAL_PASSWORD).build();
				else{
					mCtl.addMember(member, member.getSessionId());
					mMng.updateMemberAbnormalState(member.getEmail(), enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT, 0);
				}
			}
		}
		else if(state.get(enumMemberAbnormalState.SLEEP) ){
				
			final List<MailManager.MailInformation> results = mailMng.getMailDateInformationOf(member.getEmail(), enumMailType.EXCEEDED_LOGIN_COUNT);
			if(results.isEmpty()){
				
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.instanceMessage("비밀번호를 변경한지 "+enumMemberStandard.PASSWD_CHANGE_DATE_OF_MONTH.toString()+"개월 지났습니다."
						+ "\n임시 비밀번호 발급을 위해 메일을 보내주시기 바랍니다.")
				.putString("status",enumMemberAbnormalState.SLEEP.toString())
				.errorCode(enumMailState.NOT_SENDED_MAIL).build(); 
				
			}
			if(results.size()>1)
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.instanceMessage("인증과정에 문제가 생겼습니다. 임시 비밀번호 발급을 위해 메일을 보내주시기 바랍니다.")
				.putString("status",enumMemberAbnormalState.SLEEP.toString())
				.errorCode(enumMailState.DUPLICATED_SENED_MAIL).build();
			
			int hour = Integer.parseInt(enumMailStandard.NEEDED_HOUR_OF_MAIL_CERTIFICATION.toString());
			
			if(CommFunc.isPassingDateFromToday(results.get(0).sendedDate, hour, CommFunc.CalendarKind.HOUR)){
				mailMng.invalidateMailOf(member.getEmail(), enumMailType.SLEEP);
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.instanceMessage("인증기한이 초과됐습니다.  휴면계정 해제를 위한 임시 비밀번호 발급을 위해 메일을 보내주시기 바랍니다.")
				.putString("status",enumMemberAbnormalState.SLEEP.toString())
				.errorCode(enumMemberState.SLEEP).build();
			}
			else{
				
				if(!member.doLogin())
					throw (new EntityException.Builder(enumPage.LOGIN))
					.instanceMessage("")
					.errorCode(enumMemberState.NOT_EQUAL_PASSWORD).build();
				else{
					mCtl.addMember(member, member.getSessionId());
					mMng.updateMemberAbnormalState(member.getEmail(), enumMemberAbnormalState.SLEEP, 0);
				}
			}
			
		}
		else if(state.get(enumMemberAbnormalState.OLD_PASSWORD) ){
			
			final List<MailManager.MailInformation> results = mailMng.getMailDateInformationOf(member.getEmail(), enumMailType.EXCEEDED_LOGIN_COUNT);
			if(results.isEmpty()){
				final int count = Integer.parseInt( enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString()); 
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.instanceMessage("비밀번호 시도 횟수를 "+count+"회 초과 했습니다.  임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.putString("status",enumMemberAbnormalState.OLD_PASSWORD.toString())
				.errorCode(enumMailState.NOT_SENDED_MAIL).build(); 
				
			}
			if(results.size()>1)
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.putString("status",enumMemberAbnormalState.OLD_PASSWORD.toString())
				.instanceMessage("인증과정에 문제가 생겼습니다. 임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMailState.DUPLICATED_SENED_MAIL).build();
			
			
			int hour = Integer.parseInt(enumMailStandard.NEEDED_HOUR_OF_MAIL_CERTIFICATION.toString());
			
			if(CommFunc.isPassingDateFromToday(results.get(0).sendedDate, hour, CommFunc.CalendarKind.HOUR)){
				mailMng.invalidateMailOf(member.getEmail(), enumMailType.EXCEEDED_LOGIN_COUNT);
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.putString("status",enumMemberAbnormalState.OLD_PASSWORD.toString())
				.instanceMessage("인증기한이 초과됐습니다.  임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMemberState.LOST_PASSWORD).build();
			}
			else{
				
				if(!member.doLogin())
					throw (new EntityException.Builder(enumPage.LOGIN))
					.instanceMessage("")
					.errorCode(enumMemberState.NOT_EQUAL_PASSWORD).build();
				else{
					mCtl.addMember(member, member.getSessionId());
					mMng.updateMemberAbnormalState(member.getEmail(), enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT, 0);
				}
			}
		}

	}
	
	private void nonLockingMember(Member member) throws PageException, EntityException, SQLException, ControllerException {
	
		if(!member.doLogin())
			throw (new EntityException.Builder(enumPage.LOGIN))
			.instanceMessage("")
			.errorCode(enumMemberState.NOT_EQUAL_PASSWORD).build();
		else
			mCtl.addMember(member, member.getSessionId());
	}
	

}
