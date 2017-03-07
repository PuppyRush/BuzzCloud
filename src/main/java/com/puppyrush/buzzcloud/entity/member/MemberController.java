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
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.impl.EntityControllerImpl;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberType;
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
		
		throw new ControllerException("비 정상적인 접근입니다.",enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
	}
	
	
	public Member getMember(LoginForm form) throws ControllerException{
		
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
	
	private Member setMember(int uId) throws SQLException{
		
		
		PreparedStatement _ps = conn.prepareStatement("select * from user where useId = ? ");
		_ps.setInt(1, uId);
		ResultSet _rs = _ps.executeQuery();	
		_rs.next();
		
		String email = _rs.getString("email");
		return new Member.Builder(_rs.getInt("userId"), email).registrationKind(enumMemberType.valueOf(_rs.getString("registrationKind"))  )
				.nickname(_rs.getString("nickname")).build();
		
	}
	
	private Member setMember(String email) throws SQLException{
		
		
		PreparedStatement _ps = conn.prepareStatement("select * from user where email = ? ");
		_ps.setString(1, email);
		ResultSet _rs = _ps.executeQuery();	
		_rs.next();

		return new Member.Builder(_rs.getInt("userId"), email).registrationKind(enumMemberType.valueOf(_rs.getString("registrationKind"))  )
				.nickname(_rs.getString("nickname")).build();
		
	}
	
	public Member newMember(String sessionId, String email) throws ControllerException{
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
		
	
	public void addMember(String email, String sId) throws SQLException, ControllerException{
		
		Member member = setMember(email);
		
		if(member==null)
			throw new NullPointerException();

		if(containsEntity(member.getId()) == false )
			addEntity(member.getId(), member);
		
		if(containsEntity(sId) == false)
			sessionIdMap.put(sId, member.getId());
		
	}
		
	
	public void addMember(int uId, String sId) throws SQLException, ControllerException{
		
		Member member = setMember(uId);
		
		if(member==null)
			throw new NullPointerException();
		
		if(containsEntity(member.getId()) == false )
			addEntity(member.getId(), member);
		
		if(containsEntity(sId) == false)
			sessionIdMap.put(sId, member.getId());

		
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
			
		throw new ControllerException(enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
	}

	
}
