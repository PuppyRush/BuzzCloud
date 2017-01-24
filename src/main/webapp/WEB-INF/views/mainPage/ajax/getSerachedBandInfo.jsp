<%@page import="com.puppyrush.buzzcloud.dbAccess.DBManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberDB"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.Member"%>
<%@page import="java.util.*"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.Band"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.BandDB"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.BandManager"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPageError"%>
<%@page import="com.puppyrush.buzzcloud.page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.PrintWriter"%>	
	
	<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	
	<%
	
	
	JSONObject jsonobj = new JSONObject();
	if(request.getParameter("bandId")==null)
		throw new NullPointerException();
	
	int bandId = Integer.valueOf(request.getParameter("bandId"));
		
	ArrayList<String> select = new ArrayList<String>();
	HashMap<String, Object> where = new HashMap<String, Object>();
	ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		
	where.put("bandId", bandId);
	result = DBManager.getInstance().getColumnsOfAll("band", where);
	
	int ownerId = (int)result.get(0).get("owner");
	int adminId = (int)result.get(0).get("administrator");
	int rootBandId = BandManager.getInstance().getRootBandOf(bandId);
	
	
	String rootBandName = BandDB.getInstance().getBandNameOf(rootBandId);
	String ownerName = MemberDB.getInstance().getNicknameOfId(ownerId);
	String adminName = MemberDB.getInstance().getNicknameOfId(adminId);
	
	select.add("contents");
	result = DBManager.getInstance().getColumnsOfPart("bandDetail", select, where);
	
	jsonobj.put("bandContain", result.get(0).get("contents"));
	jsonobj.put("rootBandName", rootBandName);
	jsonobj.put("bandOwnerName", ownerName);
	jsonobj.put("bandAdminName", adminName);

	String json = jsonobj.toJSONString();

	out.print(json);
	
	%>