package com.puppyrush.buzzcloud.service.config.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.property.enums.enumSystem;

@Service("gettingMyAccountInfo")
public class GettingMyAccountInfo{

	@Autowired(required=false)
	private MemberController mCtl;
	
	@Autowired(required=false)
	private DBManager dbMng;


	public Map<String, Object> execute(int memberId){
		
		HashMap<String,Object> where = new HashMap<String,Object>(); 			
		HashMap<String,Object> memberInfo = new HashMap<String,Object>(); 			
		List<Map<String, Object>> memberDetail = new ArrayList<Map<String, Object>>();
		
		Member member;
		try {
			member = mCtl.getEntity(memberId);
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return memberInfo;
		}
		
		where.put("memberId", memberId);
		memberDetail = dbMng.getColumnsOfAll("memberDetail", where);
		 
		memberInfo.put("nickname", member.getNickname());
		memberInfo.put("email", member.getEmail());
		
		String fullPath = enumSystem.MEMBERS_FOLDER_PATH.toString() + member.getId() + "/" + memberDetail.get(0).get("representiveImage");
		
		memberInfo.put("representiveImage", fullPath);
		memberInfo.put("firstname", memberDetail.get(0).get("firstname") );
		memberInfo.put("lastname", memberDetail.get(0).get("lastname"));

		return memberInfo;
		
	}
		
}
