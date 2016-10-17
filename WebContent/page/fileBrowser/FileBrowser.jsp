<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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

	<!-- jQuery and jQuery UI (REQUIRED) -->
				<script			src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"			src="http://code.jquery.com/jquery-latest.js"></script>
		<script type="text/javascript" charset="utf-8"			src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>


		
<!-- Section CSS -->
		
		<!-- custom CSS -->
				<link rel="stylesheet" type="text/css"			href="/page/fileBrowser/css/filebrowser.css">
				<link rel="stylesheet" type="text/css"			href="/page/fileBrowser/css/form.css">
		
		<!-- jQuery UI (REQUIRED) -->
		<link rel="stylesheet" type="text/css"			href="//ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/themes/smoothness/jquery-ui.css">
		<script			src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/jquery-ui.min.js"></script>		
		<!-- context menu css  -->
    <link href="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.css" rel="stylesheet" type="text/css"/>
		    
		<!-- elFinder CSS (REQUIRED) -->
		<link rel="stylesheet" type="text/css"	href="/include/elFinder-2.1.16/css/elfinder.min.css">		
			<!-- <link rel="stylesheet" type="text/css" href="css/theme.css"> -->
		<link rel="stylesheet" type="text/css" media="screen"			href="/include/elFinder-2.1.16/theme_win10/css/theme.css">
		
		<!-- Section JavaScript -->
		<link rel="stylesheet" type="text/css"			href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/themes/smoothness/jquery-ui.css">
		
		<!-- elFinder JS (REQUIRED) -->
		<script src="/include/elFinder-2.1.16/js/elfinder.full.js"></script>
			<script type="text/javascript" src="/page/fileBrowser/js/fileBrowser.js"></script>
		
		<!-- Bootstrap Core JavaScript -->
		<script 
			src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		
		<!-- custom JS  -->
			<script type="text/javascript" src="/commanJs/clientSideLibrary.js"></script>

		<!-- context menu js  -->
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.ui.position.min.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/js/main.js" type="text/javascript"></script>
    <script type="text/javascript" src="/page/fileBrowser/js/contextmenu.js"></script>
		
			
		<!-- network js-->
			<script type="text/javascript" src="http://www.google.com/jsapi"></script>
			<script type="text/javascript" src="/include/network-1.5.0/network.js"></script>
			<script type="text/javascript" src="/page/fileBrowser/js/network.js"></script>
		

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

<body>


	<div id="mask"></div>
	
	<div class="issueModal">

		<div id="issue">
				<img src="/image/icon/option.jpg"	class="img-fluid context-menu-one btn btn-neutral">
		</div>

	</div>	
	
	<div class="subNetworkModal">

		<div id="subNetwork"></div>

	</div>

	<div class="supperNetworkModal">

		<div id="supperNetwork"></div>

	</div>


<div id="elfinder"></div>

<div class="container">
		<div class="row">
	    <div class="col-md-11"></div>
					<div id="context-menu-icon" class="col-md-1">
						<img src="/image/icon/option.jpg"	class="img-fluid context-menu-one btn btn-neutral">
					</div>
		</div>
	</div>


<form id="subGroupForm" method="POST" ACTION="viewSubGroup.do">
	<input type="hidden" name="groupId" id="groupId">
	
</form>


<form id="supperGroupForm" method="POST" ACTION="viewSupperGroup.do">
	<input type="hidden" name="groupId" id="groupId">
</form>


<script>
		
		window.onload=function(){
			$('.issueModal').hide();
			$('.subNetworkModal').hide();		
			$('.supperNetworkModal').hide();
			$('.supperNetwork').hide();		
			$('.subNetwork').hide();		
		}

</script>


</body>



</html>