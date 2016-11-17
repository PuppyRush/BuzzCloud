package page;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.bluejoe.elfinder.servlet.InitializeFsAtOpen;
import entity.band.Band;
import entity.band.BandController;
import page.enums.enumPage;
import page.enums.enumPageError;
import property.commandAction;

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

