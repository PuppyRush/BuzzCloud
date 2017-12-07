package com.puppyrush.buzzcloud.service.manager.bandDashboard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.property.CommFunc;

@Service("gettingMyGroups")
public class GettingMyGroups{

	@Autowired(required=false)
	private BandManager bMng;
	
	@Autowired(required=false)
	private MemberController mCtl;
	
	@Autowired(required=false)
	private MemberDB mDB;
	
	@Autowired(required=false)
	private DBManager dbMng;

	public Map<String, Object> execute(String sId) throws SQLException, ControllerException, EntityException{
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		int memberId = mCtl.getMember(sId).getId();
		List<Band> bands = bMng.getRootOfOwneredBands(memberId);
		
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		for(Band b : bands){
			String adminName = mDB.getNicknameOfId(b.getAdminId());
			int adminId = mDB.getIdOfNickname(adminName);
			int memberCount  = b.getMembers().size();
			
			Map<String, Object > map = new HashMap<String, Object >(); 
			map.put("bandId", b.getBandId());
			map.put("adminId", adminId);
			map.put("bandName", b.getBandName());
			map.put("memberCount", memberCount);
			map.put("image", getImagePath(adminId));
			res.add(map);			
		}
		returns.put("groups",res);
		return returns;
		
	}
		
	private String getImagePath(int memberId){
		
		Map<String, Object> where = new HashMap<String, Object>();
		List<String> sel = new ArrayList<String>();
		where.put("memberId", memberId);
		sel.add("image");
		
		String imageName =  (String)dbMng.getColumnsOfPart("memberDetail", sel,where).get(0).get("image");
		return CommFunc.toRelativePathFromImage(memberId, imageName);
		
	}
	
}
