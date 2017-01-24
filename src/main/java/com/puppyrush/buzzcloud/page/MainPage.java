package com.puppyrush.buzzcloud.page;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.commandAction;


	public class MainPage implements commandAction{

		@Override
		public HashMap<String, Object> requestPro(HttpServletRequest request,
				HttpServletResponse response) throws Throwable {
			
			HashMap<String, Object> r = new HashMap<String, Object>();
					
			r.put("view", enumPage.MAIN.toString());
			
			return r;
		}
	}

