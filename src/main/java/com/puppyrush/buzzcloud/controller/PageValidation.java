package com.puppyrush.buzzcloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.EntityException;
import com.puppyrush.buzzcloud.page.PageException;
import com.puppyrush.buzzcloud.page.VerifyPage;

@Controller("pageValidation")
@RequestMapping("/pageValidation")
public class PageValidation {

	@Autowired
	private VerifyPage verifyPage;
	

	@ResponseBody
	@RequestMapping(value = "/verifyPage.ajax", method = RequestMethod.GET)
	public Map<String, Object> getBandNames(HttpServletRequest rq, @RequestParam("fromPage") String fromPage) {
		
		Map<String, Object> returns = new HashMap<String, Object>();
		
		try {
			return verifyPage.execute(rq.getRequestedSessionId(), fromPage);
		} catch (EntityException e) {
			returns.putAll(e.getReturnsForAjax());
		} catch (ControllerException e) {
			returns.putAll(e.getReturnsForAjax());
		} catch (PageException e) {
			returns.putAll(e.getReturnsForAjax());
		}

		return returns;
	}

	
}
