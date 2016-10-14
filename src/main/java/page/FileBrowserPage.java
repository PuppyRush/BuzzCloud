package page;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import page.enums.enumPage;
import property.commandAction;

	public class FileBrowserPage implements commandAction{

		@Override
		public HashMap<String, Object> requestPro(HttpServletRequest request,
				HttpServletResponse response) throws Throwable {
		
			
			HashMap<String, Object> r = new HashMap<String, Object>();
			
			r.put("view", enumPage.BROWSER.toString());
			
			return r;
		}
	}

