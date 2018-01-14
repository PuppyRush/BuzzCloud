package com.puppyrush.buzzcloud.service.manager.band;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.band.enums.enumBand;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;

@Service("initMyBandInfo")
final public class InitMyBandInfo {

	@Autowired(required=false)
	private MemberController mCtl;

	@Autowired(required=false)
	private BandManager bandMng;

	int memberId;

	public Map<String, Object> execute(int	memberId) throws ControllerException, SQLException, EntityException {

		this.memberId = memberId;
		
		Map<String, Object> returns = new HashMap<String, Object>();

		returns.put("bandMembers", getBandMembers());
		returns.put("subBands", getSubBands());
		returns.put("maxCapacity", getMaxCapacity());
		returns.put("bandAuthority", getBandAuthority());
		returns.put("fileAuthority", getFileAuthority());
		
		
		Member member = mCtl.getEntity(memberId);
		returns.put("groupOwner", member.getNickname());
		returns.put("administrator", member.getNickname());
		
		return returns;

	}

	private Map<String, Integer> getBandMembers() throws EntityException, SQLException {

		Map<String, Integer> memberMap = new HashMap<String, Integer>();

		try {

			Member member = mCtl.getEntity(memberId);
			memberMap.put(member.getNickname(), member.getId());

			List<Band> adminBands = bandMng.getAdministeredBands(member.getId());
			for (Band band : adminBands) {

				List<Member> memberAry = bandMng.getMembersOf(band.getBandId());
				for (Member _member : memberAry) {
					memberMap.put(_member.getNickname(), _member.getId());
				}

			}

		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return memberMap;

	}

	private Map<String, Integer> getSubBands() throws SQLException{
		
		Map<String, Integer> bandMap = new HashMap<String, Integer>();
		bandMap.put("NewGroup", -1);
		try{
									
			Member member = mCtl.getEntity(memberId);
			
			List<Band> bands = bandMng.getRootOfOwneredBands(member.getId());
			if(bands.size()>0){
				for(int i=0 ; i < bands.size() ; i++){
					
					com.puppyrush.buzzcloud.property.tree.Tree<Band> tree = bandMng.getSubBands(bands.get(i).getBandId());
					ArrayList<Band> localBands = tree.getDatas();
					for(Band _band : localBands){
						bandMap.put(_band.getBandName(), _band.getBandId());						
					}
						
				}
			
			}
			
		}
		catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bandMap;
	}
	
	private int getMaxCapacity(){
		
		return enumBand.DEFAULT_CAPACITY.toInteger();
	
	}
	
	private Map<String, String> getBandAuthority(){	

		Map<String, String> returns = new HashMap<String, String>();
		
		for(enumBandAuthority auth : enumBandAuthority.values()){
			returns.put(auth.toString(), auth.toString());
		}
		
		return returns;
	}
	
	private Map<String, String> getFileAuthority(){
		
		Map<String, String> returns = new HashMap<String, String>();
		
		for(enumFileAuthority auth : enumFileAuthority.values()){
			returns.put(auth.toString(), auth.toString());
		}
		
		return returns;
		
	}
	

	
}
