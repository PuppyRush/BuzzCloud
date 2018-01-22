package com.puppyrush.buzzcloud.service.entry;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.puppyrush.buzzcloud.controller.form.ContactForm;
import com.puppyrush.buzzcloud.entity.message.instanceMessage.*;
import com.puppyrush.buzzcloud.mail.PostManImple;
import com.puppyrush.buzzcloud.mail.PostMan;
import com.puppyrush.buzzcloud.mail.enumMail;

@Service("contact")
public class Contact {

	@Autowired(required = false)
	PostMan postman;
	

	public Map<String,Object> execute(ContactForm form) throws AddressException, MessagingException{
				
		Map<String,Object> returns = new HashMap<String,Object>();

		postman.send(new PostManImple.Builder(form.getFrom(),enumMail.gmailID.toString()).subject(form.getSubject()).content(form.getContain()).build());
		
		returns.putAll(new InstanceMessage("관리자에게 메일을 보냈습니다.", enumInstanceMessage.SUCCESS).getMessage());
		return returns;
		
	}
}
