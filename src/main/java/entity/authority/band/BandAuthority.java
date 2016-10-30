package entity.authority.band;

import java.sql.Timestamp;
import java.util.EnumMap;

import entity.authority.Authority;

public class BandAuthority extends Authority{

	private EnumMap<enumBandAuthority,Boolean> authorityType;
	
	public BandAuthority(int id, Timestamp date,  EnumMap<enumBandAuthority,Boolean> type){
		super(id,date);
		authorityType = type;
	}

	public EnumMap<enumBandAuthority, Boolean> getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(EnumMap<enumBandAuthority, Boolean> authorityType) {
		this.authorityType = authorityType;
	}

	public BandAuthority getMinimalAuthority(){
		
		EnumMap<enumBandAuthority,Boolean> auths = new EnumMap<>(enumBandAuthority.class);
		for(enumBandAuthority _auth : enumBandAuthority.values())
			auths.put(_auth, false);     
		
		return new BandAuthority(-1, new Timestamp(System.currentTimeMillis()), auths);
		
	}
	
	
}
