package com.puppyrush.buzzcloud.service.entity.member;


import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.controller.form.LoginForm;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberAbnormalState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.mail.enumMailType;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;



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
		
	public Map<String,Object> execute(LoginForm form){
				
		Map<String,Object> returns = new HashMap<String,Object>();
		
		try{
				
			Member member = mCtl.getMember(form);
			member.setSessionId(form.getSessionId());
			
			returns.putAll(doLoginAsCase(member));
					
			/*returns.put("nickname", member.getNickname());
			returns.put("id", String.valueOf(member.getId()));
			returns.put("email", member.getEmail());*/
			returns.putAll(new InstanceMessage("로그인에 성공하셨습니다.", InstanceMessageType.SUCCESS).getMessage());
		}catch(PageException e){
			e.printStackTrace();
			returns.put("is_successLogin", false);
			returns.put("view",e.getPage().toString());		
			returns.putAll(new InstanceMessage(e.getMessage(), InstanceMessageType.ERROR).getMessage());
		}catch(EntityException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			returns.put("view", e.getToPage().toString());	
			
			InstanceMessage msg = new InstanceMessage(e.getMessage(), InstanceMessageType.ERROR);
			returns.putAll(msg.getMessage());
		}catch(SQLException e){
			e.printStackTrace();
			returns.put("view", enumPage.ERROR403);
			
			InstanceMessage msg = new InstanceMessage("알수없는 오류 발생. 관리자에게 문의하세요", InstanceMessageType.ERROR);
			returns.putAll(msg.getMessage());
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			returns.put("view", enumPage.ERROR403.toString());
			InstanceMessage msg = new InstanceMessage("알수없는 오류 발생. 관리자에게 문의하세요", InstanceMessageType.ERROR);
			returns.putAll(msg.getMessage());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returns;
	}
	
	public Map<String,Object> doLoginAsCase(Member member) throws Throwable{
		
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
				throw new PageException(enumPageError.UNKNOWN_PARA_VALUE,enumPage.ERROR404);
			
		}
		
		returns.put("view", enumPage.MAIN.toString());		
		return returns;
		
	}
	
	private Map<String,Object> oauthCase(Member member) throws Throwable{
		
		
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

	private Map<String,Object> nothingCase(Member member) throws Throwable{
		
		
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
			throw new EntityException("이메일이 존재하지 않습니다. 가입후 사용하세요. ", enumMemberState.NOT_JOIN, enumPage.JOIN);
		
		//로그인 여부 확인.				
		if(mCtl.containsEntity(member.getSessionId())){
			member = mCtl.getMember(member.getSessionId());
			if(member.isLogin())
				throw new EntityException( enumMemberState.ALREADY_LOGIN, enumPage.MAIN);
		}
		
	}
	
	private void lockingMember(EnumMap<enumMemberAbnormalState, Boolean> state, Member member) throws Exception{
		
		
		//아직 가입 인증을 안한경우.
		if(state.get(enumMemberAbnormalState.JOIN_CERTIFICATION))
			throw new EntityException(enumMemberState.NOT_JOIN_CERTIFICATION, enumPage.ENTRY);
			
		//비밀번호 분실상태인가?
		//아래의 두 상태는 비밀번호 일치여부를 검사할 필요 없음.
		if( state.get(enumMemberAbnormalState.LOST_PASSWORD) ){					
			
			if(mMng.isSendedmail(member.getEmail(), enumMailType.LOST_PASSWORD))
				throw new EntityException(enumMemberState.LOST_PASSWORD, enumPage.INPUT_CERTIFICATION_NUMBER);
			else
				throw new EntityException(enumMemberState.LOST_PASSWORD, enumPage.INPUT_MAIL);
			

		}
		//비밀번호 초과상태인가
		else if(state.get(enumMemberAbnormalState.FAILD_LOGIN) ){
		
			if(mMng.isSendedmail(member.getEmail(), enumMailType.FAILED_LOGIN))
				throw new EntityException(enumMemberState.EXCEED_FAILD_LOGIN, enumPage.INPUT_CERTIFICATION_NUMBER);
			else
				throw new EntityException(enumMemberState.EXCEED_FAILD_LOGIN, enumPage.INPUT_MAIL);

		
		}
				
		//잠김 상태중에서도 아래 두가지는 확인이 필요함.
		if(state.get(enumMemberAbnormalState.LOST_PASSWORD )){
			
			member.doLogin();
			
			
		}
		
		else if(state.get(enumMemberAbnormalState.SLEEP) ){
		
			
			
		}
		
	}
	
	private void nonLockingMember(EnumMap<enumMemberAbnormalState, Boolean> state, Member member) throws Exception{
	
		Map<String,Object> returns = new HashMap<String,Object>();
		
		if(member.doLogin()==false)
			throw new EntityException(enumMemberState.NOT_EQUAL_PASSWORD, enumPage.LOGIN);	
		else{//로그인성공
			
			InstanceMessage msg = new InstanceMessage("로그인에 성공하셨습니다.", InstanceMessageType.SUCCESS);
			returns.putAll(msg.getMessage());

			returns.put("isSuccessLogin", true);
			
		}
		
		returns.put("view", enumPage.MAIN.toString());			
		
		
	}
	

}
