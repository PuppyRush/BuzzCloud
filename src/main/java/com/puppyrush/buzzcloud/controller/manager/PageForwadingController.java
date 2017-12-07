package com.puppyrush.buzzcloud.controller.manager;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.puppyrush.buzzcloud.dbAccess.DBManager;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.authority.AuthorityManager;
import com.puppyrush.buzzcloud.entity.authority.enumAuthorityState;
import com.puppyrush.buzzcloud.entity.authority.band.BandAuthority;
import com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority;
import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.Band.BundleBand;
import com.puppyrush.buzzcloud.entity.band.BandDB;
import com.puppyrush.buzzcloud.entity.band.BandManager;
import com.puppyrush.buzzcloud.entity.member.Member;
import com.puppyrush.buzzcloud.entity.member.MemberDB;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.tree.Tree;
import com.puppyrush.buzzcloud.service.manager.band.InitMyBandInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("managerPage")
@RequestMapping("/managerPage/*")
public class PageForwadingController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	
	public PageForwadingController() {

	}


	@RequestMapping(value = "/forwading.do", method = RequestMethod.GET)
	public ModelAndView manager(@RequestParam("forwardPageName") String forwardPageName) {

		ModelAndView mv = new ModelAndView();
		Map<String, Object> returns = new HashMap<String, Object>();
		
		switch(forwardPageName){
		
			case "groupdashboard":
				mv.setViewName(enumPage.GROUP_DASHBOARD.toString());
				
				break;
		
			case "myaccount":
				mv.setViewName(enumPage.MY_ACCOUNT.toString());
				
				break;
				
			case "group":
				mv.setViewName(enumPage.GROUP_MANAGER.toString());
				break;
				
			case "member":
				mv.setViewName(enumPage.GROUP_MEMBER.toString());
				break;
				
			case "main":
				mv.setViewName(enumPage.MAIN.toString());
				break;
				
			default:
				try {
					throw (new PageException.Builder(enumPage.ERROR404))
					.errorString("비 정상적인 접근입니다.")
					.errorCode(enumPageError.UNKNOWN_PARA_VALUE).build(); 
				} catch (PageException e) {
					returns.putAll(e.getReturns());
				}
				
		
		}
		mv.addAllObjects(returns);
		mv.setViewName((String)returns.get("view"));
		
		return mv;
	}




}
