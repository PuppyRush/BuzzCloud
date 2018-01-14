package com.puppyrush.buzzcloud.service.manager.bandDashboard;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;

@Service("gettingCharts")
public class GettingCharts {

	@Autowired
	private DBManager dbMng;
	
	
	public Map<String,Object> excute(int bandId) throws EntityException, SQLException, ControllerException{
				
		Map<String, Object> returns = new HashMap<String, Object>();
		
		returns.putAll(getUsage(bandId));
		returns.putAll(getCreatedTime(bandId));
		return returns;
	}
	
	private Map<String, Object> getUsage(int bandId) throws EntityException{
			
		Map<String, Object> returns = new HashMap<String, Object>();
		
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("bandId", bandId);
		sel.add("usingCapacity");
		sel.add("maxCapacity");
		
		ColumnHelper res = dbMng.getColumnsOfPart("bandDetail", sel, where);
		
		if(res.columnSize() != 1 )
			throw (new EntityException.Builder(enumPage.GROUP_DASHBOARD))
			.instanceMessageType(enumInstanceMessage.ERROR)
			.instanceMessage("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		
		returns.put("usingCapacity", res.getInteger(0, "usingCapacity"));
		returns.put("maxCapacity", res.getInteger(0, "maxCapacity"));
		
		sel.clear();
		sel.add("name");
		res = dbMng.getColumnsOfPart("band", sel, where);
		
		
		returns.put("title", res.getString(0,"name") + " capacity");
		
		return returns;	
	}
	

	private Map<String, Object> getCreatedTime(int bandId) throws EntityException{
			
		Map<String, Object> returns = new HashMap<String, Object>();
		
		List<String> sel = new ArrayList<String>();
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("bandId", bandId);
	
		sel.clear();
		sel.add("createdDate");
		
		ColumnHelper res = dbMng.getColumnsOfPart("band", sel, where);
		if(res.columnSize() != 1 )
			throw (new EntityException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessageType(enumInstanceMessage.ERROR)
			.instanceMessage("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		returns.put("createdDate", (new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분")).format( res.getTimestamp(0,"createdDate") ).toString());
		
		List<Integer> dates = new ArrayList<Integer>();
		String str= (new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")).format(res.getTimestamp(0, "createdDate") ).toString();
		for(String s : str.split("-")){
			dates.add(Integer.valueOf(s));		
		}
		returns.put("createdDateByLong",dates);
		return returns;	
	}
}


