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
	}
	
	private int maxCapacity;
	private int usingCapacity;
	private boolean isFinal;
	private boolean isRoot;
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
		isFinal =b.isFinal;
		isRoot = b.isRoot;
		
	}
	
	public static class Builder{
	

		private boolean isFinal;
		private boolean isRoot;
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
			
			this.isFinal = false;
			this.isRoot = false;
			members = new HashMap<>();
		
			EnumMap<enumBandAuthority, Boolean> auth = new EnumMap<>(enumBandAuthority.class);
			for(enumBandAuthority auths : enumBandAuthority.values())
				auth.put(auths, false);
			bandAuthority = new BandAuthority(-1, new Timestamp(System.currentTimeMillis()), auth);
			
			subBands = null;

			
		}		

		public Builder isFinal(boolean isFinal){
			this.isFinal = isFinal;
			return this;
		}
				
		public Builder isRoot(boolean isRoot){
			this.isRoot = isRoot;
			return this;
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


	public boolean isFinal() {
		return isFinal;
	}

	public boolean isRoot() {
		return isRoot;
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
	
}
