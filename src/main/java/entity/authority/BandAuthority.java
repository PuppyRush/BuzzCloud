package entity.authority;

import java.sql.Timestamp;
import java.util.EnumMap;

import entity.authority.enums.enumBandAuthority;

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

	
	
	
}
