package com.puppyrush.buzzcloud.entity.band;




import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.impl.EntityControllerImpl;

@Service("bandController")
public final class BandController extends EntityControllerImpl<Band>{

	@Autowired
	private BandManager bandMng;

	
	public Band newBand(int bandId) throws SQLException, ControllerException, EntityException{
		Band band = null;
		
		if(containsEntity(bandId))
			band = getEntity(bandId);
	
		else{
			band = bandMng.getBand(bandId);						
			addEntity(bandId,band);
			 
		}

		
		if(band==null)
			throw new NullPointerException();
		
		return band;
	}
	


}
