package com.puppyrush.buzzcloud.controller.entity;

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
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.message.enums.InstanceMessageType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.InstanceMessage;
import com.puppyrush.buzzcloud.service.entity.band.MakingBand;
import com.puppyrush.buzzcloud.service.entity.band.UpdatingMyBand;
import com.puppyrush.buzzcloud.service.entity.member.BeingExistEmail;
import com.puppyrush.buzzcloud.service.entity.member.BeingExistNickname;
import com.puppyrush.buzzcloud.service.manager.band.InitMyBandInfo;

@Controller("memberControllerAsPage")
@RequestMapping("/member")
public class MemberController {

	@Autowired(required = false)
	private BeingExistNickname existNickname;
	
	@Autowired(required = false)
	private BeingExistEmail existEmail;
	
	
	@ResponseBody
	@RequestMapping(value = "/isExistNickname.ajax", method = RequestMethod.POST)
	public Map<String, Object> isExistNickname(String nickname) {
		return existNickname.execute(nickname);
	}

	@ResponseBody
	@RequestMapping(value = "/isExistEmail.ajax", method = RequestMethod.POST)
	public Map<String, Object> isExistEmail(String nickname) {
		return existEmail.execute(nickname);
	}
	
	
}

