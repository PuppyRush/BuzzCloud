<%@page import="entity.band.BandManager"%>
<%@page import="page.enums.enumPageError"%>
<%@page import="page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.BufferedReader, java.io.IOException, java.io.InputStreamReader"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
			
			String jStr = request.getParameter("data");

		
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject)parser.parse(jStr);
			JSONObject result = new JSONObject();
		
	
			try{
					
					BandManager.getInstance().makeBand(obj);
					result.put("isSuccess", true);		
			
			}catch(Exception e){
				result.put("isSuccess", false);
			}
			
	
			String json = result.toJSONString();
			out.print(json); 

			return;
%>