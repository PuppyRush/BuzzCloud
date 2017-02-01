package com.puppyrush.buzzcloud.service.config.account;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.controller.form.ProfileForm;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberManager;

@Service("settingProfile")
public class SettingProfile{

	@Autowired(required=false)
	private MemberController mCtl;
	
	@Autowired(required=false)
	private MemberManager mMng;
	
	@Autowired(required=false)
	private DBManager dbMng;

	public Map<String, Object> execute(ProfileForm form){
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		HashMap<String, Object> set = new HashMap<String, Object>();
		HashMap<String, Object> where = new HashMap<String, Object>();
		
		where.put("memberId", String.valueOf(form.getMemberId()));
		
		set.put("lastname", form.getLastname());
		set.put("firstname", form.getFirstname());
		dbMng.updateColumn("memberDetail", set, where);
		
		set.clear();
		set.put("nickname", form.getNickname());
		dbMng.updateColumn("member", set, where);
		
		if(mCtl.containsEntity(form.getMemberId())){
			Member member;
			try {
				member = mCtl.getEntity(form.getMemberId());
				member.setNickname(form.getNickname());
				
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
				
		if( !form.getPassword().equals("")){
			
			try {
				mMng.updatePassword(form.getMemberId(), form.getPassword());
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return returns;
		
	}
		
}
