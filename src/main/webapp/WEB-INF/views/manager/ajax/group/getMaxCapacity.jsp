<%@page import="com.puppyrush.buzzcloud.entity.band.enumBand"%>
<%@page import="java.util.*"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPageError"%>
<%@page import="com.puppyrush.buzzcloud.page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.PrintWriter"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%

			JSONObject jsonobj = new JSONObject();

			int capacity = enumBand.DEFAULT_CAPACITY.toInteger();

			jsonobj.put("capacity", capacity);
			String json = jsonobj.toJSONString();

			out.print(json);

			return;
%>