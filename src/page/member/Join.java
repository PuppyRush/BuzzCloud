package page.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;

import property.commandAction;
import member.*;
import member.enums.enumMemberType;
import page.PageException;
import page.enums.enumCautionKind;
import page.enums.enumPage;
import page.enums.enumPageError;

/**
 * JSP페이지에서 폼을 통하여 값을 전달받아 회원가입을 처리받는다. 외부로그인 경우(내부로그인이면 가입한 경우) 이전에 로그인한 적이 있다면
 * 가입절차를 밟지 않는다.
 */
public class Join implements commandAction {

	@Override
	public HashMap<String, Object> requestPro(HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		
		Member member = new Member();
		HashMap<String , Object> returns = new HashMap<String , Object>();
		try{
			if(request.getParameter("register_email")==null || request.getParameter("idType")==null || request.getParameter("register_username")==null ||
				request.getParameter("register_password")==null)
				throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404);
							
						
			String _type = (String) request.getParameter("idType");
			boolean _isexist=false;
			for(enumMemberType e : enumMemberType.values()){
				if(e.getString().equals(_type)){
					member.setIdType(e);
					_isexist = true;
				}
			}
			if(!_isexist)
				throw new PageException(enumPageError.UNKNOWN_PARA_VALUE, enumPage.ERROR404);
		
			member = Member.getMember( (String)request.getParameter("sessionId"));	
			member.setId(0);
			member.setEmail((String) request.getParameter("register_email"));
			member.setNickname((String) request.getParameter("register_username"));
			member.setRegDate(new Timestamp(System.currentTimeMillis()));
			
			switch (member.getIdType()) {
					
				case GOOGLE:
				case NAVER:			
					member.setPlanePassword(" ");
					
					break;
		
				case NOTHING:
					member.setPlanePassword((String) request.getParameter("register_password"));
					
					break;
		
				default:
					throw new PageException(enumPageError.UNKNOWN_PARA_VALUE, enumPage.ERROR404);
										
			}
			
			if(request.getParameter("to")!=null){
				returns.put("view", request.getParameter("to"));
				returns.put("message", "가입에 성공하였습니다. ");
				returns.put("messageKind", enumCautionKind.NORMAL);
			}
			else{
					returns.put("view",enumPage.ENTRY.toString());
			
			
					if (ManageMember.joinMember(member)){
							returns.put("isSuccessJoin", "true");
							returns.put("message", "가입에 성공하였습니다. 메일인증을 하신 후 로그인하세요.");
							returns.put("messageKind", enumCautionKind.NORMAL);
							
						}
					else{
						returns.put("isSuccessJoin", "false");
						returns.put("message","가입에 실패하였습니다.");
						returns.put("messageKind", enumCautionKind.ERROR);
					}
			
			}
			
		}catch(PageException e){
			returns.put("view", e.getPage().toString());
		}
	return returns;
    }
}
