package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import member.enums.enumMemberState;
import member.enums.enumMemberType;
import page.enums.enumPage;
import property.ConnectMysql;
import property.ControllerException;
import property.enums.enumController;

public class MemberController {

	private static Connection conn = ConnectMysql.getConnector();
	
	private static HashMap<String, Integer> sessionIdMap = new HashMap<String, Integer>();
	private static Map<Integer, Member> memberIdMap = new HashMap<Integer, Member>();
	
	public static boolean containsMember(String sId){
		
		if(sessionIdMap.containsKey(sId))
		{
			Integer userId = sessionIdMap.get(sId);
			if(containsMember(userId)){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean containsMember(int uId){
		
		return memberIdMap.containsKey(uId);
		
	}
	

	public static Member getMember(String sId) throws SQLException, ControllerException{
		
		if(sId==null)
			throw new NullPointerException();
		
		if(sessionIdMap.containsKey(sId)){
			Integer userId = sessionIdMap.get(sId);
			if(memberIdMap.containsKey(userId)){
				return memberIdMap.get(userId);
			}
		}
		
		throw new ControllerException("비 정상적인 접근입니다.",enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
	}
	
	public static Member getMember(int uId) throws SQLException, ControllerException{
		
		if(uId<=0)
			throw new IllegalArgumentException("userId는 0보다 커야 합니다.");
				
		if(memberIdMap.containsKey(uId))
			return memberIdMap.get(uId);
		
		throw new ControllerException("비 정상적인 접근입니다.",enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
	}
	
	private static Member setMember(int uId) throws SQLException{
		
		
		PreparedStatement _ps = conn.prepareStatement("select * from user where useId = ? ");
		_ps.setInt(1, uId);
		ResultSet _rs = _ps.executeQuery();	
		_rs.next();
		
		String email = _rs.getString("email");
		return new Member.Builder(_rs.getInt("userId"), email).idType(enumMemberType.valueOf(_rs.getString("registrationKind"))  )
				.nickname(_rs.getString("nickname")).build();
		
	}
	
	private static Member setMember(String email) throws SQLException{
		
		
		PreparedStatement _ps = conn.prepareStatement("select * from user where email = ? ");
		_ps.setString(1, email);
		ResultSet _rs = _ps.executeQuery();	
		_rs.next();

		return new Member.Builder(_rs.getInt("userId"), email).idType(enumMemberType.valueOf(_rs.getString("registrationKind"))  )
				.nickname(_rs.getString("nickname")).build();
		
	}
	
	public static void addMember(Member member,String sId) throws ControllerException{
		
		if(member==null)
			throw new NullPointerException();
		
		removeMember(sId);
		
		memberIdMap.put(member.getId(), member);
		sessionIdMap.put(sId, member.getId());
		
	}
		
	public static void addMember(String email, String sId) throws SQLException, ControllerException{
		
		Member member = setMember(email);
		
		if(member==null)
			throw new NullPointerException();
		
		removeMember(sId);
		
		memberIdMap.put(member.getId(), member);
		sessionIdMap.put(sId, member.getId());
		
	}
		
	public static void addMember(int uId, String sId) throws SQLException, ControllerException{
		
		Member member = setMember(uId);
		
		if(member==null)
			throw new NullPointerException();
		
		removeMember(sId);
		
		
		memberIdMap.put(member.getId(), member);
		sessionIdMap.put(sId, uId);
		
	}
	
	public static void removeMember(String sId) throws ControllerException{
		
		if(containsMember(sId)){
			int userId = sessionIdMap.get(sId);
			if(containsMember(userId)){
				memberIdMap.remove(userId);
				sessionIdMap.remove(sId);
				return;
			}
			else
				sessionIdMap.remove(sId);
				
		}
			
		throw new ControllerException(enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
	}

	public static void removeMember(int uId) throws ControllerException{
		
		if(containsMember(uId)){
			memberIdMap.remove(uId);
			
			for(String sId : sessionIdMap.keySet())
				if(sessionIdMap.containsKey(sId))
					sessionIdMap.remove(sId);
			return;
		}
		
		throw new ControllerException(enumController.NOT_EXIST_MEMBER_FROM_MAP);
		
	}
	

	
}
