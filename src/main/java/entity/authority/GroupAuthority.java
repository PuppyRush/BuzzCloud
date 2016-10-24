package entity.authority;

import java.sql.Timestamp;
import java.util.EnumMap;

import entity.authority.enums.enumGroupAuthority;

public class GroupAuthority extends Authority{

	private EnumMap<enumGroupAuthority,Boolean> authorityType;
	
	public GroupAuthority(int id, Timestamp date,  EnumMap<enumGroupAuthority,Boolean> type){
		super(id,date);
		authorityType = type;
	}

	public EnumMap<enumGroupAuthority, Boolean> getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(EnumMap<enumGroupAuthority, Boolean> authorityType) {
		this.authorityType = authorityType;
	}

	
	
	
}
