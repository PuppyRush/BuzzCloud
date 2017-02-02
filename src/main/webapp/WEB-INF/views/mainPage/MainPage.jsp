<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@ page import="com.puppyrush.buzzcloud.page.VerifyPage, 
com.puppyrush.buzzcloud.page.enums.enumPage, java.util.ArrayList, java.util.HashMap , 
com.puppyrush.buzzcloud.entity.member.Member , 
com.puppyrush.buzzcloud.entity.band.Band, 
com.puppyrush.buzzcloud.entity.band.BandManager" %>
<%@ page import="com.puppyrush.buzzcloud.page.enums.enumCautionKind, 
com.puppyrush.buzzcloud.entity.band.Band.BundleBand,  
com.puppyrush.buzzcloud.property.tree.Tree, 
com.puppyrush.buzzcloud.property.tree.Node"%>	
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
 
    
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

	<div id="ohsnap"></div>


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
																	
						
									
										<div class="form-group has-feedback">
											 
												<input type="text" name="searchBand" id="searchBand" class="form-control input-md"	placeholder="그룹 찾기" >
											  <i class="glyphicon glyphicon-search form-control-feedback"></i>
										</div>
									
									<div class="form-group">
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
						<img src="/image/icon/option_blue.jpg"	class="img-fluid context-menu-one btn btn-neutral">
					</div>
		</div>
	</div>

	<form id="bandForm" method="GET" ACTION="/mainPage/viewFileBrowser.do">
		<input type="hidden" name="bandId" id="bandId">
	</form>
	
	<form id="managerForm" method="POST" ACTION="/manager.do">
		<input type="hidden" name="toPage" id="toPage">
		<input type="hidden" name="bandId" id="bandId">
	</form>

<form id="logoutForm" method="POST" ACTION="/logout.do">
	</form>
	
	

<script>

	var _bands = new Array();
	var _rootsBand = new Array();


	window.onload=function(){
	
		//메세지
		var message;
		var popup_color;
	
		
		message = ${message};
		if(message !="" ){
			popup_color = ${messageKind}
			ohSnap(message,{color:popup_color});
		}
		
	
			

 
	}
	////member가져오기


	
	
</script>

	<script src="http://code.jquery.com/jquery-1.9.1.js" type="text/javascript"></script>			
	<script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>


	<!-- Bootstrap Core JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script>



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