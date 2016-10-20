package authority;

import java.sql.Timestamp;

public class Authority {

	private int authorityId;
	private Timestamp grantedDate;
	
	protected Authority(int id, Timestamp date){
		authorityId = id;
		grantedDate = date;
	}

	public Timestamp getGrantedDate() {
		return grantedDate;
	}

	public void setGrantedDate(Timestamp grantedDate) {
		this.grantedDate = grantedDate;
	}

	public int getAuthorityId() {
		return authorityId;
	}
	
}
