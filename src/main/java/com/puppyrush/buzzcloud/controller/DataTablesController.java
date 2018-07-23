package com.puppyrush.buzzcloud.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("dataTablesController")
@RequestMapping("/dataTables")
public class DataTablesController {

	public DataTablesController() {

	}

	@RequestMapping(value = "/init.ajax", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> init(HttpServletRequest rq){
		Map<String, Object> returns = new HashMap<String, Object>();
		
		return returns;

	}
	
	
	
	
}
