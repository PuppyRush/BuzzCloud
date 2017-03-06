package com.puppyrush.buzzcloud.controller.manager;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.puppyrush.buzzcloud.controller.form.ProfileForm;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.service.manager.account.GettingMyAccountInfo;
import com.puppyrush.buzzcloud.service.manager.account.SettingProfile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller("accountPage")
@RequestMapping("/managerPage/myAccount/*")
public class AccountPageController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private MemberController mCtl;
	
	@Autowired(required = false)
	private GettingMyAccountInfo accountInfo;
	
	@Autowired(required = false)
	private SettingProfile settingProfile;
	
	public AccountPageController() {

	}
	
	@ResponseBody
	@RequestMapping(value = "/getMyAccountInfo.ajax", method = RequestMethod.GET)
	public Map<String, Object> getMyAccountInfo(HttpServletRequest rq) {
		
		Map<String, Object> returns = new HashMap<String, Object>();		
		try {
			returns = accountInfo.execute(mCtl.getMember(rq.getRequestedSessionId()));
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returns;
		
	}

	@ResponseBody
	@RequestMapping(value = "/setProfile.ajax", method = RequestMethod.POST)
	public Map<String, Object> setProfile(ProfileForm form) {

		
		Map<String, Object> returns = settingProfile.execute(form);
		
		return returns;
		
	}
	

}
