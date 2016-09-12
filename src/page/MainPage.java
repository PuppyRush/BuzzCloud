package page;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import property.commandAction;

	public class MainPage implements commandAction{
		
		@Override
		public HashMap<String,Object> requestPro(HttpServletRequest request,
				HttpServletResponse response) throws Throwable {
		
			HashMap<String, Object> h = new HashMap<String, Object>();
			h.put("view", "page/mainPage.jsp");
			
			return h;
		}
	}
