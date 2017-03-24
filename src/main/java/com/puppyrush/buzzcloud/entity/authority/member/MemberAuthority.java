package com.puppyrush.buzzcloud.entity.authority.member;

import java.sql.Timestamp;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
	

	public static Map<enumMemberAuthority, Boolean> getMemberEnumMap(List<String> list){
		
		Map<enumMemberAuthority, Boolean> map = new EnumMap<enumMemberAuthority, Boolean>(enumMemberAuthority.class);
		
		for(enumMemberAuthority e : enumMemberAuthority.values()){
			map.put(e, false);
		}
		
		for(String s : list){
			map.put(enumMemberAuthority.valueOf(s),true);
		}
		
		return map;
	}
	
}
