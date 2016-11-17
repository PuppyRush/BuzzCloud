<%@page import="java.util.*"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="property.ConnectMysql"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="page.enums.enumPageError"%>
<%@page import="page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
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
			
			JSONArray ary =new JSONArray();
			JSONObject jsonobj = new JSONObject();
			List<String> partOfNicknames = new ArrayList<String>();
			
			try{
				if(request.getParameter("nickname")==null)
					throw new PageException(enumPageError.NO_PARAMATER);
					
				String partOfNickname = request.getParameter("nickname") + "%";
		
				Connection conn = ConnectMysql.getConnector();
				PreparedStatement ps = conn.prepareStatement("select nickname from member where nickname like ?");
				ps.setString(1, partOfNickname);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()){				
					partOfNicknames.add(rs.getString(1));					
				}

				
			}catch(PageException e){
   				e.printStackTrace();
   			}
		  
			
			for(String str : partOfNicknames){
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("name",str);
				ary.add(map);
			}
			String json = ary.toJSONString();

			out.print(json);

			return;
%>