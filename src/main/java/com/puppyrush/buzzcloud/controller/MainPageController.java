package com.puppyrush.buzzcloud.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.tree.Tree;
import com.puppyrush.buzzcloud.service.band.GettingSelectedBandMembers;
import com.puppyrush.buzzcloud.service.band.InitBandMap;
import com.puppyrush.buzzcloud.service.band.SearchedBandInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


@Controller("mainPage")
@RequestMapping("/mainPage")
public class MainPageController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private InitBandMap		bandMapInfo;

	@Autowired(required = false)
	private SearchedBandInfo	searchedBandInfo;


	
	public MainPageController() {

	}

	@RequestMapping(value = "/viewFileBrowser.do", method = RequestMethod.GET)
	public ModelAndView viewFileBrowser(@RequestParam("bandId") int bandId) {

		ModelAndView mv = new ModelAndView();
		
		mv.addObject("bandId", bandId);
		mv.setViewName(enumPage.BROWSER.toString());
		return mv;
	}
		
	
	@ResponseBody
	@RequestMapping(value = "/initBandMap.ajax", method = RequestMethod.POST)
	public Map<String, Object> getMyBandMap(HttpServletRequest rq) {

		String sId = rq.getRequestedSessionId();
		Map<String, Object> returns = bandMapInfo.execute(sId);

		return returns;
	}

	@ResponseBody
	@RequestMapping(value = "/getSerachedBandInfo.ajax", method = RequestMethod.GET)
	public Map<String, Object> getSerachedBandInfo(@RequestParam("bandId") int bandId) {

		return searchedBandInfo.execute(bandId);

	}

	
}
