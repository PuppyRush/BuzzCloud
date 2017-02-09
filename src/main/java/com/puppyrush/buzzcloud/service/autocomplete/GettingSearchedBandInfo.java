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

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.property.ConnectMysql;

@Service("getSearhcedBandInfo")
public class GettingSearchedBandInfo {

	@Autowired
	private DBManager dbMng;
	
	@Autowired
	private BandManager bandMng;
	
	@Autowired
	private BandDB bandDB;
	
	@Autowired
	private MemberDB memberDB;
	
	
	public Map<String,Object> excute(int bandId){
		
		Map<String, Object> returns = new HashMap<String, Object>();

		ArrayList<String> select = new ArrayList<String>();
		HashMap<String, Object> where = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			
		where.put("bandId", bandId);
		result = dbMng.getColumnsOfAll("band", where);
		
		int ownerId = (int)result.get(0).get("owner");
		int adminId = (int)result.get(0).get("administrator");
		int rootBandId = bandMng.getRootBandOf(bandId);
		
		
		String rootBandName = bandDB.getBandNameOf(rootBandId);
		String ownerName = memberDB.getNicknameOfId(ownerId);
		String adminName = memberDB.getNicknameOfId(adminId);
		
		select.add("contents");
		result = dbMng.getColumnsOfPart("bandDetail", select, where);
		
		returns.put("bandContain", result.get(0).get("contents"));
		returns.put("rootBandName", rootBandName);
		returns.put("bandOwnerName", ownerName);
		returns.put("bandAdminName", adminName);
		
		return returns;
		
	}
}
