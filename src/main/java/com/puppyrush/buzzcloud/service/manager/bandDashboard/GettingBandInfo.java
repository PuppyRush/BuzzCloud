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
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.property.ConnectMysql;
import com.puppyrush.buzzcloud.property.tree.Node;
import com.puppyrush.buzzcloud.property.tree.Tree;

@Service("gettingBandInfo2")
public class GettingBandInfo {

	@Autowired
	private DBManager dbMng;
	
	@Autowired
	private BandManager bandMng;
	
	@Autowired
	private BandDB bandDB;
	
	@Autowired
	private MemberDB memberDB;
	
	
	public Map<String,Object> excute(int bandId) throws EntityException, SQLException{
		
		Map<String, Object> returns = new HashMap<String, Object>();

		ArrayList<String> select = new ArrayList<String>();
		HashMap<String, Object> where = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			
		where.put("bandId", bandId);
		result = dbMng.getColumnsOfAll("band", where);
		
		int adminId = (int)result.get(0).get("administrator");
		Timestamp createdDate = (Timestamp)result.get(0).get("createdDate");
		String adminName = memberDB.getNicknameOfId(adminId);
		
		select.add("contents");
		result = dbMng.getColumnsOfPart("bandDetail", select, where);
		String contents = (String)result.get(0).get("contents");
		
		Tree<Band> bandTree = bandMng.getSubBands(bandId);
		
		
		returns.put("bandContain", contents);
		returns.put("bandAdminName", adminName);
		returns.put("bandCreatedDate", createdDate.toString());
		returns.put("subBandNumber", bandTree.getDatas().size());
		returns.put("bandMembersAll", getAllBandMemers(bandTree));
		return returns;
		
	}
	

	
	private int getAllBandMemers(Tree<Band> tree) throws SQLException{
		
		int count=0;
		for(BundleBand band : tree.getSubRelationNodes()){
			count += bandDB.getBandMembers(band.toBand.getBandId()).size();
		}
		return count;
	}
	
	
	
}

