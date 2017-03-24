package com.puppyrush.buzzcloud.service.entity.band;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.message.enums.InstanceMessageType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.InstanceMessage;

@Service("isExistBandName")
public class BeingExistName {

	
	@Autowired(required = false)
	private DBManager	dbAccess;
	
	public Map<String, Object> execute(String bandName) {
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("name", bandName);
		
		if(dbAccess.getColumnsOfAll("band", where).isEmpty()){
			returns.put("isExist", false);
			returns.putAll(new InstanceMessage("중복되는 그룹 이름이 없습니다.",InstanceMessageType.SUCCESS).getMessage());
		}
		else{
			returns.put("isExist", true);
			returns.putAll(new InstanceMessage("그룹이름이 중복됩니다.",InstanceMessageType.ERROR).getMessage());
		}

		return returns;

	}
	
}
