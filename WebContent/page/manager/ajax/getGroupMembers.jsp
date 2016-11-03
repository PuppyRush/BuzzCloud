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
			Map<String, Integer> memberMap = new HashMap<String,Integer>();
			

   	try{
				if(request.getParameter("memberId")==null)
					throw new PageException(enumPageError.NO_PARAMATER);
   				
				int memberId = Integer.valueOf(request.getParameter("memberId"));
				Member member = MemberController.getInstance().getEntity(memberId);
				memberMap.put(member.getNickname(), member.getId());	
				 
				ArrayList<Band> adminBands = BandManager.getInstance().getAdministeredBands(member.getId());
				for(Band band : adminBands){
					
					ArrayList<Member> memberAry =  BandManager.getInstance().getMembersOf(band.getBandId());
					for(Member _member : memberAry){
						memberMap.put(_member.getNickname(), _member.getId());	
					}
					
				}
				
   	}catch(PageException e){
   				e.printStackTrace();
   			}
		  
   	
	jsonobj.putAll(memberMap);
	String json = jsonobj.toJSONString();

	out.print(json);

	return;
			
   %>