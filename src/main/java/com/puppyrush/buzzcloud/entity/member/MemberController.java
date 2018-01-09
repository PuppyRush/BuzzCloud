package com.puppyrush.buzzcloud.entity.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.puppyrush.buzzcloud.controller.form.LoginForm;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.impl.EntityControllerImpl;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("memberController")
public final class MemberController extends EntityControllerImpl<Member>{

	private static Connection conn = ConnectMysql.getConnector();
	
	@Autowired(required=false)
	private MemberDB mDB;
	
	
	private HashMap<String, Integer> sessionIdMap = new HashMap<String, Integer>();

	public boolean containsEntity(String sId){
		
		if(sessionIdMap.containsKey(sId))
		{
			Integer userId = sessionIdMap.get(sId);
			if(containsEntity(userId)){
				return true;
			}
		}
		
		return false;
	}
	
	public Member getMember(String sId) throws ControllerException{
		
		
		if(sId==null)
			throw new NullPointerException();
		
		if(sessionIdMap.containsKey(sId)){
			Integer userId = sessionIdMap.get(sId);
			if(entityMap.containsKey(userId)){
				return entityMap.get(userId);
			}
		}			
		
		throw (new ControllerException.Builder(enumPage.ERROR404))
		.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build(); 
		
	}
	
	
	public Member getMember(LoginForm form) throws ControllerException, EntityException, SQLException{
		
		if(form.getSessionId()==null)
			throw new NullPointerException();
		
		Member member; 
		if(containsEntity(form.getSessionId())){
			member = getMember(form.getSessionId());
			member.setPlanePassword(form.getPassword());			
		}
		else{
			member = mDB.getMember(form.getEmail());
			member.setPlanePassword(form.getPassword());
						
			addMember(member, form.getSessionId());
			 
		}	
		
		return member;
		
	}
		
	public Member newMember(String sessionId, String email) throws Throwable{
		Member member = null;
		if(containsEntity(sessionId)){
			member = getMember(sessionId);
						
		}
		else{
			member = mDB.getMember(email);						
			addMember(member, sessionId);
			 
		}
		
		if(member==null)
			throw new NullPointerException();
		
		return member;
	}
	
	
	public void addMember(Member member,String sId) throws ControllerException{
		
		if(member==null)
			throw new NullPointerException();
		
		if(containsEntity(member.getId()) == false )
			addEntity(member.getId(), member);
		
		if(containsEntity(sId) == false)
			sessionIdMap.put(sId, member.getId());
		
	}
		
	
	public Member addMember(String email, String sId) throws SQLException, ControllerException, EntityException{
		
		Member member = mDB.getMember(email);
		
		if(member==null)
			throw new NullPointerException();

		if(containsEntity(member.getId()) == false )
			addEntity(member.getId(), member);
		
		if(containsEntity(sId) == false)
			sessionIdMap.put(sId, member.getId());
	
		return member;
	}
		
	
	public Member addMember(int uId, String sId) throws SQLException, ControllerException{
		

	
		if(!containsEntity(uId)){
			Member member = mDB.getMember(uId);
			
			addEntity(member.getId(), member);
		
			if(!containsEntity(sId))
				sessionIdMap.put(sId, member.getId());
			
			return member;
		}
		else
			return getEntity(uId);
	}
	
	
	public void removeMember(String sId) throws ControllerException{
		
		if(containsEntity(sId)){
			int userId = sessionIdMap.get(sId);
			if(containsEntity(userId)){
				entityMap.remove(userId);
				sessionIdMap.remove(sId);
				return;
			}
			else
				sessionIdMap.remove(sId);
				
		}
		else	
			throw (new ControllerException.Builder(enumPage.ERROR404))
			.errorCode(enumController.NOT_EXIST_MEMBER_FROM_MAP).build(); 
		
	}

	
}
