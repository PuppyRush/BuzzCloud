<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@page import="com.puppyrush.buzzcloud.page.VerifyPage, 
com.puppyrush.buzzcloud.page.enums.enumPage, java.util.ArrayList, java.util.HashMap , 
com.puppyrush.buzzcloud.entity.member.Member , 
com.puppyrush.buzzcloud.entity.band.Band, 
com.puppyrush.buzzcloud.entity.band.BandManager" %>
<%@page import="com.puppyrush.buzzcloud.entity.message.instanceMessage.enumInstanceMessage",
com.puppyrush.buzzcloud.entity.band.Band.BundleBand,  
com.puppyrush.buzzcloud.property.tree.Tree, 
com.puppyrush.buzzcloud.property.tree.Node"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%

	request.setCharacterEncoding("UTF-8");


	
%>
    
    
<!DOCTYPE html>
<html class="full" lang="ko">

<!-- Make sure the <html> tag is set to the .full CSS class. Change the background image in the full.css file. -->

<head>

			<meta http-equiv = "Content-Type" content="text/html; charset=utf-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>버즈클라우드에 오신걸 환영합니다!</title>

    <!-- Bootstrap Core CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    
    <!-- Custom CSS -->
			<link href="/page/issuePage/css/message.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -
    ->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

<script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>




<div id="ohsnap"></div>

<script type="text/javascript" src="/commanJs/clientSideLibrary.js"></script>
<script type="text/javascript" src="/page/issuePage/js/message.js"></script>

	window.onload=function(){
	
		//메세지
		var message;
		var popup_color;
		<% 
			if(request.getAttribute("message")!=null && request.getAttribute("messageKind") !=null){
				enumInstanceMessage kind = (enumInstanceMessage)request.getAttribute("messageKind");	
		%>
			  message = "<%=(String)request.getAttribute("message")%>";
			  popup_color = "<%=(String)kind.toString()%>";
			  ohSnap(message,{color:popup_color});
		<%
			}
		%>
		
	}

</body>
</html>