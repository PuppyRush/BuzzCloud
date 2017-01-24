package com.puppyrush.buzzcloud.page;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.commandAction;

	public class ManagerPage implements commandAction{

		@Override
		public HashMap<String, Object> requestPro(HttpServletRequest request,
				HttpServletResponse response) throws Throwable {
		
			
			HashMap<String, Object> r = new HashMap<String, Object>();

			if(request.getParameter("toPage")==null)
				throw new PageException(enumPageError.UNKNOWN_PARAMATER);
			
			String toPage = request.getParameter("toPage");
			
			
			switch(toPage){
			
				case "groupDashboard":
					r.put("view", enumPage.GROUP_DASHBOARD.toString());
					break;
			
				case "myaccount":
					r.put("view", enumPage.MY_ACCOUNT.toString());
					break;
					
				case "group":
					r.put("view", enumPage.GROUP_MANAGER.toString());
					break;
					
				case "member":
					r.put("view", enumPage.GROUP_MEMBER.toString());
					break;
					
									
				default:
					throw new PageException(enumPageError.UNKNOWN_PARA_VALUE);
					
			
			}
			
			
			return r;
		}
	}

