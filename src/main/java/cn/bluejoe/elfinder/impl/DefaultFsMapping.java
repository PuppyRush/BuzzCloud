package cn.bluejoe.elfinder.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.bluejoe.elfinder.service.FsMapping;
import cn.bluejoe.elfinder.service.FsServiceFactory;

@Service("defaultFsMapping")
public class DefaultFsMapping implements FsMapping {

	public static class BandMember{
		private int memberId;
		private int bandId;
		
		public BandMember(int bandId, int memberId){
			this.bandId = bandId;
			this.memberId = memberId;
		}
		
		public int getMemberId() {
			return memberId;
		}
		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}
		public int getBandId() {
			return bandId;
		}
		public void setBandId(int bandId) {
			this.bandId = bandId;
		}
		
	}
	
	private Map<BandMember, FsServiceFactory> serviceMap = new HashMap<BandMember, FsServiceFactory>();
	
	@Override
	public void addFsServiceFactory(BandMember bm) {
		// TODO Auto-generated method stub

		if(contains(bm)==false){
			throw new IllegalArgumentException("already exist object in the serviceMap");
		}

		FsServiceFactory fsService = null;
		try {
			fsService = new StaticFsServiceFactory(bm);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		serviceMap.put(bm, fsService);
	
	}

	@Override
	public void removeFsServiceFactory(BandMember bm) {
		// TODO Auto-generated method stub
		
		if(contains(bm)==false){
			throw new IllegalArgumentException("not exist object in the serviceMap");
		}
		
		serviceMap.remove(bm);

	}

	@Override
	public void removeFsServiceFactory(int bandId) {
		// TODO Auto-generated method stub
		
		for(BandMember bm : serviceMap.keySet()){
			if(bm.getBandId()==bandId)
				serviceMap.remove(bm);
		}		
	}
	
	@Override
	public FsServiceFactory getFsServiceFactory(BandMember bm) {
		// TODO Auto-generated method stub
		
		if(serviceMap.containsKey(bm) == false){
			Logger.getLogger(getClass()).warn("not exist sessionId in the serviceMap");
			throw new IllegalArgumentException("");
		}
		
		serviceMap.get(bm).invalidate();
		
		return serviceMap.get(bm);
		
	}

	@Override
	public boolean contains(BandMember bm) {
		// TODO Auto-generated method stub
		
		if(serviceMap.containsKey(bm)){
			return true;
		}
		else
			return false;
		
	}


}
