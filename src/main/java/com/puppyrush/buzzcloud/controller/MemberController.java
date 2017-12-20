package com.puppyrush.buzzcloud.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.puppyrush.buzzcloud.bzexception.BZException;
import com.puppyrush.buzzcloud.controller.form.JoinForm;
import com.puppyrush.buzzcloud.controller.form.LoginForm;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.enumSystem;
import com.puppyrush.buzzcloud.service.entity.member.SendMail;
import com.puppyrush.buzzcloud.service.entity.member.Join;
import com.puppyrush.buzzcloud.service.entity.member.Login;
import com.puppyrush.buzzcloud.service.entity.member.Logout;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
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
	
	@Autowired(required = false)
	private Join join;		

	@Autowired(required = false)
	private SendMail findPassword;
	
	@RequestMapping(value="/join.ajax", method = RequestMethod.POST )
	public @ResponseBody Map<String, Object> join(JoinForm form, HttpServletRequest rq) {
			
		
		form.setSessionId(rq.getRequestedSessionId());		
			
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = join.execute(form);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returns.putAll(new InstanceMessage(e.getMessage(), enumInstanceMessage.ERROR).getMessage());
		}
		
		return returns;
	}
	
	@RequestMapping (value="/login.do" , method = RequestMethod.POST)
	public ModelAndView login(LoginForm form, HttpServletRequest rq) {

		ModelAndView mv = new ModelAndView();
		Map<String, Object> returns = new HashMap<String, Object>();

		form.setSessionId(rq.getRequestedSessionId());
		
		returns = login.execute(form);
		
		mv.addAllObjects(returns);
		mv.addObject("id", returns.get("id"));
		mv.setViewName((String)returns.get("view"));
		
		return mv;
	}
	
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request) {

		ModelAndView mv = new ModelAndView();
		Map<String, Object> returns = new HashMap<String, Object>();
		
		try{
			returns = logout.execute(request.getRequestedSessionId());
		}
		catch( EntityException e){
			returns.putAll(e.getReturns());
		}
		catch(ControllerException e){
			returns.putAll(e.getReturns());
		}
		catch (BZException e) {
			returns.putAll(e.getReturns());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//..
			
		}
	
		
		mv.setViewName((String)returns.get("view"));
		mv.addAllObjects(returns);
		return mv;
	}
	
	@RequestMapping(value = "/inputEmail.do", method = RequestMethod.GET)
	public ModelAndView inputMail(@RequestParam("email") String email) {

		ModelAndView mv = new ModelAndView();
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = findPassword.execute(email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			returns.putAll(new InstanceMessage(enumSystem.INTERNAL_ERROR.toString(), enumInstanceMessage.ERROR).getMessage());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			returns.putAll(new InstanceMessage(enumSystem.INTERNAL_ERROR.toString(), enumInstanceMessage.ERROR).getMessage());
		} catch (EntityException e) {
			returns.putAll(e.getReturns());
		} catch (ControllerException e) {
			returns.putAll(e.getReturns());
		}
		
		
		mv.setViewName((String)returns.get("view"));
		mv.addAllObjects(returns);
		return mv;
	}
	

}
