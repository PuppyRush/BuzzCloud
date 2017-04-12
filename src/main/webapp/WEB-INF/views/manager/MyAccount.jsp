<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("UTF-8");	
%>
    


<!doctype html>
<html><head>
    <meta charset="utf-8">
    <title>BLOCKS - Bootstrap Dashboard Theme</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    
    <link href="/resources/views/manager/css/main.css" rel="stylesheet">
    <link href="/resources/views/manager/css/font-style.css" rel="stylesheet">
    <link href="/resources/views/manager/css/register.css" rel="stylesheet">
    <link href="/resources/views/manager/css/myaccount.css" rel="stylesheet">
				<!-- ohsnap css  -->
			<link href="/resources/lib/include/notificator/ohsnap.css" rel="stylesheet">
		


    <style type="text/css">
      body {
        padding-top: 60px;
      }
    </style>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    
  	<!-- Google Fonts call. Font Used Open Sans & Raleway -->
	<link href="http://fonts.googleapis.com/css?family=Raleway:400,300" rel="stylesheet" type="text/css">
  	<link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
	</head>
  <body>

	<div id="ohsnap">	</div>


  	<!-- NAVIGATION MENU -->

    <div class="navbar-nav navbar-inverse navbar-fixed-top">
        <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          
        </div> 
          <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav" id="navPages">
              <li class="active"  id="myaccount"><a href="#"><i class="icon-home icon-white"></i>My Account</a></li>                            
     							<li id="groupdashboard"> <a href="#"><i class="icon-home icon-white"></i>GroupDashboard</a></li>
              <li id="group"><a href="#" ><i class="icon-home icon-white"></i>Group</a></li>
              <li id="member"><a href="#"><i class="icon-user icon-white"></i>Member</a></li>
									<li id="main"><a href="#"><i class="icon-user icon-white"></i>Home</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
    </div>

    <div class="container">
        <div class="row">

        	<div class="col-lg-6">
        		
        		<div class="register-info-wraper">
        			<div id="register-info">
        				<div class="cont2">
        					<div class="thumbnail">
											<img src="" id="memberImage" name="memberImage" alt="Marcel Newman" class="img-rectagle" >
											 <div class="footer">
											 		<br>
									 				<form id="imageForm" method="post" action="/managerPage/myAccount/registerMemberFace.ajax" enctype="multipart/form-data">
									 					<br>
									 					<input class="btn center-block" type="file" id="file" name="file" accept=".jpg, .jpeg, .png, .bmp">
									 					<br>
			 		          		  <button type="submit" id="submitImage" class="btn btn-register">등록하기</button>
				          		  </form>
			  		        </div>
							</div><!-- /thumbnail -->
							<h2 id="fullname">Marcel Newman</h2>
        				</div>
        				<div class="row">
        					<div class="col-lg-5">
        						<div class="cont3">
        							<p id="nickname"><ok>Nickname:</ok> BASICOH</p>
        							<p id="email"><ok>Mail:</ok> hola@basicoh.com</p>
        						</div>
        					</div>
        
        				</div><!-- /inner row -->
						<hr>
						<div class="cont2">
							<h2>Choose Your Option</h2>
						</div>
						<br>
							<div class="info-user2">
								<span aria-hidden="true" class="li_user fs1"></span>
								<span aria-hidden="true" class="li_settings fs1"></span>
								<span aria-hidden="true" class="li_mail fs1"></span>
								<span aria-hidden="true" class="li_key fs1"></span>
								<span aria-hidden="true" class="li_lock fs1"></span>
								<span aria-hidden="true" class="li_pen fs1"></span>
							</div>
        			</div>
        		</div>

        	</div>

        	<div class="col-sm-6 col-lg-6">
        		<div id="register-wraper">
        		    <form id="register-form" class="profileForm">
        		        <legend>User Profile</legend>
        		    
        		    	    <div class="body">
        		        	<!-- first name -->
		    		        		<label for="name">First name</label>
		    		        		<input name="fistname"id="firstname"  class="input-huge" type="text">
		        		        	<!-- last name -->
		    		        		<label for="surname">Last name</label>
		    		        		<input name="lastname" id="lastname" class="input-huge" type="text">
        		        	<!-- username -->
        		        	<label>Nickname</label>
        		        	<input name="nickname" id="nickname" class="input-huge" type="text">
        		        	<!-- email -->
        		        	<label>E-mail</label>
        		        	<input name="email" id="email" class="input-huge" type="text" readonly="readonly">
  
        		        </div>
        		    
        		        <div class="footer">     		           
      		            <button type="button" id="submitProfile" class="btn btn-success">변경하기</button>
        		        </div>
        		    </form>
        		</div>
        	</div>

        </div>
    </div>

	<div id="footerwrap">
      	<footer class="clearfix"></footer>
      	<div class="container">
      		<div class="row">
      			<div class="col-sm-12 col-lg-12">
      			<p><img src="/resources/views/manager/images/logo.png" alt=""></p>
      			<p>Blocks Dashboard Theme - Crafted With Love - Copyright 2013</p>
      			</div>

      		</div><!-- /row -->
      	</div><!-- /container -->		
	</div><!-- /footerwrap -->  
	
		
	<form id="managerForm" method="GET" ACTION="/managerPage/forwading.do">
		<input type="hidden" name="forwardPageName" id="forwardPageName">
	</form>
	
	<script>
	
	window.onload=function(){
		if("${message}"=="")
			return;
		ohSnap("${message}",{'color': "${messageKind}" });
	}
	
	
	</script>

				
		<script type="text/javascript" src="/resources/bower_components/jquery/jquery.js"></script>    
		</script><script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
		<script	 src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/jquery-ui.min.js"></script>
		
    
 	<!-- ohsnap -->
	<script type="text/javascript" charset="utf-8"	src="https://rawgithub.com/justindomingue/ohSnap/master/ohsnap.js"	></script>
	
	<script type="text/javascript" charset="utf-8"		src="/resources/lib/commanJs/commonAjax.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	<script type="text/javascript" src="/resources/lib/commanJs/clientSideLibrary.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	<script type="text/javascript" src="/resources/lib/commanJs/formValidator.j?<?=filemtime(\'./css/readizgen.css\')?s"></script>
	<script type="text/javascript" src="/resources/views/manager/js/myaccount.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		
	
</body>




</html>