package com.puppyrush.buzzcloud.service.entity.band;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.file.FileAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.authority.member.MemberAuthority;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.Member;

@Service("gettingSelectedBandMembers")
public class GettingSelectedBandMembers{

	@Autowired(required=false)
	private DBManager dbMng;

	@Autowired(required=false)
	private BandManager bandMng;

	@Autowired(required=false)
	private AuthorityManager authMng;

	public Map<String, Object> execute(int bandId) throws ControllerException, SQLException {
				
		ArrayList<Member> members = new ArrayList<Member>();
		members = bandMng.getMembersOf(bandId);
					
		HashMap<String,Object> whereCaluse = new HashMap<String,Object>();	
		
		whereCaluse.put("bandId", bandId);
		
		Map<Integer, Map<String,Object>> bandMembers = new HashMap<Integer,Map<String,Object>>(); 
		for(Map<String,Object> map : dbMng.getColumnsOfAll("bandMember", whereCaluse).getColumns() ){
			bandMembers.put((Integer)map.get("memberId"), map);
		}
		
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		try{
			
			for(Member member : members ){
				
				HashMap<String,Object> info = new HashMap<String,Object>();
				Timestamp date = (Timestamp)bandMembers.get(member.getId()).get("joinDate");
				MemberAuthority memberAuth = authMng.getMemberAuthority(member.getId(), bandId);
				
				FileAuthority fileAuth = authMng.getFileAuthoirty(member.getId(), bandId);
				List<String> fileAuthAry = new ArrayList<String>();
				for(enumFileAuthority auth : fileAuth.getAuthorityType().keySet()){
					fileAuthAry.add(auth.name());
				}
				
				
				
				info.put("joinDate",  date.toString());
				info.put("email", member.getEmail());
				info.put("nickname", member.getNickname());
				info.put("memberAuth",memberAuth.getAuthorityType().toString());
				info.put("fileAuth",fileAuthAry);
				info.put("memberId",member.getId());
				
				returns.put(String.valueOf(member.getId()), info);
				
			}			
		
		}
		catch(EntityException e){
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return returns;
		
	}
		
	
}
