package com.puppyrush.buzzcloud.service.entity.band;

import java.io.IOException;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.puppyrush.buzzcloud.controller.form.BandForm;
import com.puppyrush.buzzcloud.dbAccess.DBException;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.enumController;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.band.enums.enumBandStandard;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;

@Service("makingMyBand")
public class MakingBand{

	@Autowired(required=false)
	private BandManager bMng;

	public Map<String, Object> execute(BandForm form) throws PageException, ControllerException, EntityException, SQLException, IOException, DBException {
		Map<String, Object> returns = new HashMap<String, Object>();

		isValidationBandInfo(form);
		
		if(bMng.makeBand(form))
			returns.putAll(new InstanceMessage("그룹생성에 성공하였습니다.", enumInstanceMessage.SUCCESS).getMessage());
		
		return returns;
	}
	
	
	public void isValidationBandInfo(BandForm form) throws PageException, SQLException{
		
		if(form.getBandCapacity() > enumBandStandard.MAX_CAPACITY.toInt())
			throw (new PageException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessage("그룹의 용량은 최대 "+enumBandStandard.MAX_CAPACITY.toInt()+"MB까지 가능합니다")
			.errorCode(enumPageError.WRONG_PARAMATER).build();
		
		if(form.getBandName().length() < enumBandStandard.NAME_MIN_LENGTH.toInt() ||
				form.getBandName().length() > enumBandStandard.NAME_MAX_LENGTH.toInt())
			throw (new PageException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessage("그룹 이름의 길이는 "+enumBandStandard.NAME_MIN_LENGTH.toInt()+"~"+enumBandStandard.NAME_MAX_LENGTH.toInt()+"길이 사이만 가능합니다.")
			.errorCode(enumPageError.WRONG_PARAMATER).build(); 
			
		Map<enumBandAuthority ,Boolean> bandAuthMap = enumBandAuthority.toEnumMap(form.getBandAuthority());
		
		if(form.getUpperBand() == -1){
			if(bandAuthMap.containsKey(enumBandAuthority.ROOT)==false){
				throw (new PageException.Builder(enumPage.GROUP_MANAGER))
				.instanceMessage("새로운 그룹을 선택한 경우 그룹권한에 최상단 그룹은 필수로 선택되어야 합니다.")
				.errorCode(enumPageError.WRONG_PARAMATER).build(); 		
			}
			else if(bandAuthMap.get(enumBandAuthority.ROOT)==false){
				throw (new PageException.Builder(enumPage.GROUP_MANAGER))
				.instanceMessage("새로운 그룹을 선택한 경우 그룹권한에 최상단 그룹은 필수로 선택되어야 합니다.")
				.errorCode(enumPageError.WRONG_PARAMATER).build(); 		
			}
		}
		else{
			if(bandAuthMap.containsKey(enumBandAuthority.ROOT) && bandAuthMap.get(enumBandAuthority.ROOT)){
				throw (new PageException.Builder(enumPage.GROUP_MANAGER))
				.instanceMessage("기존의 그룹을 선택한 경우 그룹 권한으로 최상단 그룹을 선택할 수 없습니다.")
				.errorCode(enumPageError.WRONG_PARAMATER).build(); 		
			}
		}
		
		if(bMng.isRootBand(form.getExUpperBand()))
			throw (new PageException.Builder(enumPage.GROUP_MANAGER))
			.instanceMessage("상위그룹의 그룹 권한이  최하단그룹인경우 하위 그룹을 더 이상 생성할 수 없습니다.")
			.errorCode(enumPageError.WRONG_PARAMATER).build(); 	
	}
}
