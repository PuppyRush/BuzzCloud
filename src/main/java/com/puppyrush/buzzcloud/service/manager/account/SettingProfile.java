package com.puppyrush.buzzcloud.service.manager.account;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.controller.form.ProfileForm;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;

@Service("settingProfile")
public class SettingProfile{

	@Autowired(required=false)
	private MemberController mCtl;
	
	@Autowired(required=false)
	private MemberDB mDB;
	
	@Autowired(required=false)
	private DBManager dbMng;

	public Map<String, Object> execute(ProfileForm form, int memberId) throws SQLException{
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		HashMap<String, Object> set = new HashMap<String, Object>();
		HashMap<String, Object> where = new HashMap<String, Object>();
		
		if(mDB.isExistNickname(form.getNickname(),memberId)){
			returns.putAll(new InstanceMessage("닉네임이 중복됩니다.",enumInstanceMessage.ERROR).getMessage());
		}
		else{
			where.clear();
			where.put("memberId", String.valueOf(memberId));
			
			set.put("lastname", form.getLastname());
			set.put("firstname", form.getFirstname());
			dbMng.updateColumn("memberDetail", set, where);
			
			set.clear();
			set.put("nickname", form.getNickname());
			dbMng.updateColumn("member", set, where);
			
			mCtl.updateProperty(memberId, "nickname", form.getNickname());
			
		/*	
			if(mCtl.containsEntity(memberId)){
				Member member;
				try {
					member = mCtl.getEntity(memberId);
					member.setNickname(form.getNickname());
					
				} catch (ControllerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					returns.putAll(new InstanceMessage(e.getMessage(),InstanceMessageType.ERROR).getMessage());
				}
				
			}*/
			
			returns.putAll(new InstanceMessage("변경에 성공하였습니다",enumInstanceMessage.SUCCESS).getMessage());
		}
		
		return returns;
		
	}
		
	
}
