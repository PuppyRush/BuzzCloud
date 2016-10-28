<%@page import="entity.member.MemberController"%>
<%@ page import="page.VerifyPage, page.enums.enumPage, java.util.ArrayList, java.util.HashMap , entity.member.Member , entity.band.Band, entity.band.BandManager" %>
<%@ page import="page.enums.enumCautionKind, property.tree.Tree, property.tree.Node"%>	
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
  
<%

	request.setCharacterEncoding("UTF-8");

	Member member = null;
 	boolean isSuccessVerify = false;
	HashMap<String,Object> results =  VerifyPage.Verify(session.getId(), enumPage.MAIN);
	
	if((boolean)results.get("isSuccessVerify")){
	
		member = MemberController.getInstance().getMember(session.getId());
		isSuccessVerify = true;		
		
	}else{
		isSuccessVerify = false;
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
			<link href="/include/notificator/ohsnap.css" rel="stylesheet">
		
		
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
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
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script>


	<nav class="navbar navbar-inverse navbar-fixed-bottom"
		role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">BuzzCloud</a>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a href="#">버즈클라우드?</a></li>
					<li><a href="#">어떻게 사용하죠?</a></li>
					<li><a href="#">문제점보고와 건의사항</a></li>
					<li><a href="#">개발자와 연락하기</a></li>
					<li><a href="#" class="joinToBuzzCloud">버즈클라우드에 가입하기 </a></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav>


<div id="mynetwork"></div>
	<div id="ohsnap"></div>

<form id="groupForm" method="GET" ACTION="viewFileBrowser.do">
	<input type="hidden" name="groupId" id="groupId">
</form>


<script type="text/javascript" src="/commanJs/clientSideLibrary.js"></script>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript" src="/include/network-1.5.0/network.js"></script>
<script type="text/javascript" src="/page/mainPage/js/network.js"></script>
<script type="text/javascript" charset="utf-8"	src="https://rawgithub.com/justindomingue/ohSnap/master/ohsnap.js"	></script>


<script>

	var bandMap = new Map();
	var bandAry = new Array();
	
	window.onload=function(){
	
		//메세지
		var message;
		var popup_color;
		<% if(request.getAttribute("message")!=null && request.getAttribute("messageKind") !=null){
			enumCautionKind kind = (enumCautionKind)request.getAttribute("messageKind");	
		%>
		  message = "<%=(String)request.getAttribute("message")%>";
		  popup_color = "<%=(String)kind.getString()%>";
		  ohSnap(message,{color:popup_color});
		<%
		}
	
		if(isSuccessVerify){
			
				ArrayList<Band> bands = 	BandManager.getInstance().getMyAdministeredBands(member.getId());
				if(bands.size()>0){
					for(Band band : bands){
						
						Tree<Band> tree = BandManager.getInstance().getSubBands(band.getBandId());
						HashMap<Integer,ArrayList<Band>> subBands = tree.toHashMapOfInteger();
						for(Integer key : subBands.keySet()){
							%>
								
								
							<%
							for(Band subBand : subBands.get(key) ){
												
							%>
								bandMap.put("<%=subBand.getBandId()%>","<%=subBand.getBandName()%>"));
								bandAry.put(["<%=key%>","<%=subBand.getBandId()%>"]);
							<%		
							}
							%>
							alert(bandAry[0][0] + "," + bandAry[0][1]);
							//addBandToPage(bandAry);
							<%
					}
				}
			}
		}
		%>
			
			
	}
			
	////member가져오기


</script>


</body>
</html>