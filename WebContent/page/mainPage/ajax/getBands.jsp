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
	
	
	JSONObject jsonobj = new JSONObject();
	if(request.getParameter("memberId")==null)
		throw new NullPointerException();
	
	int memberId = Integer.valueOf( request.getParameter("memberId"));
		
	HashMap<Integer, String> bandMap = new HashMap<Integer, String>();
	ArrayList<Band> bands = BandManager.getInstance().getAdministeredBandsOfRoot(memberId);
	if(bands.size()>0){
	
		for(int i=0 ; i < bands.size() ; i++){
		
			Tree<Band> tree = BandManager.getInstance().getSubBands(bands.get(i).getBandId());
			ArrayList<Band> localBands = tree.getDatas();
			for(Band _band : localBands)
				bandMap.put(_band.getBandId(), _band.getBandName());
		}
	}

	jsonobj.putAll(bandMap);
	String json = jsonobj.toJSONString();

	out.print(json);
	
	%>