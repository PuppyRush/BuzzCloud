package page;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import page.enums.enumPage;
import property.commandAction;


	public class MakeGroup implements commandAction{

		@Override
		public HashMap<String, Object> requestPro(HttpServletRequest request,
				HttpServletResponse response) throws Throwable {
			
			HashMap<String, Object> r = new HashMap<String, Object>();
					
			r.put("view", enumPage.GROUP_MANAGER.toString());
			
			return r;
		}
	}

