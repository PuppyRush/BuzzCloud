package mail;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.member.MemberManager;
import page.PageException;
import page.enums.enumCautionKind;
import page.enums.enumPage;
import page.enums.enumPageError;
import property.commandAction;

public class ReceivedMail {

	public class VerifyRegistration implements commandAction {

		@Override
		public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
				throws Throwable {

			HashMap<String , Object> returns = new HashMap<String , Object>();
			
			try{
						
				if(request.getParameter("kind")==null)		
					throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404);
				
				String kindString = (String)request.getParameter("kind");
				
				boolean isEmpty = true;
				enumMailType kind = enumMailType.JOIN;
				for(enumMailType k : enumMailType.values()){
					if(k.toString().equalsIgnoreCase(kindString)) {
						kind = k;
						isEmpty = false;
					}			
				}
				if(isEmpty)
					throw new PageException(enumPageError.UNKNOWN_PARA_VALUE, enumPage.ERROR404);
				
				switch(kind){
					
					case JOIN:
						
						if(request.getParameter("sId")==null || request.getParameter("email")==null || request.getParameter("number")==null)		
							throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404);
						
						String email = (String)request.getParameter("email");
						String planeUUID = (String)request.getParameter("number");					
						
						if (MemberManager.getInstance().resolveCertificateJoin(request.getRequestedSessionId(), email, planeUUID)) {
							returns.put("view", enumPage.ENTRY.toString());
							returns.put("message", "가입인증에 성공하셨습니다. 로그인 하세요.");
							returns.put("messageKind", enumCautionKind.NORMAL);
						} else
							throw new PageException("인증에 실패하였습니다. 관리자에게 문의 하세요.", enumPageError.UNKNOWN_PARA_VALUE, enumPage.ERROR404);
						
						break;
					
					case LOST_PASSWORD:
						
						
						
						break;
						
					case SLEEP:
						
						break;
					
					default:
						throw new PageException(enumPageError.UNKNOWN_PARA_VALUE, enumPage.ERROR404);	
						
					
					
				}
				
			}catch(PageException e){
				
				e.printStackTrace();
				returns.put("view", e.getPage().toString());
			}
			
			return returns;
			
			
		}
		
	}
	
	
}
