package com.puppyrush.buzzcloud.entity.band;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.impl.EntityControllerImpl;

@Service("bandController")
public final class BandController extends EntityControllerImpl<Band>{

	@Autowired
	private BandManager bandMng;

	
	public Band newBand(int bandId){
		Band band = null;
		try{
			if(containsEntity(bandId))
				band = getEntity(bandId);
		
			else{
				band = bandMng.getBand(bandId);						
				addEntity(bandId,band);
				 
			}
		}catch(ControllerException e){
			e.printStackTrace();
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(band==null)
			throw new NullPointerException();
		
		return band;
	}
	


}
