<%@page import="entity.member.MemberController"%>
<%@ page import="page.VerifyPage, page.enums.enumPage, java.util.ArrayList, java.util.HashMap , entity.member.Member , entity.band.Band, entity.band.BandManager" %>
<%@ page import="page.enums.enumCautionKind, entity.band.Band.BundleBand,  property.tree.Tree, property.tree.Node"%>	
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
  
<%

	request.setCharacterEncoding("UTF-8");

	Member member = null;
 	
	HashMap<String,Object> results =  VerifyPage.Verify(session.getId(), enumPage.MAIN);
	boolean isSuccessVerify = (boolean)results.get("isSuccessVerify");
	if(isSuccessVerify){
	
		member = MemberController.getInstance().getMember(session.getId());

	}
	else{

		enumPage to = (enumPage)results.get("to");
		
		request.setAttribute("message",  (String)results.get("message"));
		request.setAttribute("messageKind", results.get("messageKind"));
		response.sendRedirect(to.toString());
		return;
				
	}
	
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
			<link href="/page/mainPage/css/main.css?<?=filemtime(\'./css/readizgen.css\')?" rel="stylesheet" type="text/css">
			
			<!-- ohsnap css  -->
			<link href="/include/notificator/ohsnap.css" rel="stylesheet">
		
			<!-- context menu css  -->
    <link href="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.css" rel="stylesheet" type="text/css"/>
		    

			<script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-latest.js"></script>
			<script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
			
			<!-- Bootstrap Core JavaScript -->
			<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script>


    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

	<div id="mynetwork"></div>
			
	
	<div class="container">

		<div class="row">
	    <div class="col-md-11"></div>
					<div id="context-menu-icon" class="col-md-1">
						<img src="/image/icon/option_blue.jpg"	class="img-fluid context-menu-one btn btn-neutral">
					</div>
		</div>
	</div>

	<div id="ohsnap"></div>

	<form id="groupForm" method="GET" ACTION="/viewFileBrowser.do">
		<input type="hidden" name="groupId" id="groupId">
	</form>
	
	<form id="managerForm" method="POST" ACTION="/manager.do">
		<input type="hidden" name="toPage" id="toPage">
	</form>


	 		<!-- context menu js  -->
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.ui.position.min.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/js/main.js" type="text/javascript"></script>
    <script type="text/javascript" src="/page/mainPage/js/contextMenu.js"></script>
	

			
		<!-- ohsnap -->
		<script type="text/javascript" charset="utf-8"	src="https://rawgithub.com/justindomingue/ohSnap/master/ohsnap.js"	></script>
		
		
		<!-- custom js  -->
		<script type="text/javascript" src="/commanJs/clientSideLibrary.js"></script>
	
	<!-- network js  -->
	<script type="text/javascript" src="/include/network-1.5.0/network.js"></script>
	<script type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript" src="/page/mainPage/js/network.js"></script>
	
	

<script>

  var memberId = <%=member.getId()%>; 
	var bands;
	var rootsBand = new Array();


	window.onload=function(){
	
		//메세지
		var message;
		var popup_color;
	
		<% 
		if(request.getAttribute("message")!=null && request.getAttribute("messageKind") !=null){
			enumCautionKind kind = (enumCautionKind)request.getAttribute("messageKind");	
		%>
		  message = "<%=(String)request.getAttribute("message")%>";
		  popup_color = "<%=(String)kind.getString()%>";
		  ohSnap(message,{color:popup_color});
	  <%
		}
		%>
		
	
   $.ajax({
   url:'./ajax/getSubBand.jsp',
   data : memberId,
   dataType:'json',
   success:function(data){
   	rootsBand = new Array(data.length);
    	var i=0;
   	for (var key in data) {		   
	  			rootsBand[i] = new bundleBand();
	  			rootsBand[i].fromBand = key;
	  			rootsBand[i].toBand = data[key];
	  			i++;
			 }
          }
      })
      
      
   $.ajax({
   url:'./ajax/getBands.jsp',
   data : memberId,
   dataType:'json',
   success:function(data){
   		alert("Asd");
 	 	 bands = new Array(data.length);
    var i=0;
    for (var key in data) {		
				bands[i] = new band();
				bands[i].id = key;
				bands[i].name = data[key];
	  		i++;
			 }
          }
      })
			
	}
	
	////member가져오기


</script>

			

	

</body>
</html>