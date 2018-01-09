package com.puppyrush.buzzcloud.service.entity.band;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.property.tree.Tree;

@Service("initBandMap")
public class InitializingBandMap{

	@Autowired(required=false)
	private BandManager	bandMng;

	@Autowired(required=false)
	private MemberController mCtl;

	
	public Map<String, Object> execute(String sId) throws ControllerException, SQLException, EntityException {

		Map<String, Object> params = new HashMap<String, Object>();
				
		Member member = mCtl.getMember(sId);
		int id = member.getId();
		
		params.put("subBandRelation", getSubBandRelation(id) );
		params.put("myAllBand", getAllMyBand(id));
		params.put("rootBands", getRootBands(id));
		
		return params;
		
	}

	private Map<Integer, List<Integer>> getSubBandRelation(int memberId) throws SQLException, ControllerException {

		Map<Integer, List<Integer>> returns = new HashMap<Integer, List<Integer>>();
		
	
		try{
			List<Band> bands = bandMng.getRootOfOwneredBands(memberId);
			
			if(bands.size()>0){
			
				int mapKey=0;
				for(int i=0 ; i < bands.size() ; i++){
				
					Tree<Band> tree = bandMng.getSubBands(bands.get(i).getBandId());
					ArrayList<BundleBand> subBands = tree.getSubRelationNodes();
					
					if(!subBands.isEmpty())
						for(int l=0 ; l < subBands.size() ; l++){
							ArrayList<Integer> temp = new ArrayList<Integer>();
							temp.add(subBands.get(l).fromBand.getBandId());
							temp.add(subBands.get(l).toBand.getBandId());
							
							returns.put(mapKey, temp);
							mapKey++;
						}
					else{
						ArrayList<Integer> temp = new ArrayList<Integer>();
						temp.add(bands.get(i).getBandId());
						temp.add(bands.get(i).getBandId());
						
						returns.put(mapKey, temp);
						mapKey++;
					}
				}
			}
			
		}catch(EntityException e){
			
		}
		
		return returns;

	}

	private Map<String, Object> getAllMyBand(int memberId) throws SQLException, ControllerException {

		Map<String, Object> bandMap = new HashMap<String, Object>();

		try {

			List<Band> bands = bandMng.getRootOfOwneredBands(memberId);
			if (bands.size() > 0) {

				for (int i = 0; i < bands.size(); i++) {

					Tree<Band> tree = bandMng.getSubBands(bands.get(i).getBandId());
					ArrayList<Band> localBands = tree.getDatas();
					for (Band _band : localBands)
						bandMap.put(String.valueOf(_band.getBandId()), _band.getBandName());
				}
			}
		} catch (EntityException e) {

		}

		return bandMap;

	}
	
	private List<Integer> getRootBands(int memberId) throws EntityException, ControllerException, SQLException{
		return bandMng.getRootOfOwneredBandIds(memberId);
	
	}
}
