package com.puppyrush.buzzcloud.controller.entity;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.puppyrush.buzzcloud.controller.form.EmailForm;
import com.puppyrush.buzzcloud.controller.form.LoginForm;
import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.entity.band.enums.enumBandState;
import com.puppyrush.buzzcloud.entity.member.MemberManager;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberAbnormalState;
import com.puppyrush.buzzcloud.entity.member.enums.enumMemberState;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.InstanceMessage;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage;
import com.puppyrush.buzzcloud.mail.ReceivedMail;
import com.puppyrush.buzzcloud.mail.enumMailType;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.enumSystem;
import com.puppyrush.buzzcloud.service.entity.member.Login;
import com.puppyrush.buzzcloud.service.entity.member.Logout;
import com.puppyrush.buzzcloud.service.entity.member.SendMail;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Controller("mailControler")
@RequestMapping("/mail")
public class MailController {

	// private final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);;

	@Autowired(required = false)
	private ReceivedMail recvMail;

	@Autowired(required = false)
	private MemberManager mMng;
	
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
	
	@Autowired(required = false)
	private SendMail sendMail;
	
	@RequestMapping(value = "/sendEmail.do", method = RequestMethod.GET)
	public ModelAndView inputMail(@RequestParam("email") String email, @RequestParam("status") String memberStatus) {

		ModelAndView mv = new ModelAndView();
		Map<String, Object> returns = new HashMap<String, Object>();
		
		try {
			
			enumMemberAbnormalState state = enumMemberAbnormalState.valueOf(memberStatus);	
			mMng.updateMemberAbnormalState(email, state, 1);
			returns = sendMail.execute(email);
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
