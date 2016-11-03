<%@page import="entity.authority.band.enumBandAuthority"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="property.ConnectMysql"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="entity.member.MemberController"%>
<%@page import="entity.member.Member"%>
<%@page import="property.tree.Tree"%>
<%@page import="java.util.*"%>
<%@page import="entity.band.Band"%>
<%@page import="entity.band.Band.AuthoritedMember"%>
<%@page import="entity.band.BandManager"%>
<%@page import="entity.band.Band.BundleBand"%>
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

			for(enumBandAuthority auth : enumBandAuthority.values()){
				jsonobj.put(auth.toString(), auth.toString());
			}
			
			String json = jsonobj.toJSONString();

			out.print(json);

			return;
%>