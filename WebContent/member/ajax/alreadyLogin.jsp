<%@page import="property.enums.enumSystem"%>
<%@page import="entity.member.MemberController"%>
<%@page import="entity.member.MemberManager"%>
<%@page import="entity.member.MemberDB"%>
<%@page import="entity.member.Member"%>
<%@page import="entity.member.enums.enumMemberType"%>
<%@page import="java.util.*"%>
<%@page import="page.enums.enumPageError"%>
<%@page import="page.enums.enumPage"%>
<%@page import="page.PageException"%>
<%@page import="entity.EntityException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.PrintWriter"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%

			
			JSONObject jsonobj = new JSONObject();
			jsonobj.clear();
			HashMap<String, Object> result = new HashMap<String, Object>(); 
			String sessionId = request.getRequestedSessionId();
			
			if(MemberController.getInstance().containsEntity(sessionId)==false)
				result.put("alreadyLogin", false);
			else{
				Member member = MemberController.getInstance().getMember(sessionId);
				if(member.isLogin()){
					result.put("alreadyLogin", true);
					
				}
				else
					result.put("alreadyLogin", false);

			}

			jsonobj.putAll(result);
			String json = jsonobj.toJSONString();
			
			out.clear();
			out.clearBuffer();
			out.print(json);

			return;
%>