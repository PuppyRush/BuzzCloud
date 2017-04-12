package com.puppyrush.buzzcloud.service.manager.bandDashboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.CipherInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.property.ConnectMysql;
import com.puppyrush.buzzcloud.property.PathUtils;
import com.puppyrush.buzzcloud.property.tree.Node;
import com.puppyrush.buzzcloud.property.tree.Tree;

@Service("gettingCharts")
public class GettingCharts {

	@Autowired
	private DBManager dbMng;
	
	@Autowired
	private BandManager bMng;
	
	@Autowired
	private BandDB bDB;
	
	@Autowired
	private BandController bCtl;
	
	@Autowired
	private MemberDB mDB;
	
	@Autowired
	private MemberController mCtl;
	
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
		sel.add("createdDate");
		res = dbMng.getColumnsOfPart("band", sel, where);
		returns.put("createdDate", (Timestamp)res.get(0).get("createdDate"));
		
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
		returns.put("createdDate", (Timestamp)res.get(0).get("createdDate"));
		
		return returns;	
	}
}


