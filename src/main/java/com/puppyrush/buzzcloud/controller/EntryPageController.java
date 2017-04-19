package com.puppyrush.buzzcloud.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.puppyrush.buzzcloud.controller.form.ContactForm;
import com.puppyrush.buzzcloud.controller.form.JoinForm;
import com.puppyrush.buzzcloud.controller.form.LoginForm;
import com.puppyrush.buzzcloud.mail.postman.CeriticationJoin;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.service.entity.member.AlreadyLogin;
import com.puppyrush.buzzcloud.service.entity.member.Join;
import com.puppyrush.buzzcloud.service.entity.member.Login;
import com.puppyrush.buzzcloud.service.entity.member.Logout;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller("entryPage")
@RequestMapping("/entryPage")
public class EntryPageController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required=true)
	private Login login;
	
	@Autowired(required=true)
	private Join join;
	
	@Autowired(required=true)
	private AlreadyLogin alreadyLogin;
	
	@Autowired(required=true)
	private CeriticationJoin postman;
	
	public EntryPageController() {

	}



	@RequestMapping(value = "/alreadyLogin.ajax", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> alreadyLogin(HttpServletRequest rq){
		
		return alreadyLogin.execute(rq.getRequestedSessionId());

	}
	
	
	
	@RequestMapping("/postAlreadyLogin.do")
	public ModelAndView postAlreadyLogin() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName(enumPage.MAIN.toString());
				
		return mv;

	}		
	
	@RequestMapping(value = "/contact.ajax", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> alreadyLogin(ContactForm form){
		return postman.sendToDeveloper(form);
	}
	

}
