package com.puppyrush.buzzcloud.service.entity.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;

@Service("isExistEmail")
public class BeingExistEmail {

	
	@Autowired(required = false)
	private DBManager	dbAccess;
	
	public Map<String, Object> execute(String memberName) {
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("nickname", memberName);
		
		if(dbAccess.getColumnsOfAll("member", where).isEmpty()){
			returns.put("isExist", false);
			returns.putAll(new InstanceMessage("중복되는 이름이 없습니다.",enumInstanceMessage.SUCCESS).getMessage());
		}
		else{
			returns.put("isExist", true);
			returns.putAll(new InstanceMessage("그룹이름이 중복됩니다.",enumInstanceMessage.ERROR).getMessage());
		}

		return returns;

	}
	
}
