package com.puppyrush.buzzcloud.entity.authority.band;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.puppyrush.buzzcloud.entity.authority.Authority;

//@Repository("bandAuthority")
final public class BandAuthority extends Authority{

	private EnumMap<enumBandAuthority,Boolean> authorityType;
	
	public BandAuthority(int id, Timestamp date,  EnumMap<enumBandAuthority,Boolean> type){
		super(id,date);
		authorityType = type;
	}

	public EnumMap<enumBandAuthority, Boolean> getAuthorityType() {
		return authorityType;
	}

	public ArrayList<enumBandAuthority> toArray(){
		
		ArrayList<enumBandAuthority> ary= new ArrayList<enumBandAuthority>();
			
		for(enumBandAuthority auth : authorityType.keySet()){
			if(authorityType.get(auth) )
				ary.add(auth);
		}
			
		return ary;
		
	}
	
	public void setAuthorityType(EnumMap<enumBandAuthority, Boolean> authorityType) {
		this.authorityType = authorityType;
	}

	public BandAuthority getMinimalAuthority(){
		
		EnumMap<enumBandAuthority,Boolean> auths = new EnumMap<>(enumBandAuthority.class);
		for(enumBandAuthority _auth : enumBandAuthority.values())
			auths.put(_auth, false);     
		
		
		
		return new BandAuthority(-1, new Timestamp(System.currentTimeMillis()), auths);
		
	}
	
	
	
	
}
