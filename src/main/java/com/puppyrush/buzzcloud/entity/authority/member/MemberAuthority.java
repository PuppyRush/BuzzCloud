package com.puppyrush.buzzcloud.entity.authority.member;

import java.sql.Timestamp;
import java.util.EnumMap;

import org.springframework.stereotype.Repository;

import com.puppyrush.buzzcloud.entity.authority.Authority;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;

//@Repository("memberAuthority")
public final class MemberAuthority extends Authority{

	private enumMemberAuthority authorityType;

	public MemberAuthority(int id, Timestamp date, enumMemberAuthority type){
		super(id,date);
		authorityType = type;
	
	}

	public enumMemberAuthority getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(enumMemberAuthority authorityType) {
		this.authorityType = authorityType;
	}


	

}
