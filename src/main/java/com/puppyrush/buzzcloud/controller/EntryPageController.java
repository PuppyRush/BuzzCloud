package com.puppyrush.buzzcloud.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.puppyrush.buzzcloud.controller.form.ContactForm;
import com.puppyrush.buzzcloud.entity.message.enums.InstanceMessageType;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.InstanceMessage;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.property.enumSystem;
import com.puppyrush.buzzcloud.service.entity.member.AlreadyLogin;
import com.puppyrush.buzzcloud.service.entry.Contact;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Controller("entryPage")
@RequestMapping("/entryPage")
public class EntryPageController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required=true)
	private Contact contact;
	
	@Autowired(required=true)
	private AlreadyLogin alreadyLogin;

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
		Map<String, Object> returns = new HashMap<String, Object>();
		try {
			returns = contact.execute(form);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			returns.putAll(new InstanceMessage(enumSystem.INTERNAL_ERROR.toString(), InstanceMessageType.ERROR).getMessage());
			e.printStackTrace();
			
		}
		return returns;
	}
	

}
