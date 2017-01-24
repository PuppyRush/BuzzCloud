package com.puppyrush.buzzcloud.entity.authority.file;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;

import com.puppyrush.buzzcloud.entity.authority.Authority;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;

public class FileAuthority extends Authority{

	private EnumMap<enumFileAuthority,Boolean> authorityType;
		
	public FileAuthority(int id, Timestamp date,  EnumMap<enumFileAuthority,Boolean> type){
		super(id,date);
		authorityType = type;

	}

	public EnumMap<enumFileAuthority, Boolean> getAuthorityType() {
		return authorityType;
	}


	public ArrayList<enumFileAuthority> toArray(){
		
		ArrayList<enumFileAuthority> ary= new ArrayList<enumFileAuthority>();
			
		for(enumFileAuthority auth : authorityType.keySet()){
			if(authorityType.get(auth) )
				ary.add(auth);
		}
			
		return ary;
		
	}
	
	public void setAuthorityType(EnumMap<enumFileAuthority, Boolean> authorityType) {
		this.authorityType = authorityType;
	}
	
	public FileAuthority getMinimalAuthority(){
		
		EnumMap<enumFileAuthority,Boolean> auths = new EnumMap<>(enumFileAuthority.class);
		for(enumFileAuthority _auth : enumFileAuthority.values())
			auths.put(_auth, false);     
		
		return new FileAuthority(-1, new Timestamp(System.currentTimeMillis()), auths);
		
	}
	
}
