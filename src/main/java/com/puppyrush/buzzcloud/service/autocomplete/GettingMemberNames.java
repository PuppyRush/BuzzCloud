package com.puppyrush.buzzcloud.service.autocomplete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("getMemberNames")
public class GettingMemberNames {

	
	public List<Map<String,Object>> excute(String nickname){
		
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
			
		List<Map<String,Object>> returns = new ArrayList<Map<String,Object>>();
		
		for(String str : partOfNicknames){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name",str);
			returns.add(map);
		}
		
		return returns;
		
	}
}
