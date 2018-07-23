<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPage"%>

<!DOCTYPE html>
<html class="full" lang="ko">

<!-- Make sure the <html> tag is set to the .full CSS class. Change the background image in the full.css file. -->

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>버즈클라우드에 오신걸 환영합니다!</title>
		
		<!-- Bootstrap Core CSS -->
		<link	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
			rel="stylesheet"	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"	crossorigin="anonymous">

<!-- Section CSS -->

		<!-- custom CSS -->
				<link rel="stylesheet" type="text/css"			href="/resources/views/fileBrowser/css/filebrowser.css">

		<!-- jQuery UI (REQUIRED) -->
		<link rel="stylesheet" type="text/css"			href="//ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/themes/smoothness/jquery-ui.css">
				
		
		<!-- context menu css  -->
    <link href="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.css" rel="stylesheet" type="text/css"/>
		    
		<!-- elFinder CSS (REQUIRED) -->
		<link rel="stylesheet" type="text/css"	href="/resources/lib/include/elFinder-2.1.16/css/elfinder.min.css">		
			<!-- <link rel="stylesheet" type="text/css" href="css/theme.css"> -->
		<link rel="stylesheet" type="text/css" media="screen"			href="/resources/lib/include/elFinder-2.1.16/theme_win10/css/theme.css">
		
		<link rel="stylesheet" type="text/css" media="screen"			href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css">
		
		
		
		<!-- Section JavaScript -->
		<!-- <link rel="stylesheet" type="text/css"			href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/themes/smoothness/jquery-ui.css"> -->
		

		<!-- Bootstrap Core JavaScript -->
		<%-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script> --%>
		

				<!-- fullPage css  -->
			<link rel="stylesheet" type="text/css" href="/resources/lib/include/fullPage/jquery.fullPage.css" />
			


<!-- GoogleDocs Quicklook plugin for GoogleDrive Volume (OPTIONAL) -->
<!--<script src="js/extras/quicklook.googledocs.js"></script>-->

<!-- elFinder translation (OPTIONAL) -->
<!--<script src="js/i18n/elfinder.ru.js"></script>-->


<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body style="margin:0; padding:0;">

  <ul id="menu">
    <li data-menuanchor="firstPage" class="active"><a href="#firstPage">First section</a></li>
    <li data-menuanchor="secondPage"><a href="#secondPage">Second section</a></li>
</ul>

	<div id="fullpage">
		<div class="section active" id="section0">
				<div id="elfinder" style="height:100%; border:none;"></div>
			</div>	
   	 
	 		 <div class="section" id="section1">
	 		 		<table id="issueTable" class="display" cellspacing="0" width="100%"></div>
	 		 </div>
	   
	</div>



<div class="container">
		<div class="row">
	    <div class="col-md-11"></div>
					<div id="context-menu-icon" class="col-md-1">
						<img src="/resources/image/icon/option_blue.jpg"	class="img-fluid context-menu-one btn btn-neutral">
					</div>
		</div>
	</div>


	<div id="ohsnap">	</div>

<form id="subGroupForm" method="POST" ACTION="viewSubGroup.do">
	<input type="hidden" name="groupId" id="groupId">
	
</form>


<form id="superGroupForm" method="POST" ACTION="viewsuperGroup.do">
	<input type="hidden" name="groupId" id="groupId">
</form>

<form id="issueForm" method="POST" ACTION="viewIssueForm.do">
	<input type="hidden" name="groupId" id="groupId">
	<input type="hidden" name="isSelectUpper" id="isSelectUpper">
	<input type="hidden" name="isSelectSub" id="isSelectsub">
</form>

	<!-- jQuery and jQuery UI (REQUIRED) -->
	<script type="text/javascript" charset="utf-8"			src="http://code.jquery.com/jquery-latest.js"></script>
		<script	 src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"			src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>

			<!-- This following line is optional. Only necessary if you use the option css3:false and you want to use other easing effects rather than "linear", "swing" or "easeInOutCubic". -->
		<!-- fullPage -->
		<script type="text/javascript" src="/resources/lib/include/fullPage/vendors/jquery.easings.min.js"></script>
		<script type="text/javascript" src="/resources/lib/include/fullPage/vendors/scrolloverflow.min.js"></script>
		<script type="text/javascript" src="/resources/lib/include/fullPage/jquery.fullPage.js"></script>

	<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
	

		<!-- context menu js  -->
		<script type="text/javascript" charset="utf-8"		src="/resources/lib/commanJs/commonAjax.js"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.ui.position.min.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/js/main.js" type="text/javascript"></script>
    <script type="text/javascript" src="/resources/views/fileBrowser/js/contextmenu.js"></script>
		
		<!-- elFinder JS (REQUIRED) -->
		<script src="/resources/lib/include/elFinder-2.1.16/js/elfinder.full.js"></script>
		<script src="http://cdnjs.cloudflare.com/ajax/libs/require.js/2.3.5/require.min.js"></script>
		
		<!-- custom JS  -->
		<script type="text/javascript" src="/resources/lib/commanJs/clientSideLibrary.js"></script>
		<script type="text/javascript" src="/resources/views/fileBrowser/js/fileBrowser.js"></script>
		
<script>
	var bandId = ${bandId};

	window.onload=function(){		
		if(!"${message}"=="")
			ohSnap("${message}",{'color': "${messageKind}" });
		verifyPage("<%=(String)enumPage.MAIN.name()%>");
	};
	
	   
</script>


</body>



</html>