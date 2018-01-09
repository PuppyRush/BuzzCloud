package com.puppyrush.buzzcloud.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.InstanceMessage;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.enumSystem;
import com.puppyrush.buzzcloud.property.tree.Tree;
import com.puppyrush.buzzcloud.service.entity.band.GettingSelectedBandMembers;
import com.puppyrush.buzzcloud.service.entity.band.InitializingBandMap;
import com.puppyrush.buzzcloud.service.entity.band.SearchedBandInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;

import java.sql.SQLException;
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
	private InitializingBandMap		bandMapInfo;
	
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
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = bandMapInfo.execute(sId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			returns.putAll(new InstanceMessage(enumSystem.INTERNAL_ERROR.toString(), enumInstanceMessage.ERROR).getMessage());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			returns.putAll(e.getReturnsForAjax());
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			returns.putAll(e.getReturnsForAjax());
		}

		return returns;
	}


	
}
