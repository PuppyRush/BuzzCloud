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
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.enums.enumBandState;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
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
			
		where.put("bandId", bandId);
		ColumnHelper result = dbMng.getColumnsOfAll("band", where);
		
		if(result.columnSize() != 1 )
			throw (new EntityException.Builder(enumPage.GROUP_DASHBOARD))
			.instanceMessage(enumInstanceMessage.ERROR)
			.errorString("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		
		int adminId = result.getInteger(0, "administrator");
		Timestamp createdDate =  result.getTimestamp(0,"createdDate");
		String adminName = memberDB.getNicknameOfId(adminId);
		
		select.add("contents");
		result = dbMng.getColumnsOfPart("bandDetail", select, where);
		
		if(result.columnSize() != 1 )
			throw (new EntityException.Builder(enumPage.GROUP_DASHBOARD))
			.instanceMessage(enumInstanceMessage.ERROR)
			.errorString("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		String contents = result.getString(0, "contents");
		
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

