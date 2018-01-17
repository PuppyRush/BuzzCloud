package cn.bluejoe.elfinder.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.dbAccess.DBException;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.band.BandController;

import cn.bluejoe.elfinder.service.FsMapping;
import cn.bluejoe.elfinder.service.FsServiceFactory;

@Service("defaultFsMapping")
public class DefaultFsMapping implements FsMapping {

	
	@Autowired
	private AuthorityManager authMng;

	@Autowired(required=false)
	private BandController bandCtl;	
	
	
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
		
		@Override 
		public boolean equals(Object obj){
			
			BandMember o = (BandMember)obj;
			
			if(this.bandId==o.bandId && this.memberId == o.memberId)
				return true;
			else
				return false;
			
		}
		
		@Override
		public int hashCode(){
			
			int result = 17;
			result = 31 * result + this.bandId;
			result = 31 * result  + this.memberId;
			
			return result;
			
		}
		
	}
	
	private Map<BandMember, FsServiceFactory> serviceMap = new HashMap<BandMember, FsServiceFactory>();
	
	@Override
	public void addFsServiceFactory(BandMember bm) throws ControllerException, DBException {
		// TODO Auto-generated method stub

		if(contains(bm)){
			throw new IllegalArgumentException("already exist object in the serviceMap");
		}

		FsServiceFactory fsService = null;
		
		fsService = new StaticFsServiceFactory(bm,authMng,bandCtl);

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
	public FsServiceFactory getFsServiceFactory(BandMember bm) throws DBException, ControllerException {
		// TODO Auto-generated method stub
		
		if(serviceMap.containsKey(bm) == false){
			Logger.getLogger(getClass()).warn("not exist sessionId in the serviceMap");
			throw new IllegalArgumentException("");
		}
		
		serviceMap.get(bm).invalidate();
		
		return serviceMap.get(bm);
		
	}

	@Override
	public FsServiceFactory getFsServiceFactory(int bandId, int memberId) throws DBException, ControllerException {
		// TODO Auto-generated method stub
		return getFsServiceFactory(new BandMember(bandId, memberId));
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
