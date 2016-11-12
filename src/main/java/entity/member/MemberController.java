package entity.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import entity.ControllerException;
import entity.enumController;
import entity.band.BandController;
import entity.impl.EntityControllerImpl;
import entity.member.enums.enumMemberState;
import entity.member.enums.enumMemberType;
import page.enums.enumPage;
import property.ConnectMysql;

public final class MemberController extends EntityControllerImpl<Member>{

	private static Connection conn = ConnectMysql.getConnector();
	
	private HashMap<String, Integer> sessionIdMap = new HashMap<String, Integer>();
	
	private static class Singleton {
		private static final MemberController instance = new MemberController();
	}
	
	public static MemberController getInstance () {
		return Singleton.instance;
	}
	
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
	
	public Member getMember(String sId) throws SQLException, ControllerException{
		
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
	
	public void addMember(Member member,String sId) throws ControllerException{
		
		if(member==null)
			throw new NullPointerException();
		
		if(containsEntity(member.getId()) == false )
			addEntity(member.getId(), member);
		
		entityMap.put(member.getId(), member);
		sessionIdMap.put(sId, member.getId());
		
	}
		
	public void addMember(String email, String sId) throws SQLException, ControllerException{
		
		Member member = setMember(email);
		
		if(member==null)
			throw new NullPointerException();

		if(containsEntity(member.getId()) == false )
			addEntity(member.getId(), member);
		
		
		addEntity(member.getId(), member);
		entityMap.put(member.getId(), member);
		sessionIdMap.put(sId, member.getId());
		
	}
		
	public void addMember(int uId, String sId) throws SQLException, ControllerException{
		
		Member member = setMember(uId);
		
		if(member==null)
			throw new NullPointerException();
		
		removeMember(sId);
		
		
		entityMap.put(member.getId(), member);
		sessionIdMap.put(sId, uId);
		
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
