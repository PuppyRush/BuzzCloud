<%@page import="entity.authority.file.enumFileAuthority"%>
<%@page import="dbAccess.DBManager"%>
<%@page import="entity.band.BandDB"%>
<%@page import="entity.authority.file.FileAuthority"%>
<%@page import="entity.authority.AuthorityManager"%>
<%@page import="entity.authority.member.enumMemberAuthority"%>
<%@page import="entity.authority.AuthorityController"%>
<%@page import="entity.authority.member.MemberAuthority"%>
<%@page import="entity.member.MemberController"%>
<%@page import="entity.member.Member"%>
<%@page import="property.tree.Tree"%>

<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.*"%>
<%@page import="entity.band.Band"%>
<%@page import="entity.band.Band.AuthoritedMember"%>
<%@page import="entity.band.BandManager"%>
<%@page import="entity.band.Band.BundleBand"%>
<%@page import="page.enums.enumPageError"%>
<%@page import="page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.io.PrintWriter"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%

			JSONObject jsonobj = new JSONObject();
			ArrayList<HashMap<String,Object>> memberMap = new ArrayList<HashMap<String,Object>>();
			ArrayList<Member> members = new ArrayList<Member>();
			
			
   	try{
				if(request.getParameter("bandId")==null)
					throw new PageException(enumPageError.NO_PARAMATER);
   				
				int bandId = Integer.valueOf(request.getParameter("bandId"));
				members = BandManager.getInstance().getMembersOf(bandId);
								
				
				ArrayList<String> selCaluse = new ArrayList<String>();
				HashMap<String,Object> whereCaluse = new HashMap<String,Object>();
				
				selCaluse.add("joinDate");
				selCaluse.add("memberId");
				whereCaluse.put("bandId", bandId);
				
				HashMap<Integer, HashMap<String,Object>> bandMembers = new HashMap<Integer,HashMap<String,Object>>(); 
				for(HashMap<String,Object> map : DBManager.getInstance().getColumnsOfPart("bandMember", selCaluse, whereCaluse) ){
					bandMembers.put((Integer)map.get("memberId"), map);
				}
				
				for(Member member : members ){
					
					HashMap<String,Object> info = new HashMap<String,Object>();
					Timestamp date = (Timestamp)bandMembers.get(member.getId()).get("joinDate");
					MemberAuthority memberAuth = AuthorityManager.getInstance().getMemberAuthority(member.getId(), bandId);
					
					FileAuthority fileAuth = AuthorityManager.getInstance().getFileAuthoirty(member.getId(), bandId);
					JSONArray fileAuthAry = new JSONArray();
					for(enumFileAuthority auth  : fileAuth.getAuthorityType().keySet()){
						fileAuthAry.add(auth.name());
					}
					
					
					info.put("joinDate",  date.toString());
					info.put("email", member.getEmail());
					info.put("nickname", member.getNickname());
					info.put("memberAuth",memberAuth.getAuthorityType().toString());
					info.put("fileAuth",fileAuthAry.toJSONString());
					info.put("memberId",member.getId());
					
					jsonobj.put(member.getId(), info);
					//memberMap.add(info);
					
				}
				
   	}catch(PageException e){
   				e.printStackTrace();
   			}
		  
 	   	
   	
	String json = jsonobj.toJSONString();

	out.print(json);

	return;
			
   %>