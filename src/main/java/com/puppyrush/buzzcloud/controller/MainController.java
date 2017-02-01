package com.puppyrush.buzzcloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller("main")
public class MainController{

	//private static final Logger logger = (Logger) LoggerFactory.getLogger(MainController.class);	

	
	@RequestMapping("/")
	public String root(Model model){
		
		return "entryPage/EntryPage";
		
	}
}

