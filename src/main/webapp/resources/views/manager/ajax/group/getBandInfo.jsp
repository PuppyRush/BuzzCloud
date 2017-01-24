<%@page import="com.puppyrush.buzzcloud.dbAccess.DBManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.authority.AuthorityManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.authority.band.BandAuthority"%>
<%@page import="com.puppyrush.buzzcloud.entity.authority.band.enumBandAuthority"%>
<%@page import="com.puppyrush.buzzcloud.entity.authority.file.FileAuthority"%>
<%@page import="com.puppyrush.buzzcloud.entity.authority.file.enumFileAuthority"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.BandDB"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.Member"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberDB"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.BandManager"%>
<%@page import="com.puppyrush.buzzcloud.property.tree.Tree"%>
<%@page import="java.util.*"%>

<%@page import="com.puppyrush.buzzcloud.page.enums.enumPageError"%>
<%@page import="com.puppyrush.buzzcloud.page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.io.PrintWriter"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%

			
			JSONObject jsonobj = new JSONObject();
			HashMap<String, Object> where = new HashMap<String, Object>();
			ArrayList<HashMap<String,Object>> bandInfo = new ArrayList<HashMap<String,Object>>(); 
			ArrayList<HashMap<String,Object>> bandDetail = new ArrayList<HashMap<String,Object>>();
			
			try{
				if(request.getParameter("bandId")==null)
					throw new PageException(enumPageError.NO_PARAMATER);
					
				int bandId = Integer.valueOf(request.getParameter("bandId"));
				where.put("bandId", bandId);
				
				bandInfo = DBManager.getInstance().getColumnsOfAll("band", where);
				bandDetail = DBManager.getInstance().getColumnsOfAll("bandDetail", where);
				
				jsonobj.put("bandId", bandId);
				jsonobj.put("bandName", bandInfo.get(0).get("name"));
				jsonobj.put("bandCapacity", bandDetail.get(0).get("maxCapacity"));
				jsonobj.put("bandContains", bandDetail.get(0).get("bandContains"));
				
				int ownerId = (int)bandInfo.get(0).get("owner");
				int adminId = (int)bandInfo.get(0).get("administrator");
				int upperBandId = BandManager.getInstance().getUpperBand(bandId);
				
				String ownerNickname = MemberDB.getInstance().getNicknameOfId(ownerId);
				String adminNickname = MemberDB.getInstance().getNicknameOfId(adminId);
				String upperBandName = BandDB.getInstance().getBandNameOf(upperBandId);
				
				jsonobj.put("ownerId", ownerId);
				jsonobj.put("adminId", adminId);
				jsonobj.put("upperBandId", upperBandId);
				jsonobj.put("upperBandName", upperBandName);
				jsonobj.put("ownerNickname", ownerNickname);
				jsonobj.put("adminNickname", adminNickname);
				
				JSONArray memberAry = new JSONArray();
				ArrayList<Member> members =  BandManager.getInstance().getMembersOf(bandId);
				for(Member member : members){
					HashMap<Integer,String> temp = new HashMap<Integer,String>();
					temp.put(member.getId(), member.getNickname());
					memberAry.add(temp);
				}
				jsonobj.put("bandMembers", memberAry);
				
				BandAuthority ba = AuthorityManager.getInstance().getBandAuthority(bandId);
				JSONArray bandAuth = new JSONArray();
				for(enumBandAuthority auth : ba.toArray())
					bandAuth.add(auth.toString());
				
				jsonobj.put("bandAuthority",bandAuth );
				
			}catch(PageException e){
				e.printStackTrace();
			}
			
			String json = jsonobj.toJSONString();

			out.print(json);

			return;
%>