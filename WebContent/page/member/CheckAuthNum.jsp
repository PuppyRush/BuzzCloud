

<%@page import="java.util.*"%>
<%@page import="page.VerifyPage"%>
<%@page import="page.enums.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%

/* 	request.setCharacterEncoding("UTF-8");
	boolean isFailVerify;
	HashMap<String,Object> results =  VerifyPage.Verify(session.getId(), enumPage.DEVELOPER);
	if(!(boolean)results.get("isSuccessVerify")){
		isFailVerify =false;
		enumPage to = (enumPage)results.get("to");
		
		request.setAttribute("message",  (String)results.get("message"));
		request.setAttribute("messageKind", results.get("messageKind"));

		return;
		
	}else
		isFailVerify = true; */

%>




<!doctype html>
<html lang="en">
 <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
  <title>Widget Store - ì¸ì¦ë²í¸ ìë ¥</title>
  <link href="WidgetClientPage/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="WidgetClientPage/css/stylish-portfolio.css" rel="stylesheet">
	<!-- bootsnipp down -->
	<link href="WidgetClientPage/css/login.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="WidgetClientPage/font-awesome/font-awesome.css" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
 </head>
 <body>
	<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header" align="center">
					<img class="img-circle" id="img_logo2" src="WidgetClientPage/img/test1.jpg">
					
				</div>
    <div id="div-forms">
		<form id="register-edit-form"  method="GET" ACTION="checkCertificationLostPw.do">
			<div class="modal-body">
		    	<div id="div-edit-register-msg">
                    <div id="icon-edit-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                         <span id="text-register-msg">인증번호 입력</span>
                    </div>
					<input id="chkNum" class="form-control" title="" type="text" placeholder="이메일로 전송된 비밀번호를 입력하세요." required>
					</div>
				<div class="modal-footer">
                    <div>
                         <button type="submit" class="btn btn-primary btn-lg btn-block">확인하기</button>
					</div>
                </div>
			</div>
       </form>
    </div>
	

    <script src="WidgetClientPage/js/jquery.js"></script>
    <script src="WidgetClientPage/js/bootstrap.min.js"></script>
 </body>
</html>
