package com.puppyrush.buzzcloud.service.entity.band;

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
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.band.enums.enumBandState;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;

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
	

	
	public Map<String, Object> execute(int bandId) throws EntityException, SQLException{
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		List<String> select = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		

		where.put("bandId", bandId);
		ColumnHelper result = dbMng.getColumnsOfAll("band", where);

		if(result.isEmpty() )
			throw (new EntityException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessageType(enumInstanceMessage.ERROR)
			.instanceMessage("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		
		int ownerId = result.getInteger(0, "owner");
		int adminId = result.getInteger(0, "administrator");
		int rootBandId = bandMng.getRootBandOf(bandId);

		String rootBandName = bandDB.getBandNameOf(rootBandId);
		String ownerName = mDB.getNicknameOfId(ownerId);
		String adminName = mDB.getNicknameOfId(adminId);

		select.add("contents");
		result = dbMng.getColumnsOfPart("bandDetail", select, where);

		if(result.isEmpty() )
			throw (new EntityException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessageType(enumInstanceMessage.ERROR)
			.instanceMessage("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		
		returns.put("bandContain", result.getString(0, "contents"));
		returns.put("rootBandName", rootBandName);
		returns.put("bandOwnerName", ownerName);
		returns.put("bandAdminName", adminName);

		return returns;
		
	}
		
}
