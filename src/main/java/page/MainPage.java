package page;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.band.Band;
import entity.band.BandController;
import page.enums.enumPage;
import page.enums.enumPageError;
import property.commandAction;


	public class MainPage implements commandAction{

		@Override
		public HashMap<String, Object> requestPro(HttpServletRequest request,
				HttpServletResponse response) throws Throwable {
			
			HashMap<String, Object> r = new HashMap<String, Object>();
					
			r.put("view", enumPage.MAIN.toString());
			
			return r;
		}
	}

