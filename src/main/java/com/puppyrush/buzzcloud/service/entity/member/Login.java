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
			
			returns.putAll(doLoginAsCase(member));
					
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
	
	public Map<String,Object> doLoginAsCase(Member member) throws PageException, AddressException, EntityException, ControllerException, SQLException, MessagingException{
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		switch(member.getUserType()){
			case NOTHING:
				
				returns.putAll(nothingCase(member));
			
				break;
				
			case NAVER:
			case GOOGLE:
				
				returns.putAll(oauthCase(member));
			
				break;
				
			default:
				throw (new PageException.Builder(enumPage.ERROR404))
				.errorString("비 정상적인 접근입니다.")
				.errorCode(enumPageError.UNKNOWN_PARA_VALUE).build(); 
			
		}
		
		returns.put("view", enumPage.MAIN.toString());		
		return returns;
		
	}
	
	private Map<String,Object> oauthCase(Member member) throws PageException, SQLException, ControllerException{
		
		
		Map<String, Object> returns = new HashMap<String, Object>();
	
		if(mDB.isJoin(member.getEmail()))
			member.doLogin();
			if(member.verify())
				mCtl.addMember(member, member.getSessionId());
		else{
			member.doJoin();
			member.doLogin();
			if(member.verify())
				mCtl.addMember(member, member.getSessionId());
			
		}
			
		return returns;
	}

	private Map<String,Object> nothingCase(Member member) throws EntityException, ControllerException, SQLException, PageException, AddressException, MessagingException{
		
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		checkJoinAndPreLogin(member);

		EnumMap<enumMemberAbnormalState, Boolean> state = mDB.getMemberAbnormalStates(member.getId());
		//잠김상태인가?
		if( state.containsValue(true)){
		
			lockingMember(state, member);
			
		}//ABNORMAL=0. 잠김상태가 아니면 로그인을 시도한다.
		else{
		
			nonLockingMember(state, member);
			
		}	
		
		
		return returns;
		
	}
		
	private void checkJoinAndPreLogin(Member member) throws EntityException, SQLException, ControllerException{
		
		//가입여부 확인.
		if(!mDB.isJoin(member.getEmail()))
			throw (new EntityException.Builder(enumPage.JOIN))
			.errorCode(enumMemberState.NOT_JOIN).errorString("이메일이 존재하지 않습니다. 가입후 사용하세요. ").build();

		//로그인 여부 확인.				
		if(mCtl.containsEntity(member.getSessionId())){
			member = mCtl.getMember(member.getSessionId());
			if(member.isLogin())
				throw (new EntityException.Builder(enumPage.MAIN))
				.errorCode(enumMemberState.ALREADY_LOGIN).errorString("이메일이 존재하지 않습니다. 가입후 사용하세요. ").build();
		
		}
		
	}
	
	private void lockingMember(EnumMap<enumMemberAbnormalState, Boolean> state, Member member) throws SQLException, EntityException, PageException, AddressException, MessagingException{
		
		
		//아직 가입 인증을 안한경우.
		if(state.get(enumMemberAbnormalState.JOIN_CERTIFICATION))
			throw (new EntityException.Builder(enumPage.ENTRY))
			.errorCode(enumMemberState.NOT_JOIN_CERTIFICATION).build();
			
		//비밀번호 분실상태인가?
		//아래의 두 상태는 비밀번호 일치여부를 검사할 필요 없음.
		else if( state.get(enumMemberAbnormalState.LOST_PASSWORD) ){					
			
			List<MailManager.MailInformation> results = mailMng.getMailDateInformationOf(member.getEmail(), enumMailType.LOST_PASSWORD);
			if(results.isEmpty()){
				throw (new EntityException.Builder(enumPage.LOST_PASSWORD))
				.errorString("비밀번호 분실상태입니다. 임시 비밀번호 발급을 위해 메일을 작성 바랍니다.")
				.errorCode(enumMailState.NOT_SENDED_MAIL).build(); 
			}
			if(results.size()>1){
				mailMng.invalidateMailOf(member.getEmail(), enumMailType.LOST_PASSWORD);
				throw (new EntityException.Builder(enumPage.LOST_PASSWORD))
				.errorString("인증과정에 문제가 생겼습니다. 임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMailState.DUPLICATED_SENED_MAIL).build();
			}
			int hour = Integer.parseInt(enumMailStandard.NEEDED_HOUR_OF_MAIL_CERTIFICATION.toString());
			
			if(CommFunc.isPassingDateFromToday(results.get(0).sendedDate, hour, CommFunc.CalendarKind.HOUR)){
				mailMng.invalidateMailOf(member.getEmail(), enumMailType.LOST_PASSWORD);
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.errorString("인증기한이 초과됐습니다.  임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMemberState.LOST_PASSWORD).build();
			}
			else{
				member.doLogin();
			}

		}
		//비밀번호 초과상태인가
		else if(state.get(enumMemberAbnormalState.EXCEEDED_LOGIN_COUNT) ){
		
			final List<MailManager.MailInformation> results = mailMng.getMailDateInformationOf(member.getEmail(), enumMailType.EXCEEDED_LOGIN_COUNT);
			if(results.isEmpty()){
				final int count = Integer.parseInt( enumMemberStandard.POSSIBILLTY_FAILD_LOGIN_NUM.toString()); 
				mMng.setLostPassword(member.getEmail());
				throw (new EntityException.Builder(enumPage.LOST_PASSWORD))
				.errorString("비밀번호 시도 횟수를 "+count+"회 초과 했습니다.  임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMailState.NOT_SENDED_MAIL).build(); 
				
			}
			if(results.size()>1)
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.errorString("인증과정에 문제가 생겼습니다. 임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMailState.DUPLICATED_SENED_MAIL).build();
			
			int hour = Integer.parseInt(enumMailStandard.NEEDED_HOUR_OF_MAIL_CERTIFICATION.toString());
			
			if(CommFunc.isPassingDateFromToday(results.get(0).sendedDate, hour, CommFunc.CalendarKind.HOUR)){
				mailMng.invalidateMailOf(member.getEmail(), enumMailType.EXCEEDED_LOGIN_COUNT);
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.errorString("인증기한이 초과됐습니다.  임시 비밀번호 발금을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMemberState.LOST_PASSWORD).build();
			}
			else{
				member.doLogin();
			}
		}
		else if(state.get(enumMemberAbnormalState.SLEEP) ){
				
			final List<MailManager.MailInformation> results = mailMng.getMailDateInformationOf(member.getEmail(), enumMailType.EXCEEDED_LOGIN_COUNT);
			if(results.isEmpty()){
				
				mMng.setLostPassword(member.getEmail());
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.errorString("계정이 휴면상태입니다.  임시 비밀번호 발급을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMailState.NOT_SENDED_MAIL).build(); 
				
			}
			if(results.size()>1)
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.errorString("인증과정에 문제가 생겼습니다. 임시 비밀번호 발급을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMailState.DUPLICATED_SENED_MAIL).build();
			
			int hour = Integer.parseInt(enumMailStandard.NEEDED_HOUR_OF_MAIL_CERTIFICATION.toString());
			
			if(CommFunc.isPassingDateFromToday(results.get(0).sendedDate, hour, CommFunc.CalendarKind.HOUR)){
				mailMng.invalidateMailOf(member.getEmail(), enumMailType.SLEEP);
				throw (new EntityException.Builder(enumPage.INPUT_MAIL))
				.errorString("인증기한이 초과됐습니다.  휴면계정 해제를 위한 임시 비밀번호 발급을 위해 메일을 보내주시기 바랍니다.")
				.errorCode(enumMemberState.SLEEP).build();
			}
			else{
				member.doLogin();
			}
			
		}
		
	}
	
	private void nonLockingMember(EnumMap<enumMemberAbnormalState, Boolean> state, Member member) throws PageException, EntityException {
	
		Map<String,Object> returns = new HashMap<String,Object>();
		
		if(member.doLogin()==false)
			throw (new EntityException.Builder(enumPage.LOGIN))
			.errorString("인증과정에 문제가 생겼습니다. 임시 비밀번호 발급을 위해 메일을 보내주시기 바랍니다.")
			.errorCode(enumMemberState.NOT_EQUAL_PASSWORD).build();	
		else{//로그인성공
			
			InstanceMessage msg = new InstanceMessage("로그인에 성공하셨습니다.", enumInstanceMessage.SUCCESS);
			returns.putAll(msg.getMessage());

			returns.put("isSuccessLogin", true);
			
		}
		
		returns.put("view", enumPage.MAIN.toString());			
		
		
	}
	

}
