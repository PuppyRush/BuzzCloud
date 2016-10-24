package entity.authority;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.EnumMap;

import entity.authority.enums.enumFileAuthority;

public class FileAuthority extends Authority{

	private EnumMap<enumFileAuthority,Boolean> authorityType;
		
	public FileAuthority(int id, Timestamp date,  EnumMap<enumFileAuthority,Boolean> type){
		super(id,date);
		authorityType = type;

	}

	public EnumMap<enumFileAuthority, Boolean> getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(EnumMap<enumFileAuthority, Boolean> authorityType) {
		this.authorityType = authorityType;
	}

}
