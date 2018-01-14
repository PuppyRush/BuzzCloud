package com.puppyrush.buzzcloud.service.entity.band;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.puppyrush.buzzcloud.controller.form.BandForm;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityController;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.FileAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.entity.band.Band.AuthoritedMember;
import com.puppyrush.buzzcloud.entity.band.enums.enumBandState;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.entity.band.BandDB;

@Service("gettingBandInfo")
public class GettingSelectedBandInfo{

	@Autowired(required=false)
	private MemberDB mDB;
	
	@Autowired(required=false)
	private BandManager bandMng;
	
	@Autowired(required=false)
	private BandDB bandDB;
	
	@Autowired(required=false)
	private AuthorityManager authMng;
	
	@Autowired(required = false)
	private DBManager	dbAccess;
	
	public Map<String, Object> execute(int bandId) throws ControllerException, SQLException, EntityException {

		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> where = new HashMap<String, Object>();

		where.put("bandId", bandId);

		ColumnHelper bandInfo = dbAccess.getColumnsOfAll("band", where);
		ColumnHelper bandDetail = dbAccess.getColumnsOfAll("bandDetail", where);

		if(bandInfo.isEmpty() || bandDetail.isEmpty())
			throw (new EntityException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessageType(enumInstanceMessage.ERROR)
			.instanceMessage("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		
		result.put("bandId", bandId);
		result.put("bandName", bandInfo.getString(0, "name"));
		result.put("bandCapacity", bandDetail.getInteger(0,"maxCapacity"));
		result.put("bandContains", bandDetail.getInteger(0, "bandContains"));

		int ownerId = bandInfo.getInteger(0, "owner");
		int adminId = bandInfo.getInteger(0, "administrator");
		int upperBandId = bandMng.getUpperBand(bandId);

		String ownerNickname = mDB.getNicknameOfId(ownerId);
		String adminNickname = mDB.getNicknameOfId(adminId);
		String upperBandName = bandDB.getBandNameOf(upperBandId);

		result.put("ownerId", ownerId);
		result.put("adminId", adminId);
		result.put("upperBandId", upperBandId);
		result.put("upperBandName", upperBandName);
		result.put("ownerNickname", ownerNickname);
		result.put("adminNickname", adminNickname);

		List<Map<Integer, String>> memberAry = new ArrayList<Map<Integer, String>>();
		ArrayList<Member> members = bandMng.getMembersOf(bandId);
		for (Member member : members) {
			HashMap<Integer, String> temp = new HashMap<Integer, String>();
			temp.put(member.getId(), member.getNickname());
			memberAry.add(temp);
		}
		result.put("bandMembers", memberAry);

		BandAuthority ba = authMng.getBandAuthority(bandId);
		List<String> bandAuth = new ArrayList<String>();
		for (enumBandAuthority auth : ba.toArray())
			bandAuth.add(auth.getString());
		result.put("bandAuthority", bandAuth);
		
		FileAuthority fa = authMng.getFileAuthoirty(ownerId, upperBandId);
		List<String> fileAuth = new ArrayList<String>();
		for (enumFileAuthority auth : fa.toArray())
			fileAuth.add(auth.getString());
		result.put("fileAuthority", fileAuth);
		
		return result;
		
	}

	
}
