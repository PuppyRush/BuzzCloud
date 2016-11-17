package entity.band;

import java.sql.Connection;

import java.sql.Timestamp;
import java.util.HashMap;

import java.util.EnumMap;

import entity.authority.band.BandAuthority;
import entity.authority.band.enumBandAuthority;
import entity.authority.file.FileAuthority;
import entity.authority.member.MemberAuthority;
import entity.interfaces.Entity;
import entity.member.Member;
import property.ConnectMysql;
import property.tree.Tree;

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
	
	private String driverPath;
	private int maxCapacity;
	private int usingCapacity;
	private int bandId;
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
	
	}
	
	public static class Builder{
	
		private String driverPath;
		private int maxCapacity;
		private int usingCapacity;
		private int bandId;
		private int ownerId;
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
	
}
