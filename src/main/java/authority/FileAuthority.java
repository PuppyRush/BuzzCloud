package authority;

import java.sql.Timestamp;
import java.util.EnumMap;

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
