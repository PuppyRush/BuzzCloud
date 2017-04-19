package com.puppyrush.buzzcloud.service.manager.account;

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
import com.puppyrush.buzzcloud.property.PathUtils;
import com.puppyrush.buzzcloud.property.enumSystem;

@Service("gettingMyAccountInfo")
public class GettingMyAccountInfo{

	@Autowired(required=false)
	private MemberController mCtl;
	
	@Autowired(required=false)
	private DBManager dbMng;


	public Map<String, Object> execute(Member member){
		
		HashMap<String,Object> where = new HashMap<String,Object>(); 			
		HashMap<String,Object> memberInfo = new HashMap<String,Object>(); 			
		List<Map<String, Object>> memberDetail = new ArrayList<Map<String, Object>>();
		
				
		where.put("memberId", member.getId());
		memberDetail = dbMng.getColumnsOfAll("memberDetail", where);
		 
		memberInfo.put("nickname", member.getNickname());
		memberInfo.put("email", member.getEmail());
		
	
		
		
		memberInfo.put("image", PathUtils.toRelativePathFromImage(member.getId(), (String)memberDetail.get(0).get("image")));
		memberInfo.put("firstname", memberDetail.get(0).get("firstname") );
		memberInfo.put("lastname", memberDetail.get(0).get("lastname"));

		return memberInfo;
		
	}
		
}
