package com.puppyrush.buzzcloud.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.puppyrush.buzzcloud.property.ConnectMysql;

public class AutoComplete {

	
	public List<Map<String,String>> getMembersOfPart(String nickname){
		
		List<String> partOfNicknames = new ArrayList<String>();
		

		String partOfNickname = nickname + "%";
		
			try{
			
			Connection conn = ConnectMysql.getConnector();
			PreparedStatement ps = conn.prepareStatement("select nickname from member where nickname like ?");
			ps.setString(1, partOfNickname);
			ResultSet rs = ps.executeQuery();
			
			
			
			while(rs.next()){				
				partOfNicknames.add(rs.getString(1));					
			}
	
			rs.close();
			ps.close();

		}
		catch(SQLException e){
			e.printStackTrace();	
		}
			
		List<Map<String,String>> returns = new ArrayList<Map<String,String>>();
		
		for(String str : partOfNicknames){
			Map<String,String> map = new HashMap<String,String>();
			map.put("name",str);
			returns.add(map);
		}
		
		return returns;
		
	}
}
