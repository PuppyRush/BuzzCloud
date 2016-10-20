package group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.EnumMap;

import authority.Authority;
import authority.FileAuthority;
import authority.GroupAuthority;
import authority.MemberAuthority;
import authority.enumFileAuthority;
import authority.enumMemberAuthority;
import member.Member;
import message.*;
import property.ConnectMysql;

public class Group {
	
	private static Connection conn = ConnectMysql.getConnector();
	
	private static class AuthoritedMember{
		
		public Member member;
		public MemberAuthority memberAuthority;
		public FileAuthority fileAuthority;
		
		public AuthoritedMember(Member m, MemberAuthority memberAuth, FileAuthority fileAuth){
			member = m;
			memberAuthority = memberAuth;
			fileAuthority = fileAuth;
		}
		
	}
	
	private int groupId;
	private Member owner;
	private String groupName;
	private GroupAuthority groupAuthority;
	private HashMap<Integer,Group> upperGroups;
	private HashMap<Integer,Group> subGroups;
	private HashMap<Integer,AuthoritedMember> members;
	

	private Group(Builder b){
		
		groupId = b.groupId;
		owner = b.owner;
		groupName = b.groupName;
		upperGroups = b.upperGroups;
		subGroups = b.subGroups;
		members = b.members;		
		
	}
	
	public static class Builder{
		
		private int groupId;
		private Member owner;
		private String groupName;
		private GroupAuthority groupAuthority;
		private HashMap<Integer,Group> upperGroups;
		private HashMap<Integer,Group> subGroups;
		private HashMap<Integer,AuthoritedMember> members; 
		
		private MemberAuthority memberAuth;
		private FileAuthority fileAuth;
		
		public Builder(Member owner, String name) throws SQLException{
			this.owner = owner;
			groupName = name;
			
			subGroups = new HashMap<Integer,Group>();
			upperGroups = new HashMap<Integer,Group>();
			members = new HashMap<Integer,AuthoritedMember>();
			
			
			setGroupId();
			setGroupAuthority();
			setMemberAuthority();
			setFileAuthority();
			
			AuthoritedMember auth = new AuthoritedMember(owner, memberAuth, fileAuth);
		
		}
		
		private void setGroupId() throws SQLException{
			
			PreparedStatement ps = conn.prepareStatement("select groupId from group where owner = ?");
			ps.setInt(1, owner.getId());
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				groupId = rs.getInt("groupId");
			else
				throw new SQLException("사용자가 속한 그룹이 없습니다.");
			
		}
		
		private void setGroupAuthority() throws SQLException{
			
			PreparedStatement ps = conn.prepareStatement("select * from groupAuthority where groupId = ?");
			ps.setInt(1, groupId);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				groupId = rs.getInt("groupId");
			else
				throw new SQLException("그룹에 해당하는 권한이 존재하지 않습니다.");
			
		}
	
		private void setMemberAuthority() throws SQLException{
			
			PreparedStatement ps = conn.prepareStatement("select * from memberAuthority where groupId = ? , userId = ?");
			ps.setInt(1, groupId);
			ps.setInt(2, owner.getId());
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				
				boolean isExist = false;
				for(enumMemberAuthority auth : enumMemberAuthority.values())
					if(auth.equals(rs.getString("memberType"))){
						memberAuth = new MemberAuthority(rs.getInt("authorityId"), rs.getTimestamp("grantedDate"), auth);
						isExist = true;
					}
				if(!isExist)
					throw new SQLException("해당하는 memberType이 없습니다.");
			

			}
			else
				throw new SQLException("그룹에 해당하는 권한이 존재하지 않습니다.");
			
		}
			
		private void setFileAuthority() throws SQLException{
			
			PreparedStatement ps = conn.prepareStatement("select * from fileAuthority where groupId = ? , userId = ?");
			ps.setInt(1, groupId);
			ps.setInt(2, owner.getId());
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
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
				
				fileAuth = new FileAuthority(rs.getInt("authorityId"), rs.getTimestamp("createdDate"), auths);
				
			}
			else
				throw new SQLException("그룹에 해당하는 권한이 존재하지 않습니다.");
			
		}
	
		private void setUpperGroup(){
			
			
			
		}
		
		private void setSubGroup(){
			
			
		}
		

		public Builder groupId(int id){
			groupId = id;
			return this;
		}
		
		public Builder upperGroups(HashMap<Integer, Group> groups){
			upperGroups = groups;
			return this;
		}
		
		public Builder subGroups(HashMap<Integer, Group> groups){
			subGroups = groups;
			return this;
		}
			
		public Group build(){
			return new Group(this);
		}

		
	}
	
}
