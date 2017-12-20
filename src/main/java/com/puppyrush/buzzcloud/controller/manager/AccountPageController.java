package com.puppyrush.buzzcloud.controller.manager;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


import com.puppyrush.buzzcloud.controller.form.ProfileForm;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.service.manager.account.GettingMyAccountInfo;
import com.puppyrush.buzzcloud.service.manager.account.RegisteMemberFace;
import com.puppyrush.buzzcloud.service.manager.account.SettingProfile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Controller("accountPage")
@RequestMapping("/managerPage/myAccount/*")
public class AccountPageController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private MemberController	mCtl;

	@Autowired(required = false)
	private GettingMyAccountInfo	accountInfo;

	@Autowired(required = false)
	private SettingProfile		settingProfile;

	@Autowired(required = false)
	private RegisteMemberFace		registeImage;
	
	
	public AccountPageController() {

	}

	@ResponseBody
	@RequestMapping(value = "/getMyAccountInfo.ajax", method = RequestMethod.GET)
	public Map<String, Object> getMyAccountInfo(HttpServletRequest rq) {

		Map<String, Object> returns = new HashMap<String, Object>();
		
		try {
			returns = accountInfo.execute(mCtl.getMember(rq.getRequestedSessionId()));
		} catch (EntityException e) {
			returns.putAll(e.getReturns());
		} catch (ControllerException e) {
			returns.putAll(e.getReturns());
		}
	
		return returns;

	}

	@ResponseBody
	@RequestMapping(value = "/setProfile.ajax", method = RequestMethod.POST)
	public Map<String, Object> setProfile(HttpServletRequest rq, ProfileForm form) {

		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = settingProfile.execute(form, mCtl.getMember(rq.getRequestedSessionId()).getId());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			returns.putAll(e.getReturns());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returns;

	}

	@RequestMapping(value = "/registerMemberFace.ajax", method = RequestMethod.POST)
	public ModelAndView registeMemberFace(HttpServletRequest request) {

		ModelAndView mv = new ModelAndView(enumPage.MY_ACCOUNT.toString());
		try {
			mv.addAllObjects(registeImage.execute(request.getRequestedSessionId(), (MultipartHttpServletRequest)request));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			mv.addAllObjects(new InstanceMessage(e.getMessage(),enumInstanceMessage.SUCCESS).getMessage());
			mv.setViewName(enumPage.ERROR404.toString());
		}
	
		return mv;

	}

}
