package com.puppyrush.buzzcloud.service.manager.bandDashboard;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.dbAccess.DBManager.ColumnHelper;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.enums.enumBandState;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.CommFunc;

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
	

	
	private Map<String, Object> getAllRequestedJoinMember(int bandId) throws SQLException, ControllerException, EntityException{
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		
		HashMap<String, Object> where = new HashMap<String, Object>();
		where.put("bandId", bandId);
		ColumnHelper result = dbMng.getColumnsOfAll("bandRequestJoin", where);
		
		if(result.columnSize() != 1 )
			throw (new EntityException.Builder(enumPage.GROUP_DASHBOARD))
			.instanceMessage(enumInstanceMessage.ERROR)
			.errorString("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		
		for(Map<String,Object> map : result.getColumns()){
			Map<String, String> joins = new HashMap<String, String>();
			
			int memberId = (Integer)map.get("memberId");
			Timestamp date = (Timestamp)map.get("requestDate");
			String nickname = mCtl.containsEntity(memberId) ? mCtl.getEntity(memberId).getNickname() : mDB.getNicknameOfId(memberId);
			
			List<String> _sel = new ArrayList<String>();
			Map<String, Object> _where = new HashMap<String, Object>();
			_where.put("memberId", memberId);
			_sel.add("image");
			
			String imageName="";
			
			ColumnHelper ch =  dbMng.getColumnsOfPart("memberDetail", _sel, _where);
			if(ch.columnSize() != 1 ){
				imageName = CommFunc.toRelativePathFromImage(memberId, imageName);
			}
			else{
				String temp =ch.getString(0, "image");
				
				if(temp.contains("default"))
					imageName = temp;
				else
					imageName = CommFunc.toRelativePathFromImage(memberId, imageName);
			}
			
			joins.put("memberId", String.valueOf(memberId));
			joins.put("imagePath", imageName);
			joins.put("nickname", nickname);
			joins.put("date", date.toString());
			returns.put(String.valueOf(memberId), joins);
		}
		
		return returns;
	}

	
}

