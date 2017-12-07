package com.puppyrush.buzzcloud.service.manager.bandDashboard;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;

@Service("gettingCharts")
public class GettingCharts {

	@Autowired
	private DBManager dbMng;
	
	
	public Map<String,Object> excute(int bandId) throws EntityException, SQLException, ControllerException{
				
		Map<String, Object> returns = new HashMap<String, Object>();
		
		returns.putAll(getUsage(bandId));
		returns.putAll(getCreatedTime(bandId));
		return returns;
	}
	
	private Map<String, Object> getUsage(int bandId){
			
		Map<String, Object> returns = new HashMap<String, Object>();
		
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("bandId", bandId);
		sel.add("usingCapacity");
		sel.add("maxCapacity");
		List<Map<String,Object>> res = dbMng.getColumnsOfPart("bandDetail", sel, where);
		
		returns.put("usingCapacity", (int)res.get(0).get("usingCapacity"));
		returns.put("maxCapacity", (int)res.get(0).get("maxCapacity"));
		
		sel.clear();
		sel.add("name");
		res = dbMng.getColumnsOfPart("band", sel, where);
		
		
		returns.put("title", (String)res.get(0).get("name") + " capacity");
		
		return returns;	
	}
	

	private Map<String, Object> getCreatedTime(int bandId){
			
		Map<String, Object> returns = new HashMap<String, Object>();
		
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("bandId", bandId);
	
		sel.clear();
		sel.add("createdDate");
		List<Map<String,Object>> res = dbMng.getColumnsOfPart("band", sel, where);
		returns.put("createdDate", (new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분")).format( ((Timestamp)res.get(0).get("createdDate")) ).toString());
		
		List<Integer> dates = new ArrayList<Integer>();
		String str= (new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")).format( ((Timestamp)res.get(0).get("createdDate")) ).toString();
		for(String s : str.split("-")){
			dates.add(Integer.valueOf(s));		
		}
		returns.put("createdDateByLong",dates);
		return returns;	
	}
}


