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

@Controller("memberPage")
@RequestMapping("/member")
public class MemberController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private Logout	logout;

	@Autowired(required = false)
	private Login login;		

	@RequestMapping (value="/login.do" , method = RequestMethod.POST)
	public ModelAndView login(@RequestParam("postPage") String postPage, LoginForm form, HttpServletRequest rq) {

		ModelAndView mv = new ModelAndView();
		Map<String, Object> returns = new HashMap<String, Object>();

		form.setSessionId(rq.getRequestedSessionId());
		
		returns = login.execute(form);
		
		mv.addAllObjects(returns);
		mv.setViewName(postPage);
		
		return mv;
	}
	
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request) {

		ModelAndView mv = new ModelAndView();
		Map<String,Object> returns = logout.execute(request.getRequestedSessionId());
		
		
		mv.setViewName((String)returns.get("view"));
		mv.addAllObjects(returns);
		return mv;
	}
	

}
