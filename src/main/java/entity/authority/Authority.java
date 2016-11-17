package entity.authority;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.EnumMap;

import entity.authority.band.BandAuthority;
import entity.authority.band.enumBandAuthority;
import entity.interfaces.Entity;
import entity.interfaces.EnumEntity;
import property.ConnectMysql;

public abstract class Authority implements Entity{

	
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
