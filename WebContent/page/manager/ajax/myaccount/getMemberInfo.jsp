<%@page import="dbAccess.DBManager"%>
<%@page import="property.enums.enumSystem"%>
<%@page import="entity.member.MemberController"%>
<%@page import="entity.member.MemberManager"%>
<%@page import="entity.member.MemberDB"%>
<%@page import="entity.member.Member"%>
<%@page import="property.tree.Tree"%>
<%@page import="java.util.*"%>
<%@page import="entity.band.Band"%>
<%@page import="entity.band.BandManager"%>
<%@page import="page.enums.enumPageError"%>
<%@page import="page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.PrintWriter"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
	/**
			  * jQuery File Tree JSP Connector
			  * Version 1.0
			  * Copyright 2008 Joshua Gould
			  * 21 April 2008
			*/
			
			JSONObject jsonobj = new JSONObject();
			HashMap<String,Object> where = new HashMap<String,Object>(); 			
			HashMap<String,Object> memberInfo = new HashMap<String,Object>(); 			
			ArrayList<HashMap<String, Object>> memberDetail = new ArrayList<HashMap<String, Object>>();
			
			if(request.getParameter("memberId")==null)
				throw new PageException(enumPageError.NO_PARAMATER);
				
			int memberId = Integer.valueOf(request.getParameter("memberId"));
			Member member = MemberController.getInstance().getEntity(memberId);
			
			where.put("memberId", memberId);
			memberDetail = DBManager.getInstance().getColumnsOfAll("memberDetail", where);
			 
			memberInfo.put("nickname", member.getNickname());
			memberInfo.put("email", member.getEmail());
			
			String fullPath = enumSystem.MEMBERS_FOLDER_PATH.toString() + member.getId() + "/" + memberDetail.get(0).get("representiveImage");
			
			memberInfo.put("representiveImage", fullPath);
			memberInfo.put("firstname", memberDetail.get(0).get("firstname") );
			memberInfo.put("lastname", memberDetail.get(0).get("lastname"));

			jsonobj.putAll(memberInfo);
			String json = jsonobj.toJSONString();

			out.print(json);

			return;
%>