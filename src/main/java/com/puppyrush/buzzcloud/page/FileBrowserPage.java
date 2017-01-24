package com.puppyrush.buzzcloud.page;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.puppyrush.buzzcloud.entity.band.Band;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.page.enums.enumPage;
import com.puppyrush.buzzcloud.page.enums.enumPageError;
import com.puppyrush.buzzcloud.property.commandAction;

import cn.bluejoe.elfinder.servlet.InitializeFsAtOpen;

	public class FileBrowserPage implements commandAction{

		@Override
		public HashMap<String, Object> requestPro(HttpServletRequest request,
				HttpServletResponse response) throws Throwable {
		
			
			HashMap<String, Object> r = new HashMap<String, Object>();
			
			if(request.getParameter("bandId")==null)
				throw new PageException(enumPageError.UNKNOWN_PARAMATER);
			
			int bandId = Integer.valueOf(request.getParameter("bandId"));
			Band band = BandController.getInstance().newBand(bandId);
			
			
			InitializeFsAtOpen.setGroup(band.getBandName(), band.getDriverPath());
			
			r.put("view", enumPage.BROWSER.toString());
			
			return r;
		}
	}

