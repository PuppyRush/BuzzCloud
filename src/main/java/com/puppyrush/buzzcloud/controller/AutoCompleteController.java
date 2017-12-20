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
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.tree.Tree;
import com.puppyrush.buzzcloud.service.autocomplete.GettingBandNames;
import com.puppyrush.buzzcloud.service.autocomplete.GettingMemberNames;
import com.puppyrush.buzzcloud.service.autocomplete.GettingSearchedBandInfo;
import com.puppyrush.buzzcloud.service.entity.band.GettingSelectedBandMembers;
import com.puppyrush.buzzcloud.service.entity.band.InitializingBandMap;
import com.puppyrush.buzzcloud.service.entity.band.SearchedBandInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller("autoCompleteController")
@RequestMapping("/autocomplete")
public class AutoCompleteController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private GettingBandNames getBandNames;

	@Autowired(required = false)
	private GettingMemberNames getMemberNames;
	
	@Autowired(required = false)
	private GettingSearchedBandInfo getSearchedBandInfo;
	
	
	public AutoCompleteController() {

	}

	@ResponseBody
	@RequestMapping(value = "/getBandNames.ajax", method = RequestMethod.GET)
	public List<Map<String, Object>> getBandNames(@RequestParam("bandName") String bandName) {

		return getBandNames.excute(bandName);

	}
	

	@ResponseBody
	@RequestMapping(value = "/getMemberNames.ajax", method = RequestMethod.GET)
	public List<Map<String, Object>> getMemberNames(@RequestParam("nickname") String nickname) {

		return getMemberNames.excute(nickname);

	}
	
	@ResponseBody
	@RequestMapping(value = "/getSerachedBandInfo.ajax", method = RequestMethod.GET)
	public Map<String, Object> getSerachedBandInfo(@RequestParam("bandId") int bandId) {

		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = getSearchedBandInfo.excute(bandId);
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			returns.putAll(e.getInstanceMessage());
		}
		return returns;
	}


}
