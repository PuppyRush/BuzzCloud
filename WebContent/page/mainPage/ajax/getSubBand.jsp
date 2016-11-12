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
	
	<%
	
		ArrayList<Band> bands = BandManager.getInstance().getAdministeredBandsOfRoot(member.getId());
		HashMap<Integer,Integer> bundleMap = new HashMap<Integer,Integer>();
		if(bands.size()>0){
		
			for(int i=0 ; i < bands.size() ; i++){
			
					Tree<Band> tree = BandManager.getInstance().getSubBands(bands.get(i).getBandId());
					ArrayList<BundleBand> subBands = tree.getSubRelationNodes();
					
					for(BundleBand band : subBands)
						bundleMap.put(band.fromBand, band.toBand);
		
			}
		}
			

		jsonobj.putAll(bundleMap);
		String json = jsonobj.toJSONString();

		out.print(json);
		
	
	%>