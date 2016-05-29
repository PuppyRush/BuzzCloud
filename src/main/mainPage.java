package main;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import property.commandAction;

	public class mainPage implements commandAction{
		
		@Override
		public HashMap<String,String> requestPro(HttpServletRequest request,
				HttpServletResponse response) throws Throwable {
		
			HashMap<String, String> h = new HashMap<String, String>();
			h.put("view", "mainPage/mainPage.jsp");
			
			return h;
		}
	}
