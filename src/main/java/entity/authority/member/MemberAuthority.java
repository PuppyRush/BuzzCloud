package entity.authority.member;

import java.sql.Timestamp;
import java.util.EnumMap;

import entity.authority.Authority;
import entity.authority.band.BandAuthority;
import entity.authority.band.enumBandAuthority;

public class MemberAuthority extends Authority{

	private enumMemberAuthority authorityType;

	public MemberAuthority(int id, Timestamp date, enumMemberAuthority type){
		super(id,date);
		authorityType = type;
	
	}

	public enumMemberAuthority getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(enumMemberAuthority authorityType) {
		this.authorityType = authorityType;
	}


	public MemberAuthority getMinimalAuthority(){
		
		
		return new MemberAuthority(-1, new Timestamp(System.currentTimeMillis()), enumMemberAuthority.VIEWER);
		
	}
	

}
