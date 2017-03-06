package com.puppyrush.buzzcloud.service.band;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.puppyrush.buzzcloud.controller.form.BandForm;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityController;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandManager;

import com.puppyrush.buzzcloud.entity.member.MemberDB;

import com.puppyrush.buzzcloud.entity.band.Band.AuthoritedMember;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.entity.band.BandDB;

@Service("updatingMyBand")
public class UpdatingMyBand{

	@Autowired(required=false)
	private MemberDB mDB;

	@Autowired(required=false)
	private BandManager bMng;
	
	@Autowired(required=false)
	private BandDB bDB;

	@Autowired(required=false)
	private DBManager dbMng;

	@Autowired(required=false)
	private BandController bCtl;

	@Autowired(required=false)
	private AuthorityController authCtl;
	
	@Autowired(required=false)
	private AuthorityManager authMng;
	
	public Map<String, Object> execute(int memberId, BandForm form) {

		Map<String, Object> params = new HashMap<String, Object>();
		updateBand(form);
	
		return params;
		
	}

	
	public boolean updateBand(BandForm form){
		
		HashMap<String, Object> where = new HashMap<String, Object>();
		HashMap<String, Object> set = new HashMap<String, Object>();
	
		try{
		
			String bandName = form.getBandName();
			int upperBandId = form.getUpperBand();
			int ownerId = mDB.getIdOfNickname(form.getBandOwner());
			int bandId = bDB.getIdOfName(bandName, upperBandId);
			if(upperBandId == -1)
				upperBandId = bandId;
			
			List<Integer> memberAry = form.getMembers();
			bMng.addNewBandMembers(bandId, memberAry);
			
			
			set.put("name", bandName);		
			set.put("owner", ownerId);
			where.put("bandId", bandId);			
			dbMng.updateColumn("band", set, where);
						
			where.clear();
			set.clear();
			
			
			set.put("maxCapacity", form.getBandCapacity());
			set.put("contents", form.getBandContain());	
			where.put("bandId", bandId);
			dbMng.updateColumn("bandDetail", set, where);
			
			where.clear();
			set.clear();
			
			
			for(String auth : form.getBandAuthority()){
				set.put(auth, 1);
			}	
			where.put("bandId", bandId);
			dbMng.updateColumn("bandAuthority", set, where);
			
			where.clear();
			set.clear();
						
			
			
			for(String auth : form.getFileAuthority()){
				set.put(enumFileAuthority.valueOf(auth).toString(), 1);
			}	
			where.put("bandId", bandId);
			dbMng.updateColumn("fileAuthority", set, where);
			
			where.clear();
			set.clear();
		
			
			EnumMap<enumFileAuthority ,Boolean> fileAuthMap = new EnumMap<enumFileAuthority ,Boolean>(enumFileAuthority.class);
			for(String auth : form.getFileAuthority()){
				fileAuthMap.put(enumFileAuthority.valueOf(auth), true);
			}	
			
			Band band;
			if( bCtl.containsEntity(bandId))
				band =  bCtl.getEntity(bandId);
			else{
				band = bMng.getBand(bandId);
				bCtl.addEntity(bandId, band);
			}
			
			BandAuthority bandAuthority = authMng.getBandAuthority(bandId);
			if(authCtl.containsEntity(bandAuthority.getAuthorityId()) == false)
				authCtl.addEntity(bandAuthority.getAuthorityId(), bandAuthority);
			band.setBandAuthority(bandAuthority);
			
			for(Integer memberId :  band.getMembers().keySet()){				
				AuthoritedMember am = band.getMembers().get(memberId);
				am.getFileAuthority().setAuthorityType(fileAuthMap);		
			}
			
			
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
}
