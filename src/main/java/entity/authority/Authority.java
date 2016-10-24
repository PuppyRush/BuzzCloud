package entity.authority;

import java.sql.Connection;
import java.sql.Timestamp;

import entity.Entity;
import property.ConnectMysql;

public class Authority implements Entity{


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
