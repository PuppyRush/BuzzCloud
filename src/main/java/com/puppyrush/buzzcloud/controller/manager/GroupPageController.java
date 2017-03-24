package com.puppyrush.buzzcloud.controller.manager;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberController;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.property.tree.Tree;
import com.puppyrush.buzzcloud.service.entity.band.GettingSelectedBandInfo;
import com.puppyrush.buzzcloud.service.entity.band.GettingSelectedBandMembers;
import com.puppyrush.buzzcloud.service.entity.band.SearchedBandInfo;
import com.puppyrush.buzzcloud.service.manager.band.InitMyBandInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller("groupPage")
@RequestMapping("/managerPage/groupConfig/*")
public class GroupPageController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;


	@Autowired(required = false)
	private MemberController		mCtl;

	@Autowired(required = false)
	private InitMyBandInfo			bandInfo;

	@Autowired(required = false)
	private GettingSelectedBandMembers	gettingSelectedBandMembers;

	@Autowired(required = false)
	private SearchedBandInfo		searhcedBandInfo;

	@Autowired(required = false)
	private GettingSelectedBandInfo gettingBandInfo;
	
	public GroupPageController() {

	}

	@ResponseBody
	@RequestMapping(value = "/initMyBandInformation.ajax", method = RequestMethod.GET)
	public Map<String, Object> initMyBandInformation(HttpServletRequest rq) {

		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = bandInfo.execute(mCtl.getMember(rq.getRequestedSessionId()).getId());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returns;

	}

	@ResponseBody
	@RequestMapping(value = "/getBandInfo.ajax", method = RequestMethod.POST)
	public Map<String, Object> getBandInfo(@RequestParam("bandId") int bandId) {
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = gettingBandInfo.execute(bandId);
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returns;
	}

	@ResponseBody
	@RequestMapping(value = "/getSelectedBandMembers.ajax", method = RequestMethod.POST)
	public Map<String, Object> gettingSelectedBandMembers(@RequestParam("bandId") int bandId) {

		Map<String, Object> returns = new HashMap<String, Object>();
		returns = gettingSelectedBandMembers.execute(bandId);

		return returns;

	}

	@ResponseBody
	@RequestMapping(value = "/searhcedBandInfo.ajax", method = RequestMethod.POST)
	public Map<String, Object> searhcedBandInfo(@RequestParam("bandId") int bandId) {

		Map<String, Object> returns = new HashMap<String, Object>();
		returns = searhcedBandInfo.execute(bandId);

		return returns;

	}
}
