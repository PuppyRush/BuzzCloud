package com.puppyrush.buzzcloud.service.autocomplete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("getBandNames")
public class GettingBandNames {

	Connection conn = ConnectMysql.getConnector();
	
	private class SimpleBand{
		
		private String name;
		private int id;
		
	}
	
	@Autowired
	private BandManager bandMng;
	
	public List<Map<String,Object>> excute(String name){
		
		List<Map<String,Object>> returns = new ArrayList<Map<String,Object>>();
		
		try{
			
			for(SimpleBand simple : getLikeNames(name)){
				
				int rootBandId = bandMng.getRootBandOf(simple.id);
				
				PreparedStatement ps = conn.prepareStatement("select name from band where bandId = ?");
				ps.setInt(1, rootBandId);
				ResultSet rs = ps.executeQuery();
				rs.next();
				String rootBandName = rs.getString(1);
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name",simple.name);
				map.put("root", rootBandName);
				map.put("selectedBandId",simple.id);
				map.put("rootBandId",rootBandId);
				returns.add(map);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return returns;
		
	}
	
	
	private List<SimpleBand> getLikeNames(String pattern){
		
		List<SimpleBand> nameArray = new ArrayList<SimpleBand>();
	

		String partOfNickname = pattern + "%";
		
		try{	
			
			PreparedStatement ps = conn.prepareStatement("select name,bandId from band where name like ?");
			ps.setString(1, partOfNickname);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){				
				SimpleBand simple = new SimpleBand();
				simple.name = rs.getString(1);
				simple.id = rs.getInt(2);
				nameArray.add(simple);					
			}
	
			rs.close();
			ps.close();

		}
		catch(SQLException e){
			e.printStackTrace();	
		}
			
		return nameArray;
		
	}
	
}
