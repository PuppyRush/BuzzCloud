package com.puppyrush.buzzcloud.controller.entity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.puppyrush.buzzcloud.service.entity.member.BeingExistEmail;
import com.puppyrush.buzzcloud.service.entity.member.BeingExistNickname;
import com.puppyrush.buzzcloud.service.entity.member.ChangePassword;
import com.puppyrush.buzzcloud.service.entity.member.Join;
import com.puppyrush.buzzcloud.service.entity.member.Login;
import com.puppyrush.buzzcloud.service.entity.member.Logout;

@Controller("memberControllerAsPage")
@RequestMapping("/member")
public class MemberController {

	@Autowired(required = false)
	private BeingExistNickname existNickname;

	@ResponseBody
	@RequestMapping(value = "/isExistNickname.ajax", method = RequestMethod.POST)
	public Map<String, Object> isExistNickname(String nickname) {
		return existNickname.execute(nickname);
	}

	
	@Autowired(required = false)
	private BeingExistEmail existEmail;
	
	@ResponseBody
	@RequestMapping(value = "/isExistEmail.ajax", method = RequestMethod.POST)
	public Map<String, Object> isExistEmail(String nickname) {
		return existEmail.execute(nickname);
	}
	

	@Autowired(required = false)
	private Join join;		

	@RequestMapping(value="/join.ajax", method = RequestMethod.POST )
	public @ResponseBody Map<String, Object> join(JoinForm form, HttpServletRequest rq) {
			
		
		form.setSessionId(rq.getRequestedSessionId());		
			
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = join.execute(form);
		} 
		catch( EntityException e){
			returns.putAll(e.getReturnsForAjax());
		} catch (BZException e) {
			// TODO Auto-generated catch block
			returns.putAll(e.getReturnsForAjax());
		} catch (Exception e){
			returns.putAll(new EntityException.Builder(enumPage.ENTRY)
			.instanceMessage("로그인 한 유저가 아닙니다.")
			.errorCode(enumMemberState.NOT_LOGIN).build().getReturnsForAjax()); 
		
		}
		return returns;
	}
	
	
	@Autowired(required = false)
	private Login login;		
	
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
	
	
	@Autowired(required = false)
	private Logout	logout;
	
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
		
	
	@Autowired(required = false)
	private ChangePassword changePassword;
	
	@RequestMapping(value = "/changePassword.ajax", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> changePassword(HttpServletRequest request, String oldPassword, String newPassword) {

		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		try {
			returns.putAll(changePassword.execute(request.getRequestedSessionId(), oldPassword, newPassword));
		} catch (ControllerException e) {
			returns.putAll(e.getReturnsForAjax());
		} catch (EntityException e) {
			returns.putAll(e.getReturnsForAjax());
		} catch (PageException e) {
			returns.putAll(e.getReturnsForAjax());
		} catch (SQLException e) {
			//
		}
		
		return returns;
	}
}

