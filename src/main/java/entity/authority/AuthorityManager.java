package entity.authority;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import entity.authority.enums.enumFileAuthority;
import entity.authority.enums.enumGroupAuthority;
import entity.authority.enums.enumMemberAuthority;
import entity.group.Group;
import property.ConnectMysql;

public class AuthorityManager {

	protected Connection conn = ConnectMysql.getConnector();
		
	private static class Singleton {
		private static final AuthorityManager instance = new AuthorityManager();
	}
	
	public static AuthorityManager getInstance () {
		return Singleton.instance;
	}

	public FileAuthority getFileAuthoirty(int userId, int groupId) {  
		
		FileAuthority fAuth = null;
		
		try{
			
			PreparedStatement ps = conn.prepareStatement("select * from fileAuthority where groupId = ? , userId = ?");
			ps.setInt(1, groupId);
			ps.setInt(2, userId);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
		
			groupId = rs.getInt("groupId");
			
			EnumMap<enumFileAuthority, Boolean> auths = new EnumMap<enumFileAuthority, Boolean>(enumFileAuthority.class);
			if(rs.getInt("canRemove")==1)
				auths.put(enumFileAuthority.REMOVE, true);
			else
				auths.put(enumFileAuthority.REMOVE, false);
			
			if(rs.getInt("canDownload")==1)
				auths.put(enumFileAuthority.DOWNLOAD, true);
			else
				auths.put(enumFileAuthority.DOWNLOAD, false);
			
			if(rs.getInt("canUpload")==1)
				auths.put(enumFileAuthority.UPLOAD, true);
			else
				auths.put(enumFileAuthority.UPLOAD, false);
			
			if(rs.getInt("canCreate")==1)
				auths.put(enumFileAuthority.CREATE, true);
			else
				auths.put(enumFileAuthority.CREATE, false);
			
			fAuth = new FileAuthority(rs.getInt("authorityId"), rs.getTimestamp("createdDate"), auths);
			
	
		}catch(SQLException e){
			
			EnumMap<enumFileAuthority, Boolean> auths = new EnumMap<enumFileAuthority, Boolean>(enumFileAuthority.class);
			auths.put(enumFileAuthority.CREATE, false);
			auths.put(enumFileAuthority.DOWNLOAD, false);
			auths.put(enumFileAuthority.REMOVE, false);
			auths.put(enumFileAuthority.UPLOAD, false);
			
			fAuth = new FileAuthority(-1, new Timestamp(System.currentTimeMillis()), auths);
			
			e.printStackTrace();
		}
		
		return fAuth;
	}
 	

	public GroupAuthority getGroupAuthority(int groupId){  
		
		GroupAuthority gAuth = null;
		
		try{
		
			PreparedStatement ps = conn.prepareStatement("select * from groupAuthority where groupId = ?");
			ps.setInt(1, groupId);
			ResultSet rs = ps.executeQuery();
		
			rs.next();
			

			groupId = rs.getInt("groupId");
			
			EnumMap<enumGroupAuthority, Boolean> auths = new EnumMap<enumGroupAuthority, Boolean>(enumGroupAuthority.class);
			
			if(rs.getInt("isFinal")==1)
				auths.put(enumGroupAuthority.FINAL, true);
			else
				auths.put(enumGroupAuthority.FINAL, false);
		
			if(rs.getInt("isRoot")==1)
				auths.put(enumGroupAuthority.ROOT, true);
			else
				auths.put(enumGroupAuthority.ROOT, false);
			
			if(rs.getInt("canJoinMember")==1)
				auths.put(enumGroupAuthority.CAN_JOIN_MEMBER, true);
			else
				auths.put(enumGroupAuthority.CAN_JOIN_MEMBER, false);
			
			gAuth = new GroupAuthority(rs.getInt("groupId"), rs.getTimestamp("createdDate"), auths );
					
			ps.close();
			rs.close();
			
		}catch(SQLException e){
						
			EnumMap<enumGroupAuthority, Boolean> auths = new EnumMap<enumGroupAuthority, Boolean>(enumGroupAuthority.class);
			auths.put(enumGroupAuthority.CAN_JOIN_MEMBER, false);
			auths.put(enumGroupAuthority.ROOT, false);
			auths.put(enumGroupAuthority.FINAL, false);
			
			gAuth = new GroupAuthority(-1, new Timestamp(System.currentTimeMillis()),auths);
			e.printStackTrace();
		}
		
		return gAuth;	
		
	}

	public MemberAuthority getMemberAuthority(int ownerId, int groupId){ 
		
		MemberAuthority mAuth = null;
		
		try{
			
			PreparedStatement ps = conn.prepareStatement("select * from memberAuthority where groupId = ? , userId = ?");
			ps.setInt(1, groupId);
			ps.setInt(2, ownerId);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			boolean isExist = false;
			for(enumMemberAuthority auth : enumMemberAuthority.values())
				if(auth.equals(rs.getString("memberType"))){
					mAuth = new MemberAuthority(rs.getInt("authorityId"), rs.getTimestamp("grantedDate"), auth);
					isExist=true;
					break;
				}
			if(!isExist)
				throw new SQLException("해당하는 memberType이 없습니다.");
			
			ps.close();
			rs.close();
		
		}catch(SQLException e){
			mAuth = new MemberAuthority(-1, new Timestamp(System.currentTimeMillis()) , enumMemberAuthority.VIWER);
			e.printStackTrace();
		}
		
		return mAuth;
	}

	public int setUpperGroup(int groupId){ 
		
		GroupAuthority gAuth = null;
		PreparedStatement ps = null;
		ResultSet rs= null;
		int upperGroupId = groupId;
		try{
			

			if(gAuth.getAuthorityType().containsKey(enumGroupAuthority.ROOT) == false)
				throw new NullPointerException("그룹의 그룹권한 변수에 ROOT혹은 FINAL권한이 map에 존재하지 않습니다.");
			
			
			ps = conn.prepareStatement("select * from groupRelation where from from = ?");
			ps.setInt(1, groupId);
			rs = ps.executeQuery();
			rs.next();
			upperGroupId = rs.getInt("to");
			
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
			
		}finally{
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return upperGroupId; 
	}
	
	public ArrayList<Integer> setSubGroup(int groupId){ 
		
		List<Integer> subGroups = new ArrayList<Integer>();
		GroupAuthority gAuth = null;
		PreparedStatement ps = null;
		ResultSet rs= null;
		
		try{

			if(gAuth.getAuthorityType().containsKey(enumGroupAuthority.FINAL) == false)
				throw new NullPointerException("그룹의 그룹권한 변수에 ROOT혹은 FINAL권한이 map에 존재하지 않습니다.");
							
			ps = conn.prepareStatement("select * from groupRelation where from from = ?");
			ps.setInt(1, groupId);
			rs = ps.executeQuery();
			while(rs.next())			
				subGroups.add(rs.getInt("to"));
			
		}catch(SQLException e){
			e.printStackTrace();
			
		}finally{
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(gAuth.getAuthorityType().get(enumGroupAuthority.ROOT) )
			isRoot = true;
		else				
			isRoot = false;				
		
		
		
	}
}
