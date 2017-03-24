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
		
		List<List<Object>> partOfNicknames = new ArrayList<List<Object>>();
		

		String partOfNickname = nickname + "%";
		
			try{
			
			Connection conn = ConnectMysql.getConnector();
			PreparedStatement ps = conn.prepareStatement("select nickname, memberId from member where nickname like ?");
			ps.setString(1, partOfNickname);
			ResultSet rs = ps.executeQuery();
						
			while(rs.next()){				
				List<Object> ary = new ArrayList<Object>();
				ary.add(rs.getString("nickname") );
				ary.add(rs.getInt("memberId"));
				partOfNicknames.add(ary);
			}
	
			rs.close();
			ps.close();

		}
		catch(SQLException e){
			e.printStackTrace();	
		}
			
		List<Map<String,Object>> returns = new ArrayList<Map<String,Object>>();
		
		for(List<Object> ary : partOfNicknames){
			Map<String,Object> map = new HashMap<String,Object>();
			for(int i=0; i < ary.size() ; i++){
				map.put("name",(String)ary.get(0));
				map.put("id",(int)ary.get(1));
			}
			returns.add(map);
		}
		
		return returns;
		
	}
}
