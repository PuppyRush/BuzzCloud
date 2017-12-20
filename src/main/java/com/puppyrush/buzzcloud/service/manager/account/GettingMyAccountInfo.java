package com.puppyrush.buzzcloud.service.manager.account;

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
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.CommFunc;
import com.puppyrush.buzzcloud.property.enumSystem;

@Service("gettingMyAccountInfo")
public class GettingMyAccountInfo{

	@Autowired(required=false)
	private MemberController mCtl;
	
	@Autowired(required=false)
	private DBManager dbMng;


	public Map<String, Object> execute(Member member) throws EntityException{
		
		HashMap<String,Object> where = new HashMap<String,Object>(); 			
		HashMap<String,Object> memberInfo = new HashMap<String,Object>(); 			
		
				
		where.put("memberId", member.getId());
		ColumnHelper memberDetail = dbMng.getColumnsOfAll("memberDetail", where);
		 
		if(memberDetail.isEmpty() )
			throw (new EntityException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessage(enumInstanceMessage.ERROR)
			.errorString("그룹 정보를 찾지 못하였습니다. 관리자에게 문의하세요.")
			.errorCode(enumBandState.NOT_EXIST_BAND).build();
		
		
		memberInfo.put("nickname", member.getNickname());
		memberInfo.put("email", member.getEmail());
		
		memberInfo.put("image", CommFunc.toRelativePathFromImage(member.getId(), memberDetail.getString(0, "image")));
		memberInfo.put("firstname", memberDetail.getString(0, "firstname") );
		memberInfo.put("lastname", memberDetail.getString(0, "lastname"));

		return memberInfo;
		
	}
		
}
