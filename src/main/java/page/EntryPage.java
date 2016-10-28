package page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.EntityException;
import entity.enumEntityState;
import entity.band.Band;
import entity.band.BandController;
import entity.band.BandManager;
import entity.member.Member;
import entity.member.MemberController;
import entity.member.enums.enumMemberState;
import page.enums.enumPage;
import property.commandAction;

	public class EntryPage implements commandAction{

		@Override
		public HashMap<String, Object> requestPro(HttpServletRequest request,
				HttpServletResponse response) throws Throwable {
		
			
			HashMap<String, Object> r = new HashMap<String, Object>();
			
			r.put("view", enumPage.ENTRY.toString());
			
			return r;
		}
	}

