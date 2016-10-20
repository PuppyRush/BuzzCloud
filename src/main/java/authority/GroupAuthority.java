package authority;

import java.sql.Timestamp;
import java.util.EnumMap;

public class GroupAuthority extends Authority{

	private EnumMap<enumGroupAuthority,Boolean> authorityType;
	
	public GroupAuthority(int id, Timestamp date,  EnumMap<enumGroupAuthority,Boolean> type){
		super(id,date);
		authorityType = type;
	}


}
