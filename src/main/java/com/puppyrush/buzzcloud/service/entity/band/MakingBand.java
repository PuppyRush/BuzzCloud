package com.puppyrush.buzzcloud.service.entity.band;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.puppyrush.buzzcloud.controller.form.BandForm;
import com.puppyrush.buzzcloud.entity.band.BandManager;

import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;

@Service("makingMyBand")
public class MakingBand{

	@Autowired(required=false)
	private BandManager bMng;

	public Map<String, Object> execute(BandForm form) {
		Map<String, Object> returns = new HashMap<String, Object>();
		if(bMng.makeBand(form))
			returns.putAll(new InstanceMessage("그룹생성에 성공하였습니다.", InstanceMessageType.SUCCESS).getMessage());
		
		return returns;
	}
	
}
