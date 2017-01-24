<%@page import="com.puppyrush.buzzcloud.property.enums.enumSystem"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberManager"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.MemberDB"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.Member"%>
<%@page import="com.puppyrush.buzzcloud.entity.member.enums.enumMemberType"%>
<%@page import="java.util.*"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPageError"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPage"%>
<%@page import="com.puppyrush.buzzcloud.page.PageException"%>
<%@page import="com.puppyrush.buzzcloud.entity.EntityException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.io.PrintWriter"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%

			
			JSONObject jsonobj = new JSONObject();
			
			try{
				if(request.getParameter("email")==null || request.getParameter("idType")==null || request.getParameter("password")==null|| request.getParameter("nickname")==null)
					throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404);
								
				enumMemberType idType = enumMemberType.valueOf(request.getParameter("idType"));		
				
			
				String sId = request.getRequestedSessionId();
				String email =request.getParameter("email"); 
				String pw="";			
				String nickname= request.getParameter("nickname");
				
				switch (idType) {
						
					case GOOGLE:
					case NAVER:			
						pw="";
						
						break;
			
					case NOTHING:
						pw = request.getParameter("password");
						if(pw ==null || pw.length()<7)
							throw new PageException(enumPageError.NO_PARAMATER, enumPage.ERROR404); 
						
						break;
			
					default:
						throw new PageException(enumPageError.UNKNOWN_PARA_VALUE, enumPage.ERROR404);
											
				}	
		
				Member tempMember = new Member.Builder(email).registrationKind(idType).planePassword(pw).build();
				
				if(MemberDB.getInstance().isJoin(tempMember.getEmail()) ){
					
					jsonobj.put("isSuccess", "false");
					jsonobj.put("message", "이미 가입한 유저입니다.");
					
				}
				else{
					
					tempMember.doJoin();
					MemberManager.getInstance().requestCertificateJoin(tempMember);					
					
					jsonobj.put("isSuccess", "true");
					jsonobj.put("message", "가입에 성공하였습니다. 메일인증을 하신 후 로그인하세요.");
						
				}
			}
			catch(PageException e){
				jsonobj.put("isSuccess", "false");
				jsonobj.put("message", e.getMessage());
				
			}catch(EntityException e){
				jsonobj.put("isSuccess", "false");
				jsonobj.put("message", e.getMessage());
				jsonobj.put("page",e.getToPage().toString());
			}catch(Exception e){
				jsonobj.put("isSuccess", "false");
				jsonobj.put("message", e.getMessage());
			}
			catch(Throwable e){
				jsonobj.put("isSuccess", "false");
				jsonobj.put("message", e.getMessage());
			}
						
			String json = jsonobj.toJSONString();

			out.print(json);

			return;
%>