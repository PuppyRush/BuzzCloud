<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPage"%>
 
    
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
			<link href="/resources/views/mainPage/css/main.css?<?=filemtime(\'./css/readizgen.css\')?" rel="stylesheet" type="text/css">
			<link href="/resources/views/mainPage/css/form.css?<?=filemtime(\'./css/readizgen.css\')?" rel="stylesheet" type="text/css">
			
			<!-- ohsnap css  -->
			<link href="/resources/lib/include/notificator/ohsnap.css" rel="stylesheet">
		
			<!-- context menu css  -->
     <link href="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.css" rel="stylesheet" type="text/css"/>
    
    <!--  autocomplete -->
		<link href="/resources/lib/include/easyautocomplete/easy-autocomplete.min.css" rel="stylesheet"/>
		    

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>




	<div id="mask"></div>

	<div class="searchModal">

		<div class="text-vertical-center" tabindex="-1" role="dialog"	 aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"	aria-hidden="true">×</button>
						<h3 class="text-center">그룹 가입하기</h3>
					</div>
					<div class="modal-body">

						<form class="form col-md-12 center-block" id="innerJoinForm"	>
																	
						
									
										<div class="form-group">
												<input type="search" name="searchBand" id="searchBand" class="eac-square"	placeholder="그룹 찾기" >
											  
										</div>
									
									<div class="form-group ">
											<input type="text" name="rootBandName" id="rootBandName" class="form-control input-md"	placeholder="최상위 그룹 이름"  readonly>
										</div>
										
										<div class="form-group">
											<input type="text" name="bandOwnerName" id="bandOwnerName" class="form-control input-md"	placeholder="그룹 소유주"  readonly>
										</div>
										
										<div class="form-group">
											<input type="text" name="bandAdminName" id="bandAdminName" class="form-control input-md"	placeholder="그룹 관리자"  readonly>
										</div>
									
										
											<textarea id="bandContain" name="bandContain" placeholder="그룹 설명" rows="3" readonly ></textarea>
										
										
										
										<div id="infonetwork"></div>
										
										<div class="form-group">
											<button class="btn btn-primary btn-md btn-block" id="joinBandButton" type="button"	>그룹가입 요청하기 </button>
										
										</div>
			
										<input type="hidden" id="idType" name="idType" value="NOTHING">
						</form>

					</div>
					<div class="modal-footer">
						<div class="col-md-12">
							<button class="close" data-dismiss="modal" aria-hidden="true">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<div id="mynetwork"></div>
	
	<div class="container">

		<div class="row">
	    <div class="col-md-11"></div>
					<div id="context-menu-icon" class="col-md-1">
						<img src="/resources/image/icon/option_blue.jpg"	class="img-fluid context-menu-one btn btn-neutral">
					</div>
		</div>
	</div>

	<div id="ohsnap"></div>

	<form id="bandForm" method="GET" ACTION="/mainPage/viewFileBrowser.do">
		<input type="hidden" name="bandId" id="bandId">
	</form>
	
	<form id="managerForm" method="GET" ACTION="/managerPage/forwading.do">
		<input type="hidden" name="forwardPageName" id="forwardPageName">
	</form>

<form id="logoutForm" method="GET" ACTION="/member/logout.do">
	</form>
	
			

	

<script>

	var _bands = new Array();
	var _rootsBand = new Array();


	window.onload=function(){		
		if(!"${message}"=="")
			ohSnap("${message}",{'color': "${messageKind}" });
		verifyPage("<%=(String)enumPage.MAIN.name()%>");
	}


</script>

<script type="text/javascript" src="/resources/bower_components/jquery/jquery.js"></script>    
</script><script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
		<script	 src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/jquery-ui.min.js"></script>
<!-- 	<!-- context menu js  
  <script src="/resources/bower_components/jQuery-contextMenu/dist/jquery.contextMenu.min.js" type="text/javascript"></script>
  <script src="/resources/bower_components/jQuery-contextMenu/dist/jquery.ui.position.min.js" type="text/javascript"></script>
		 -->
<!-- context menu js  -->
  <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.js" type="text/javascript"></script>
  <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.ui.position.min.js" type="text/javascript"></script>
  <script src="https://swisnl.github.io/jQuery-contextMenu/js/main.js" type="text/javascript"></script>
  <script type="text/javascript" src="/resources/views/mainPage/js/contextMenu.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		
			
		<!-- ohsnap -->
		<script type="text/javascript" charset="utf-8"	src="https://rawgithub.com/justindomingue/ohSnap/master/ohsnap.js"	></script>
		
			<!-- autocomplete  -->
	<script type="text/javascript" src="/resources/lib/include/easyautocomplete/jquery.easy-autocomplete.js"></script>
	
	
		<!-- custom js  -->
		<script type="text/javascript" charset="utf-8"		src="/resources/lib/commanJs/commonAjax.js"></script>
		<script type="text/javascript" src="/resources/lib/commanJs/clientSideLibrary.js"></script>
		<script type="text/javascript" src="/resources/views/mainPage/js/form.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		<script type="text/javascript" src="/resources/views/mainPage/js/autocomplete.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	
			<!-- network js  -->
		<script type="text/javascript" src="/resources/lib/include/network-1.5.0/network.js"></script>
		<script type="text/javascript" src="http://www.google.com/jsapi"></script>
		<script type="text/javascript" src="/resources/views/mainPage/js/network.js"></script>
		
		

</body>
</html>