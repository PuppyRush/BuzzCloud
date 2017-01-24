package com.puppyrush.buzzcloud.property;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;


@Controller("MainController")
public class MainController{

	//private static final Logger logger = (Logger) LoggerFactory.getLogger(MainController.class);	

	
	@RequestMapping("/")
	public String root(Model model){
		
		return "entryPage/EntryPage";
		
	}
}

