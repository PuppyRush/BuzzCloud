<%@page import="entity.authority.file.enumFileAuthority"%>
<%@page import="java.util.*"%>
<%@page import="page.enums.enumPageError"%>
<%@page import="page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.PrintWriter"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%

			JSONObject jsonobj = new JSONObject();

			for(enumFileAuthority auth : enumFileAuthority.values()){
				jsonobj.put(auth.getString().trim(), auth.getString().trim());
			}
			
			String json = jsonobj.toJSONString();

			out.print(json);

			return;
%>