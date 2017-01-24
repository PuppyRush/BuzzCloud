<%@page import="com.puppyrush.buzzcloud.property.enums.enumSystem"%>
<%@page import="com.puppyrush.buzzcloud.dbAccess.DBManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberDB"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.Member"%>
<%@page import="com.puppyrush.buzzcloud.property.tree.Tree"%>
<%@page import="java.util.*"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.Band"%>
<%@page import="com.puppyrush.buzzcloud.entity.band.BandManager"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPageError"%>
<%@page import="com.puppyrush.buzzcloud.page.PageException"%>
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