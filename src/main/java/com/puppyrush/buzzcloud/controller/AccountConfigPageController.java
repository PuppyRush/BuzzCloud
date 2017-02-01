package com.puppyrush.buzzcloud.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.puppyrush.buzzcloud.controller.form.ProfileForm;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.property.tree.Tree;
import com.puppyrush.buzzcloud.service.config.account.GettingMyAccountInfo;
import com.puppyrush.buzzcloud.service.config.account.SettingProfile;
import com.puppyrush.buzzcloud.service.config.band.InitMyBandInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("accountPage")
@RequestMapping("/configPage/MyAccount/*")
public class AccountConfigPageController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	
	public AccountConfigPageController() {

	}

	
	@ResponseBody
	@RequestMapping(value = "/getMyAccountInfo.ajax", method = RequestMethod.POST)
	public Map<String, Object> getMyAccountInfo(@RequestParam("memberId") int memberId) {

		GettingMyAccountInfo info =new GettingMyAccountInfo();
		Map<String, Object> returns = info.execute(memberId);
		
		return returns;
		
	}

	@ResponseBody
	@RequestMapping(value = "/setProfile.ajax", method = RequestMethod.POST)
	public Map<String, Object> setProfile(ProfileForm form) {

		SettingProfile set = new SettingProfile();
		Map<String, Object> returns = set.execute(form);
		
		return returns;
		
	}


}
