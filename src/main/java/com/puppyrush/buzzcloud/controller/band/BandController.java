package com.puppyrush.buzzcloud.controller.band;

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

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.message.enums.InstanceMessageType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.InstanceMessage;

@Controller("bandControllerAsPage")
@RequestMapping("/band")
public class BandController {

	@Autowired(required = false)
	private BandDB bandDB;

	@Autowired(required = false)
	private MemberController mCtl;

	
	@ResponseBody
	@RequestMapping(value = "/requestBandJoin.ajax", method = RequestMethod.POST)
	public Map<String, Object> requestBandJoin(HttpServletRequest rq, @RequestParam("bandId") int bandId) {

		Map<String, Object> returns = new HashMap<String, Object>();

		
		try {
			returns = bandDB.makeBandRequestJoin(bandId, mCtl.getMember(rq.getRequestedSessionId()).getId());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returns.put("isSuccess", false);
			InstanceMessage msg = new InstanceMessage("그룹가입 요청에 실패하였습니다.  관리자에게 문의하세요",InstanceMessageType.ERROR);
			returns.putAll(msg.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returns.put("isSuccess", false);
			InstanceMessage msg = new InstanceMessage("그룹가입 요청에 실패하였습니다.  관리자에게 문의하세요",InstanceMessageType.ERROR);
			returns.putAll(msg.getMessage());
		}
		return returns;
	}

	
}

