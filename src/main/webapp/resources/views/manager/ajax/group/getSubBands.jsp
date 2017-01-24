<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.Member"%>
<%@page import="com.puppyrush.buzzcloud.property.tree.Tree"%>
<%@page import="java.util.*"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.Band"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.Band.AuthoritedMember"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.BandManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.Band.BundleBand"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPageError"%>
<%@page import="com.puppyrush.buzzcloud.page.PageException"%>
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
			Map<String, Integer> bandMap = new HashMap<String, Integer>();
			bandMap.put("NewGroup", -1);
			try{
				if(request.getParameter("memberId")==null)
					throw new PageException(enumPageError.NO_PARAMATER);
					
				
				int memberId = Integer.valueOf(request.getParameter("memberId"));
				Member member = MemberController.getInstance().getEntity(memberId);
				
				ArrayList<Band> bands = BandManager.getInstance().getAdministeredBandsOfRoot(member.getId());
				if(bands.size()>0){
					for(int i=0 ; i < bands.size() ; i++){
						
						Tree<Band> tree = BandManager.getInstance().getSubBands(bands.get(i).getBandId());
						ArrayList<Band> localBands = tree.getDatas();
						for(Band _band : localBands){
							bandMap.put(_band.getBandName(), _band.getBandId());						
						}
							
					}
				
				}
				
			}catch(PageException e){
   				e.printStackTrace();
   			}
		  
					

			jsonobj.putAll(bandMap);
			String json = jsonobj.toJSONString();

			out.print(json);

			return;
%>