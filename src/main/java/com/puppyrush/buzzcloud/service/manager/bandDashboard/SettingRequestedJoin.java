package com.puppyrush.buzzcloud.service.manager.bandDashboard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.controller.form.RequestedJoinForm;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;

@Service("settingRequestedJoin")
public class SettingRequestedJoin {

	@Autowired
	private DBManager dbMng;
	
	@Autowired
	private BandManager bandMng;
	
	@Autowired
	private BandDB bandDB;
	
	@Autowired
	private MemberDB memberDB;
	
	
	public Map<String,Object> excute(RequestedJoinForm form) throws EntityException, SQLException{
		
		Map<String, Object> returns = new HashMap<String, Object>();
		String name = memberDB.getNicknameOfId(form.getMemberId());
		
		HashMap<String, Object> where = new HashMap<String, Object>();
		where.put("memberId", form.getMemberId());
		where.put("bandId", form.getBandId());
		
		dbMng.deleteColumns("bandRequestJoin", where);
		
		if(form.isAccept()){
			where.clear();
			List<String> col = new ArrayList<String>();
			col.add("bandId");
			col.add("memberId");
			
			List<Object> val = new ArrayList<Object>();
			val.add(form.getBandId());
			val.add(form.getMemberId());
			dbMng.insertColumn("bandMember", col, val);
			
			returns.putAll(new InstanceMessage("구성원"+name+" 에 대하여 가입요청을 승인하였습니다.", enumInstanceMessage.SUCCESS).getMessage());
		}
		else{
			returns.putAll(new InstanceMessage("구성원"+name+" 에 대하여 가입요청을 거절하였습니다.", enumInstanceMessage.WARNING).getMessage());
		}
		
		
		returns.put("memberId", form.getMemberId());
		return returns;
		
	}

	
}


