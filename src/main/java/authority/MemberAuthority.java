package authority;

import java.sql.Timestamp;
import java.util.EnumMap;

public class MemberAuthority extends Authority{

	private enumMemberAuthority authorityType;

	public MemberAuthority(int id, Timestamp date, enumMemberAuthority type){
		super(id,date);
		authorityType = type;
	
	}

	
}
