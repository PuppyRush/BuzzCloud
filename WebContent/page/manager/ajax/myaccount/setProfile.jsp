<%@page import="property.enums.enumSystem"%>
<%@page import="dbAccess.DBManager"%>
<%@page import="entity.member.MemberController"%>
<%@page import="entity.member.MemberManager"%>
<%@page import="entity.member.MemberDB"%>
<%@page import="entity.member.Member"%>
<%@page import="property.tree.Tree"%>
<%@page import="java.util.*"%>
<%@page import="entity.band.Band"%>
<%@page import="entity.band.BandManager"%>
<%@page import="page.enums.enumPageError"%>
<%@page import="page.PageException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.PrintWriter"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%


			JSONObject jsonobj = new JSONObject();
			 			
			HashMap<String, Object> set = new HashMap<String, Object>();
			HashMap<String, Object> where = new HashMap<String, Object>();
			
			if(request.getParameter("memberId")==null)
				throw new PageException(enumPageError.NO_PARAMATER);
				
			String lastname = request.getParameter("lastname");
			String nickname = request.getParameter("nickname");
			String firstname = request.getParameter("firstname");
			
			int memberId = Integer.valueOf(request.getParameter("memberId"));
			
			where.put("memberId", String.valueOf(memberId));
			
			set.put("lastname", lastname);
			set.put("firstname", firstname);
			DBManager.getInstance().updateColumn("memberDetail", set, where);
			
			set.clear();
			set.put("nickname", nickname);
			DBManager.getInstance().updateColumn("member", set, where);
			
			if(MemberController.getInstance().containsEntity(memberId)){
				Member member = MemberController.getInstance().getEntity(memberId);
				member.setNickname(nickname);
			}
						
			jsonobj.put("isSuccess", true);
			String json = jsonobj.toJSONString();

			out.print(json);

			return;
%>