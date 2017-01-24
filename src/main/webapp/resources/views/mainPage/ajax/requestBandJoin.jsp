<%@page import="com.puppyrush.buzzcloud.dbAccess.DBManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.Member"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.BandDB"%>
<%@page import="java.util.*"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.io.PrintWriter"%>	
	
	<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	
	<%
	
	
	JSONObject jsonobj = new JSONObject();
	if(request.getParameter("bandId")==null || request.getParameter("memberId")==null)
		throw new NullPointerException();
		
	int bandId = Integer.valueOf(request.getParameter("bandId"));
	int memberId = Integer.valueOf(request.getParameter("memberId"));
	
	BandDB.getInstance().makeBandRequestJoin(bandId, memberId);

	jsonobj.put("isSuccess", true);
	String json = jsonobj.toJSONString();

	out.print(json);
	
	%>