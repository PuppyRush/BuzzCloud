package com.puppyrush.buzzcloud.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.puppyrush.buzzcloud.controller.form.EmailForm;
import com.puppyrush.buzzcloud.controller.form.LoginForm;
import com.puppyrush.buzzcloud.mail.ReceivedMail;
import com.puppyrush.buzzcloud.mail.enumMailType;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.service.entity.member.Login;
import com.puppyrush.buzzcloud.service.entity.member.Logout;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller("mailControler")
@RequestMapping("/mail")
public class MailController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private ReceivedMail recvMail;

	@RequestMapping (value="/join.do" , method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest rq, EmailForm form ) {
		Map<String, Object> returns = new HashMap<String, Object>();
		ModelAndView mv = new ModelAndView();
		try {
			returns = recvMail.resolveCertification(form , rq.getRequestedSessionId());
			mv.setViewName((String)returns.get("view"));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mv;
	}
	
	

}
