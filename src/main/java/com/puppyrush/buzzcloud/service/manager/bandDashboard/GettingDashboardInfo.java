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

@Service("gettingDashboardInfo")
public class GettingDashboardInfo {

	@Autowired
	private DBManager dbMng;
		
	@Autowired
	private MemberDB mDB;
	
	@Autowired
	private MemberController mCtl;
	
	public Map<String,Object> excute(int bandId) throws EntityException, SQLException, ControllerException{
		
		Map<String, Object> returns = new HashMap<String, Object>();

		returns.put("requestedJoin", getAllRequestedJoinMember(bandId));
				
		return returns;
	}
	

	
	private Map<String, Object> getAllRequestedJoinMember(int bandId) throws SQLException, ControllerException{
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		
		HashMap<String, Object> where = new HashMap<String, Object>();
		where.put("bandId", bandId);
		List<Map<String, Object>> result = dbMng.getColumnsOfAll("bandRequestJoin", where);
		for(Map<String,Object> map : result){
			Map<String, String> joins = new HashMap<String, String>();
			
			int memberId = (Integer)map.get("memberId");
			Timestamp date = (Timestamp)map.get("requestDate");
			String nickname = mCtl.containsEntity(memberId) ? mCtl.getEntity(memberId).getNickname() : mDB.getNicknameOfId(memberId);
			
			List<String> _sel = new ArrayList<String>();
			Map<String, Object> _where = new HashMap<String, Object>();
			_where.put("memberId", memberId);
			_sel.add("image");
			
			String temp = (String)dbMng.getColumnsOfPart("memberDetail", _sel, _where).get(0).get("image");
			String imageName="";
			if(temp.contains("default"))
				imageName = temp;
			else
				imageName = PathUtils.toRelativePathFromImage(memberId, imageName);
			
			joins.put("memberId", String.valueOf(memberId));
			joins.put("imagePath", imageName);
			joins.put("nickname", nickname);
			joins.put("date", date.toString());
			returns.put(String.valueOf(memberId), joins);
		}
		
		return returns;
	}

	
}

