package com.puppyrush.buzzcloud.service.band;

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

@Service("searchedBandInfo")
public class SearchedBandInfo{

	@Autowired(required=false)
	private DBManager dbMng;

	@Autowired(required=false)
	private BandManager bandMng;

	@Autowired(required=false)
	private BandDB bandDB;
	
	@Autowired(required=false)
	private MemberDB mDB;
	

	
	public Map<String, Object> execute(int bandId){
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		List<String> select = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		where.put("bandId", bandId);
		result = dbMng.getColumnsOfAll("band", where);

		int ownerId = (int) result.get(0).get("owner");
		int adminId = (int) result.get(0).get("administrator");
		int rootBandId = bandMng.getRootBandOf(bandId);

		String rootBandName = bandDB.getBandNameOf(rootBandId);
		String ownerName = mDB.getNicknameOfId(ownerId);
		String adminName = mDB.getNicknameOfId(adminId);

		select.add("contents");
		result = dbMng.getColumnsOfPart("bandDetail", select, where);

		returns.put("bandContain", result.get(0).get("contents"));
		returns.put("rootBandName", rootBandName);
		returns.put("bandOwnerName", ownerName);
		returns.put("bandAdminName", adminName);

		return returns;
		
	}
		
}
