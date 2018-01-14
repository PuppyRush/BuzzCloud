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
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumEntityState;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.band.enums.enumBandState;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
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
	
	
	public Map<String,Object> excute(int bandId) throws EntityException, SQLException{
		
		Map<String, Object> returns = new HashMap<String, Object>();

		ArrayList<String> select = new ArrayList<String>();
		
		HashMap<String, Object> where = new HashMap<String, Object>();	
		where.put("bandId", bandId);
		
		ColumnHelper ch = dbMng.getColumnsOfAll("band", where);
		if(ch.isEmpty())
			throw (new EntityException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessageType(enumInstanceMessage.ERROR)
			.instanceMessage("오류가 발생했습니다.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		int ownerId = ch.getInteger(0, ("owner"));
		int adminId = ch.getInteger(0, ("administrator"));
		int rootBandId = bandMng.getRootBandOf(bandId);
		
		
		String rootBandName = bandDB.getBandNameOf(rootBandId);
		String ownerName = memberDB.getNicknameOfId(ownerId);
		String adminName = memberDB.getNicknameOfId(adminId);
		
		select.add("contents");
		ch = dbMng.getColumnsOfPart("bandDetail", select, where);
		
		returns.put("bandContain", ch.getString(0, ("contents")));
		returns.put("rootBandName", rootBandName);
		returns.put("bandOwnerName", ownerName);
		returns.put("bandAdminName", adminName);
		
		return returns;
		
	}
}
