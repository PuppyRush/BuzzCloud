package com.puppyrush.buzzcloud.controller.manager;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.puppyrush.buzzcloud.controller.form.FileForm;
import com.puppyrush.buzzcloud.controller.form.ProfileForm;
import com.puppyrush.buzzcloud.controller.form.RequestedJoinForm;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.message.enums.InstanceMessageType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.InstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.service.autocomplete.GettingSearchedBandInfo;
import com.puppyrush.buzzcloud.service.manager.account.GettingMyAccountInfo;
import com.puppyrush.buzzcloud.service.manager.account.RegisteMemberFace;
import com.puppyrush.buzzcloud.service.manager.account.SettingProfile;
import com.puppyrush.buzzcloud.service.manager.bandDashboard.GettingBandInfo;
import com.puppyrush.buzzcloud.service.manager.bandDashboard.GettingCharts;
import com.puppyrush.buzzcloud.service.manager.bandDashboard.GettingDashboardInfo;
import com.puppyrush.buzzcloud.service.manager.bandDashboard.GettingMyGroups;
import com.puppyrush.buzzcloud.service.manager.bandDashboard.SettingRequestedJoin;

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

@Controller("groupDashboardPage")
@RequestMapping("/managerPage/groupDashboard/*")
public class GroupDashboard {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private MemberController	mCtl;

	@Autowired(required = false)
	private GettingMyGroups gettingMyGroups;
	
	@Autowired(required = false)
	private GettingBandInfo gettingBandInfo;
	
	@Autowired(required = false)
	private GettingDashboardInfo gettingDashboardInfo;
	
	@Autowired(required = false)
	private SettingRequestedJoin settingRequestedJoin;
	
	@Autowired(required = false)
	private GettingCharts gettingCharts;
	
	public GroupDashboard() {

	}

	@ResponseBody
	@RequestMapping(value = "/getMyGroups.ajax", method = RequestMethod.GET)
	public Map<String, Object> getMyAccountInfo(HttpServletRequest rq) {

		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = gettingMyGroups.execute(rq.getRequestedSessionId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returns;

	}

	
	@ResponseBody
	@RequestMapping(value = "/getBandInfo.ajax", method = RequestMethod.GET)
	public Map<String, Object> getSerachedBandInfo(@RequestParam("bandId") int bandId) {
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns =  gettingBandInfo.excute(bandId);
		} catch (EntityException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returns;
	}

	@ResponseBody
	@RequestMapping(value = "/getDashboardInfoAll.ajax", method = RequestMethod.GET)
	public Map<String, Object> getDashboardInfo(@RequestParam("bandId") int bandId) {
		
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns =  gettingDashboardInfo.excute(bandId);
		} catch (EntityException | SQLException | ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returns;
	}

	@ResponseBody
	@RequestMapping(value = "/acceptMember.ajax", method = RequestMethod.GET)
	public Map<String, Object> acceptMember(RequestedJoinForm form, boolean isAccept) {
		form.setAccept(isAccept);
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns =  settingRequestedJoin.excute(form);
		} catch (EntityException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returns;
	}

	@ResponseBody
	@RequestMapping(value = "/denyMember.ajax", method = RequestMethod.GET)
	public Map<String, Object> denyMember(RequestedJoinForm form, boolean isAccept) {
		form.setAccept(isAccept);
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns =  settingRequestedJoin.excute(form);
		} catch (EntityException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returns;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getCharts.ajax", method = RequestMethod.GET)
	public Map<String, Object> getCharts(int bandId) {
		
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns =  gettingCharts.excute(bandId);
		} catch (EntityException | SQLException | ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returns;
	}
	
}
