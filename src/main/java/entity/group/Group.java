package entity.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.EnumMap;

import entity.Entity;
import entity.authority.Authority;
import entity.authority.AuthorityManager;
import entity.authority.FileAuthority;
import entity.authority.GroupAuthority;
import entity.authority.MemberAuthority;
import entity.authority.enums.enumFileAuthority;
import entity.authority.enums.enumGroupAuthority;
import entity.authority.enums.enumMemberAuthority;
import entity.member.Member;
import entity.member.MemberManager;
import entity.message.*;
import property.ConnectMysql;

public final class Group implements Entity{
	
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
	
	private boolean isFinal;
	private boolean isRoot;
	
	private int groupId;
	private int ownerId;
	private String groupName;
	private GroupAuthority groupAuthority;
	private int upperGroup;
	private ArrayList<Integer> subGroups;
	private HashMap<Integer,AuthoritedMember> members;
	

	private Group(Builder b){ 
		
		groupId = b.groupId;
		ownerId = b.ownerId;
		groupName = b.groupName;
		upperGroup = b.upperGroupId;
		subGroups = b.subGroups;
		members = b.members;		
		
	}
	
	public static class Builder{
	

		private boolean isFinal;
		private boolean isRoot;
				
		private int groupId;
		private int ownerId;
		private String groupName;
		private Timestamp createdDate;
		private GroupAuthority groupAuthority;
		private int upperGroupId;
		private ArrayList<Integer> subGroups;
		private HashMap<Integer,AuthoritedMember> members; 
		
		private MemberAuthority memberAuth;
		private FileAuthority fileAuth;
		
		public Builder(int ownerId,int groupId , String groupName) throws SQLException{ 
			
			this.isFinal = false;
			this.isRoot = false;
			this.groupId = groupId;
			this.ownerId = ownerId;
			this.groupName = groupName;
			
			subGroups = new ArrayList<Integer>();
			members = new HashMap<Integer,AuthoritedMember>();
						
			groupAuthority = AuthorityManager.getInstance().getGroupAuthority(groupId);
			MemberAuthority mAuth= AuthorityManager.getInstance().getMemberAuthority(ownerId, groupId);
			FileAuthority fAuth = AuthorityManager.getInstance().getFileAuthoirty(ownerId, groupId);
			
			Member member = MemberManager.getMember(ownerId);
			AuthoritedMember auth = new AuthoritedMember(member, mAuth, fAuth);
		
		}
					
		
	
		public Builder createdDate(Timestamp date){
			createdDate = date; return this;
		}

		public Builder groupId(int id){
			groupId = id;
			return this;
		}
		
			
		public Group build(){
			return new Group(this);
		}

		
	}
	
}
