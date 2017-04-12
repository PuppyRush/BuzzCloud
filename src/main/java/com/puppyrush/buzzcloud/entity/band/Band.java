package com.puppyrush.buzzcloud.entity.band;

import java.sql.Connection;

import java.sql.Timestamp;
import java.util.HashMap;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.FileAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.MemberAuthority;
import com.puppyrush.buzzcloud.entity.interfaces.Entity;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.property.ConnectMysql;
import com.puppyrush.buzzcloud.property.tree.Tree;

import java.util.EnumMap;

//@Repository("band")
public final class Band implements Entity{
	
	private static Connection conn = ConnectMysql.getConnector();
	

	public static class BundleBand{
		public Band toBand;
		public Band fromBand;
		
		public BundleBand(Band to, Band from){
			toBand = to;
			fromBand = from;
		}
	}
	
	public static class AuthoritedMember{
		
		public Member member;
		public MemberAuthority memberAuthority;
		public FileAuthority fileAuthority;
		
		public AuthoritedMember(Member m, MemberAuthority memberAuth, FileAuthority fileAuth){
			member = m;
			memberAuthority = memberAuth;
			fileAuthority = fileAuth;
		}

		public Member getMember() {
			return member;
		}

		public void setMember(Member member) {
			this.member = member;
		}

		public MemberAuthority getMemberAuthority() {
			return memberAuthority;
		}

		public void setMemberAuthority(MemberAuthority memberAuthority) {
			this.memberAuthority = memberAuthority;
		}

		public FileAuthority getFileAuthority() {
			return fileAuthority;
		}

		public void setFileAuthority(FileAuthority fileAuthority) {
			this.fileAuthority = fileAuthority;
		}
	}
	
	private String driverNickname;
	private String driverPath;
	private int maxCapacity;
	private int usingCapacity;
	private int bandId;
	private int adminId;
	private String contents;
	
	private int ownerId;
	private String bandName;
	private BandAuthority bandAuthority;
	private int upperBandId;
	private Tree<Band> subBands;
	private HashMap<Integer,AuthoritedMember> members;
	

	private Band(Builder b){ 

		maxCapacity = b.maxCapacity;
		usingCapacity = b.usingCapacity;
		bandId = b.bandId;
		ownerId = b.ownerId;
		bandName = b.bandName;
		upperBandId = b.upperBandId;
		subBands = b.subBands;
		members = b.members;		
		driverPath = b.driverPath;
		adminId = b.adminId;
		this.contents = b.contents;
		this.bandAuthority = b.bandAuthority;
		this.driverNickname = b.driverNickname;
		
	}
	
	public static class Builder{
	
		private String driverNickname;
		private String driverPath;
		private int maxCapacity;
		private int usingCapacity;
		private int bandId;
		private int ownerId;
		private int adminId;
		private String contents;
		private String bandName;
		private int upperBandId;
		private BandAuthority bandAuthority;
		private Tree<Band> subBands;
		private HashMap<Integer,AuthoritedMember> members; 

		public Builder(){
			
			
		}
		
		public Builder(int bandId,int ownerId, String bandName){ 
			

			this.bandId = bandId;
			this.ownerId = ownerId;
			this.bandName = bandName;

			members = new HashMap<>();
		
			EnumMap<enumBandAuthority, Boolean> auth = new EnumMap<>(enumBandAuthority.class);
			for(enumBandAuthority auths : enumBandAuthority.values())
				auth.put(auths, false);
			bandAuthority = new BandAuthority(-1, new Timestamp(System.currentTimeMillis()), auth);
			
			subBands = null;

			
		}		

		public Builder maxCapacity(int max){
			this.maxCapacity = max;
			return this;
		}
		
		public Builder usingCapacity(int using){
			this.usingCapacity = using;
			return this;
		}
		

		public Builder subBands(Tree<Band> subBands){
			this.subBands = subBands;			
			return this;
		}

		public Builder upperBandId(int upperBandId){
			this.upperBandId = upperBandId; 
			return this;
		}

		public Builder driverPath(String path){
			this.driverPath = path; 
			return this;
		}
		
		public Builder driverNickname(String name){
			this.driverNickname = name; 
			return this;
		}
		
		public Builder adminId(int id){
			this.adminId = id; 
			return this;
		}
		
		public Builder contents(String contents){
			this.contents = contents;
			return this;
		}
		
		
		public Builder bandAuhority(BandAuthority bandAuthority){
			this.bandAuthority = bandAuthority;
			return this;
		}
		public Builder members(HashMap<Integer,AuthoritedMember> members){
			this.members = members;
			return this;
		}

		public Band build(){
			return new Band(this);
		}

		
	}


	public int getBandId() {
		return bandId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public String getBandName() {
		return bandName;
	}

	public BandAuthority getBandAuthority() {
		return bandAuthority;
	}

	public int getUpperBandId() {
		return upperBandId;
	}

	public Tree<Band> getSubBands() {
		return subBands;
	}

	public HashMap<Integer, AuthoritedMember> getMembers() {
		return members;
	}

	public void addMember(AuthoritedMember am){
		members.put(am.getMember().getId(), am);
	}
	public void removeMember(Integer id){
		members.remove(id);
	}
	
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public void setUsingCapacity(int usingCapacity) {
		this.usingCapacity = usingCapacity;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public void setBandName(String bandName) {
		this.bandName = bandName;
	}

	public void setBandAuthority(BandAuthority bandAuthority) {
		this.bandAuthority = bandAuthority;
	}

	public void setUpperBandId(int upperBandId) {
		this.upperBandId = upperBandId;
	}

	public void setSubBands(Tree<Band> subBands) {
		this.subBands = subBands;
	}

	public void setMembers(HashMap<Integer, AuthoritedMember> members) {
		this.members = members;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}
	
	
	public String getDriverNickname() {
		return driverNickname;
	}

	public void setDriverNickname(String driverNickname) {
		this.driverNickname = driverNickname;
	}
	
	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public int getUsingCapacity() {
		return usingCapacity;
	}
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
