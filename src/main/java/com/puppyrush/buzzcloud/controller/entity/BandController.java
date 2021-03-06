package com.puppyrush.buzzcloud.controller.entity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.puppyrush.buzzcloud.controller.form.BandForm;
import com.puppyrush.buzzcloud.dbAccess.DBException;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.service.entity.band.BeingExistName;
import com.puppyrush.buzzcloud.service.entity.band.MakingBand;
import com.puppyrush.buzzcloud.service.entity.band.UpdatingMyBand;
import com.puppyrush.buzzcloud.service.manager.band.InitMyBandInfo;

@Controller("bandControllerAsPage")
@RequestMapping("/band")
public class BandController {

	@Autowired(required = false)
	private BandDB bandDB;
	
	@Autowired(required = false)
	private MemberController mCtl;
	
	@Autowired(required = false)
	private MakingBand makingBand;

	@Autowired(required = false)
	private UpdatingMyBand updateBand;
		
	@Autowired(required = false)
	private BeingExistName existName;
	
	@ResponseBody
	@RequestMapping(value = "/requestBandJoin.ajax", method = RequestMethod.POST)
	public Map<String, Object> requestBandJoin(HttpServletRequest rq, @RequestParam("bandId") int bandId) {

		Map<String, Object> returns = new HashMap<String, Object>();

		
		try {
			returns = bandDB.makeBandRequestJoin(bandId, mCtl.getMember(rq.getRequestedSessionId()).getId());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			returns.put("isSuccess", false);
			InstanceMessage msg = new InstanceMessage("그룹가입 요청에 실패하였습니다.  관리자에게 문의하세요",enumInstanceMessage.ERROR);
			returns.putAll(msg.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			returns.put("isSuccess", false);
			InstanceMessage msg = new InstanceMessage("그룹가입 요청에 실패하였습니다.  관리자에게 문의하세요",enumInstanceMessage.ERROR);
			returns.putAll(msg.getMessage());
		}
		return returns;
	}


	@ResponseBody
	@RequestMapping(value = "/updateBand.ajax", method = RequestMethod.POST)
	public Map<String, Object> updateBand(@RequestParam("bandId") int bandId, BandForm bandForm) {

		Map<String, Object> returns = new HashMap<String, Object>();
		
		try {
			returns = updateBand.execute(bandId,bandForm);
			InstanceMessage msg = new InstanceMessage("그룹정보 변경에 성공하였습니다.", enumInstanceMessage.SUCCESS);
			returns.putAll(msg.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			InstanceMessage msg = new InstanceMessage("그룹정보 변경에 실패하였습니다.  관리자에게 문의하세요",enumInstanceMessage.ERROR);
			returns.putAll(msg.getMessage());
		} catch (ControllerException e) {
			returns.putAll(e.getReturnsForAjax());
		} catch (EntityException e) {
			returns.putAll(e.getReturnsForAjax());
		} catch ( IllegalArgumentException e){
			InstanceMessage msg = new InstanceMessage(e.getMessage(),enumInstanceMessage.ERROR);
			returns.putAll(msg.getMessage());
		} catch( Exception e){
			InstanceMessage msg = new InstanceMessage(e.getMessage(),enumInstanceMessage.ERROR);
			returns.putAll(msg.getMessage());
		}
		

		return returns;

	}


	@ResponseBody
	@RequestMapping(value = "/makeBand.ajax", method = RequestMethod.POST)
	public Map<String, Object> updateBand(BandForm bandForm) throws ControllerException, EntityException, SQLException, IOException, DBException{

		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = makingBand.execute(bandForm);
		} catch (PageException e) {
			// TODO Auto-generated catch block
			returns.putAll(e.getReturnsForAjax());
		}

		return returns;

	}

	@ResponseBody
	@RequestMapping(value = "/isExistName.ajax", method = RequestMethod.GET)
	public Map<String, Object> isExistNameOfBand(String bandName) {
		return existName.execute(bandName);
	}

		
	
}

