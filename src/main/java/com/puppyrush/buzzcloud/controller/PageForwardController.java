package com.puppyrush.buzzcloud.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.puppyrush.buzzcloud.controller.form.LoginForm;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.service.entity.member.Login;
import com.puppyrush.buzzcloud.service.entity.member.Logout;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller("pageForward")
@RequestMapping("/forwardPage.do")
public class PageForwardController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private Logout	logout;

	@Autowired(required = false)
	private Login login;		

	@RequestMapping (value="*" , method = RequestMethod.GET)
	public ModelAndView login(@RequestParam("page") String page) {

		ModelAndView mv = new ModelAndView();
		String viewname = "";
		try{
			viewname = enumPage.valueOf(page).toString();
		}catch(Exception e){
			viewname = enumPage.ERROR403.toString();
		}
		mv.setViewName(viewname);
		return mv;
	}
	

}
