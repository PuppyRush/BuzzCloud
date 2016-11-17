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
	
	request.setCharacterEncoding("UTF-8");
	if(request.getParameter("memberId")==null)
		throw new NullPointerException();
	
		JSONObject jsonobj = new JSONObject();
		int memberId = Integer.valueOf(request.getParameter("memberId"));
	
		ArrayList<Band> bands = BandManager.getInstance().getAdministeredBandsOfRoot(memberId);
		HashMap<Integer,ArrayList<Integer>> bundleMap = new HashMap<Integer,ArrayList<Integer>>();
		if(bands.size()>0){
		
			int mapKey=0;
			for(int i=0 ; i < bands.size() ; i++){
			
					Tree<Band> tree = BandManager.getInstance().getSubBands(bands.get(i).getBandId());
					ArrayList<BundleBand> subBands = tree.getSubRelationNodes();
					
					for(int l=0 ; l < subBands.size() ; l++){
						ArrayList<Integer> temp = new ArrayList<Integer>();
						temp.add(subBands.get(l).fromBand.getBandId());
						temp.add(subBands.get(l).toBand.getBandId());
						
						bundleMap.put(mapKey, temp);
						mapKey++;
					}
			}
		}
			

		jsonobj.putAll(bundleMap);
		String json = jsonobj.toJSONString();

		out.print(json);
		
	
	%>