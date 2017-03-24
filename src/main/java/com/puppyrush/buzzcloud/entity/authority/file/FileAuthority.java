package com.puppyrush.buzzcloud.entity.authority.file;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.puppyrush.buzzcloud.entity.authority.Authority;

//@Repository("fileAuthority")
public class FileAuthority extends Authority{

	private Map<enumFileAuthority,Boolean> authorityType;
		
	public FileAuthority(int id, Timestamp date,  Map<enumFileAuthority,Boolean> type){
		super(id,date);
		authorityType = type;

	}

	public Map<enumFileAuthority, Boolean> getAuthorityType() {
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

	
	public static Map<enumFileAuthority, Boolean> getFileEnumMap(List<String> list){
		
		Map<enumFileAuthority, Boolean> map = new EnumMap<enumFileAuthority, Boolean>(enumFileAuthority.class);
		
		for(enumFileAuthority e : enumFileAuthority.values()){
			map.put(e, false);
		}
		
		for(String s : list){
			map.put(enumFileAuthority.valueOf(s),true);
		}
		
		return map;
	}
	
}
