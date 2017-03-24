package com.puppyrush.buzzcloud.entity.authority;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.interfaces.Entity;
import com.puppyrush.buzzcloud.entity.interfaces.EnumEntity;
import com.puppyrush.buzzcloud.property.ConnectMysql;

public abstract class Authority implements Entity{

	
	private int authorityId;
	private Timestamp grantedDate;

	public void setAuthorityId(int authorityId) {
		this.authorityId = authorityId;
	}

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
