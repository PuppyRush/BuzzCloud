<%@page import="com.puppyrush.buzzcloud.dbAccess.DBManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.Member"%>
<%@page import="com.puppyrush.buzzcloud.property.tree.Tree"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="com.puppyrush.buzzcloud.property.ConnectMysql"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.*"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.Band"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.Band.AuthoritedMember"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.BandManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.Band.BundleBand"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPageError"%>
<%@page import="com.puppyrush.buzzcloud.page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.io.PrintWriter"%>	
	
	<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	
	<%
	
	JSONArray ary =new JSONArray();
	JSONObject jsonobj = new JSONObject();
	if(request.getParameter("bandName")==null)
		throw new NullPointerException();
	
	String bandName = request.getParameter("bandName");
		
	HashMap<String, Object> where = new HashMap<String, Object>();
	ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		
	result = DBManager.getInstance().getColumnsOfAll("band", where);

	for(int i=0 ; i < result.size() ; i++){
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("name", (String)result.get(i).get("name"));
		map.put("id", (int)result.get(i).get("bandId"));
		ary.add(map);
	}
	
	

	String json = ary.toJSONString();

	out.print(json);
	
	%>